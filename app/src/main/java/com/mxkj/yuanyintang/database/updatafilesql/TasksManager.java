package com.mxkj.yuanyintang.database.updatafilesql;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.mainui.myself.activity.DownLoadFileManagerActivity;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.file.DelFile;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import static com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE;

public class TasksManager {

    private static TasksManager instance;

    public static String getFilePath(Context context) {
        if (CacheUtils.INSTANCE.getBoolean(context, MUSIC_DIR_PRIVATE, false) == true) {
            String path = Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + "Music";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        } else {
            return Constants.Path.APP_PATH + File.separator + "Music";
        }
    }

    public static TasksManager getImpl() {
        if (instance == null) {
            synchronized (TasksManager.class) {
                if (instance == null)
                    instance = new TasksManager();
            }
        }
        return instance;
    }

    private List<UpDataFileBean> modelList;

    public List<UpDataFileBean> getModelList() {
        return modelList;
    }

    private TasksManager() {
        modelList = new UpDataFileDao(MainApplication.Companion.getContext()).isQueryDataList("success", false);
        initData();
    }

    private void initData() {
        for (int i = 0; i < modelList.size(); i++) {
            UpDataFileBean fileBean = modelList.get(i);
            addTask(fileBean);
        }
    }

    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    public SparseArray<BaseDownloadTask> getTaskSparseArray() {
        return taskSparseArray;
    }

    public void setTaskSparseArray(SparseArray<BaseDownloadTask> taskSparseArray) {
        this.taskSparseArray = taskSparseArray;
    }

    public void addTaskForViewHolder(final BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(final int id) {
        taskSparseArray.remove(id);
    }

    public void updateViewHolder(final int id, final DownLoadFileManagerActivity.TaskItemViewHolder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }
        task.setTag(holder);
    }

    public void releaseTask() {
        taskSparseArray.clear();
        modelList.clear();
    }

    private FileDownloadConnectListener listener;

    private void registerServiceConnectionListener(final WeakReference<DownLoadFileManagerActivity> activityWeakReference) {
        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }

        listener = new FileDownloadConnectListener() {

            @Override
            public void connected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }
                activityWeakReference.get().postNotifyDataChanged();
            }

            @Override
            public void disconnected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public void onCreate(final WeakReference<DownLoadFileManagerActivity> activityWeakReference) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(activityWeakReference);
        }
    }

    public void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTask();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public UpDataFileBean get(final int position) {
        return modelList.get(position);
    }

    public UpDataFileBean getById(final int id) {
        for (UpDataFileBean model : modelList) {
            if (model.getTastId() == id) {
                return model;
            }
        }
        return null;
    }

    /**
     * @param status Download Status
     * @return has already downloaded
     * @see FileDownloadStatus
     */
    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(final int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(final int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    public UpDataFileBean addTask(UpDataFileBean fileBean) {
        return addTask(fileBean.getUrl(), fileBean.getPath(), fileBean);
    }

    private UpDataFileBean addTask(final String url, final String path, UpDataFileBean fileBean) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        final int id = FileDownloadUtils.generateId(url, path);
        UpDataFileBean model = getById(id);
        if (model != null) {
            return model;
        }
        final UpDataFileBean newModel = add(url, path, fileBean);
        if (newModel != null) {
            modelList.add(newModel);
        }
        return newModel;
    }

    public UpDataFileBean add(final String url, final String path, UpDataFileBean fileBean) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        final int id = FileDownloadUtils.generateId(url, path);
        UpDataFileBean model = new UpDataFileBean();
        model.setTastId(id);
        model.setMusic_name(StringUtils.isEmpty(fileBean.getMusic_name()));
        model.setImg_url(StringUtils.isEmpty(fileBean.getImg_url()));
        model.setMusic_id(StringUtils.isEmpty(fileBean.getMusic_id()));
        model.setUrl(url);
        model.setPath(path);
        model.setNickname(fileBean.getNickname());
        model.setUpTime(fileBean.getUpTime());
        model.setSuccess(fileBean.getSuccess());
        model.setExt(TextUtils.isEmpty(fileBean.getExt()) ? "mp3" : fileBean.getExt());
        return model;
    }

    private static final String TAG = "TasksManager";

    public void downLoadFile(MusicBean musicBean, String url, Context context) {
        Boolean isData = new UpDataFileDao(MainApplication.Companion.getContext()).isQueryData("musicId", musicBean.getMusic_id());
        if (isData) {
            Toast.create(MainApplication.Companion.getContext()).show("下载列表已经存在");
            return;
        }

        final UpDataFileBean upDataFileBean = new UpDataFileBean();
        upDataFileBean.setUpTime(0);
        upDataFileBean.setMusic_name(StringUtils.isEmpty(musicBean.getMusic_name()));
        try {
            upDataFileBean.setImg_url(StringUtils.isEmpty(musicBean.getImgpic_info().getLink()));

        } catch (RuntimeException e) {
        }

        upDataFileBean.setNickname(StringUtils.isEmpty(musicBean.getMusician_name()));
        String filePath = FileDownloadUtils.generateFilePath(getFilePath(context), TextUtils.isEmpty(musicBean.getMusic_name())
                ? System.currentTimeMillis() + "" + musicBean.getMusic_name()
                : StringUtils.isEmpty(musicBean.getMusic_name()));
        int taskID = FileDownloadUtils.generateId(url, filePath);
        Log.d(TAG, "downLoadFile: ---------->" + taskID);
        upDataFileBean.setTastId(taskID);
        upDataFileBean.setPath(filePath);
        upDataFileBean.setUrl(url);
        upDataFileBean.setMusic_id(musicBean.getMusic_id());
        upDataFileBean.setCollection(musicBean.getCollection());
        upDataFileBean.setUid(musicBean.getUid());
        upDataFileBean.setSong_id(musicBean.getSong_id());
        upDataFileBean.setSongName(musicBean.getSongName());
        upDataFileBean.setExt(musicBean.getExt());
        upDataFileBean.setFileName(musicBean.getMusic_name());
        upDataFileBean.setSuccess(false);
        addBaseDownLoadTask(upDataFileBean);
    }

    private void addBaseDownLoadTask(final UpDataFileBean upDataFileBean) {
        final BaseDownloadTask task = FileDownloader.getImpl().create(upDataFileBean.getUrl())
                .setPath(upDataFileBean.getPath())
                .setCallbackProgressTimes(100)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "pending: --------->");
                        if (null != MainApplication.Companion.getApplication().getCurrentActivity()) {
                            if (MainApplication.Companion.getApplication().getCurrentActivity() instanceof BaseActivity) {
                                ((BaseActivity) MainApplication.Companion.getApplication().getCurrentActivity()).setSnackBar("已添加到下载列表", "", R.drawable.icon_success);
                            }
                        }
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        (MainApplication.Companion.getApplication().getCurrentActivity()).sendBroadcast(new Intent("cgCollect"));
                        new UpDataFileDao(MainApplication.Companion.getContext()).upSuccessDataKey(task.getId());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d(TAG, "error: ----------->" + e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                });
        new UpDataFileDao(MainApplication.Companion.getContext()).add(upDataFileBean);
        TasksManager.getImpl().addTaskForViewHolder(task);
        task.start();
        Log.d(TAG, "addBaseDownLoadTask: --------->" + task.getId());
    }

    public void refreshData() {
        modelList.clear();
        modelList.addAll(new UpDataFileDao(MainApplication.Companion.getContext()).isQueryDataList("success", false));
    }

    public void clearData() {
        for (int i = 0; i < modelList.size(); i++) {
            if (!modelList.get(i).getSuccess()) {
                TasksManager.getImpl().removeTaskForViewHolder(modelList.get(i).getId());
                new UpDataFileDao(MainApplication.Companion.getApplication()).deleteHelper(modelList.get(i).getId());
                DelFile.deleteFile(modelList.get(i).getPath());
                DelFile.deleteFile(modelList.get(i).getPath() + ".temp");
            }
        }
        releaseTask();
    }

    public void removeData(int position, Boolean success) {
        new UpDataFileDao(MainApplication.Companion.getContext()).deleteHelper(modelList.get(position).getId());
        removeTaskForViewHolder(modelList.get(position).getId());
        DelFile.deleteFile(modelList.get(position).getPath());
        DelFile.deleteFile(modelList.get(position).getPath() + ".temp");
        modelList.clear();
        modelList.addAll(new UpDataFileDao(MainApplication.Companion.getContext()).isQueryDataList("success", success));

    }


}
