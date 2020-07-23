package com.mxkj.yuanyintang.mainui.myself.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.kevin.crop.UCrop;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.login_regist.RegSuccessRecommend2;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.newapp.myself.AddMusicianTagActivity;
import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.image.CropImgActivity;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.mainui.myself.bean.CityBean;
import com.mxkj.yuanyintang.mainui.myself.bean.ProvinceBean;
import com.mxkj.yuanyintang.utils.file.GetPathFromUri4kitkat;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.app.TimeUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.BlurUtil;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.MusicIanEvent;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.CAMERA_REQUEST_CODE;
import static com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.REQUEST_CODE_CHOOSE_PHOTO;


/**
 * 编辑个人资料
 * Created by LiuJie on 2017/9/30.
 */
public class EditPersonalProfileActivity extends StandardUiActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "EditPersonalProfileActi";
    private static final int GET_MUSICIAN_TAG_REQUST = 0x1024;
    private static final int GET_MUSICIAN_LIKE_REQUST = 0x1026;
    private static final int GET_MUSICIAN_INTRO = 0x1025;
    @BindView(R.id.layout_select_headimg)
    RelativeLayout layout_select_headimg;
    @BindView(R.id.layout_select_sex)
    LinearLayout layout_select_sex;
    @BindView(R.id.ll_intro)
    LinearLayout ll_intro;
    @BindView(R.id.layout_select_birthday)
    LinearLayout layout_select_birthday;
    @BindView(R.id.layout_select_address)
    LinearLayout layout_select_address;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tvIntro)
    TextView tvIntro;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.civ_headimg)
    CircleImageView civ_headimg;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.iv_is_vip)
    ImageView iv_is_vip;
    @BindView(R.id.leftBT)
    Button leftButton;
    @BindView(R.id.rightBT)
    Button rightButton;
    @BindView(R.id.llLike)
    LinearLayout llLike;
    @BindView(R.id.llTag)
    LinearLayout llTag;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvLike)
    TextView tvLike;
    private String hash;//头像hash
    TimePickerView pvTime;
    OptionsPickerView pvOptions;
    private String province, city, provinceCode, cityCode,content;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private UserInfo userInfo;
    CityBean cityBean;

    public static final String DATA = "_data";

    // 剪切后图像文件
    private Uri mDestinationUri;
    /**
     * 图片选择的监听回调
     */
    private OnPictureSelectedListener mOnPictureSelectedListener;
    //    private String synopsiStr = "";
    private String musicianTagJsonArr = "[]";
    private String musicianLikeJsonArr = "[]";
    private String introStr = "";

    @Override
    public int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_edit_personal_profile;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(EditPersonalProfileActivity.this);
        userInfo = (UserInfo) getIntent().getSerializableExtra(DATA);
        if (userInfo == null) {
            UserInfoUtil.getUserInfoByJson("", new UserInfoUtil.UserCallBack() {
                @Override
                public void doNext(UserInfo infoBean) {
                    if (infoBean != null) {
                        userInfo = infoBean;
                    }
                }
            });
        }
        initTitle();
        initOnClick();
        initTimePicker();
        if (null != userInfo) {
            refreshData(userInfo);
        }
        if (null != userInfo) {
            iv_is_vip.setVisibility(userInfo.getData().getIs_music() == 3 ? View.VISIBLE : View.GONE);
        }
    }

    private void initOnClick() {
        RxView.clicks(layout_select_headimg)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        chooseCardImg();
                    }
                });
        RxView.clicks(layout_select_sex)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        com.mxkj.yuanyintang.extraui.dialog.SelectSexDialog selectSexDialog = new com.mxkj.yuanyintang.extraui.dialog.SelectSexDialog();
                        selectSexDialog.show(getSupportFragmentManager(), "mSelectSexDialog");
                        selectSexDialog.setMyDialogFragmentListener(new com.mxkj.yuanyintang.extraui.dialog.SelectSexDialog.MyDialogFragmentListener() {
                            @Override
                            public void getDataFrom_DialogFragment(String sex) {
                                tv_sex.setText(StringUtils.isEmpty(sex));
                            }
                        });
                    }
                });

        RxView.clicks(layout_select_birthday)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null != pvTime) {
                            pvTime.show();
                        }
                    }
                });
        RxView.clicks(llLike)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("edit", true);
                        goActivityForResult(MusicLikeTagActivity.class, bundle, GET_MUSICIAN_LIKE_REQUST);
                    }
                });
        RxView.clicks(llTag)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("edit", true);
                        goActivityForResult(AddMusicianTagActivity.class, bundle, GET_MUSICIAN_TAG_REQUST);
                    }
                });
        RxView.clicks(layout_select_address)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null != pvOptions) {
                            pvOptions.show();
                        } else {
                            Toast.create(EditPersonalProfileActivity.this).show("数据初始中...");
                        }
                    }
                });
        RxView.clicks(ll_intro)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putString("introduce", tvIntro.getText().toString());
                        goActivityForResult(EditIntroActivity.class,bundle, GET_MUSICIAN_INTRO);
                    }
                });


    }

    private void initTitle() {
        leftButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.icon_back_white), null, null, null);
        rightButton.setText("确定");
        rightButton.setTextColor(ContextCompat.getColor(this, R.color.white_text));
        RxView.clicks(leftButton)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        showConfirmDialog();

                    }
                });

        RxView.clicks(rightButton)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        editPersonalProfile();
                    }
                });
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

    private void editPersonalProfile() {
        if (TextUtils.isEmpty(tv_sex.getText().toString())) {
            setSnackBar("请选择性别", "", R.drawable.icon_good);
            return;
        }
        if (TextUtils.isEmpty(tv_birthday.getText().toString())) {
            setSnackBar("请选择生日", "", R.drawable.icon_good);
            return;
        }
        if (TextUtils.isEmpty(provinceCode)) {
            setSnackBar("请选择所在地", "", R.drawable.icon_good);
            return;
        }
        showLoadingView();
        HttpParams param = new HttpParams();
        param.put("sex", (TextUtils.equals("男", tv_sex.getText().toString()) ? 1 : 0) + "");
        param.put("day", tv_birthday.getText().toString());
        param.put("province", provinceCode);
        param.put("city", cityCode);
        if (!TextUtils.isEmpty("")) {
            param.put("qq", "");
        }
//        if (!TextUtils.isEmpty(introStr)) {
//            param.put("signature", introStr);
//        }
        param.put("signature", tvIntro.getText().toString());

        param.put("nickname", et_nickname.getText().toString());
        param.put("head", hash);
        NetWork.INSTANCE.getMember(this, param, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                UserInfoUtil.getUserInfoById(0, EditPersonalProfileActivity.this, new UserInfoUtil.UserCallBack() {
                    @Override
                    public void doNext(UserInfo infoBean) {
                        hideLoadingView();
                        RxBus.getDefault().post(new MusicIanEvent("refresh"));
                        finish();
                    }
                });
            }

            @Override
            public void doError(String msg) {
                hideLoadingView();

            }

            @Override
            public void doResult() {

            }
        });
    }

    @Override
    protected void initData() {
        String cityJson = CacheUtils.INSTANCE.getString(EditPersonalProfileActivity.this, Constants.DataManager.CITY_DATA_JSON, "");
        if (TextUtils.isEmpty(cityJson)) {
            NetWork.INSTANCE.getCity(this, "1", new NetWork.TokenCallBack() {

                @Override
                public void doNext(String resultData, Headers headers) {
                    if (!TextUtils.isEmpty(resultData)) {
                        CacheUtils.INSTANCE.setString(EditPersonalProfileActivity.this, Constants.DataManager.CITY_DATA_JSON, resultData);
                    }
                    refreshData(resultData);
                }

                @Override
                public void doError(String msg) {

                }

                @Override
                public void doResult() {

                }
            });
        } else {
            refreshData(cityJson);
        }
    }

    private void refreshData(final String data) {
        cityBean = JSON.parseObject(data, CityBean.class);
        if (null != cityBean && cityBean.getData().size() > 0) {
            for (int i = 0; i < cityBean.getData().size(); i++) {
                ProvinceBean provinceBean = new ProvinceBean();
                provinceBean.setName(cityBean.getData().get(i).getName());
                provinceBean.setCode(cityBean.getData().get(i).getId() + "");
                List<ProvinceBean.CityBean> city = new ArrayList<>();
                ArrayList<String> cityArray = new ArrayList<>();
                if (null != cityBean.getData().get(i).get_child()) {
                    for (int i1 = 0; i1 < cityBean.getData().get(i).get_child().size(); i1++) {
                        cityArray.add(cityBean.getData().get(i).get_child().get(i1).getName());
                        city.add(new ProvinceBean.CityBean(cityBean.getData().get(i).get_child().get(i1).getName(), cityBean.getData().get(i).get_child().get(i1).getId() + ""));
                        provinceBean.setCity(city);
                    }
                }
                options2Items.add(cityArray);
                options1Items.add(provinceBean);
            }
            initOptionPicker();
        }
    }

    @Override
    protected void initEvent() {
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(@android.support.annotation.NonNull Uri fileUri, Bitmap bitmap) {
                BlurUtil.glideBlurImg(EditPersonalProfileActivity.this, fileUri, 23, iv_bg);
                civ_headimg.setImageBitmap(bitmap);
                changeIcon(bitmap, fileUri);
            }
        });
    }

    public void changeIcon(Bitmap bitmap, @android.support.annotation.NonNull Uri fileUri) {
        String filePath = GetPathFromUri4kitkat.getPathByUri4kitkat(this, fileUri);
        String imagePath = Uri.decode(filePath);
        File file = new File(imagePath);
        List<File> listFile = new ArrayList<>();
        listFile.add(file);
        if (listFile != null && listFile.size() > 0) {
            FileUploadUtil fileUploadUtil = new FileUploadUtil();
            fileUploadUtil.setFileList(listFile);
            fileUploadUtil.upload(this, 1, new FileUploadUtil.UpLoadCallback() {
                @Override
                public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                    if (finishBeans != null && finishBeans.size() > 0) {
                        hash = finishBeans.get(0).getImgpic();
                    }
                }

                @Override
                public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    upLoadImg(ChoosePicPopWindow.INSTANCE.getTempFile());
                    upLoadHeadImg(ChoosePicPopWindow.INSTANCE.getTempFile().getAbsolutePath());
                    break;
                case GET_MUSICIAN_TAG_REQUST:
                    musicianTagJsonArr = data.getStringExtra("musicianTag");
                    if (musicianTagJsonArr != null && musicianTagJsonArr.length() > 5) {
                        List<UserInfo.DataBean.IdentityTagBean> identityTagBeans = JSON.parseArray(musicianTagJsonArr, UserInfo.DataBean.IdentityTagBean.class);
                        String identityTagStr = "";
                        for (int i = 0; i < identityTagBeans.size(); i++) {
                            identityTagStr += (identityTagBeans.get(i).getTitle() + "·");
                        }
                        tvTag.setText(identityTagStr);
                    }

                    break;
                case GET_MUSICIAN_LIKE_REQUST:
                    musicianLikeJsonArr = data.getStringExtra("musicianLikeTag");
                    if (musicianLikeJsonArr != null && musicianLikeJsonArr.length() > 5) {
                        List<UserInfo.DataBean.InterestTagBean> interestTagBeans = JSON.parseArray(musicianLikeJsonArr, UserInfo.DataBean.InterestTagBean.class);
                        String likeTagStr = "";
                        for (int i = 0; i < interestTagBeans.size(); i++) {
                            if (interestTagBeans.get(i).getSelected() == 1) {
                                likeTagStr += (interestTagBeans.get(i).getTitle() + "·");
                            }
                        }
                        tvLike.setText(likeTagStr);
                    }

                    break;
                case GET_MUSICIAN_INTRO:
                    introStr = data.getStringExtra("musicianIntro");
                    if (introStr == null) introStr = "朕还没想好怎么写呢~";
                    tvIntro.setText(introStr);
                    break;
                case REQUEST_CODE_CHOOSE_PHOTO:
                    ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                    if (selectedImages.size() > 0) {
                        String s = selectedImages.get(0);
                        upLoadImg(new File(s));
                        upLoadHeadImg(s);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadHeadImg(String s) {
        ImageLoader.with(this)
                .override(CacheUtils.INSTANCE.getInt(EditPersonalProfileActivity.this, Constants.Other.WIDTH, 0), 254)
                .file(s)
                .scale(ScaleMode.CENTER_CROP)
                .blur(25)
                .into(iv_bg);
        ImageLoader.with(this)
                .override(80, 80)
                .file(s)
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(civ_headimg);
    }

    private void upLoadImg(File file) {
        String imagePath = file.getAbsolutePath();
        file = new File(imagePath);
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        fileUploadUtil.setFileList(files);
        fileUploadUtil.upload(this, 1, new FileUploadUtil.UpLoadCallback() {
            @Override
            public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                if (finishBeans != null && finishBeans.size() > 0) {
                    hash = finishBeans.get(0).getImgpic();
                }
            }

            @Override
            public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {

            }
        });
    }

    @AfterPermissionGranted(1)
    public void startCropActivity(@android.support.annotation.NonNull Uri uri) {
        UCrop.of(uri, uri)
                .withAspectRatio(3, 2)
                .withTargetActivity(CropImgActivity.class)
                .start(this);
    }

    public void setOnPictureSelectedListener(OnPictureSelectedListener l) {
        this.mOnPictureSelectedListener = l;
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    @SuppressLint("WrongConstant")
    private void handleCropResult(@android.support.annotation.NonNull Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri && null != mOnPictureSelectedListener) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOnPictureSelectedListener.onPictureSelected(resultUri, bitmap);
        } else {
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    @SuppressLint("WrongConstant")
    private void handleCropError(@android.support.annotation.NonNull Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
        } else {
//            ToastUtil.showToast(getApplicationContext(), "无法剪切选择图片");
        }
    }


    public interface OnPictureSelectedListener {
        void onPictureSelected(Uri fileUri, Bitmap bitmap);
    }

    private void deleteTempPhotoFile() {
        File tempFile = new File("/data/data/com.mxkj.yuanyintang/cache/cropImage.jpeg");
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1950, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2025, 11, 31);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (System.currentTimeMillis() > date.getTime()) {
                    tv_birthday.setText(TimeUtils.getTime(date));
                } else {
                    setSnackBar("选择时间不能大于现在时间", "", R.drawable.icon_fails);
                }
            }
        }).setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                final ImageView iv_left = (ImageView) v.findViewById(R.id.iv_left);
                final ImageView iv_right = (ImageView) v.findViewById(R.id.iv_right);
                iv_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
                iv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(18)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }

    private void initOptionPicker() {//条件选择器初始化
        if (null != pvOptions) {
            return;
        }
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                provinceCode = options1Items.get(options1).getCode();
                province = options1Items.get(options1).getPickerViewText();
                city = "";
                cityCode = "";
                if (null != options1Items.get(options1).getCity()) {
                    cityCode = StringUtils.isEmpty(options1Items.get(options1).getCity().get(options2).getCode());
                    city = StringUtils.isEmpty(options1Items.get(options1).getCity().get(options2).getName());
                }
                String tx = province + "  " + city;
                       /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                tv_address.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_city, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
                        tv_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.returnData();
                                pvOptions.dismiss();
                            }
                        });
                        final ImageView iv_left = (ImageView) v.findViewById(R.id.iv_left);
                        final ImageView iv_right = (ImageView) v.findViewById(R.id.iv_right);
                        iv_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.dismiss();
                            }
                        });
                        iv_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setTextColorCenter(ContextCompat.getColor(this, R.color.color_17_text))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .build();

        //pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }

    private void refreshData(UserInfo userInfo) {
        ImageLoader.with(this)
                .url(userInfo.getData().getHead_link())
                .asCircle()
                .scale(ScaleMode.CENTER_CROP)
                .into(civ_headimg);
        et_nickname.setText(StringUtils.isEmpty(userInfo.getData().getNickname()));
        tvIntro.setText(StringUtils.isEmpty(userInfo.getData().getSignature()));
        Log.e("hhhhhhh",""+userInfo.getData().getSignature());
        tv_sex.setText(userInfo.getData().getSex() == 1 ? "男" : "女");
        tv_birthday.setText(StringUtils.isEmpty(userInfo.getData().getDay()));
        tv_address.setText(StringUtils.isEmpty(userInfo.getData().getProvince_text()) + "  " + StringUtils.isEmpty(userInfo.getData().getCity_text()));
        BlurUtil.glideBlurImg(this, userInfo.getData().getHead_link(), 25, iv_bg);
        provinceCode = userInfo.getData().getProvince() + "";
        cityCode = userInfo.getData().getCity() + "";
        hash = userInfo.getData().getHead();
        content = userInfo.getData().getSignature();

        String likeTagStr = "";
        List<UserInfo.DataBean.InterestTagBean> interest_tag = userInfo.getData().getInterest_tag();
        for (int i = 0; i < interest_tag.size(); i++) {
            if (interest_tag.get(i).getSelected() == 1) {
                likeTagStr += (interest_tag.get(i).getTitle() + "·");
            }
        }
        tvLike.setText(likeTagStr);
        List<UserInfo.DataBean.IdentityTagBean> identityTagBeans = userInfo.getData().getIdentity_tag();
        String identityTagStr = "";
        for (int i = 0; i < identityTagBeans.size(); i++) {
            if (identityTagBeans.get(i).getSelected() == 1) {
                identityTagStr += (identityTagBeans.get(i).getTitle() + "·");
            }
        }
        tvTag.setText(identityTagStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userInfo = null;
    }


    @Override
    public void onBackPressed() {
        showConfirmDialog();
    }


    public void showConfirmDialog() {
        BaseConfirmDialog.Companion.newInstance().cancleText("退出").confirmText("保存").content("是否保存资料？").title("保存资料").isShowYxy(true).showDialog(this, new BaseConfirmDialog.onBtClick() {
            @Override
            public void onConfirm() {
                editPersonalProfile();
            }

            @Override
            public void onCancle() {
                finish();
            }
        });
    }
}
