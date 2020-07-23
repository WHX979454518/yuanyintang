package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.MusicComparator;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.PreferencesUtility;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SideBar;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.Song;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SongDao;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.SortOrder;
import com.mxkj.yuanyintang.utils.app.HandlerUtil;
import com.mxkj.yuanyintang.utils.app.TimeUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LocalMusicListActivity extends StandardUiActivity {
    private String searchWord;
    @BindView(R.id.recyclerview)
    ListView listView;
    @BindView(R.id.fram_list)
    FrameLayout framList;
    @BindView(R.id.re_scan)
    TextView re_scan;
    @BindView(R.id.tv_total_song)
    TextView tv_total_song;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.dialog_text)
    TextView dialogText;
    @BindView(R.id.sidebar)
    SideBar sidebar;
    private SingleCheckListAdapter mAdapter;
    private PreferencesUtility mPreferences;
    private HashMap<String, Integer> positionMap = new HashMap<>();
    private boolean isAZSort = true;
    private HandlerUtil handler;
    private ArrayList<Song> entities = new ArrayList<>();
    ArrayList<Song> searchList = new ArrayList<>();
    private Song selecteSongBean;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_local_music_list;
    }

    @Override
    protected void initView() {
        handler = HandlerUtil.getInstance(LocalMusicListActivity.this);
        ButterKnife.bind(this);
        showRightButton();
        setRightButton("上传", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTitleText("扫描歌曲");
        mAdapter = new SingleCheckListAdapter();
        listView.setAdapter(mAdapter);

        if(null!= getIntent().getStringExtra("biaoshi")){
            Log.e("sssssss",""+getIntent().getStringExtra("biaoshi"));
            String biaoshiflag = getIntent().getStringExtra("biaoshi");
            if(biaoshiflag.equals("1") || biaoshiflag == "1"){
                hideRightButton();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selecteSongBean = mAdapter.getItem(i);
                mAdapter.updateStatus(selecteSongBean);
                RxBus.getDefault().post(entities.get(i));
            }
        });
    }

    @Override
    protected void initData() {
        SongDao songDao = new SongDao(LocalMusicListActivity.this);
        entities = (ArrayList<Song>) songDao.listHelper();
        if (entities != null && entities.size() > 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    freshAdapter(entities);
                }
            });
        }
    }

    @Override
    protected void initEvent() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                initData();
                if (editable.length() == 0) {
                    searchWord = null;
                } else {
                    searchWord = etSearch.getText().toString();
                    searchList.clear();
                    Observable.fromArray(entities)
                            .flatMap(new Function<List<Song>, ObservableSource<Song>>() {
                                @Override
                                public ObservableSource<Song> apply(@io.reactivex.annotations.NonNull List<Song> songs) throws Exception {
                                    return Observable.fromIterable(songs);
                                }
                            })
                            .filter(new Predicate<Song>() {
                                @Override
                                public boolean test(Song song) throws Exception {
                                    return TextUtils.isEmpty(etSearch.getText()) ? false : song.getSongName().contains(etSearch.getText().toString());
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Song>() {
                                @Override
                                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                                }

                                @Override
                                public void onNext(@io.reactivex.annotations.NonNull Song song) {
                                    searchList.add(song);
                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                    entities = searchList;
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onComplete() {
                                    entities = searchList;
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });
    }

    private void freshAdapter(ArrayList<Song> entities) {
        if (entities != null && entities.size() > 0) {
            tv_total_song.setText("共扫描出" + entities.size() + "首歌曲");
            mPreferences = PreferencesUtility.getInstance(LocalMusicListActivity.this);
            isAZSort = mPreferences.getSongSortOrder().equals(SortOrder.SongSortOrder.SONG_A_Z);
            if (isAZSort) {
                Collections.sort(entities, new MusicComparator());
                for (int i = 0; i < entities.size(); i++) {
                    if (positionMap.get(entities.get(i).getSort()) != null) {
                        positionMap.put(entities.get(i).getSort(), i);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
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

    @OnClick({R.id.re_scan, R.id.rl_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_scan:
                BaseConfirmDialog.Companion.newInstance().confirmText("扫描").cancleText("取消").isOneBtn(false).title("重新扫描").content("是否重新扫描歌曲？扫描可能持续\n" + "几分钟").showDialog(LocalMusicListActivity.this, new BaseConfirmDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        SongDao songDao = new SongDao(LocalMusicListActivity.this);
                        songDao.deleteAllHelper();
                        entities = (ArrayList<Song>) songDao.listHelper();
                        startActivity(new Intent(LocalMusicListActivity.this, BeforeScanActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancle() {

                    }
                });
                break;
            case R.id.rl_search:
                searchWord = etSearch.getText().toString();
                if (TextUtils.isEmpty(searchWord)) {
                    return;
                }
                searchList.clear();
                initData();
                Observable.fromArray(entities)
                        .flatMap(new Function<List<Song>, ObservableSource<Song>>() {
                            @Override
                            public ObservableSource<Song> apply(@io.reactivex.annotations.NonNull List<Song> songs) throws Exception {
                                return Observable.fromIterable(songs);
                            }
                        })
                        .filter(new Predicate<Song>() {
                            @Override
                            public boolean test(Song song) throws Exception {
                                return TextUtils.isEmpty(etSearch.getText()) ? false : song.getSongName().contains(etSearch.getText().toString());
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Song>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull Song song) {
                                searchList.add(song);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                entities = searchList;
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onComplete() {
                                entities = searchList;
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                break;
        }
    }

    /**
     * 这个adapter本来是配合右侧按照拼音首字母排序的竖直bar用的，后面产品又去掉了那个bar
     */
    private class SingleCheckListAdapter extends BaseAdapter {
        private int count;

        public SingleCheckListAdapter() {
        }

        public void setMaxCount(int count) {
            this.count = count;
        }

        @Override
        public int getCount() {
            if (entities != null && entities.size() > 0) {
                if (count != 0) {
                    if (count > 50) {
                        return 50;
                    } else {
                        return count;
                    }
                }
                return entities.size();
            }
            return 0;
        }

        @Override
        public Song getItem(int position) {
            return entities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Nullable
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_list_item, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.singer = convertView.findViewById(R.id.singer);
            holder.songname = convertView.findViewById(R.id.songname);
            holder.cbSelect = convertView.findViewById(R.id.cb_select);
            holder.durition = convertView.findViewById(R.id.durition);

            if (entities.size() > position) {
                Song entity = entities.get(position);
                if (searchWord != null) {
                    holder.songname.setSpecifiedTextsColor(entity.getSongName(), searchWord, Color.parseColor("#ff6699"));
                } else {
                    holder.songname.setText(entity.getSongName());
                }
                holder.singer.setText(entity.getPath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "").replace(entity.getSongName(), ""));
                holder.durition.setText(TimeUtils.getCurrentTimeMS(entity.getDuration() * 1000));
                if (entity.isChecked == 1) {
                    holder.cbSelect.setChecked(true);
                } else {
                    holder.cbSelect.setChecked(false);
                }
            }

            return convertView;
        }

        public void updateStatus(@Nullable Song entity) {
            if (entities == null || entity == null) {
                return;
            }
            for (int i = 0; i < entities.size(); i++) {
                if (entity.getPath().equals(entities.get(i).getPath())) {
                    entities.get(i).setIsChecked(1);
                } else {
                    entities.get(i).setIsChecked(0);
                }
            }
            notifyDataSetChanged();
        }

        class ViewHolder {
            CheckBox cbSelect;
            SearTextView songname;
            SearTextView singer;
            TextView durition;
        }
    }
}
