package com.mxkj.yuanyintang.musicplayer.playcache;

import android.text.TextUtils;

import com.mxkj.yuanyintang.musicplayer.playcache.sourcestorage.SourceInfoStorage;
import com.mxkj.yuanyintang.musicplayer.playcache.sourcestorage.SourceInfoStorageFactory;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.MusicNoFileIoEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;

/**
 * {@link Source} that uses http resource as source for {@link ProxyCache}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class HttpUrlSource implements Source {

    private static final Logger LOG = LoggerFactory.getLogger("HttpUrlSource");

    private static final int MAX_REDIRECTS = 5;
    private final SourceInfoStorage sourceInfoStorage;
    private SourceInfo sourceInfo;
    private HttpURLConnection connection;
    private InputStream inputStream;

    public HttpUrlSource(String url) {
        this(url, SourceInfoStorageFactory.newEmptySourceInfoStorage());
    }

    public HttpUrlSource(String url, SourceInfoStorage sourceInfoStorage) {
        this.sourceInfoStorage = Preconditions.checkNotNull(sourceInfoStorage);
        SourceInfo sourceInfo = sourceInfoStorage.get(url);
        this.sourceInfo = sourceInfo != null ? sourceInfo :
                new SourceInfo(url, Integer.MIN_VALUE, ProxyCacheUtils.getSupposablyMime(url));
    }

    public HttpUrlSource(HttpUrlSource source) {
        this.sourceInfo = source.sourceInfo;
        this.sourceInfoStorage = source.sourceInfoStorage;
    }

    @Override
    public synchronized int length() throws ProxyCacheException {
        if (sourceInfo.length == Integer.MIN_VALUE) {
            fetchContentInfo();
        }
        return sourceInfo.length;
    }

    @Override
    public void open(int offset) throws ProxyCacheException {
        try {
            connection = openConnection(offset, -1);
            String mime = connection.getContentType();
            inputStream = new BufferedInputStream(connection.getInputStream(), ProxyCacheUtils.DEFAULT_BUFFER_SIZE);
            int length = readSourceAvailableBytes(connection, offset, connection.getResponseCode());
            this.sourceInfo = new SourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
        } catch (IOException e) {
            throw new ProxyCacheException("Error opening connection for " + sourceInfo.url + " with offset " + offset, e);
        }
    }

    private int readSourceAvailableBytes(HttpURLConnection connection, int offset, int responseCode) throws IOException {
        int contentLength = connection.getContentLength();
        return responseCode == HTTP_OK ? contentLength
                : responseCode == HTTP_PARTIAL ? contentLength + offset : sourceInfo.length;
    }

    @Override
    public void close() throws ProxyCacheException {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (NullPointerException | IllegalArgumentException e) {
                String message = "Wait... but why? WTF!? " +
                        "Really shouldn't happen any more after fixing https://github.com/danikula/AndroidVideoCache/issues/43. " +
                        "If you read it on your device log, please, notify me danikula@gmail.com or create issue here https://github.com/danikula/AndroidVideoCache/issues.";
                throw new RuntimeException(message, e);
            }
        }
    }

    @Override
    public int read(byte[] buffer) throws ProxyCacheException {
        if (inputStream == null) {
            throw new ProxyCacheException("Error reading data from " + sourceInfo.url + ": connection is absent!");
        }
        try {
            return inputStream.read(buffer, 0, buffer.length);
        } catch (InterruptedIOException e) {
            throw new InterruptedProxyCacheException("Reading source " + sourceInfo.url + " is interrupted", e);
        } catch (IOException e) {
            throw new ProxyCacheException("Error reading data from " + sourceInfo.url, e);
        }
    }

    private void fetchContentInfo() throws ProxyCacheException {
        LOG.debug("Read content info from " + sourceInfo.url);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = openConnection(0, 10000);
            int length = urlConnection.getContentLength();
            String mime = urlConnection.getContentType();
            inputStream = urlConnection.getInputStream();
            this.sourceInfo = new SourceInfo(sourceInfo.url, length, mime);
            this.sourceInfoStorage.put(sourceInfo.url, sourceInfo);
            LOG.debug("Source info fetched: " + sourceInfo);
        } catch (IOException e) {
            LOG.error("Error fetching info from " + sourceInfo.url, e);
//            RxBus.getDefault().post(new MusicNoFileIoEvent());
        } finally {
            ProxyCacheUtils.close(inputStream);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private HttpURLConnection openConnection(int offset, int timeout) throws IOException, ProxyCacheException {
        try {
            HttpURLConnection connection;
            boolean redirected;
            int redirectCount = 0;
            String url = this.sourceInfo.url;
            do {
                LOG.debug("Open connection " + (offset > 0 ? " with offset " + offset : "") + " to " + url);
                connection = (HttpURLConnection) new URL(url).openConnection();
                if (offset > 0) {
                    connection.setRequestProperty("Range", "bytes=" + offset + "-");
                }
                if (timeout > 0) {
                    connection.setConnectTimeout(timeout);
                    connection.setReadTimeout(timeout);
                }
                int code = connection.getResponseCode();
                redirected = code == HTTP_MOVED_PERM || code == HTTP_MOVED_TEMP || code == HTTP_SEE_OTHER;
                if (redirected) {
                    url = connection.getHeaderField("Location");
                    redirectCount++;
                    connection.disconnect();
                }
                if (redirectCount > MAX_REDIRECTS) {
                    throw new ProxyCacheException("Too many redirects: " + redirectCount);
                }
            } while (redirected);
            return connection;
        } catch (RuntimeException e) {
        }
        return null;
    }

    public synchronized String getMime() throws ProxyCacheException {
        if (TextUtils.isEmpty(sourceInfo.mime)) {
            fetchContentInfo();
        }
        return sourceInfo.mime;
    }

    public String getUrl() {
        return sourceInfo.url;
    }

    @Override
    public String toString() {
        return "HttpUrlSource{sourceInfo='" + sourceInfo + "}";
    }
}
