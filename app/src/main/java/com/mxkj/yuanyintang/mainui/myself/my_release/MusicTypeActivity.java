package com.mxkj.yuanyintang.mainui.myself.my_release;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
import com.mxkj.yuanyintang.databinding.ItemMusicTypeBinding;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.myself.activity.ChooseMusicTag;
import com.mxkj.yuanyintang.mainui.myself.bean.MusicTypeBean;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity.GET_MUSIC_TYPE_RESU_CODE;

public class MusicTypeActivity extends StandardUiActivity {
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.lv_musicType)
    ListView lvMusicType;
    private List<MusicTypeBean.DataBean> typeBeanList = new ArrayList<>();
    BaseBindingAdapter<MusicTypeBean.DataBean, ItemMusicTypeBinding> adapterType;

    @Override
    public int setLayoutId() {
        return R.layout.activity_music_type;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        adapterType = new BaseBindingAdapter<MusicTypeBean.DataBean, ItemMusicTypeBinding>(this, typeBeanList, R.layout.item_music_type) {
            @Override
            public void bindObj(ItemMusicTypeBinding itemAddmusicTonewsheetBinding, MusicTypeBean.DataBean dataBean) {
                if (dataBean != null && dataBean.getTitle() != null) {
                    itemAddmusicTonewsheetBinding.setObj(dataBean);
                }
            }
        };
        lvMusicType.setAdapter(adapterType);
        lvMusicType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MusicTypeActivity.this, ChooseMusicTag.class);
                intent.putExtra("cateId", typeBeanList.get(i).getId());
                startActivity(intent);
                Intent intent1 = new Intent();
                intent1.putExtra("cateBean", typeBeanList.get(i));
                setResult(GET_MUSIC_TYPE_RESU_CODE, intent1);
                finish();
                CacheUtils.INSTANCE.setString(MusicTypeActivity.this, "selectMusicTag", "");
            }
        });

    }

    @Override
    protected void initData() {
        getMusicType();

    }

    private void getMusicType() {
        HttpParams params = new HttpParams();
        params.put("model_alias", "music");
        NetWork.INSTANCE.getMusicType(this, params, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                MusicTypeBean musicTypeBean = JSON.parseObject(resultData, MusicTypeBean.class);
                typeBeanList = musicTypeBean.getData();
                adapterType.setList(typeBeanList);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    @Override
    protected void initEvent() {
    }

    @OnClick({R.id.leftButton, R.id.rightButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
                break;
        }
    }
}
