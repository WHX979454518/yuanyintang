package com.mxkj.yuanyintang.mainui.myself.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kevin.crop.UCrop;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.activity.MainActivity;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanSongSheetBean;
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.image.CropImgActivity;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;
import com.mxkj.yuanyintang.mainui.pond.widget.ChoosePondTag;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.utils.file.GetPathFromUri4kitkat;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.RefreshDataEvent;
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.tempFile;

/**
 * Created by LiuJie on 2017/10/17.
 */

public class EditSongActivity extends StandardUiActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.pond_song)
    FlowLayout pond_song;
    @BindView(R.id.tv_select_song_tag)
    RelativeLayout tv_select_song_tag;
    @BindView(R.id.tv_song_name_length)
    TextView tv_song_name_length;
    @BindView(R.id.et_song_name)
    EditText et_song_name;
    @BindView(R.id.et_synopsis)
    EditText et_synopsis;
    @BindView(R.id.iv_song_cover)
    ImageView iv_song_cover;

    public static final int RESULT_TAG_CODE = 7;
    private List<PondHotTagBean.DataBean> selectedTagList = new ArrayList<>();
    private MySongListBean.DataBeanX.DataBean dataBean;
    private MusicIanSongSheetBean.DataBean dataBeanmusic;
    private MyReleaseBean.DataBean dataBeanMymusic;
    protected InputMethodManager inputManager;


    // 拍照临时图片
    private String mTempPhotoPath;
    private String hash;//头像hash

    private String biaoshi;
    // 剪切后图像文件
    private Uri mDestinationUri;
    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记

    /**
     * 图片选择的监听回调
     */
    private OnPictureSelectedListener mOnPictureSelectedListener;

    @Override
    public int setLayoutId() {
        return R.layout.activity_edit_song;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    private SwitchButton switch_people_who;
    private Boolean isCheckedflag = false;
    private int isprivate=0;
    @Override
    protected void initView() {
        ButterKnife.bind(this);

        switch_people_who = findViewById(R.id.switch_people_who);


        Bundle bundle = this.getIntent().getExtras();
        hash = "";
        biaoshi = bundle.getString("biaoshi");
        if(biaoshi.equals("2") || biaoshi == "2"){
            dataBeanmusic = (MusicIanSongSheetBean.DataBean) bundle.getSerializable("data");
            Log.e("gggg",""+dataBeanmusic.toString());
            isprivate = dataBeanmusic.getIs_private();
        }else if(biaoshi.equals("3") || biaoshi == "3"){
            dataBeanMymusic = (MyReleaseBean.DataBean) getIntent().getSerializableExtra("data");
        }else {
            dataBean = (MySongListBean.DataBeanX.DataBean) getIntent().getSerializableExtra("data");
        }

        setTitleText("编辑歌单");
        setRightButtonText("完成");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });

        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        newSong();
                    }
                });

        RxView.clicks(tv_select_song_tag)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) selectedTagList);
                        bundle.putString("type", "song");
                        bundle.putInt("num", 6);
                        bundle.putString("title", "选择歌单标签");
                        goActivityForResult(ChoosePondTag.class, bundle, RESULT_TAG_CODE);
                    }
                });

        RxTextView.textChanges(et_song_name)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        int length = charSequence.toString().length();
//                        tv_song_name_length.setText("标题(" + length + "/30)");
                    }
                });
        RxView.clicks(iv_song_cover)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        hideKeyboard();
//                        chooseCardImg();
                        picture();
                    }
                });


        //这里判断是否是私密歌单,如果是私密歌单，就会默认打开私密开关
        if(isprivate==1){
            switch_people_who.setChecked(true);
        }else {
            switch_people_who.setChecked(false);
        }

        switch_people_who.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Log.i("msg","私密"+isChecked);
                isCheckedflag = isChecked;
            }
        });

        initFlowLayout();
    }

    private void newSong() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < selectedTagList.size(); i++) {
            org.json.JSONObject object = new org.json.JSONObject();
            try {
                object.put("id", selectedTagList.get(i).getId());
                object.put("title", selectedTagList.get(i).getTitle());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(jsonArray.toString()) && TextUtils.equals("[]", jsonArray.toString())) {
            setSnackBar("请选择标签", "", R.drawable.icon_fails);
            return;
        }

        if (isCheckedflag == true){
            isprivate=1;
        }else {
            isprivate=0;
        }
        String id = "";
        if(biaoshi.equals("2") || biaoshi == "2"){
            id = dataBeanmusic.getId()+"";
        }else if(biaoshi.equals("3") || biaoshi == "3"){
            id = dataBeanMymusic.getId()+"";
        }
        else {
            id = dataBean.getId()+ "";
        }

        NetWork.INSTANCE.getEditSong(this
                , id
                , et_song_name.getText().toString()
                , hash
                , TextUtils.isEmpty(hash) ? 0 : 1
                , et_synopsis.getText().toString()
                , jsonArray.toString(), "", "1"
                ,isprivate+"",
                new NetWork.TokenCallBack() {

                    @Override
                    public void doNext(String resultData, Headers headers) {
                        setSnackBar("编辑成功", "", R.drawable.icon_success);
                        RxBus.getDefault().post(new RefreshDataEvent(0));
                        RxBus.getDefault().post(new RefreshDataEvent(1));
                        finish();
                    }

                    @Override
                    public void doError(String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });
    }

    private void initFlowLayout() {
        if(biaoshi.equals("2") || biaoshi == "2"){
            if (null != dataBeanmusic&&null!=dataBeanmusic.getImgpic_info()) {
                ImageLoader.with(this)
                        .url(dataBeanmusic.getImgpic_info().getLink())
                        .asSquare()
                        .scale(ScaleMode.CENTER_CROP)
                        .into(iv_song_cover);
                hash = dataBeanmusic.getImgpic();
                et_song_name.setText(StringUtils.isEmpty(dataBeanmusic.getTitle()));
                et_song_name.setSelection(et_song_name.getText().toString().length());
//            et_synopsis.setText(StringUtils.isEmpty(dataBean.()));
            }
        }else if(biaoshi.equals("3") || biaoshi == "3"){
            if (null != dataBeanMymusic&&null!=dataBeanMymusic.getImgpic_info()) {
                ImageLoader.with(this)
                        .url(dataBeanMymusic.getImgpic_info().getLink())
                        .asSquare()
                        .scale(ScaleMode.CENTER_CROP)
                        .into(iv_song_cover);
                hash = dataBeanMymusic.getImgpic();
                et_song_name.setText(StringUtils.isEmpty(dataBeanMymusic.getTitle()));
                et_song_name.setSelection(et_song_name.getText().toString().length());
//            et_synopsis.setText(StringUtils.isEmpty(dataBean.()));
            }
        }
        else {
            if (null != dataBean&&null!=dataBean.getImgpic_info()) {
                ImageLoader.with(this)
                        .url(dataBean.getImgpic_info().getLink())
                        .asSquare()
                        .scale(ScaleMode.CENTER_CROP)
                        .into(iv_song_cover);
                hash = dataBean.getImgpic();
                et_song_name.setText(StringUtils.isEmpty(dataBean.getTitle()));
                et_song_name.setSelection(et_song_name.getText().toString().length());
//            et_synopsis.setText(StringUtils.isEmpty(dataBean.()));
            }
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(@android.support.annotation.NonNull Uri fileUri, Bitmap bitmap) {
                iv_song_cover.setImageBitmap(bitmap);
//                changeIcon(bitmap, fileUri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_TAG_CODE:
                    List<PondHotTagBean.DataBean> dataBeanList = (List<PondHotTagBean.DataBean>) data.getSerializableExtra("data");
                    if (null != dataBeanList) {
                        selectedTagList.clear();
                        selectedTagList.addAll(dataBeanList);
                        if (selectedTagList.size() > 0) {
                            pond_song.removeAllViews();
                            for (int i = 0; i < selectedTagList.size(); i++) {
                                TextView tvTag = (TextView) LayoutInflater.from(EditSongActivity.this).inflate(
                                        R.layout.newchild_song_tag, pond_song, false);
                                if(i>=5){
                                    tvTag.setText(StringUtils.isEmpty(selectedTagList.get(i).getTitle())+"...");
                                }else if(i==selectedTagList.size()-1){
                                    tvTag.setText(StringUtils.isEmpty(StringUtils.isEmpty(selectedTagList.get(i).getTitle())));
                                }else {
                                    tvTag.setText(StringUtils.isEmpty(selectedTagList.get(i).getTitle())+"•");
                                }
                                pond_song.addView(tvTag);
                            }
//                            tv_select_song_tag.setText("歌单标签(" + selectedTagList.size() + "/6)");
                        }
                    }
                    break;
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
//                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(tempFile));
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
                    break;

                case PictureConfig.REQUEST_PICTURE:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                        Glide.with(EditSongActivity.this)
                                .load(media.getPath())
                                .into(iv_song_cover);
                        hash = media.getPath();
                    }
                    imagePath = Uri.decode(hash);
                    File file = new File(imagePath);
                    files.add(file);
                    FileUploadUtil fileUploadUtil =new  FileUploadUtil();;
                    fileUploadUtil.setFileList(files);
                    fileUploadUtil.upload(this, 1, new FileUploadUtil.UpLoadCallback() {
                        @Override
                        public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                            if(finishBeans.size()==1){
                                hash = finishBeans.get(0).getImgpic();
                            }else if(finishBeans.size()==2){
                                hash = finishBeans.get(1).getImgpic();
                            }else if(finishBeans.size()==3){
                                hash = finishBeans.get(2).getImgpic();
                            }else if(finishBeans.size()==4){
                                hash = finishBeans.get(3).getImgpic();
                            }else if(finishBeans.size()==5){
                                hash = finishBeans.get(4).getImgpic();
                            }else if(finishBeans.size()==6){
                                hash = finishBeans.get(5).getImgpic();
                            }else if(finishBeans.size()==7){
                                hash = finishBeans.get(6).getImgpic();
                            }
                        }

                        @Override
                        public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {

                        }
                    });
                    break;
            }

        }
    }


    private String imagePath;
    public static List<File> files = new ArrayList<>();
    public void changeIcon(Bitmap bitmap, @android.support.annotation.NonNull Uri fileUri) {
        String filePath = GetPathFromUri4kitkat.getPathByUri4kitkat(this, fileUri);
        String imagePath = Uri.decode(filePath);
        File file = new File(imagePath);
        List<File> listFile = new ArrayList<>();
        listFile.add(file);

        FileUploadUtil fileUploadUtil = new FileUploadUtil();
        fileUploadUtil.setFileList(listFile);
        fileUploadUtil.upload(this, 1, new FileUploadUtil.UpLoadCallback() {
            @Override
            public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                if(finishBeans.size()==1){
                    hash = finishBeans.get(0).getImgpic();
                }else if(finishBeans.size()==2){
                    hash = finishBeans.get(1).getImgpic();
                }else if(finishBeans.size()==3){
                    hash = finishBeans.get(2).getImgpic();
                }else if(finishBeans.size()==4){
                    hash = finishBeans.get(3).getImgpic();
                }else if(finishBeans.size()==5){
                    hash = finishBeans.get(4).getImgpic();
                }else if(finishBeans.size()==6){
                    hash = finishBeans.get(5).getImgpic();
                }else if(finishBeans.size()==7){
                    hash = finishBeans.get(6).getImgpic();
                }

            }

            @Override
            public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {

            }
        });
    }

    @AfterPermissionGranted(1)
    private void chooseCardImg() {
        ChoosePicPopWindow.INSTANCE.showPopupWindow(EditSongActivity.this, findViewById(R.id.main), true, new ChoosePicPopWindow.ChoosePicCallback() {
            @Override
            public void chooseFromLocal(Intent pickIntent) {
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pond_song.removeAllViews();
        pond_song = null;
        CacheUtils.INSTANCE.setString(EditSongActivity.this,"selectTopicTag", "");
    }

    /**
     * hide keyboard
     */
    public void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private List<LocalMedia> selectList = new ArrayList<LocalMedia>();
    //进入相册显示图片
    private void picture(){
        PictureSelector.create(EditSongActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.REQUEST_PICTURE);//结果回调onActivityResult code
    }

}
