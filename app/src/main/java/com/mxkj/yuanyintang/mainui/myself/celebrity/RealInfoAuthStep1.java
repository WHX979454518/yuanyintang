package com.mxkj.yuanyintang.mainui.myself.celebrity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.bean.DegreeBean;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileBean;
import com.mxkj.yuanyintang.utils.ali_file_upload.FileUploadUtil;
import com.mxkj.yuanyintang.utils.app.ActivityCollector;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.photopicker.activity.BGAPhotoPickerActivity;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.MarqueTextView;
import com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.dynamic.activity.PublishDynamic.REQUEST_CODE_CHOOSE_PHOTO;
import static com.mxkj.yuanyintang.widget.choosepic_popuowindow.ChoosePicPopWindow.CAMERA_REQUEST_CODE;

public class RealInfoAuthStep1 extends StandardUiActivity {
    private static final String REAL_NAME = "realName";
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.step_text)
    TextView stepText;
    @BindView(R.id.wb_notice)
    WebView wb_notice;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bt_next)
    TextView btNext;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.img_status)
    ImageView imgStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_authIng)
    LinearLayout llAuthIng;
    @BindView(R.id.mar_tv_msg)
    MarqueTextView marTvMsg;
    @BindView(R.id.ll_idCard)
    TextView llIdCard;
    @BindView(R.id.ll_otherCard)
    TextView llOtherCard;
    @BindView(R.id.tv_s)
    TextView tvS;
    @BindView(R.id.tv_review_status)
    TextView tvReviewStatus;
    @BindView(R.id.tv_review_time)
    TextView tvReviewTime;
    @BindView(R.id.tv_tips_text)
    TextView tvTipsText;
    @BindView(R.id.ll_degree)
    LinearLayout ll_degree;

    @BindView(R.id.img_card1)
    ImageView imgCard1;
    @BindView(R.id.img_card2)
    ImageView imgCard2;
    @BindView(R.id.img_cardEg1)
    ImageView img_cardEg1;
    @BindView(R.id.img_cardEg2)
    ImageView img_cardEg2;
    @BindView(R.id.et_realName)
    EditText et_realName;
    @BindView(R.id.certificate_type_ed)
    TextView certificate_type_ed;
    @BindView(R.id.et_IdCard)
    EditText et_IdCard;
    @BindView(R.id.tv_confirm_oneBtn)
    TextView tv_confirm_oneBtn;
    @BindView(R.id.certificate_type_rl)
    RelativeLayout certificate_type_rl;

    @BindView(R.id.im_example1)
    ImageView im_example1;
    @BindView(R.id.im_example2)
    ImageView im_example2;
    @BindView(R.id.img_mask1)
    LinearLayout img_mask1;
    @BindView(R.id.img_mask2)
    LinearLayout img_mask2;
    WindowManager wm;
    View inflate;
    Dialog dialog;
    TextView real_info_ok,real_info_idcard_tv,real_info_othercertificates_tv;
    RelativeLayout real_info_idcard,real_info_othercertificates;


    private int setTo;//0代表身份证正面
    private boolean isadd1 = false, isIsadd2 = false;
    private String path1, path2;
    private PopupWindow popupWindow;
    @Nullable
    private String hash_card2 = "", hash_card1 = "";
    private boolean needFinish = true;
    private int authType;
    Handler handler = new Handler();
    private boolean finishAll;


    private int is_auth;
    private int auth_status;
    @Override
    public int setLayoutId() {
        return R.layout.activity_real_info_auth;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        initWebView();
    }

    @Override
    protected void initData() {
        setData("realnamenotice");
    }

    @Override
    protected void initEvent() {
        showLoadingView();
        NetWork.INSTANCE.authStatus(this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                AuthBean authBean = JSON.parseObject(resultData, AuthBean.class);
                isUserEvent(authBean);
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

    private void isUserEvent(AuthBean authBean) {
        hideLoadingView();
        List<AuthBean.DataBean> data = authBean.getData();
        AuthBean.DataBean dataBean = null;
        if (data != null && data.size() > 0) {
            dataBean = data.get(0);
            is_auth = dataBean.getIs_auth();
            auth_status = dataBean.getAuth_state();
        }
        switch (auth_status) {
            case -1://未通过
                final AuthBean.DataBean finalDataBean = dataBean;
                scrollView.setVisibility(View.GONE);
                llAuthIng.setVisibility(View.VISIBLE);
                tvTips.setText(finalDataBean.getReason());
                tvStatus.setText(authBean.getMsg());
                tvSure.setText("再次提交");
                imgStatus.setImageResource(R.drawable.auth_fail);
                break;
            case 0://未提交
                if (is_auth == 1) {
                    scrollView.setVisibility(View.VISIBLE);
                    llAuthIng.setVisibility(View.GONE);
                } else if (is_auth == 0) {
                    scrollView.setVisibility(View.GONE);
                    llAuthIng.setVisibility(View.VISIBLE);
                    tvTips.setText(authBean.getMsg());
                    tvSure.setText("完善资料");
                    imgStatus.setImageResource(R.drawable.auth_add_info);
                }
                break;
            case 1://已提交
                scrollView.setVisibility(View.GONE);
                llAuthIng.setVisibility(View.VISIBLE);
                tvStatus.setText(authBean.getMsg());
                tvTips.setText("真实姓名  " + dataBean.getName() + "\n" + "身份证号 " + dataBean.getNumber());
                tvSure.setText("朕知道了");
                imgStatus.setImageResource(R.drawable.auth_ingg);
                break;
            case 2://通过
                scrollView.setVisibility(View.GONE);
                llAuthIng.setVisibility(View.VISIBLE);
                imgStatus.setImageResource(R.drawable.auth_success);
                tvStatus.setText(authBean.getMsg());
                tvTips.setText("真实姓名  " + dataBean.getName() + "\n" + "身份证号 " + dataBean.getNumber());
                tvSure.setText("朕知道了");
                break;
        }
    }

    @OnClick({R.id.finish, R.id.tv_sure, R.id.ll_idCard, R.id.ll_otherCard, R.id.img_card1, R.id.img_card2,R.id.tv_confirm_oneBtn,R.id.certificate_type_rl})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, RealInfoAuthStep2.class);
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.tv_sure:
                switch (auth_status) {
                    case -1://失败，再次提交
                        NetWork.INSTANCE.changeAuthStatus(RealInfoAuthStep1.this, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {

                            }

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });
                        NetWork.INSTANCE.authStatus(this, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                AuthBean authBean = JSON.parseObject(resultData, AuthBean.class);
                                isUserEvent(authBean);
                            }

                            @Override
                            public void doError(String msg) {
                                hideLoadingView();
                            }

                            @Override
                            public void doResult() {

                            }
                        });
//                        scrollView.setVisibility(View.VISIBLE);
//                        llAuthIng.setVisibility(View.GONE);
                        Intent intent1 = new Intent(RealInfoAuthStep1.this,RealInfoAuthStep1.class);
                        finish();
                        startActivity(intent1);

                        break;
                    case 0://编辑资料

                        finish();
                        break;
                    case 1://已提交
                        finish();
                        break;
                    case 2://通过
                        finish();
                        break;
                }
                break;
            case R.id.ll_idCard:
                intent.putExtra("authType", 0);
                startActivity(intent);
                break;
            case R.id.ll_otherCard:
                intent.putExtra("authType", 1);
                startActivity(intent);
                break;


            case R.id.img_card1:
                setTo = 0;
                chooseCardImg();
                break;
            case R.id.img_card2:
                setTo = 1;
                chooseCardImg();
                break;
            case R.id.tv_confirm_oneBtn:
                Auth();
                break;

            case R.id.certificate_type_rl:
                dialog = new Dialog(RealInfoAuthStep1.this, R.style.ActionSheetDialogStyle);
                //填充对话框的布局
                inflate = LayoutInflater.from(RealInfoAuthStep1.this).inflate(R.layout.realinfoauthstep_dialog, null);
                //初始化控件
                real_info_ok = inflate.findViewById(R.id.real_info_ok);//确定
                real_info_idcard = inflate.findViewById(R.id.real_info_idcard);//身份证RelativeLayout
                real_info_idcard_tv= inflate.findViewById(R.id.real_info_idcard_tv);//身份证
                real_info_othercertificates = inflate.findViewById(R.id.real_info_othercertificates);//其他证件RelativeLayout
                real_info_othercertificates_tv = inflate.findViewById(R.id.real_info_othercertificates_tv);//其他证件
                //将布局设置给Dialog
                dialog.setContentView(inflate);
                //获取当前Activity所在的窗体
                Window dialogWindow1 = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow1.setGravity(Gravity.BOTTOM);
                //获得窗体的属性
                WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
                lp1.y = 2;//设置Dialog距离底部的距离
                wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display d1 = wm.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p1 = dialogWindow1.getAttributes(); // 获取对话框当前的参数值
                p1.height = (int) (d1.getHeight() * 0.4); // 高度设置为屏幕的0.6，根据实际情况调整
                p1.width = (int) (d1.getWidth() * 1); // 宽度设置为屏幕的0.65，根据实际情况调整
                //将属性设置给窗体
                dialogWindow1.setAttributes(lp1);
                dialog.show();//显示对话框
                real_info_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                real_info_idcard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        certificate_type_ed.setTextColor(Color.parseColor("#2b2b2b"));
                        certificate_type_ed.setText(real_info_idcard_tv.getText());
                        img_cardEg1.setImageResource(R.mipmap.card1);
                        img_cardEg2.setImageResource(R.drawable.card2);
                    }
                });
                real_info_othercertificates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        certificate_type_ed.setTextColor(Color.parseColor("#2b2b2b"));
                        certificate_type_ed.setText(real_info_othercertificates_tv.getText());
                        img_cardEg1.setImageResource(R.mipmap.img_other_documents1);
                        img_cardEg2.setImageResource(R.drawable.img_other_documents2);
                    }
                });
                break;


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

    private void setData(String code) {
        NetWork.INSTANCE.getUserNotice(this, code, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                JSONObject jsonObject = JSON.parseObject(resultData);
                JSONObject data = jsonObject.getJSONObject("data");
                String title = data.getString("title");
                String content = data.getString("content");
                wb_notice.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
        NetWork.INSTANCE.getDegree(this, "auth", new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                DegreeBean degreeBean = JSON.parseObject(resultData, DegreeBean.class);
                DegreeBean.DataBean data = degreeBean.getData();
                if (data != null) {
                    ll_degree.setVisibility(View.VISIBLE);
                    String wait_time_desc = data.getWait_time_desc();
                    tvReviewTime.setText("预计审核时间："+wait_time_desc);
                    tvTipsText.setText(StringUtils.isEmpty(data.getRemark()));
//                    1畅通，2正常，3繁忙，4堵爆了 ，0暂停
                    int status = data.getStatus();
                    GradientDrawable myGrad = (GradientDrawable) tvReviewStatus.getBackground();
                    switch (status) {
                        case 0:
                            tvReviewStatus.setText("暂停");
                            myGrad.setColor(Color.parseColor("#cccccc"));
                            break;
                        case 1:
                            tvReviewStatus.setText("畅通");
                            myGrad.setColor(Color.parseColor("#c3e645"));
                            break;
                        case 2:
                            tvReviewStatus.setText("正常");
                            myGrad.setColor(Color.parseColor("#7fd4ff"));

                            break;
                        case 3:
                            tvReviewStatus.setText("繁忙");
                            myGrad.setColor(Color.parseColor("#ffb2b2"));

                            break;
                        case 4:
                            tvReviewStatus.setText("堵爆了");
                            myGrad.setColor(Color.parseColor("#ffad73"));
                            break;
                    }


                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });

    }

    private void initWebView() {
        wb_notice.getSettings().setJavaScriptEnabled(true);
        wb_notice.getSettings().setBuiltInZoomControls(true);
        wb_notice.getSettings().setDisplayZoomControls(false);
        wb_notice.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wb_notice.getSettings().setDefaultTextEncodingName("UTF-8");
        wb_notice.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.getSettings().setMixedContentMode(webView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意6.0权限
        }
    }

    public static class AuthBean {
        private int code;
        private String msg;
        private List<DataBean> data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * is_auth : 2
             * name : 哈*哈
             * number : 111111****1111
             */

            private int is_auth;
            private int c_state;
            private int auth_state;
            private int certification_distinction;
            private String name;
            private String realname;
            private String number;
            private String reason;
            private String phone;
            private String email;

            public int getAuth_state() {
                return auth_state;
            }

            public void setAuth_state(int auth_state) {
                this.auth_state = auth_state;
            }

            public int getC_state() {
                return c_state;
            }

            public void setC_state(int c_state) {
                this.c_state = c_state;
            }

            public int getCertification_distinction() {
                return certification_distinction;
            }

            public void setCertification_distinction(int certification_distinction) {
                this.certification_distinction = certification_distinction;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public int getIs_auth() {
                return is_auth;
            }

            public void setIs_auth(int is_auth) {
                this.is_auth = is_auth;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }
    }




    private void chooseCardImg() {
        ChoosePicPopWindow.INSTANCE.showPopupWindow(this, findViewById(R.id.activity_real_info), false, new ChoosePicPopWindow.ChoosePicCallback() {
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

    private void upLoadImg(final File file) {
        String imagePath = file.getAbsolutePath();
        if (setTo == 0) {
            path1 = imagePath;
            CacheUtils.INSTANCE.setString(RealInfoAuthStep1.this, "path1", imagePath);
            if(null != imagePath || imagePath.equals("")){
                im_example1.setVisibility(View.GONE);
                img_mask1.setVisibility(View.GONE);
            }
            Glide.with(this).load(file).asBitmap().into(img_cardEg1);
        } else if (setTo == 1) {
            path2 = imagePath;
            CacheUtils.INSTANCE.setString(RealInfoAuthStep1.this, "path2", imagePath);
            if(null != imagePath || imagePath.equals("")){
                im_example2.setVisibility(View.GONE);
                img_mask2.setVisibility(View.GONE);
            }
            Glide.with(this).load(file).asBitmap().into(img_cardEg2);

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
            fileUploadUtil.upload(RealInfoAuthStep1.this, 1, new FileUploadUtil.UpLoadCallback() {
                @Override
                public void upLoadSuccess(List<FileBean.DataBean> finishBeans) {
                    Log.e(TAG, "实名认证上传图片----finishBeans==: " + finishBeans.size());
                    hideLoadingView();
                    if (finishBeans != null && finishBeans.size() > 0) {
                        String imgpic_link = finishBeans.get(0).getImgpic_link();
                        if (!TextUtils.isEmpty(imgpic_link)) {
                            if (setTo == 0) {
                                hash_card1 = finishBeans.get(0).getImgpic();
                                Glide.with(RealInfoAuthStep1.this).load(imgpic_link).asBitmap().into(img_cardEg1);
                                isadd1 = true;
                            } else {
                                isIsadd2 = true;
                                hash_card2 = finishBeans.get(0).getImgpic();
                                Glide.with(RealInfoAuthStep1.this).load(imgpic_link).asBitmap().into(img_cardEg2);
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


    private void Auth() {
        Log.e(TAG, "Auth: ----"+hash_card1+"----"+hash_card2 );
        if (hash_card1 == null || hash_card2 == null) {
            setSnackBar("未添加证件照片", "", R.drawable.icon_fails);
        } else {
            if (TextUtils.isEmpty(et_realName.getText().toString().trim())) {
                setSnackBar("请填写真实姓名", "", R.drawable.icon_fails);
            } else {
                if (TextUtils.isEmpty(certificate_type_ed.getText().toString().trim())) {
                    setSnackBar("请选择证件类型", "", R.drawable.icon_fails);
                } else {
                    if (TextUtils.isEmpty(et_IdCard.getText().toString().trim())) {
                        setSnackBar("请填写证件号码", "", R.drawable.icon_fails);
                    } else {
                        HttpParams params = new HttpParams();
                        params.put("distinction", "1");
                        params.put("front", hash_card1);
                        params.put("back", hash_card2);
                        params.put("realname", et_realName.getText().toString().trim());
                        params.put("number", et_IdCard.getText().toString().trim());
//                        params.put("address", et_IdCard.getText().toString().trim());
                        params.put("type", authType);
                        NetWork.INSTANCE.RealInfoAuth(RealInfoAuthStep1.this, params, new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
//                                更新本地保存的用户信息数据
                                UserInfoUtil.getUserInfoById(0, RealInfoAuthStep1.this, new UserInfoUtil.UserCallBack() {
                                    @Override
                                    public void doNext(UserInfo infoBean) {

                                    }
                                });
                                scrollView.setVisibility(View.GONE);
                                llAuthIng.setVisibility(View.VISIBLE);
                                finishAll=true;
                            }

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });
                    }
                }
            }
        }
    }


}
