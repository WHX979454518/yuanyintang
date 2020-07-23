package com.mxkj.yuanyintang.mainui.myself.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.jakewharton.rxbinding2.view.RxView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileBean;
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileDao;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/10.
 */

public class DownLoadFileManagerActivity extends StandardUiActivity {
    private static final String TAG = "DownLoadFileManagerActivity";
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.tv_start_all_tast)
    TextView tv_start_all_tast;
    @BindView(R.id.tv_clear_download_task)
    TextView tv_clear_download_task;
    private TaskItemAdapter adapter;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_download_file_center;
    }

    @Override
    protected void initView() {
        Log.e("下载", "initView: 创建" );
        ButterKnife.bind(this);
        setTitleText("下载中心");
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(tv_clear_download_task)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MaterialDialog("是否清空全部下载任务？", null, null, new MaterialDialogBtnClickListener() {
                            @Override
                            public void onBtnClick(int code) {
                                if (code == 1) {
                                    clearDownLoadFile();
                                }
                            }
                        });
                    }
                });
        RxView.clicks(tv_start_all_tast)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MaterialDialog("是否开始全部下载任务？", null, null, new MaterialDialogBtnClickListener() {
                            @Override
                            public void onBtnClick(int code) {
                                if (code == 1) {
                                    startDownLoadFile();
                                }
                            }
                        });
                    }
                });
        initRecyclerView();
        TasksManager.getImpl().refreshData();
        TasksManager.getImpl().onCreate(new WeakReference<>(this));
    }

    private void startDownLoadFile() {
        new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            List<UpDataFileBean> upDataFileBeanList = TasksManager.getImpl().getModelList();
                            for (int i = 0; i < upDataFileBeanList.size(); i++) {
                                final UpDataFileBean upDataFileBean = upDataFileBeanList.get(i);
                                if (upDataFileBean.getSuccess()) {
                                    continue;
                                }
                                final BaseDownloadTask task = FileDownloader.getImpl().create(upDataFileBean.getUrl())
                                        .setPath(upDataFileBean.getPath())
                                        .setCallbackProgressTimes(100)
                                        .setListener(adapter.taskDownloadListener);

                                TasksManager.getImpl().addTaskForViewHolder(task);
                                TasksManager.getImpl().updateViewHolder(upDataFileBean.getId(), adapter.getHolder());
                                task.start();
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.create(DownLoadFileManagerActivity.this).show("没有存储权限");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskItemAdapter() {
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        // 设置菜单创建器。
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                final int adapterPosition = menuBridge.getAdapterPosition();
                TasksManager.getImpl().removeData(adapterPosition, false);
                adapter.notifyDataSetChanged();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startDownLoadFile();
            }
        }, 500);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    public void postNotifyDataChanged() {
        if (adapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    // ==================== view adapter ====================

    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {

        public TaskItemViewHolder(View itemView) {
            super(itemView);
            assignViews();
        }

        private View findViewById(final int id) {
            return itemView.findViewById(id);
        }

        /**
         * viewHolder position
         */
        private int position;
        /**
         * download id
         */
        private int id;

        public void update(final int id, final int position) {
            this.id = id;
            this.position = position;
        }

        public void updateDownloaded() {
            donutProgress.setMax(1);
            donutProgress.setProgress(1);

            tv_describe.setText("已下载完成");
            showDownLoadFileView("complete");
        }

        public void updateNotDownloaded(final int status, final long sofar, final long total) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar
                        / (float) total;
                donutProgress.setMax(100);
                donutProgress.setProgress((int) (percent * 100));
            } else {
                donutProgress.setMax(1);
                donutProgress.setProgress(0);
            }

            switch (status) {
                case FileDownloadStatus.error://出错
                    tv_describe.setText("出错啦");
                    showDownLoadFileView("error");
                    break;
                case FileDownloadStatus.paused://暂停
                    tv_describe.setText("暂停");
                    showDownLoadFileView("paused");
                    break;
                default://未下载
                    tv_describe.setText("未下载");
                    showDownLoadFileView("not");
                    break;
            }
            //开始
            showDownLoadFileView("start");
        }

        private void showDownLoadFileView(String s) {
            iv_wait.setVisibility(View.GONE);
            donutProgress.setVisibility(View.GONE);
            iv_start_download.setVisibility(View.GONE);
            iv_delete_download.setVisibility(View.GONE);
            switch (s) {
                case "error":

                    break;
                case "paused":

                    break;
                case "waiting":
                    iv_wait.setVisibility(View.VISIBLE);
                    break;
                case "not":
                    iv_wait.setVisibility(View.GONE);
                    break;
                case "delete":

                    break;
                case "downing":
                    donutProgress.setVisibility(View.VISIBLE);
                    break;
                case "start":
                    iv_start_download.setVisibility(View.VISIBLE);
                    break;
                case "complete":
                    iv_delete_download.setVisibility(View.VISIBLE);
                    iv_wait.setVisibility(View.GONE);
                    donutProgress.setVisibility(View.GONE);
                    iv_start_download.setVisibility(View.GONE);
                    break;
            }
        }

        public void updateDownloading(final int status, final long sofar, final long total) {
            final float percent = sofar
                    / (float) total;
            donutProgress.setMax(100);
            donutProgress.setProgress((int) (percent * 100));
            switch (status) {
                case FileDownloadStatus.pending://队列中
                    tv_describe.setText("队列中");
                    showDownLoadFileView("waiting");
                    break;
                case FileDownloadStatus.started://开始下载
                    tv_describe.setText("开始下载");
                    break;
                case FileDownloadStatus.connected://已连接上
                    tv_describe.setText("已连接上");
                    break;
                case FileDownloadStatus.progress://下载中///
                    tv_describe.setText("下载中...");
                    showDownLoadFileView("downing");
                    break;
                default:
                    tv_describe.setText("正在下载");
                    break;
            }

        }

        private TextView tv_name;
        private TextView tv_describe;
        private ImageView iv_wait;
        private ImageView iv_start_download;
        private ImageView iv_delete_download;
        private FrameLayout layout_action;
        private DonutProgress donutProgress;

        private void assignViews() {
            tv_name = (TextView) findViewById(R.id.tv_name);
            tv_describe = (TextView) findViewById(R.id.tv_describe);
            iv_wait = (ImageView) findViewById(R.id.iv_wait);
            layout_action = (FrameLayout) findViewById(R.id.layout_action);
            donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
            iv_start_download = (ImageView) findViewById(R.id.iv_start_download);
            iv_delete_download = (ImageView) findViewById(R.id.iv_delete_download);
        }
    }

    private static class TaskItemAdapter extends RecyclerView.Adapter<TaskItemViewHolder> {
        TaskItemViewHolder holder;

        public TaskItemViewHolder getHolder() {
            return holder;
        }

        private FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

            private TaskItemViewHolder checkCurrentHolder(final BaseDownloadTask task) {
                if (task != null) {
                    final TaskItemViewHolder tag = (TaskItemViewHolder) task.getTag();
                    if (tag != null && tag.id != task.getId()) {
                        return null;
                    }
                    return tag;
                }
                return null;
            }

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.pending(task, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }
                tag.updateDownloading(FileDownloadStatus.pending, soFarBytes, totalBytes);
                tag.tv_describe.setText("队列中");
            }

            @Override
            protected void started(BaseDownloadTask task) {
                super.started(task);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.tv_describe.setText("开始下载");
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.updateDownloading(FileDownloadStatus.connected, soFarBytes, totalBytes);
                tag.tv_describe.setText("已连接上");
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.progress(task, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }
                tag.updateDownloading(FileDownloadStatus.progress, soFarBytes, totalBytes);
            }

            @SuppressLint("LongLogTag")
            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }
                Log.e(TAG, "error: " + e);
                tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes(), task.getLargeFileTotalBytes());
                TasksManager.getImpl().removeTaskForViewHolder(task.getId());
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.paused(task, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }
                tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
                tag.tv_describe.setText("暂停");
                TasksManager.getImpl().removeTaskForViewHolder(task.getId());
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }
                tag.updateDownloaded();
                new UpDataFileDao(MainApplication.Companion.getContext()).upSuccessDataKey(task.getId());
                TasksManager.getImpl().removeTaskForViewHolder(task.getId());
                TasksManager.getImpl().refreshData();
            }
        };
        private View.OnClickListener taskActionOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    return;
                }
                TaskItemViewHolder holder = (TaskItemViewHolder) v.getTag();
                if (holder.iv_start_download.getVisibility() == View.VISIBLE) {
                    // to start
                    final UpDataFileBean upDataFileBean = TasksManager.getImpl().get(holder.position);
                    final BaseDownloadTask task = FileDownloader.getImpl().create(upDataFileBean.getUrl())
                            .setPath(upDataFileBean.getPath())
                            .setCallbackProgressTimes(100)
                            .setListener(taskDownloadListener);

                    TasksManager.getImpl().addTaskForViewHolder(task);
                    TasksManager.getImpl().updateViewHolder(holder.id, holder);
                    task.start();
                    return;
                }

                if (holder.donutProgress.getVisibility() == View.VISIBLE) {
                    // to pause
                    FileDownloader.getImpl().pause(holder.id);
                    return;
                }
                if (holder.iv_wait.getVisibility() == View.VISIBLE) {
                    // to start
                    final UpDataFileBean upDataFileBean = TasksManager.getImpl().get(holder.position);
                    final BaseDownloadTask task = FileDownloader.getImpl().create(upDataFileBean.getUrl())
                            .setPath(upDataFileBean.getPath())
                            .setCallbackProgressTimes(100)
                            .setListener(taskDownloadListener);

                    TasksManager.getImpl().addTaskForViewHolder(task);
                    TasksManager.getImpl().updateViewHolder(holder.id, holder);
                    task.start();
                    return;
                }
                if (holder.iv_delete_download.getVisibility() == View.VISIBLE) {
                    // to delete
                    new File(TasksManager.getImpl().get(holder.position).getPath()).delete();
                    holder.layout_action.setEnabled(true);
                    holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS, 0, 0);
                }
            }
        };

        @Override
        public TaskItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            holder = new TaskItemViewHolder(
                    LayoutInflater.from(
                            parent.getContext())
                            .inflate(R.layout.item_download_file_manager, parent, false));

            holder.layout_action.setOnClickListener(taskActionOnClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(TaskItemViewHolder holder, int position) {
            final UpDataFileBean upDataFileBean = TasksManager.getImpl().get(position);

            holder.update(upDataFileBean.getTastId(), position);
            holder.layout_action.setTag(holder);
            holder.tv_name.setText(upDataFileBean.getMusic_name());

            TasksManager.getImpl().updateViewHolder(holder.id, holder);

            holder.layout_action.setEnabled(true);

            if (TasksManager.getImpl().isReady()) {
                final int status = TasksManager.getImpl().getStatus(upDataFileBean.getTastId(), upDataFileBean.getPath());
                if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                        status == FileDownloadStatus.connected) {
                    // start task, but file not created yet
                    holder.updateDownloading(status, TasksManager.getImpl().getSoFar(upDataFileBean.getTastId())
                            , TasksManager.getImpl().getTotal(upDataFileBean.getTastId()));
                } else if (!new File(upDataFileBean.getPath()).exists() &&
                        !new File(FileDownloadUtils.getTempPath(upDataFileBean.getPath())).exists()) {
                    // not exist file
                    holder.updateNotDownloaded(status, 0, 0);
                } else if (TasksManager.getImpl().isDownloaded(status)) {
                    // already downloaded and exist
                    holder.updateDownloaded();
                } else if (status == FileDownloadStatus.progress) {
                    // downloading
                    holder.updateDownloading(status, TasksManager.getImpl().getSoFar(upDataFileBean.getTastId())
                            , TasksManager.getImpl().getTotal(upDataFileBean.getTastId()));
                } else {
                    // not start
                    holder.updateNotDownloaded(status, TasksManager.getImpl().getSoFar(upDataFileBean.getTastId())
                            , TasksManager.getImpl().getTotal(upDataFileBean.getTastId()));
                }
            } else {
                holder.tv_describe.setText("加载中...");
                holder.layout_action.setEnabled(false);
            }
        }

        @Override
        public int getItemCount() {
            return TasksManager.getImpl().getTaskCounts();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void clearDownLoadFile() {
        TasksManager.getImpl().clearData();
        adapter.notifyDataSetChanged();
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_70);
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(DownLoadFileManagerActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(DownLoadFileManagerActivity.this, R.color.base_red))
                        .setText("  删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };

}
