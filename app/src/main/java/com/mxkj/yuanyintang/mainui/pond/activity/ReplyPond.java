package com.mxkj.yuanyintang.mainui.pond.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerPreviewActivity;
import com.mxkj.yuanyintang.utils.photopicker.widget.BGASortableNinePhotoLayout;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment;
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ReplyPond extends StandardUiActivity implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    public static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int CREATE_VOTE = 201;
    @BindView(R.id.img_selected)
    ImageView imgSelected;
    @BindView(R.id.tv_selectmusic)
    TextView tvSelectmusic;
    @BindView(R.id.tv_selectsinger)
    TextView tvSelectsinger;
    @BindView(R.id.delet_seletmusic)
    ImageView deletSeletmusic;
    @BindView(R.id.ll_selectmusic)
    LinearLayout llSelectmusic;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.nineimg_layout)
    BGASortableNinePhotoLayout mPhotosSnpl;
    @BindView(R.id.emoji)
    ImageView emoji;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.music)
    ImageView music;
    @BindView(R.id.ll_botom_menu)
    LinearLayout llBotomMenu;
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.tv_total_textnum)
    TextView tv_total_textnum;
    private int maxNum_photo = 9;//最多可以选多少张图片,默认9
    private MusicInfo.DataBean selectedBean;
    /**
     * 发布的参数值
     */
    private String content;
    private int music_id;
    private HttpParams params;
    private String hashStr = "";//图片哈希值
    private Handler handler = new Handler();
    private ArrayList<String> selectedList = new ArrayList<>();
    private String pondId;//要回复的id
    private EmotionMainFragment emotionMainFragment;

    @Override
    public int setLayoutId() {
        return R.layout.activity_reply_pond;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initEmotionMainFragment();
        pondId = getIntent().getStringExtra("pondId");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTitleText("回复池塘");
        setRightButtonText("回复");
        getNavigationBar().getRightButton().setTextColor(Color.parseColor("#ff6699"));
    }

    @Override
    protected void initEvent() {
        mPhotosSnpl.setMaxItemCount(maxNum_photo);
        mPhotosSnpl.setSortable(true);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        if (music_id == 0) {
            llSelectmusic.setVisibility(View.GONE);
        }
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                content = editable.toString();
                if (editable.length() <= 500) {
                    tv_total_textnum.setText( editable.length() + "/500");
                }else{
//                    setSnackBar("已经超出最大字数限制", "", R.drawable.icon_tips_bad);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.leftButton, R.id.rightButton, R.id.delet_seletmusic, R.id.emoji, R.id.picture, R.id.music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.delet_seletmusic:
                music_id = 0;
                llSelectmusic.setVisibility(View.GONE);
                break;
            case R.id.emoji:
                emotionMainFragment.mEmotionKeyboard.showEmotionLayout();
                break;
            case R.id.picture:
                choicePhotoWrapper();
                break;
            case R.id.music:
                Intent intent = new Intent(this, AddMusicNew.class);
                startActivityForResult(intent, AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN);
                break;
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
                publishTopic();
//                if (CacheUtils.INSTANCE.getBoolean(ReplyPond.this, Constants.User.IS_LOGIN, false)) {
//                    publishTopic();
//                } else {
//                    startActivity(new Intent(ReplyPond.this, QuickLoginActivityNew.class));
//                }
                break;
        }
    }

    /**
     * 图片参数设置
     */
    private void publishTopic() {
        hashStr = "";
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            etContent.setFocusable(true);
            etContent.setError("输入回复内容");
        } else {
            showLoadingView();
            params = new HttpParams();
            params.put("content", content);
            /**
             * 有图片
             *
             * */
            final ArrayList<String> selectedList = mPhotosSnpl.getData();
            if (selectedList != null && selectedList.size() > 0) {
                try {
                    IMGCompressUtils.CompressorImage1(selectedList.size(), getApplicationContext(), selectedList, new IMGCompressUtils.CompressCallback() {
                        @Override
                        public void fileCallback(@NonNull List<File> files) {
                            if (files != null && files.size() > 0) {
                                FileUploadUtil fileUploadUtil = new FileUploadUtil();
                                fileUploadUtil.setFileList(files);
                                fileUploadUtil.upload(ReplyPond.this, 1, new FileUploadUtil.UpLoadCallback() {
                                    @Override
                                    public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                                        if (finishBeans != null && finishBeans.size() > 0) {
                                            for (int i = 0; i < finishBeans.size(); i++) {
                                                if (finishBeans.size() > i) {
                                                    String hash = "";
                                                    hash = finishBeans.get(i).getImgpic();
                                                    if (i != finishBeans.size() - 1) {
                                                        hashStr += hash + ",";
                                                    } else {
                                                        hashStr += hash;
                                                    }
                                                }
                                            }
                                            if (!TextUtils.isEmpty(hashStr)) {
                                                params.put("imglist", hashStr);
                                                publish(params);
                                            }

                                        }
                                    }

                                    @Override
                                    public void upLoadFailure(PutObjectRequest request, ServiceException serviceException) {
                                        hideLoadingView();
                                        setSnackBar("图片添加失败，请重试", "", R.drawable.icon_fails);
                                        Log.e(TAG, "上传失败回调: -------" + serviceException);
                                    }
                                });
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                publish(params);
            }
        }
    }

    /**
     * 发布
     */

    private void publish(HttpParams params) {
        /**
         * 音乐iD
         *
         * */
        if (music_id != 0) {
            params.put("music_id", music_id + "");
        }
        if (pondId != null) {
            params.put("pid", pondId);
        }
        Log.e(TAG, "publish: " + params);
        NetWork.INSTANCE.replyTopic(ReplyPond.this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                Log.e(TAG, "doNext=====" + resultData);
                setSnackBar("回复成功！", "", R.drawable.icon_success);
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultData);
                JSONObject dataObj = jsonObject.getJSONObject("data");
                String data = JSON.toJSONString(dataObj);
                PondCommentBean.DataBean dataBean = JSON.parseObject(data, PondCommentBean.DataBean.class);
                hideLoadingView();
                Intent intent = new Intent(PondDetialActivityNew.POND_COMMENT_ACTION);
                intent.putExtra(PondDetialActivityNew.POND_COMMENT_BEAN, dataBean);
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void doError(String msg) {
                Log.e(TAG, "doError: " + msg);
                hideLoadingView();
            }

            @Override
            public void doResult() {
            }
        });
    }

    /**
     * 去相册选择照片
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {//去选择照片
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, maxNum_photo, null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:  1.访问设备上的照片  2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(ReplyPond.this, "您拒绝了选取照片所需的权限!", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
                ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                selectedList.clear();
                selectedList.addAll(selectedImages);
            } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
                mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
                ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                selectedList.clear();
                selectedList.addAll(selectedImages);
            } else if (requestCode == AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN && resultCode == AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN) {
                llSelectmusic.setVisibility(View.VISIBLE);
                selectedBean = (MusicInfo.DataBean) data.getSerializableExtra("selectedBean");
                if (selectedBean != null) {
                    music_id = selectedBean.getId();
                    tvSelectmusic.setText(selectedBean.getTitle());
                    tvSelectsinger.setText(selectedBean.getNickname());
                    try {
                        ImageLoader.with(this)
                                .override(40, 40)
                                .url(selectedBean.getImgpic_info().getLink())
                                .error(R.drawable.nothing)
                                .into(imgSelected);
                    } catch (RuntimeException e) {
                    }


                }
            }
            if (BGAPhotoPickerActivity.getSelectedImages(data) != null && BGAPhotoPickerActivity.getSelectedImages(data).size() > 0) {
                maxNum_photo = 9 - BGAPhotoPickerActivity.getSelectedImages(data).size();
            }
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
        maxNum_photo = 9 - mPhotosSnpl.getData().size();
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtils.INSTANCE.setString(ReplyPond.this, "selectTopicTag", "");
    }


    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框   true  表示使用表情键盘的输入框，false使用键盘之外的
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, false);
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);//隐藏表情键盘的输入框和发送按钮
//        //隐藏控件
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView((EditText) etContent);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
