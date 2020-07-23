package com.mxkj.yuanyintang.mainui.pond.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.DegreeBean;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment.EmotionMainFragment;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.LogUtils;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.mainui.myself.bean.MyPondBean;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.HelpCenterActivity;
import com.mxkj.yuanyintang.mainui.pond.VoteActivity;
import com.mxkj.yuanyintang.mainui.pond.bean.LocationInfo;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;
import com.mxkj.yuanyintang.mainui.pond.bean.PondTagListBean;
import com.mxkj.yuanyintang.mainui.pond.bean.VoteBean;
import com.mxkj.yuanyintang.mainui.pond.widget.ChoosePondTag;
import com.mxkj.yuanyintang.mainui.pond.widget.ChoosePondTagNew;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.net.ApiAddress;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerPreviewActivity;
import com.mxkj.yuanyintang.utils.photopicker.widget.BGASortableNinePhotoLayout;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.IMGCompressUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.widget.SearTextView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Headers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * created by LiuJie -2017/10/23
 */
public class PublishPond extends StandardUiActivity implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    public static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int CREATE_VOTE = 201;
    @BindView(R.id.finish)
    TextView finish;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.ll_tag)
    LinearLayout ll_tag;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_choose_location)
    TextView tv_choose_location;
    @BindView(R.id.view_title)
    TextView viewTitle;
    @BindView(R.id.publish)
    TextView publish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
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
    @BindView(R.id.to_choose_tag)
    TextView toChooseTag;
    @BindView(R.id.emoji)
    ImageView emoji;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.music)
    ImageView music;
    @BindView(R.id.vote)
    ImageView imVote;
    @BindView(R.id.img_del_loc)
    ImageView img_del_loc;
    @BindView(R.id.ll_botom_menu)
    LinearLayout llBotomMenu;
    @BindView(R.id.nineimg_layout)
    BGASortableNinePhotoLayout mPhotosSnpl;
    @BindView(R.id.tv_cote_title)
    TextView tvCoteTitle;
    @BindView(R.id.img_dele_cote)
    ImageView imgDeleCote;
    @BindView(R.id.rl_cote)
    LinearLayout rlCote;
    @BindView(R.id.img_softKeytag)
    ImageView imgSoftKeytag;
    @BindView(R.id.fl_emotionview_main)
    FrameLayout flEmotionviewMain;
    private int maxNum_photo = 9;//最多还可以选多少张图片
    private MusicInfo.DataBean selectedBean;
    private VoteBean voteBean;
    @BindView(R.id.tv_total_textnum)
    TextView tvTotalTextnum;

    @BindView(R.id.ll_hideSoft)
    LinearLayout ll_hideSoft;
    /**
     * 发布的参数值
     */
    private String title;
    private String content;
    private int status = 1;
    private int source = 1;
    private int music_id;
    private String hashTagJson = "[]";
    private HttpParams params;
    private String hashStr = "";//图片哈希值
    private Handler handler = new Handler();
    private int topicId;//回复池塘用
    private String url = ApiAddress.INSTANCE.BASE_URL + ApiAddress.PUBLISH_TOPIC;
    private ArrayList<String> selectedList = new ArrayList<>();//选择的本地的图片
    private String vioteItemstr = "";
    private EmotionMainFragment emotionMainFragment;
    public static final String DATA = "data";
    private boolean isShowTip = true;
    private InputMethodManager mInputManager;
    private MyPondBean.DataBean.PondBean pondBean;
    private int editStatus;//编辑的时候判断是不是草稿，是草稿用put提交
    private ArrayList<String> imgList;
    public static final int REQ_LOCATION = 10010;
    private String addressDesc = "";
    private Double lonTitude, latitude;

    @Override
    public int setLayoutId() {
        return R.layout.activity_publish_pond;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        Log.e(TAG, "PublishPond: ");
        hideTitle(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            pondBean = (MyPondBean.DataBean.PondBean) bundle.getSerializable(DATA);
            if (pondBean != null) {
                topicId = pondBean.getId();
                title = pondBean.getTitle();
                content = pondBean.getContent();
                imgList = new ArrayList<>();
                List<MyPondBean.DataBean.PondBean.ImglistInfoBean> imglist_info = pondBean.getImglist_info();
                if (imglist_info != null) {
                    for (int i = 0; i < imglist_info.size(); i++) {
                        imgList.add(imglist_info.get(i).getLink());
                    }
                }
                int uid = pondBean.getUid();
                etContent.setText(content);
                editStatus = pondBean.getStatus();
                //图片列表
                if (imgList != null && imgList.size() > 0) {
                    for (int i = 0; i < imgList.size(); i++) {
                        if (!TextUtils.isEmpty(imgList.get(i))) {
                            OkHttpUtils.get().url(imgList.get(i)).build().execute(new FileCallBack(Constants.Path.APP_CACHE_PATH, System.currentTimeMillis() + ".png") {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                }

                                @Override
                                public void onResponse(@NonNull File file, int id) {
                                    selectedList.add(file.getAbsolutePath());
                                    mPhotosSnpl.setData(selectedList);
                                    maxNum_photo = 9 - selectedList.size();
                                }
                            });
                        }
                    }
                }
            }
        }
        initEmotionMainFragment();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initEvent() {
        CacheUtils.INSTANCE.setString(PublishPond.this, "selectTopicTag", "[]");
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                Log.e(TAG, "afterTextChanged: " + editable.toString());
                content = editable.toString();
                if (editable.length() <= 2000) {
                    tvTotalTextnum.setText((2000 - editable.length()) + "");
                }
                if (editable.length() > 2000 && isShowTip == true) {
                    isShowTip = false;
                    setSnackBar("已经超出最大字数限制", "", R.drawable.icon_tips_bad);
                }
            }
        });
    }

    @Override
    protected void initData() {
        params = new HttpParams();
    }

    private void showSoftInput() {
        imgSoftKeytag.setImageResource(R.drawable.icon_soft_down);
        etContent.requestFocus();
        etContent.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(etContent, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        imgSoftKeytag.setImageResource(R.drawable.icon_soft_up);
        mInputManager.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight;
        }

        if (softInputHeight < 0) {
            LogUtils.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        return softInputHeight;
    }

    @OnClick({R.id.finish, R.id.tv_choose_location, R.id.img_del_loc, R.id.ll_hideSoft, R.id.img_dele_cote, R.id.publish, R.id.delet_seletmusic, R.id.to_choose_tag, R.id.emoji, R.id.picture, R.id.music, R.id.vote})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                MobclickAgent.onEvent(this,"publish_pond_cancel");
                finish();
                break;
            case R.id.img_del_loc:
                addressDesc = "";
                lonTitude = 0D;
                latitude = 0D;
                tv_location.setVisibility(View.GONE);
                tv_choose_location.setVisibility(View.VISIBLE);
                img_del_loc.setVisibility(View.GONE);
                break;
            case R.id.tv_choose_location:
                MobclickAgent.onEvent(this,"publish_pond_add_location");

                goActivityForResult(LocationListActivity.class, REQ_LOCATION);
                break;
            case R.id.img_dele_cote:
                voteBean = null;
                rlCote.setVisibility(View.GONE);
                break;
            case R.id.publish:
                MobclickAgent.onEvent(this,"publish_pond_publish");
                status = 1;
                if (CacheUtils.INSTANCE.getBoolean(PublishPond.this, Constants.User.IS_LOGIN) == true) {
                    publishTopic();
                } else {
                    startActivity(new Intent(this, QuickLoginActivityNew.class));
                }
                break;
            case R.id.delet_seletmusic:
                music_id = 0;
                llSelectmusic.setVisibility(View.GONE);
                break;
            case R.id.to_choose_tag:
                MobclickAgent.onEvent(this,"publish_pond_add_tag");

                Intent intent2 = new Intent(this, ChoosePondTagNew.class);
                intent2.putExtra("selectTopicTag", hashTagJson);
                startActivity(intent2);
                break;
            case R.id.emoji:
                MobclickAgent.onEvent(this,"publish_pond_add_emoji");
                emotionMainFragment.mEmotionKeyboard.showEmotionLayout();
                break;
            case R.id.picture:
                choicePhotoWrapper();
                break;
            case R.id.music:
                MobclickAgent.onEvent(this,"publish_pond_add_music");

                Intent intent = new Intent(this, AddMusicNew.class);
                startActivityForResult(intent, AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN);
                break;
            case R.id.vote://创建投票
                MobclickAgent.onEvent(this,"publish_pond_add_vote");

                Intent intent1 = new Intent(this, VoteActivity.class);
                if (voteBean != null) {
                    intent1.putExtra("voteBean", voteBean);
                }
                startActivityForResult(intent1, CREATE_VOTE);
                break;
            case R.id.ll_hideSoft:
                if (isSoftInputShown()) {
                    hideSoftInput();
                } else {
                    showSoftInput();
                }
                break;
        }
    }

    /**
     * 图片参数设置
     */
    private void publishTopic() {
//        String text = etContent.getText().toString();
//        Pattern p = Pattern.compile("[0-9]*");
//        Matcher m = p.matcher(text);
//        if(m.matches() ){
//            Toast.makeText(PublishPond.this,"输入的是数字", Toast.LENGTH_SHORT).show();
//        }
//        p=Pattern.compile("[a-zA-Z]");
//        m=p.matcher(text);
//        if(m.matches()){
//            Toast.makeText(PublishPond.this,"输入的是字母", Toast.LENGTH_SHORT).show();
//        }
//        p=Pattern.compile("[\u4e00-\u9fa5]");
//        m=p.matcher(text);
//        if(m.matches()){
//            if(text.length()>5){
//                Toast.makeText(PublishPond.this,"输入的是5个汉字", Toast.LENGTH_SHORT).show();
//            }
//        }
        //这里是判断到底有没有5个是汉字
        int n=0;
        int nnum = 0;
        for(int i=0; i<etContent.getText().toString().length(); i++) {
            n = (int) etContent.getText().toString().charAt(i);
            if ((19968 <= n && n < 40623)) {
//                Toast.makeText(PublishPond.this,"第" + i + "个字符是中文", Toast.LENGTH_SHORT).show();
                nnum++;
            }
        }


        if (TextUtils.isEmpty(etContent.getText().toString())) {
            etContent.setFocusable(true);
            setSnackBar("输入池塘内容", "", R.drawable.icon_fails);
        } else if (nnum<5) {
            etContent.setFocusable(true);
            setSnackBar("内容不能少于5个字", "", R.drawable.icon_fails);
        } else {
            showLoadingView();
//            params.put("title", etTitle.getText().toString().trim());
            params.put("content", content);
            /**
             * 有图片
             *
             * */
            final ArrayList<String> selectedList = mPhotosSnpl.getData();
            if (selectedList != null && selectedList.size() > 0) {
                //上传图片并拿到哈希值
                try {
                    IMGCompressUtils.CompressorImage1(selectedList.size(), getApplicationContext(), selectedList, new IMGCompressUtils.CompressCallback() {
                        @Override
                        public void fileCallback(@NonNull List<File> files) {
                            if (files.size() > 0) {
                                FileUploadUtil fileUploadUtil = new FileUploadUtil();
                                fileUploadUtil.setFileList(files);
                                fileUploadUtil.upload(PublishPond.this, 1, new FileUploadUtil.UpLoadCallback() {
                                    @Override
                                    public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                                        hashStr = "";
                                        for (int i = 0; i < finishBeans.size(); i++) {
                                            String hash = "";
                                            hash = finishBeans.get(i).getImgpic();
                                            if (i != finishBeans.size() - 1) {
                                                hashStr += hash + ",";
                                            } else {
                                                hashStr += hash;
                                            }
                                        }
                                        if (!hashStr.isEmpty()) {
                                            params.put("imglist", hashStr);
                                            publish(params);
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
        if (!TextUtils.isEmpty(addressDesc)) {
            params.put("x", lonTitude);
            params.put("y", latitude);
            params.put("place_desc", addressDesc);
        }
        params.put("source", String.valueOf(source));
        params.put("status", String.valueOf(status));
        /**
         * 池塘标签
         *
         * */
        if (hashTagJson != null) {
            params.put("tag", hashTagJson);
        }

        /**
         * 音乐iD
         *
         * */
        if (music_id != 0) {
            params.put("item_id", music_id + "");
            params.put("item_type", "1");
        }
        if (topicId != 0) {
            params.put("id", topicId + "");
            url = ApiAddress.INSTANCE.BASE_URL + ApiAddress.PUBLISH_TOPIC + "/";
            url += topicId;
        }
        if (voteBean != null) {
            vioteItemstr = "";
            params.put("choice", "1");
            params.put("votetype", voteBean.getVotetype());
            params.put("hide", voteBean.getHide());
            params.put("question_name", voteBean.getQuestion_name());
            List<VoteBean.VoteItem> itemList = voteBean.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                if (i != itemList.size() - 1) {
                    vioteItemstr += "{\"id\":" + itemList.get(i).getId() + "," + "\"optionname\":\"" + itemList.get(i).getOptionname() + "\"},";
                } else {
                    vioteItemstr += "{\"id\":" + itemList.get(i).getId() + "," + "\"optionname\":\"" + itemList.get(i).getOptionname() + "\"}";
                }
            }
            vioteItemstr = "[" + vioteItemstr + "]";
            params.put("vote", vioteItemstr);
        } else {
            params.put("choice", "0");
        }
        Log.e(TAG, "publish: " + params);

        NetWork.INSTANCE.publishTopic(PublishPond.this, status, topicId, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                if (status == 1) {
                    setSnackBar("发布成功~", "", R.drawable.icon_success);
                }
                finish();

            }

            @Override
            public void doError(String msg) {
            }

            @Override
            public void doResult() {
                hideLoadingView();
            }
        });
    }

    /**
     * 去相册选择照片
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {//去选择照片

        MobclickAgent.onEvent(this,"publish_pond_add_pic");

        if (maxNum_photo > 0 && maxNum_photo <= 9) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            if (EasyPermissions.hasPermissions(this, perms)) {
                // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, maxNum_photo, null, false), REQUEST_CODE_CHOOSE_PHOTO);
            } else {
                EasyPermissions.requestPermissions(this, "图片选择需要以下权限:  1.访问设备上的照片  2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
            }
        } else {
            setSnackBar("最多选择9张哦~", "", R.drawable.icon_tips_bad);
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
            Toast.makeText(PublishPond.this, "您拒绝了选取照片所需的权限!", Toast.LENGTH_SHORT);
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
                if (selectedImages.size() > 0) {
                    mPhotosSnpl.setVisibility(View.VISIBLE);
                }
            } else if (resultCode == RESULT_OK && requestCode == REQ_LOCATION) {

                LocationInfo locationInfo = (LocationInfo) data.getSerializableExtra("LOCATION_DATA");
                addressDesc = locationInfo.getAddressDesc();
                lonTitude = locationInfo.getLonTitude();
                latitude = locationInfo.getLatitude();
                tv_location.setVisibility(View.VISIBLE);
                tv_choose_location.setVisibility(View.GONE);
                img_del_loc.setVisibility(View.VISIBLE);
                tv_location.setText(locationInfo.getAddressDesc());
            } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
                mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
                ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
                selectedList.clear();
                selectedList.addAll(selectedImages);
                if (selectedImages.size() > 0) {
                    mPhotosSnpl.setVisibility(View.VISIBLE);
                }
            } else if (requestCode == AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN && resultCode == AddMusicNew.REQUEST_CODE_SELECTED_MUSIC_BEAN) {
                selectedBean = (MusicInfo.DataBean) data.getSerializableExtra("selectedBean");
                if (selectedBean != null) {
                    llSelectmusic.setVisibility(View.VISIBLE);
                    music_id = selectedBean.getId();
                    tvSelectmusic.setText(selectedBean.getTitle());
                    tvSelectsinger.setText(selectedBean.getNickname());
                    if (selectedBean.getMusic_type() == 1) {
                        tv_type.setVisibility(View.VISIBLE);
                    }
                    ImageLoader.with(this)

                            .override(40, 40)
                            .url(selectedBean.getImgpic_info().getLink())
                            .error(R.drawable.nothing)
                            .into(imgSelected);
                }
            } else if (requestCode == CREATE_VOTE) {//投票
                voteBean = (VoteBean) data.getSerializableExtra("voteBean");
                Log.e(TAG, "onActivityResult: " + voteBean.toString());
                rlCote.setVisibility(View.VISIBLE);
                tvCoteTitle.setText(voteBean.getQuestion_name());
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
        setTagView();
    }

    private void setTagView() {
        if (CacheUtils.INSTANCE.getString(PublishPond.this, "selectTopicTag", "") != null && !CacheUtils.INSTANCE.getString(PublishPond.this, "selectTopicTag", "").equals("")) {
            String selectTopicTag = CacheUtils.INSTANCE.getString(PublishPond.this, "selectTopicTag", "");
            hashTagJson = selectTopicTag;
            try {
                final List<PondTagListBean.DataBean.TagBean> list = JSON.parseArray(selectTopicTag, PondTagListBean.DataBean.TagBean.class);
                ll_tag.removeAllViews();
                for (int i = 0; i < list.size(); i++) {
                    if (i < 3) {
                        View view = LayoutInflater.from(PublishPond.this).inflate(R.layout.pond_tag_del, null);
                        final TextView tvTag = view.findViewById(R.id.tvTag);
                        tvTag.setText(list.get(i).getTitle());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final TextView tvTag = view.findViewById(R.id.tvTag);
                                for (int i = 0; i < list.size(); i++) {
                                    if (tvTag.getText().toString().equals(list.get(i).getTitle())) {
                                        list.remove(i);
                                        CacheUtils.INSTANCE.setString(PublishPond.this, "selectTopicTag", JSON.toJSONString(list));
                                    }

                                }
                                ll_tag.removeView(view);
                            }
                        });
                        ll_tag.addView(view);

                    }
                }
            } catch (RuntimeException e) {
            }


        } else {
            toChooseTag.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtils.INSTANCE.setString(PublishPond.this, "selectTopicTag", "");
    }

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

    private void MaterialDialog(String content) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(
                content)//
                .btnText("取消", "确定")//
                .titleTextSize(16)
                .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                .contentTextSize(14)
                .btnTextSize(14)
                .btnTextColor(ContextCompat.getColor(this, R.color.base_red)
                        , ContextCompat.getColor(this, R.color.base_red))
                .showAnim(null)//
                .dismissAnim(null)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        status = 3;
                        if (!TextUtils.isEmpty(etContent.getText())) {
                            publishTopic();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            finish();
                        }
                    }
                }
        );
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            emotionMainFragment.mEmotionKeyboard.hideEmotionLayout(true);
        }
        return super.dispatchTouchEvent(ev);
    }
}
