package com.mxkj.yuanyintang.mainui.myself.celebrity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.REQUEST_CODE_CHOOSE_PHOTO;
import static com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.CAMERA_REQUEST_CODE;

public class RealInfoAuthStep2 extends StandardUiActivity {
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.step_text)
    TextView stepText;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bt_next)
    TextView btNext;
    @BindView(R.id.navigationBarRl)
    RelativeLayout navigationBar;
    @BindView(R.id.img_cardEg1)
    ImageView imgCardEg1;
    @BindView(R.id.ll_idCard)
    LinearLayout llIdCard;
    @BindView(R.id.img_cardEg2)
    ImageView imgCardEg2;
    @BindView(R.id.ll_otherCard)
    LinearLayout llOtherCard;
    @BindView(R.id.img_card1)
    ImageView imgCard1;
    @BindView(R.id.ll_card1)
    LinearLayout llCard1;
    @BindView(R.id.img_card2)
    ImageView imgCard2;
    @BindView(R.id.ll_card2)
    LinearLayout llCard2;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.type_text)
    TextView typeText;
    @BindView(R.id.cardeg1Text)
    TextView cardeg1Text;
    @BindView(R.id.cardeg2Text)
    TextView cardeg2Text;


    private int setTo;//0代表身份证正面
    private boolean isadd1 = false, isIsadd2 = false;
    private String path1, path2;
    private PopupWindow popupWindow;
    @Nullable
    private String hash_card2 = "", hash_card1 = "";
    private boolean needFinish = true;
    private int authType;
    Handler handler = new Handler();

    @Override
    public int setLayoutId() {
        return R.layout.activity_real_info_auth_step2;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        btNext.setVisibility(View.VISIBLE);
        title.setText("选择证件");
        stepText.setText("(2/3)");
        authType = getIntent().getIntExtra("authType", 0);

        if (authType == 1) {
            cardeg1Text.setText("证件正面");
            cardeg2Text.setText("证件反面");
            typeText.setText("证件照示例");
            imgCardEg1.setImageResource(R.drawable.img_other_documents1);
            imgCardEg2.setImageResource(R.drawable.img_other_documents2);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
    }

    private void upLoadImg(final File file) {
        String imagePath = file.getAbsolutePath();
        if (setTo == 0) {
            path1 = imagePath;
            CacheUtils.INSTANCE.setString(RealInfoAuthStep2.this, "path1", imagePath);
            Glide.with(this).load(file).asBitmap().into(imgCard1);
        } else if (setTo == 1) {
            path2 = imagePath;
            CacheUtils.INSTANCE.setString(RealInfoAuthStep2.this, "path2", imagePath);
            Glide.with(this).load(file).asBitmap().into(imgCard2);

        }
//        ArrayList<String> path = new ArrayList<>();
//        path.add(imagePath);
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(imagePath));
        showLoadingView();
//        try {
//            IMGCompressUtils.CompressorImage1(1, getApplicationContext(), path, new IMGCompressUtils.CompressCallback() {
//                @Override
//                public void fileCallback(@NonNull List<File> files) {
        if (files != null && files.size() > 0) {
            FileUploadUtil fileUploadUtil = new FileUploadUtil();
            fileUploadUtil.setFileList(files);
            fileUploadUtil.upload(RealInfoAuthStep2.this, 1, new FileUploadUtil.UpLoadCallback() {
                @Override
                public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                    Log.e(TAG, "实名认证上传图片----finishBeans==: " + finishBeans.size());
                    hideLoadingView();
                    if (finishBeans != null && finishBeans.size() > 0) {
                        String imgpic_link = finishBeans.get(0).getImgpic_link();
                        if (!TextUtils.isEmpty(imgpic_link)) {
                            if (setTo == 0) {
                                hash_card1 = finishBeans.get(0).getImgpic();
                                Glide.with(RealInfoAuthStep2.this).load(imgpic_link).asBitmap().into(imgCard1);
                                isadd1 = true;
                            } else {
                                isIsadd2 = true;
                                hash_card2 = finishBeans.get(0).getImgpic();
                                Glide.with(RealInfoAuthStep2.this).load(imgpic_link).asBitmap().into(imgCard2);
                            }
                        } else {
                            setSnackBar("上传异常，请重新添加证件照", "", R.drawable.icon_fails);
                        }
                    }
                    Log.e(TAG, "upLoadSuccess图片哈希值1====: -------" + hash_card1 + "图片哈希值2===------" + hash_card2);
                }

                @Override
                public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {
                    hideLoadingView();
                }
            });
        }
    }

    @OnClick({R.id.finish, R.id.bt_next, R.id.img_cardEg1, R.id.img_cardEg2, R.id.img_card1, R.id.img_card2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.bt_next:
//              authType
                Intent intent = new Intent(this, RealInfoAuthStep3.class);
                intent.putExtra("authType", authType);
                intent.putExtra("hash1", hash_card1);
                intent.putExtra("hash2", hash_card2);
                startActivity(intent);
                break;
            case R.id.img_cardEg1:
                break;
            case R.id.img_cardEg2:
                break;
            case R.id.img_card1:
                setTo = 0;
                chooseCardImg();
                break;
            case R.id.img_card2:
                setTo = 1;
                chooseCardImg();
                break;
        }
    }

    private void chooseCardImg() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    upLoadImg(ChoosePicPopWindow.INSTANCE.getTempFile());
                    break;
                case REQUEST_CODE_CHOOSE_PHOTO:
                    ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                    if (selectedImages.size() > 0) {
                        String s = selectedImages.get(0);
                        upLoadImg(new File(s));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCollector.INSTANCE.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }
}