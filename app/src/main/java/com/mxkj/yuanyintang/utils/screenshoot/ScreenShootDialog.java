package com.mxkj.yuanyintang.utils.screenshoot;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.myself.activity.FeedBackActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.mainui.myself.activity.SuggestActivity;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;

import static com.mxkj.yuanyintang.mainui.myself.activity.FeedBackActivity.SCREEN_SHOOT_PATH;

/**
 * Created by zuixia on 2018/4/21.
 */

public class ScreenShootDialog {
    private String path;
    private DiaLogBuilder dialog;

    public static ScreenShootDialog newInstance() {
        return new ScreenShootDialog();
    }

    public ScreenShootDialog showDialog(final Context mContext, final String path) {
        this.path = path;
        View view = View.inflate(mContext, R.layout.dialog_screen_shoot, null);
        dialog = new DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER);

        TextView tv_feedback = view.findViewById(R.id.tv_feedback);
        TextView tv_share = view.findViewById(R.id.tv_share);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setDismiss();
            }
        });
        tv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SuggestActivity.class);
                intent.putExtra(SCREEN_SHOOT_PATH,path);
                mContext.startActivity(intent);

            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path == null) {
                    return;
                }
                MusicBean musicBean = new MusicBean();
                MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                shareDataBean.setType("img");
                shareDataBean.setImgFilePath(path);
//                shareDataBean.setUid(bean.getUid());
//                shareDataBean.setNickname(bean.getNickname());
//                shareDataBean.setTitle(bean.getTitle());
                musicBean.setShareDataBean(shareDataBean);
                ShareBottomDialog shareBottomDialog = new ShareBottomDialog(mContext, musicBean);
                shareBottomDialog.show();

            }
        });
        dialog.show();
        return this;
    }


}
