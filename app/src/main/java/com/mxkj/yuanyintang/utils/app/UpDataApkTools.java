package com.mxkj.yuanyintang.utils.app;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.HomeActivity;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.bean.UpDataBean;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * 更新APP  下载
 * Created by LiuJie on 2017/11/20.
 */

public class UpDataApkTools {

    private static UpDataApkTools instance;
    DiaLogBuilder upDataApkDialogBuilder;
    private int downloadId = 0;
    private String url;
    private String savePath;
    private ProgressBar schedule_progressbar;
    private TextView tv_progress;
    private LinearLayout layout_updata_loading_view;
    private RelativeLayout layout_updata_content_view;
    private TextView tv_updata_content;
    private Context context;

    public static UpDataApkTools getInstance() {
        if (instance == null) {
            synchronized (TasksManager.class) {
                if (instance == null)
                    instance = new UpDataApkTools();
            }
        }
        return instance;
    }

    public UpDataApkTools setContext(Context context) {
        this.context = context;
        return this;
    }

    public interface DownLoadApkListening {
        void onCompleted();
    }

    private DownLoadApkListening downLoadApkListening;

    FileDownloadNotificationListener fileDownloadListener = new FileDownloadNotificationListener(new FileDownloadNotificationHelper()) {
        @Override
        protected BaseNotificationItem create(BaseDownloadTask task) {
            return new NotificationItem(task.getId(), "源音塘app更新下载中...", "");
        }

        @Override
        public void destroyNotification(BaseDownloadTask task) {
            super.destroyNotification(task);
            downloadId = 0;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            if (schedule_progressbar != null) {
                schedule_progressbar.setIndeterminate(true);
            }
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            Log.e(TAG, "权限没问题、、、、更新出错: "+e );
        }

        private static final String TAG = "UpDataApkTools";

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            if (schedule_progressbar != null) {
                if (totalBytes < 0) {
                    schedule_progressbar.setIndeterminate(true);
                } else {
                    schedule_progressbar.setIndeterminate(false);
                    schedule_progressbar.setMax(totalBytes);
                    schedule_progressbar.setProgress(soFarBytes);
                    tv_progress.setText(((StringUtils.getStringValueWith2Suffix((soFarBytes * 1f) / (totalBytes * 1f) * 100))) + "%");
                }
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            if (schedule_progressbar != null) {
                schedule_progressbar.setIndeterminate(false);
                schedule_progressbar.setProgress(task.getSmallFileTotalBytes());
            }
            final File file = new File(savePath, task.getFilename());
            tv_progress.setText("100%");
            if (file.exists()) {
                installApp(context, file);

            }
            if (null != upDataApkDialogBuilder) {
                upDataApkDialogBuilder.setDismiss();
            }
        }
    };

    public static class NotificationItem extends BaseNotificationItem {

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        private NotificationItem(int id, String title, String desc) {
            super(id, title, desc);
            Intent[] intents = new Intent[2];
            intents[0] = Intent.makeMainActivity(new ComponentName(MainApplication.Companion.getContext(),
                    HomeActivity.class));
            intents[1] = new Intent(MainApplication.Companion.getContext(), HomeActivity.class);

            this.pendingIntent = PendingIntent.getActivities(MainApplication.Companion.getContext(), 0, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder = new NotificationCompat.
                    Builder(FileDownloadHelper.getAppContext());

            builder.setDefaults(Notification.DEFAULT_LIGHTS)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setContentTitle(getTitle())
                    .setContentText(desc)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher);

        }

        @Override
        public void show(boolean statusChanged, int status, boolean isShowProgress) {

            String desc = getDesc();
            switch (status) {
                case FileDownloadStatus.pending:
                    desc += " pending";
                    break;
                case FileDownloadStatus.started:
                    desc += " started";
                    break;
                case FileDownloadStatus.progress:
                    desc += " progress";
                    break;
                case FileDownloadStatus.retry:
                    desc += " retry";
                    break;
                case FileDownloadStatus.error:
                    desc += " error";
                    break;
                case FileDownloadStatus.paused:
                    desc += " paused";
                    break;
                case FileDownloadStatus.completed:
                    desc += " completed";
                    break;
                case FileDownloadStatus.warn:
                    desc += " warn";
                    break;
            }

            builder.setContentTitle(getTitle())
                    .setContentText(desc);

            if (statusChanged) {
                builder.setTicker(desc);
            }

            builder.setProgress(getTotal(), getSofar(), !isShowProgress);
            getManager().notify(getId(), builder.build());
        }
    }

    public void pauseDownLoad() {
        if (downloadId != 0) {
            FileDownloader.getImpl().pause(downloadId);
        }
    }

    private void upDataApkStart() {
        RxPermissions rxPermissions = new RxPermissions((Activity) context);
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            Log.e(TAG, "权限允许: ");
                            layout_updata_loading_view.setVisibility(View.VISIBLE);
                            layout_updata_content_view.setVisibility(View.GONE);
                            if (downloadId == 0 && !TextUtils.isEmpty(url)) {
                                downloadId = FileDownloader.getImpl().create(url)
                                        .setPath(savePath, true)
                                        .setListener(fileDownloadListener)
                                        .start();
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            Log.e(TAG, "用户拒绝了该权限，没有选中『不再询问: ");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        } else {
                            Log.e(TAG, "用户拒绝了该权限，并且选中『不再询问』 ");
                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            Toast.create(context).show("没有外部存储权限,请到系统设置开启源音塘的存储权限");
                        }
                    }
                });
    }

    public UpDataApkTools upDataApkData(Boolean isNetData, String jsonData) {
        if (System.currentTimeMillis() - CacheUtils.INSTANCE.getLong(context, Constants.Other.UP_APK_TIME, 0L) < (1000 * 24)) {
            return this;
        }
        final int versionCode = CommonUtils.INSTANCE.getAPPVersionCode(context);
        final String versionName = CommonUtils.INSTANCE.getAPPVersionName(context);
        savePath = Constants.Path.APP_PATH + File.separator + "yytApk" + (versionCode + 1);
        final File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        View view = View.inflate(context, R.layout.dialog_updata_apk, null);
        upDataApkDialogBuilder = new DiaLogBuilder(context)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        ImageView iv_cancle_updata = view.findViewById(R.id.iv_cancle_updata);
        TextView tv_updata_app = view.findViewById(R.id.tv_updata_app);
        TextView tv_service_updata = view.findViewById(R.id.tv_service_updata);
        tv_updata_content = view.findViewById(R.id.tv_updata_content);
        tv_progress = view.findViewById(R.id.tv_progress);
        schedule_progressbar = view.findViewById(R.id.schedule_progressbar);
        layout_updata_loading_view = view.findViewById(R.id.layout_updata_loading_view);
        layout_updata_content_view = view.findViewById(R.id.layout_updata_content_view);
        if (!isNetData) {
            iv_close.setVisibility(View.GONE);
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDataApkDialogBuilder.setDismiss();
            }
        });
        RxView.clicks(tv_updata_app)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        upDataApkStart();
                    }
                });
        RxView.clicks(tv_service_updata)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        upDataApkDialogBuilder.setDismiss();
                    }
                });
        RxView.clicks(iv_cancle_updata)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        final MaterialDialog dialog = new MaterialDialog(context);
                        dialog.content(
                                "是否暂停app更新")
                                .btnText("取消", "确定")
                                .titleTextSize(16)
                                .titleTextColor(ContextCompat.getColor(context, R.color.color_14_text))
                                .contentTextColor(ContextCompat.getColor(context, R.color.color_66_text))
                                .contentTextSize(14)
                                .btnTextSize(14)
                                .btnTextColor(ContextCompat.getColor(context, R.color.base_red)
                                        , ContextCompat.getColor(context, R.color.base_red))
                                .showAnim(null)
                                .dismissAnim(null)
                                .show();

                        dialog.setOnBtnClickL(
                                new OnBtnClickL() {//left btn click listener
                                    @Override
                                    public void onBtnClick() {
                                        dialog.dismiss();
                                    }
                                },
                                new OnBtnClickL() {//right btn click listener
                                    @Override
                                    public void onBtnClick() {
                                        dialog.dismiss();
                                        if (downloadId != 0) {
                                            FileDownloader.getImpl().pause(downloadId);
                                            downloadId = 0;
                                        }
                                        upDataApkDialogBuilder.setDismiss();
                                    }
                                }
                        );

                    }
                });
        if (isNetData) {
            NetWork.INSTANCE.getVersions(context, versionName, new NetWork.TokenCallBack() {

                @Override
                public void doNext(String resultData, Headers headers) {
                    upDataApk(resultData);
                }

                @Override
                public void doError(String msg) {

                }

                @Override
                public void doResult() {

                }
            });
        } else {
            upDataApk(jsonData);
        }
        return this;
    }

    private void upDataApk(String resultData) {
        CacheUtils.INSTANCE.setLong(context, Constants.Other.UP_APK_TIME, System.currentTimeMillis());
        UpDataBean upDataBean = JSON.parseObject(resultData, UpDataBean.class);
        if (null != upDataBean && null != upDataBean.getData()) {
            if (upDataBean.getData().size() > 0) {
                tv_updata_content.setText(StringUtils.isEmpty(upDataBean.getData().get(0).getRemark()));
            }
            if (context.getMainLooper() == null) {
                Looper.prepare();
                showUpApkDialog(upDataBean.getData());
                Looper.loop();
            } else {
                showUpApkDialog(upDataBean.getData());
            }
        } else {
            if (!(context instanceof HomeActivity)) {
                Toast.create(context).show("" + upDataBean.getMsg());
            }
        }
    }

    private static final String TAG = "UpDataApkTools";

    private void showUpApkDialog(List<UpDataBean.DataBean> data) {
        url = data.get(0).getFile_video_link();
        if (null != upDataApkDialogBuilder) {
            upDataApkDialogBuilder.show();
        }
    }

    private static boolean installApp(final Context context, File appFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", appFile);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
            }
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                context.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public UpDataApkTools setCanceledOnTouchOutside(Boolean isCancel) {
        if (null != upDataApkDialogBuilder) {
            upDataApkDialogBuilder.setCanceledOnTouchOutside(isCancel);
        }
        return this;
    }
}
