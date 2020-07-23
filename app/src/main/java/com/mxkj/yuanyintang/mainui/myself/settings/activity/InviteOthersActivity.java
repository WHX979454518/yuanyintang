package com.mxkj.yuanyintang.mainui.myself.settings.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.GetBitmapFromView;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class InviteOthersActivity extends BaseActivity {

    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.headImg)
    CircleImageView headImg;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.inCodeNum)
    TextView inCodeNum;
    @BindView(R.id.tv_myIncode)
    TextView tvMyIncode;
    @BindView(R.id.ll_myIncode)
    LinearLayout llMyIncode;
    @BindView(R.id.rl_myIncode_Pic)
    RelativeLayout llMyIncodePic;
    @BindView(R.id.savePic)
    ImageView savePic;
    @BindView(R.id.sina)
    TextView sina;
    @BindView(R.id.wechat_moments)
    TextView wechatMoments;
    @BindView(R.id.wechat)
    TextView wechat;
    @BindView(R.id.others)
    TextView others;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.img_head_share)
    CircleImageView imgHeadShare;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.tv_invite_red)
    TextView tvInviteRed;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.qr_code)
    ImageView qrCode;
    @BindView(R.id.tv_his)
    TextView tvHis;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.tv_incode)
    TextView tvIncode;
    @BindView(R.id.rl_share_layout)
    RelativeLayout rlShareLayout;
    @BindView(R.id.rl_invite_layout)
    RelativeLayout rlInviteLayout;
    private File fileMyIncode;
    private MusicBean.ShareDataBean shareDataBean;
    private MusicBean musicBean;
    private UMImage imageLyric;
    private UserInfo.DataBean userInfoBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invute_others);
        ButterKnife.bind(this);
        StatusBarUtil.baseTransparentStatusBar(this);
        UserInfoUtil.getUserInfoByJson(CacheUtils.INSTANCE.getString(InviteOthersActivity.this,Constants.User.USER_JSON), new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null) {
                    userInfoBean = infoBean.getData();
                }
            }
        });
        initView();
    }

    private void initView() {
        if (userInfoBean != null) {
            tvNickName.setText("亲爱的 " + userInfoBean.getNickname());
            nickName.setText(userInfoBean.getNickname());
            inCodeNum.setText(userInfoBean.getCount().getMycodeCount() + "");
            tvMyIncode.setText(userInfoBean.getMycode());
            tvIncode.setText(userInfoBean.getMycode());
            Glide.with(this).load(userInfoBean.getHead_link()).asBitmap().placeholder(R.drawable.nothing).error(R.drawable.nothing).into(imgHeadShare);
            Glide.with(this).load(userInfoBean.getHead_link()).asBitmap().placeholder(R.drawable.nothing).error(R.drawable.nothing).into(headImg);
            Glide.with(this).load(userInfoBean.getShare_mycode_url_link()).asBitmap().placeholder(R.drawable.nothing).error(R.drawable.nothing).into(qrCode);
            llMyIncode.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager copy = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (userInfoBean != null) {
                        copy.setText("我正在源音塘听二次元音乐，输入我的邀请码" + userInfoBean.getMycode() + " 和我一起吧，还可以参与精彩的线上活动哦！");
                        Toast.makeText(InviteOthersActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.finish, R.id.share, R.id.savePic, R.id.sina, R.id.wechat_moments, R.id.wechat, R.id.others})
    public void onClick(View view) {
        //设置分享链接
        UMWeb web = new UMWeb(userInfoBean.getShare_mycode_url());//shareUrl
//      web.setTitle(userInfoBean.getNickname());//标题
        web.setDescription("我正在源音塘听二次元音乐，输入我的邀请码" + userInfoBean.getMycode() + " 和我一起吧，还可以参与精彩的线上活动哦！");
        web.setTitle("我正在源音塘听二次元音乐，输入我的邀请码" + userInfoBean.getMycode() + " 和我一起吧，还可以参与精彩的线上活动哦！");
        UMImage thumbweb;
        UMImage image;
        image = new UMImage(this, R.drawable.invate_share);
        thumbweb = new UMImage(this, R.drawable.invate_share);
        thumbweb.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.setThumb(thumbweb);//缩略// 图
        web.setThumb(thumbweb);  //缩略图  注意在新浪平台，缩略图属于必传参数，否则会报错
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.share:
                shareInviteCode("web");
                break;
            case R.id.savePic:
                showLoadingView();
                createShareBitmap();
                if (fileMyIncode != null) {
                    hideLoadingView();
                    initInviteShareDialog();
                }
                break;
            case R.id.sina:
                createShareBitmap();
                ShareThird(SHARE_MEDIA.SINA, web);
                break;
            case R.id.wechat_moments:
                createShareBitmap();
                ShareThird(SHARE_MEDIA.WEIXIN_CIRCLE, web);
                break;
            case R.id.wechat:
                createShareBitmap();
                ShareThird(SHARE_MEDIA.WEIXIN, web);
                break;
            case R.id.others:
                shareInviteCode("web");
                break;
        }
    }

    private void ShareThird(SHARE_MEDIA share_media, UMWeb web) {
        new ShareAction(this)
                .setPlatform(share_media)
                .withText(web.getDescription())
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }

    /**
     * 底部弹框分享
     */
    public void shareInviteCode(String shareType) {
        createShareBitmap();
        if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
            if (CacheUtils.INSTANCE.getBoolean(InviteOthersActivity.this,Constants.User.IS_LOGIN, false) == true) {
                musicBean = new MusicBean();
                shareDataBean = new MusicBean.ShareDataBean();
//                shareDataBean.setType("img");
                shareDataBean.setType(shareType);
                if (fileMyIncode != null) {
                    shareDataBean.setImgFilePath(fileMyIncode.getAbsolutePath());
                }
                if (userInfoBean != null) {
                    shareDataBean.setUid(userInfoBean.getId());
                    shareDataBean.setTopicContent("我正在源音塘听二次元音乐，输入我的邀请码" + userInfoBean.getMycode() + " 和我一起吧，还可以参与精彩的线上活动哦！");
                    shareDataBean.setTitle(userInfoBean.getNickname());
                    shareDataBean.setShareUrl(userInfoBean.getShare_mycode_url());
                    shareDataBean.setShareMyIncode(true);
                    musicBean.setShareDataBean(shareDataBean);
                    ShareBottomDialog shareBottomDialog = new ShareBottomDialog(InviteOthersActivity.this, musicBean);
                    shareBottomDialog.show();
                }
            } else {
                goActivity(LoginRegMainPage.class);
            }
        }
    }

    /***
     * 创建分享图片bitmap
     * */
    private void createShareBitmap() {
        rlShareLayout.setVisibility(View.VISIBLE);
        Bitmap viewBitmap = GetBitmapFromView.createViewBitmap(this, rlShareLayout);
        fileMyIncode = GetBitmapFromView.saveMyBitmap("邀请码", viewBitmap);
        imageLyric = new UMImage(this, fileMyIncode);
        rlShareLayout.setVisibility(View.GONE);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            setSnackBar("分享成功", "", R.drawable.icon_success);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            setSnackBar("分享失败", "", R.drawable.icon_fails);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            setSnackBar("取消分享", "", R.drawable.icon_tips_bad);
        }
    };

    /**
     * 图片保存成功弹窗
     */
    public void initInviteShareDialog() {
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    fileMyIncode.getAbsolutePath(), fileMyIncode.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        View view = View.inflate(this, R.layout.dialog_invite_share, null);
        diaLogBuilder = new DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .show();
        diaLogBuilder.setCanceledOnTouchOutside(true);
        TextView tv_know = view.findViewById(R.id.tv_know);
        ImageView diss_dialog = view.findViewById(R.id.img_close_dialog);
        diss_dialog.setVisibility(View.VISIBLE);
        tv_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareInviteCode("img");
                diaLogBuilder.setDismiss();
            }
        });
        diss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogBuilder.setDismiss();
            }
        });
    }

}
