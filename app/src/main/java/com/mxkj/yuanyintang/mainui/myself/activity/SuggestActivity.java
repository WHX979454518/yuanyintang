package com.mxkj.yuanyintang.mainui.myself.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.extraui.bean.ReportOperationBean;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerPreviewActivity;
import com.mxkj.yuanyintang.utils.photopicker.widget.BGASortableNinePhotoLayout;
import com.mxkj.yuanyintang.widget.NavigationBar;

import java.io.File;
import java.io.IOException;
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

import static com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity.EXTRA_SELECTED_IMAGES;

public class SuggestActivity extends StandardUiActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_pic_count)
    TextView tv_pic_count;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_qq)
    EditText et_qq;
    @BindView(R.id.nineimg_layout)
    BGASortableNinePhotoLayout mPhotosSnpl;
    private int maxNum_photo = 5;//最多可以选多少张图片,默认9
    private ArrayList<String> selectedList = new ArrayList<>();//选择的本地的图片
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    public static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private String fileList = "";
    private Handler handler = new Handler();

    List<ReportOperationBean.DataBean> data = new ArrayList<>();
    private int pid;
    public static final String SCREEN_SHOOT_PATH = "SCREEN_SHOOT_PATH";

    @Override
    public int setLayoutId() {
        return R.layout.suggest;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("意见反馈");
        setRightButtonText("提交");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        initRecyclerView();
        RxView.clicks(navigationBar.getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Boolean isCheck = false;
                        Boolean isContent = false;
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getCheck()) {
                                pid = data.get(i).getId();
                                isContent = data.get(i).getRequired() == 1 ? true : false;
                                isCheck = true;
                            }
                        }
                        if (isContent) {
                            if (!isCheck) {
                                setSnackBar("请选择评论类型", "", R.drawable.icon_good);
                                return;
                            }
                        }
                        if (TextUtils.isEmpty(et_content.getText().toString())) {
                            setSnackBar("请输入反馈内容", "", R.drawable.icon_good);
                            return;
                        }
                        report();
                    }
                });

        setLeftButton("", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseConfirmDialog.Companion.newInstance().title("退出反馈").content("你的意见还未提交，是否退出？").confirmText("退出").cancleText("取消").showDialog(SuggestActivity.this, new BaseConfirmDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        finish();
                    }

                    @Override
                    public void onCancle() {

                    }
                });
            }
        });
    }

    private void report() {
        if (CacheUtils.INSTANCE.getBoolean(SuggestActivity.this, Constants.User.IS_LOGIN, false)) {
            showLoadingView();
            /**
             * 有图片
             *
             * */
            final ArrayList<String> selectedList = mPhotosSnpl.getData();
            if (selectedList != null && selectedList.size() > 0) {
//            if (selectedList.size() > 0) {
                //上传图片并拿到哈希值
                try {
                    IMGCompressUtils.CompressorImage1(selectedList.size(), getApplicationContext(), selectedList, new IMGCompressUtils.CompressCallback() {
                        @Override
                        public void fileCallback(@android.support.annotation.NonNull List<File> files) {
                            if (files != null && files.size() > 0) {
                                FileUploadUtil fileUploadUtil = new FileUploadUtil();
                                fileUploadUtil.setFileList(files);
                                fileUploadUtil.upload(SuggestActivity.this, 1, new FileUploadUtil.UpLoadCallback() {
                                    @Override
                                    public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                                        if (finishBeans != null && finishBeans.size() > 0) {
                                            for (int i = 0; i < finishBeans.size(); i++) {
                                                String hash = "";
                                                hash = finishBeans.get(i).getImgpic();
                                                if (i != finishBeans.size() - 1) {
                                                    fileList += hash + ",";
                                                } else {
                                                    fileList += hash;
                                                }
                                            }
                                        }
                                        if (!fileList.isEmpty()) {
                                            publish(fileList);
                                        }
                                    }

                                    @Override
                                    public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {

                                    }
                                });
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    hideLoadingView();
                }
            } else {
                publish("");
            }
        }else {
//            Intent intent = new Intent(SuggestActivity.this,LoginRegMainPage.class);
//            startActivity(intent);
            showLoadingView();
            publish("");
        }
    }

    private void publish(String s) {
        if (TextUtils.isEmpty(et_content.getText().toString())){
            setSnackBar("你还没有输入内容","",R.drawable.icon_tips_bad);
            return;
        }
        HttpParams params = new HttpParams();
        params.put("qq", et_qq.getText().toString());
        params.put("content", et_content.getText().toString());
        params.put("filelist", fileList);
        NetWork.INSTANCE.suggest(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                BaseConfirmDialog.Companion.newInstance().confirmText("好的").cancleText("").isOneBtn(true).title("提交成功").isShowYxy(true).content("感谢您的反馈，我们会尽快处理哒~").showDialog(SuggestActivity.this, new BaseConfirmDialog.onBtClick() {
                    @Override
                    public void onConfirm() {
                        finish();
                    }

                    @Override
                    public void onCancle() {

                    }
                });
            }

            @Override
            public void doError(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    setSnackBar("举报失败", "", R.drawable.icon_fails);
                }
            }

            @Override
            public void doResult() {
                hideLoadingView();
            }
        });
    }

    private void initRecyclerView() {
        mPhotosSnpl.setMaxItemCount(maxNum_photo);
        mPhotosSnpl.setSortable(true);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getFeedback(this, 8 + "", new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                ReportOperationBean reportOperationBean = JSON.parseObject(resultData, ReportOperationBean.class);
                refreshData(reportOperationBean);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void refreshData(ReportOperationBean reportOperationBean) {
        data.clear();
        data.addAll(reportOperationBean.getData());
    }

    @Override
    protected void initEvent() {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(SCREEN_SHOOT_PATH);
        if (stringExtra != null) {
            selectedList.clear();
            selectedList.add(stringExtra);
            mPhotosSnpl.setVisibility(View.VISIBLE);
            Intent intent1 = new Intent();
            intent1.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedList);
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(intent1));

        }

    }

    /**
     * 九宫格控件的监听事件
     */
    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
        selectedList.clear();
        selectedList.addAll(mPhotosSnpl.getData());
        tv_pic_count.setText("上传图片(" + mPhotosSnpl.getData().size() + "/5)");
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
                ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                selectedList.clear();
                selectedList.addAll(selectedImages);
                if (selectedImages.size() > 0) {
                    mPhotosSnpl.setVisibility(View.VISIBLE);
                }
            } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
                mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
                ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                selectedList.clear();
                selectedList.addAll(selectedImages);
                if (selectedImages.size() > 0) {
                    mPhotosSnpl.setVisibility(View.VISIBLE);
                }
            }
            if (BGAPhotoPickerActivity.getSelectedImages(data) != null && BGAPhotoPickerActivity.getSelectedImages(data).size() > 0) {
                maxNum_photo = 5 - mPhotosSnpl.getData().size();
                tv_pic_count.setText("上传图片(" + mPhotosSnpl.getData().size() + "/5)");
            }
        }
    }

    /**
     * 去相册选择照片
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, maxNum_photo, null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:  1.访问设备上的照片  2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(SuggestActivity.this, "您拒绝了选取照片所需的权限!", Toast.LENGTH_SHORT);
        }
    }

    //使用这个不会出现手机back键的监听事件的被抢占
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int keyCode = event.getKeyCode();
        if(keyCode==KeyEvent.KEYCODE_BACK){
            BaseConfirmDialog.Companion.newInstance().title("退出反馈").content("你的意见还未提交，是否退出？").confirmText("退出").cancleText("取消").showDialog(SuggestActivity.this, new BaseConfirmDialog.onBtClick() {
                @Override
                public void onConfirm() {
                    finish();
                }

                @Override
                public void onCancle() {

                }
            });
        }
        return super.dispatchKeyEvent(event);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
////            BaseConfirmDialog.Companion.newInstance().title("退出反馈").content("你的意见还未提交，是否退出？").confirmText("退出").cancleText("取消").showDialog(SuggestActivity.this, new BaseConfirmDialog.onBtClick() {
////                @Override
////                public void onConfirm() {
////                    finish();
////                }
////
////                @Override
////                public void onCancle() {
////
////                }
////            });
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
