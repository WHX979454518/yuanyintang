package com.mxkj.yuanyintang.mainui.myself.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.DialogActivity;

/**
 * Created by LiuJie on 2017/10/7.
 */

public class SelectSexDialog extends DialogActivity {

    private Button button1, button2, button3;
    public static final String SEX_DATA = "sex";

    @Override
    protected int exitAnim() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_sex_popupwindow);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button1 = (Button) findViewById(android.R.id.button1);
        button2 = (Button) findViewById(android.R.id.button2);
        button3 = (Button) findViewById(android.R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backData("男");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backData("女");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void backData(String s) {
        Intent intent = new Intent();
        intent.putExtra(SEX_DATA, s);
        setResult(RESULT_OK, intent);
        finish();
    }
}
