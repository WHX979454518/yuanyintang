package com.mxkj.yuanyintang.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean;
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;


public class MvVideoActivity extends AppCompatActivity {

    private NiceVideoPlayer mNiceVideoPlayer;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mvvideoavtivity);
        init();
    }

    private String bioashi;

    private void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mvdate");

        bioashi = (String) bundle.getSerializable("bioashi");
        if (bioashi.equals("2")) {
            MyReleaseBean.DataBean date = (MyReleaseBean.DataBean) bundle.getSerializable("mymv");
//            mvMusicName.setText(date.getTitle());
//            //mvUserPhoto.setImageResource(date.getImgpic_link());
//            mvUserName.setText(date.getNickname());
//            //mvPlaynumber.setText(date.getCounts());//记得强转类型，是int型的
//            mvUpdate.setText(date.getPlaytime());
            mNiceVideoPlayer = (NiceVideoPlayer) findViewById(R.id.nice_video_player);

            mNiceVideoPlayer.setUp(date.getMv_info().get(0).getLink(), null);
        } else {
            HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean date = (HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean) bundle.getSerializable("mv");
//            mvMusicName.setText(date.getTitle());
//            //mvUserPhoto.setImageResource(date.getImgpic_link());
//            mvUserName.setText(date.getNickname());
//            //mvPlaynumber.setText(date.getCounts());//记得强转类型，是int型的
//            mvUpdate.setText(date.getPlaytime());

            mNiceVideoPlayer = (NiceVideoPlayer) findViewById(R.id.nice_video_player);
            Log.i("mmmmm", "" + date.getMv_info().getLink());
            mNiceVideoPlayer.setUp(date.getMv_info().getLink(), null);
        }
        //获取数据


        //mNiceVideoPlayer.setUp("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4", null);
        NiceVideoPlayerController controller = new NiceVideoPlayerController(this);
        controller.setTitle("");
        //controller.setImage("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg");//封面图
        mNiceVideoPlayer.setController(controller);
        mNiceVideoPlayer.start();


        if (MediaService.mediaPlayer != null) {
            MediaService.mediaPlayer.pause();
        } else {

        }


        back = (ImageView) findViewById(R.id.back);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mNiceVideoPlayer.isFullScreen()) {
                    mNiceVideoPlayer.exitFullScreen();
                } else if (mNiceVideoPlayer.isTinyWindow()) {
                    mNiceVideoPlayer.exitTinyWindow();
                } else {
                    MvVideoActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNiceVideoPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mNiceVideoPlayer.restart();
    }

}
