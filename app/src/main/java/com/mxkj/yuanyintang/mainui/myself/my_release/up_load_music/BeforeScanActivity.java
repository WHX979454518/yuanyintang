package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.Song;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SongDao;
import com.mxkj.yuanyintang.utils.app.HandlerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 扫描主页面
 */
public class BeforeScanActivity extends StandardUiActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_begin)
    TextView tvBegin;
    @BindView(R.id.ck_scan)
    CheckBox ckScan;
    private HandlerUtil handler;
    private ArrayList<Song> entities = new ArrayList<>();
    private boolean isScanShortSong;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_before_scan;
    }

    @Override
    protected void initView() {
        handler = HandlerUtil.getInstance(BeforeScanActivity.this);
        ButterKnife.bind(this);
        setTitleText("扫描歌曲");
    }

    @Override
    protected void initData() {
        final SongDao songDao = new SongDao(BeforeScanActivity.this);
        entities = (ArrayList<Song>) songDao.listHelper();
        if (entities != null && entities.size() > 0) {
            Observable.fromArray(entities)
                    .flatMap(new Function<List<Song>, ObservableSource<Song>>() {
                        @Override
                        public ObservableSource<Song> apply(@io.reactivex.annotations.NonNull List<Song> dataBeen) throws Exception {
                            return Observable.fromIterable(dataBeen);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Song>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull Song data) {
                            String path = data.getPath();
                            File file = new File(path);
                            if (!file.exists()) {
                                entities.remove(data);
                            }
                        }


                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            if (entities != null && entities.size() > 0) {
                                startActivity(new Intent(BeforeScanActivity.this, LocalMusicListActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onComplete() {
                            if (entities != null && entities.size() > 0) {
                                startActivity(new Intent(BeforeScanActivity.this, LocalMusicListActivity.class));
                            }
                            finish();
                        }
                    });
        }
    }

    @Override
    protected void initEvent() {
        ckScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    //刷新列表
    @SuppressLint("StaticFieldLeak")
    @AfterPermissionGranted(1)

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

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

    @OnClick({R.id.tv_begin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_begin:
                Intent intent = new Intent(this, ScaningActivity.class);
                intent.putExtra("isScanShortSong", isScanShortSong);
                intent.putExtra("isList", getIntent().getBooleanExtra("isList",false));
                Log.e("sssssss",""+getIntent().getStringExtra("biaoshi"));
                if(null!= getIntent().getStringExtra("biaoshi")&&getIntent().getStringExtra("biaoshi").equals("1")){
                    intent.putExtra("biaoshi",getIntent().getStringExtra("biaoshi"));
                }else {
                    intent.putExtra("biaoshi",getIntent().getStringExtra("2"));
                }

                startActivity(intent);
                finish();
                break;
        }
    }
}
