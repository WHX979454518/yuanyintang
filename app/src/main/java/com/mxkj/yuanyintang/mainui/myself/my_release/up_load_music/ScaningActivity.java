package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.CircleProgressView;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.MusicLoader;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.Song;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SongDao;
import com.mxkj.yuanyintang.mainui.newapp.myself.LocalMusicActivity;
import com.mxkj.yuanyintang.utils.app.HandlerUtil;
import com.mxkj.yuanyintang.utils.file.SdUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 正在扫描。。。（同时间扫描内置SD卡和外置SD卡）
 */
public class ScaningActivity extends StandardUiActivity implements EasyPermissions.PermissionCallbacks {
    String TAG = "LocalMusicListActivity";
    @BindView(R.id.scan_progeress)
    CircleProgressView scanProgeress;
    @BindView(R.id.fram_scaning)
    FrameLayout framScaning;
    @BindView(R.id.tv_scan_percent)
    TextView tvScanPercent;
    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.tv_music_num)
    TextView tvMusicNum;
    @BindView(R.id.ck_scaning)
    CheckBox ckScaning;
    @BindView(R.id.img_scaning)
    ImageView imgScaning;
    private HandlerUtil handler;
    private ArrayList<Song> entities = new ArrayList<>();
    private boolean isScanShortSong = false;
    public Disposable disposable;
    private String scanIngPath = "";
    private int scanIngSize = 0;


    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scaning;
    }

    @Override
    protected void initView() {
        handler = HandlerUtil.getInstance(ScaningActivity.this);
        ButterKnife.bind(this);
        setTitleText("扫描歌曲");
    }

    @Override
    protected void initData() {
        Glide.with(this).load(R.drawable.scan_ing).asGif().into(imgScaning);
        List<String> extSDCardPath = SdUtil.getExtSDCardPathList();
        if (extSDCardPath != null && extSDCardPath.size() > 0) {
            for (int i = 0; i < extSDCardPath.size(); i++) {
                Log.e(TAG, extSDCardPath.get(i));
                File sdPath = new File(extSDCardPath.get(i));
                reloadAdapter(sdPath);
            }
        }
    }

    @Override
    protected void initEvent() {
        isScanShortSong = getIntent().getBooleanExtra("isScanShortSong", false);
        ckScaning.setChecked(isScanShortSong);
        ckScaning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isScanShortSong = true;
                } else {
                    isScanShortSong = false;
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @AfterPermissionGranted(1)
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @OnClick({R.id.tv_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_stop:
                BaseConfirmDialog.Companion.newInstance().content("扫描就快完啦,再等等呀~").title("取消扫描").cancleText("再等等").confirmText("取消扫描").showDialog(this, new BaseConfirmDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        if (entities != null && entities.size() > 0) {
                            if (getIntent().getBooleanExtra("isList",false)==true) {
                                startActivity(new Intent(ScaningActivity.this, LocalMusicActivity.class));
                            }else{
                                startActivity(new Intent(ScaningActivity.this, LocalMusicListActivity.class));

                            }
                        } else {
                            SongDao songDao = new SongDao(ScaningActivity.this);
                            songDao.deleteAllHelper();
                            if (getIntent().getBooleanExtra("isList",false)==true) {
                                startActivity(new Intent(ScaningActivity.this, LocalMusicActivity.class));
                            }else{
                                startActivity(new Intent(ScaningActivity.this, BeforeScanActivity.class));
                            }
                        }
                        finish();
                    }
                    @Override
                    public void onCancle() {

                    }
                });
                break;
        }
    }
    @SuppressLint("StaticFieldLeak")
    @AfterPermissionGranted(1)
    public void reloadAdapter(File folders) {
        Observable.just(folders).flatMap(new Function<File, ObservableSource<File>>() {
            @Override
            public ObservableSource<File> apply(@NonNull File file) throws Exception {
                return listFile(file);
            }
        }).filter(new Predicate<File>() {
            @Override
            public boolean test(final File file) throws Exception {
                String filename = file.getName();
                int j = filename.lastIndexOf(".");
                String suf = filename.substring(j + 1);//得到文件后缀
//                mp3,wma,wav,m4a,ape
                if (suf.equalsIgnoreCase("mp3") || suf.equalsIgnoreCase("wma") || suf.equalsIgnoreCase("wav") || suf.equalsIgnoreCase("m4a") || suf.equalsIgnoreCase("ape")) {
                    Song songInfo = MusicLoader.getSongInfo(file);
                    if (songInfo != null) {
                        SongDao songDao = new SongDao(ScaningActivity.this);
                        if (isScanShortSong == false) {
                            if (songInfo.getDuration() > 60) {
                                songDao.add(songInfo);
                                entities.add(songInfo);
                            }
                        } else {
                            songDao.add(songInfo);
                            entities.add(songInfo);
                        }
                    }
                }
                scanIngSize = entities.size();
                String[] strs = file.getPath().split("/");
                scanIngPath = strs[strs.length - 2].toString() + "/" + strs[strs.length - 1].toString();
                return file.exists();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(File file) {
                tvMusicNum.setText("已找到" + scanIngSize + "首歌曲");
                tvPath.setText(scanIngPath);
            }

            @Override
            public void onError(Throwable e) {
                startActivity(new Intent(ScaningActivity.this, LocalMusicListActivity.class));
            }

            @Override
            public void onComplete() {

//                startActivity(new Intent(ScaningActivity.this, LocalMusicListActivity.class));
//                finish();

                Intent intent = new Intent(ScaningActivity.this, LocalMusicListActivity.class);
                Log.e("sssssss",""+getIntent().getStringExtra("biaoshi"));
                intent.putExtra("biaoshi",getIntent().getStringExtra("biaoshi"));
                startActivity(intent);
                finish();
            }
        });
    }
    private Observable<File> listFile(File file) {
        if (file.isDirectory()) {
            Observable<File> ob = Observable.fromArray(file.listFiles())
                    .flatMap(new Function<File, ObservableSource<File>>() {
                        @Override
                        public ObservableSource<File> apply(@NonNull File file) throws Exception {
                            return listFile(file);
                        }
                    });
            return ob;
        } else {
            return Observable.just(file);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        BaseConfirmDialog.Companion.newInstance().content("扫描即将完成,要退出吗").title("取消扫描").cancleText("再等等").confirmText("取消扫描").showDialog(this, new BaseConfirmDialog.onBtClick() {
            @Override
            public void onConfirm() {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
                if (entities != null && entities.size() > 0) {
                    startActivity(new Intent(ScaningActivity.this, LocalMusicListActivity.class));
                } else {
                    SongDao songDao = new SongDao(ScaningActivity.this);
                    songDao.deleteAllHelper();
                    startActivity(new Intent(ScaningActivity.this, BeforeScanActivity.class));
                }
                finish();
            }

            @Override
            public void onCancle() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


}
