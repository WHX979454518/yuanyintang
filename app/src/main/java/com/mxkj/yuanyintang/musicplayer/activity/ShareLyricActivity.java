package com.mxkj.yuanyintang.musicplayer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.activity.MainActivity;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.GetBitmapFromView;
import com.mxkj.yuanyintang.widget.NavigationBar;
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;
import static com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.CAMERA_REQUEST_CODE;

public class ShareLyricActivity extends StandardUiActivity {
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 2;
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.lv_lyric)
    TextView lvLyric;
    @BindView(R.id.tv_songname)
    TextView tvSongname;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.img_song)
    ImageView img_song;
    @BindView(R.id.qrcode_img)
    ImageView qrcode_img;
    @BindView(R.id.ll_share_lyric)
    LinearLayout llShareLyric;
    @BindView(R.id.ll_choosepic)
    LinearLayout llChoosepic;
    @BindView(R.id.ll_share_view)
    LinearLayout ll_share_view;
    @BindView(R.id.save_img_ll)
    LinearLayout save_img_ll;


    @BindView(R.id.show)
    ImageView show;


    private ArrayList<String> selectedLyric = new ArrayList<>();
    private int maxNum_photo = 1;
    private File fileLyric;

    private String qrcodeurl = "";


    private boolean showphoto = false;
    @Override
    public int setLayoutId() {
        return R.layout.activity_share_lyric;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        navigationBar.init();
        setTitleText("生成海报");
//        setRightButtonText("保存");
        hideRightButton();
        getNavigationBar().getRightButton().setTextColor(Color.parseColor("#ff4c55"));
        selectedLyric = getIntent().getStringArrayListExtra("selectedLyric");

        qrcodeurl  = getIntent().getStringExtra("qrcodeurl");

        if (selectedLyric != null) {
            String lyricStr = "";
            for (int i = 0; i < selectedLyric.size(); i++) {
                if(i == selectedLyric.size()-1){
                    lyricStr += selectedLyric.get(i);
                }else {
                    lyricStr += selectedLyric.get(i) + "\n";
                }
                Log.e("ooooooo",""+lyricStr);
            }
//            lyricStr = lyricStr.replaceAll("\r|\n", "");
//            System.out.println("转换后："+lyricStr);
            lvLyric.setText(lyricStr);
            if (bean != null) {
                tvSinger.setText(bean.getNickname());
                tvSongname.setText("" + bean.getTitle());
                try {
                    ImageLoader.with(this)
                            .url(bean.getImgpic_info().getLink())
                            .into(img_song);
                } catch (RuntimeException e) {
                }
            }
        }

        //生成二维码
        if(null== qrcodeurl || qrcodeurl.equals("") || qrcodeurl == ""){

        }else {
            String content = qrcodeurl.trim();
            try {
                if (!TextUtils.isEmpty(content)) {
                    Bitmap bitmap = Create2DCode(content);
                    qrcode_img.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(ShareLyricActivity.this, "地址为空~~", Toast.LENGTH_SHORT).show();
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
    }

    @OnClick({R.id.leftButton, R.id.rightButton, R.id.ll_share_lyric, R.id.ll_choosepic,R.id.save_img_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_share_lyric:
                shareLyric();
                break;
            case R.id.ll_choosepic:
                ChoosePicPopWindow.INSTANCE.showPopupWindow(this, findViewById(R.id.main), false, new ChoosePicPopWindow.ChoosePicCallback() {
                    @Override
                    public void chooseFromLocal(Intent pickIntent) {
                    }

                    @Override
                    public void tackPhoto(Intent takeIntent) {
                        startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
                    }
                });
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                break;
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
//                Bitmap viewBitmap = GetBitmapFromView.createViewBitmap(this, ll_share_view);
//                fileLyric = GetBitmapFromView.saveMyBitmap(bean.getTitle(), viewBitmap);
//                if (fileLyric != null) {
//                    setSnackBar("保存成功！", "", R.drawable.icon_success);
//                }
                break;
            case R.id.save_img_ll:
                showphoto = true;
                if(showphoto == true){
                    llChoosepic.setVisibility(View.GONE);
                }
                Bitmap viewBitmap = GetBitmapFromView.createViewBitmap(this, ll_share_view);
                fileLyric = GetBitmapFromView.saveMyBitmap(bean.getTitle(), viewBitmap);
                if (fileLyric != null) {
                    setSnackBar("保存成功！", "", R.drawable.icon_success);
                }
                showphoto = false;
                llChoosepic.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
            if (selectedImages.size() > 0) {
                String s = selectedImages.get(0);
                Glide.with(this).load(s).asBitmap().into(img_song);
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            Glide.with(this).load(ChoosePicPopWindow.INSTANCE.getTempFile()).asBitmap().into(img_song);
        }
    }

    public void shareLyric() {
        if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
//            if (CacheUtils.INSTANCE.getBoolean(this, Constants.User.IS_LOGIN, false) == true && bean != null) {
                showphoto = true;
                if(showphoto == true){
                    llChoosepic.setVisibility(View.GONE);
                }
                Bitmap viewBitmap = GetBitmapFromView.createViewBitmap(this, ll_share_view);
                fileLyric = GetBitmapFromView.saveMyBitmap(bean.getTitle(), viewBitmap);
                MusicBean musicBean = new MusicBean();
                MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                shareDataBean.setType("img");
                if (fileLyric != null) {
                    shareDataBean.setImgFilePath(fileLyric.getAbsolutePath());
                }
                shareDataBean.setUid(bean.getUid());
                shareDataBean.setNickname(bean.getNickname());
                shareDataBean.setTitle(bean.getTitle());
                musicBean.setShareDataBean(shareDataBean);
                ShareBottomDialog shareBottomDialog = new ShareBottomDialog(this, musicBean);
                shareBottomDialog.show();
                showphoto = false;
                llChoosepic.setVisibility(View.VISIBLE);
//            } else {
//                goActivity(LoginRegMainPage.class);
//            }
        }
    }


    /**
     * 用字符串生成二维码
     *
     * @param str
     * @author J!nl!n
     * @return
     * @throws WriterException
     */
    public Bitmap Create2DCode(String str) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 480, 480, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
