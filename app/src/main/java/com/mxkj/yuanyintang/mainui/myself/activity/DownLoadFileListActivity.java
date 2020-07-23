package com.mxkj.yuanyintang.mainui.myself.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.database.updatafilesql.TasksManager;
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileBean;
import com.mxkj.yuanyintang.database.updatafilesql.UpDataFileDao;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.file.SdUtil;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.mainui.myself.apdater.DownLoadFileAdapter;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.file.DelFile;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.MusicOpenationEvent;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.mxkj.yuanyintang.utils.constant.Constants.User.MUSIC_DIR_PRIVATE;

/**
 * Created by LiuJie on 2017/10/10.
 */

public class DownLoadFileListActivity extends StandardUiActivity {
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.tv_total_space)
    TextView tv_total_space;
    @BindView(R.id.tv_all_playing)
    RelativeLayout tv_all_playing;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.tv_all_playing_tv)
    TextView tv_all_playing_tv;
    @BindView(R.id.tv_batch_operation)
    TextView tv_batch_operation;
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.iv_right)
    GifImageView iv_right;
    @BindView(R.id.layout_right)
    View layout_right;
    @BindView(R.id.recyclerview)
    SwipeMenuRecyclerView recyclerview;
    @BindView(R.id.layout_not_data)
    LinearLayout layout_not_data;

    DownLoadFileAdapter downLoadFileAdapter;
    List<UpDataFileBean> dataBeanList = new ArrayList<>();
    private Disposable mDownLoadFileDisposable;
    private View musicDirDialogView;

    @Override
    public int setLayoutId() {
        return R.layout.activity_download_file_list;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        RxView.clicks(iv_left)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(layout_right)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        FileDownloader.getImpl().pauseAll();
                        goActivity(DownLoadFileManagerActivity.class);
                    }
                });
        RxView.clicks(tv_total_space)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        boolean b = SdUtil.externalMemoryAvailable();
                        if (b == false) {
                            setSnackBar("请检查SD卡状态", "", R.drawable.icon_tips_bad);
                        } else {
                            initMusicDirDialog();
                        }
                    }
                });
        RxView.clicks(tv_all_playing)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
                            sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                            play.setImageDrawable(getResources().getDrawable(R.mipmap.icon_index_songlist_play));
                        }else {
                        MaterialDialog("是否全部播放？", null, null, new MaterialDialogBtnClickListener() {
                            @Override
                            public void onBtnClick(int code) {
                                if (code == 1) {
                                    Observable.fromArray(dataBeanList)
                                            .flatMap(new Function<List<UpDataFileBean>, ObservableSource<UpDataFileBean>>() {
                                                @Override
                                                public ObservableSource<UpDataFileBean> apply(@NonNull List<UpDataFileBean> upDataFileBeen) throws Exception {
                                                    return Observable.fromIterable(upDataFileBeen);
                                                }
                                            })
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<UpDataFileBean>() {
                                                @Override
                                                public void accept(@NonNull UpDataFileBean dataFileBean) throws Exception {
                                                    Log.e(TAG, "localPlay----: accept" + dataFileBean.getSongName());
                                                    PlayCtrlUtil.INSTANCE.localityPlay(DownLoadFileListActivity.this, dataFileBean, false, dataFileBean.getMusic_id().equals(dataBeanList.get(0).getMusic_id()) ? true : false);
                                                    play.setImageDrawable(getResources().getDrawable(R.mipmap.icon_index_songlist_play2));
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
                });

        initSwipeRecyclerView();
    }

    private void getData() {
        final List<UpDataFileBean> upDataFileBeanList = new UpDataFileDao(MainApplication.Companion.getContext()).listHelper();
//        if (null != upDataFileBeanList&&upDataFileBeanList.size()>0) {
        if (null != upDataFileBeanList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long size = dirSize(new File(FileDownloadUtils.getDefaultSaveRootPath()));
                    if (null != tv_batch_operation) {
                        tv_batch_operation.setText("占用手机" + ((size > 1048576) ? (size / 1048576) + "M" : (size > 1024) ? (size / 1024) + "kb" : size + "字节") + "空间");
                    }

                }
            });

            if (upDataFileBeanList.size() <= 0) {
                layout_right.setVisibility(View.INVISIBLE);
                layout_right.setClickable(false);
                layout_not_data.setVisibility(View.VISIBLE);
            } else {
                layout_right.setVisibility(View.VISIBLE);
                layout_right.setClickable(true);
            }

        }
        List<UpDataFileBean> upDataFileBeanList2 = new UpDataFileDao(MainApplication.Companion.getContext()).isQueryDataList("success", true);
        if (null != upDataFileBeanList2 && null != downLoadFileAdapter) {
            dataBeanList.clear();
            dataBeanList.addAll(upDataFileBeanList2);
            downLoadFileAdapter.notifyDataSetChanged();
            tv_all_playing_tv.setText("全部播放（共" + dataBeanList.size() + "首）");
        }
    }

    private void isDownload() {
        Boolean isDown = false;
        List<UpDataFileBean> upDataFileBeanList = TasksManager.getImpl().getModelList();
        for (int i = 0; i < upDataFileBeanList.size(); i++) {
            int status = TasksManager.getImpl().getStatus(TasksManager.getImpl().getModelList().get(i).getTastId(), TasksManager.getImpl().getModelList().get(i).getPath());
            if (status != -3) {
                isDown = true;
            }
        }
        if (isDown) {
            tv_tip.setVisibility(View.VISIBLE);
            iv_right.setImageResource(R.drawable.icon_downloading);
            final MediaController mc = new MediaController(this);
            mc.setMediaPlayer((GifDrawable) iv_right.getDrawable());
            mc.setAnchorView(iv_right);
        } else {
            tv_tip.setVisibility(View.GONE);
            iv_right.setImageResource(R.drawable.icon_download_file);
        }
    }

    private void initSwipeRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        recyclerview.setSwipeMenuCreator(swipeMenuCreator);
        downLoadFileAdapter = new DownLoadFileAdapter(dataBeanList, getSupportFragmentManager());
        recyclerview.setAdapter(downLoadFileAdapter);
        recyclerview.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // 菜单在RecyclerView的Item中的Position。
                RxBus.getDefault().post(new MusicOpenationEvent(adapterPosition, 6));
            }
        });
    }

    @Override
    protected void initData() {

    }

    private long dirSize(File dir) {
        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i]);
                } else {
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    @Override
    protected void initEvent() {
        initSpace();
        mDownLoadFileDisposable = RxBus.getDefault().toObservable(MusicOpenationEvent.class)
                .subscribeWith(new RxBusSubscriber<MusicOpenationEvent>() {
                    @Override
                    protected void onEvent(MusicOpenationEvent musicOpenationEvent) throws Exception {
                        if ((6 == musicOpenationEvent.getType())) {
                            DelFile.deleteFile(dataBeanList.get(musicOpenationEvent.getPosition()).getPath());
                            new UpDataFileDao(MainApplication.Companion.getContext()).deleteHelper(dataBeanList.get(musicOpenationEvent.getPosition()).getId());
                            getData();
                        }
                    }
                });

        RxBus.getDefault().add(mDownLoadFileDisposable);
    }

    private void initSpace() {
        boolean aBoolean = CacheUtils.INSTANCE.getBoolean(this, MUSIC_DIR_PRIVATE, false);
        long totalInternalMemorySize, totalExternalMemorySize, availableInternalMemorySize, availableExternalMemorySize;
        if (aBoolean == true) {//内部存储
            totalInternalMemorySize = SdUtil.getTotalInternalMemorySize();
            availableInternalMemorySize = SdUtil.getAvailableInternalMemorySize();
            String s = SdUtil.formatSize(this, (totalInternalMemorySize - availableInternalMemorySize));
            tv_total_space.setText("已使用" + s + ",剩余" + SdUtil.formatSize(this, availableInternalMemorySize));
        } else {//外部存储
            totalExternalMemorySize = SdUtil.getTotalExternalMemorySize();
            availableExternalMemorySize = SdUtil.getAvailableExternalMemorySize();
            String s = SdUtil.formatSize(this, (totalExternalMemorySize - availableExternalMemorySize));
            tv_total_space.setText("已使用" + s + ",剩余" + SdUtil.formatSize(this, availableExternalMemorySize));
        }
    }

    /**
     * 存储位置选择弹框
     */
    public void initMusicDirDialog() {
        musicDirDialogView = View.inflate(this, R.layout.dialog_music_dir, null);
        final DiaLogBuilder pushDiaLogBuilder = new DiaLogBuilder(this)
                .setContentView(musicDirDialogView)
                .setFullScreen()
                .setGrvier(Gravity.CENTER).show();
        TextView odd_in = musicDirDialogView.findViewById(R.id.odd_in);
        TextView odd_ex = musicDirDialogView.findViewById(R.id.odd_ex);
        TextView tv_in_path = musicDirDialogView.findViewById(R.id.tv_in_path);
        TextView tv_ex_path = musicDirDialogView.findViewById(R.id.tv_ex_path);
        TextView tv_sure = musicDirDialogView.findViewById(R.id.tv_sure);
        TextView tv_cancel = musicDirDialogView.findViewById(R.id.tv_cancel);
        RelativeLayout rl_in_dir = musicDirDialogView.findViewById(R.id.rl_in_dir);
        RelativeLayout rl_ex_dir = musicDirDialogView.findViewById(R.id.rl_ex_dir);
        final CheckBox cb_in = musicDirDialogView.findViewById(R.id.cb_in);
        final CheckBox cb_ex = musicDirDialogView.findViewById(R.id.cb_ex);
        try {
            tv_in_path.setText(Environment.getDataDirectory().getPath() + File.separator + "Music");
            tv_ex_path.setText(Constants.Path.APP_PATH + File.separator + "Music");
        } catch (RuntimeException e) {
        }

        odd_in.setText(SdUtil.formatSize(this, SdUtil.getAvailableInternalMemorySize()) + "/" + SdUtil.formatSize(this, SdUtil.getTotalInternalMemorySize()));
        odd_ex.setText(SdUtil.formatSize(this, SdUtil.getAvailableExternalMemorySize()) + "/" + SdUtil.formatSize(this, SdUtil.getTotalExternalMemorySize()));
        cb_in.setChecked(CacheUtils.INSTANCE.getBoolean(DownLoadFileListActivity.this, MUSIC_DIR_PRIVATE, true));
        cb_ex.setChecked(!CacheUtils.INSTANCE.getBoolean(DownLoadFileListActivity.this, MUSIC_DIR_PRIVATE, true));
        rl_in_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb_in.setChecked(true);
                cb_ex.setChecked(false);
            }
        });
        rl_ex_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb_in.setChecked(false);
                cb_ex.setChecked(true);
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushDiaLogBuilder.setDismiss();
                CacheUtils.INSTANCE.setBoolean(DownLoadFileListActivity.this, MUSIC_DIR_PRIVATE, cb_in.isChecked());
                initSpace();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushDiaLogBuilder.setDismiss();
            }
        });
        pushDiaLogBuilder.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        isDownload();
        initSpace();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().remove(mDownLoadFileDisposable);
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
            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
            }
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(DownLoadFileListActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(DownLoadFileListActivity.this, R.color.base_red))
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
