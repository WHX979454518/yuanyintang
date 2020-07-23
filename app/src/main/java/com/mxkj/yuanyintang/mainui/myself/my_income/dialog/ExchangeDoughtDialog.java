package com.mxkj.yuanyintang.mainui.myself.my_income.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.myself.my_income.EdittextUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by zuixia on 2018/5/14.
 */

public class ExchangeDoughtDialog {

    private DiaLogBuilder dialog;
    int fish_to_coin_rate;
    float my_fish_num;

    public static ExchangeDoughtDialog newInstance() {
        return new ExchangeDoughtDialog();
    }

    public ExchangeDoughtDialog fish_to_coin_rate(int fish_to_coin_rate) {
        this.fish_to_coin_rate = fish_to_coin_rate;
        return this;
    }

    public ExchangeDoughtDialog my_fish_num(float my_fish_num) {
        this.my_fish_num = my_fish_num;
        return this;
    }

    public ExchangeDoughtDialog showDialog(final Context mContext, final ExchangeDoughtDialog.onBtClick onBtClick) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.exchange_dought_dialog, null);
        final EditText et_num = view.findViewById(R.id.et_num);
        TextView tv_my_fish_num = view.findViewById(R.id.tv_my_fish_num);
        TextView tv_exchange_all = view.findViewById(R.id.tv_exchange_all);
        ImageView img_close = view.findViewById(R.id.img_close);
        final TextView tv_sure = view.findViewById(R.id.tv_sure);
        final TextView tv_can_exchange_num = view.findViewById(R.id.tv_can_exchange_num);
        final NumberFormat nf = new DecimalFormat("#.##");
        final NumberFormat nf2 = new DecimalFormat("#");
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setDismiss();
            }
        });


        tv_my_fish_num.setText("拥有小鱼干" + nf.format(my_fish_num));
        tv_can_exchange_num.setText("可获得" + nf2.format(my_fish_num * fish_to_coin_rate) + "个甜甜圈");

        tv_exchange_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO  全部兑换
                NumberFormat nf = new DecimalFormat("#.##");
                String format = nf.format(my_fish_num);
                et_num.setText(format + "");
            }
        });
        et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    et_num.setSelection(et_num.getText().length());
                    float et_num = Float.valueOf(charSequence.toString());
                    if (et_num > my_fish_num) {
                        tv_can_exchange_num.setText("你没有那么多小鱼干");
                        tv_sure.setClickable(false);
                        return;
                    } else {
                        float v = et_num * fish_to_coin_rate;
                        String format = nf.format(v);
                        tv_can_exchange_num.setText("可获得" + format + "个甜甜圈");
                        tv_sure.setClickable(true);
                    }
                } else {
                    float v = my_fish_num * fish_to_coin_rate;
                    String format = nf.format(v);
                    tv_can_exchange_num.setText("可获得" + format + "个甜甜圈");
                    tv_sure.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                EdittextUtils.judgeNumber(editable,et_num);
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_num.getText())) {
                    if (my_fish_num >= (Float.valueOf(et_num.getText().toString()))) {
                        onBtClick.onConfirm(Float.valueOf(et_num.getText().toString()));
                        dialog.setDismiss();
                    } else {
                        tv_can_exchange_num.setText("你没有那么多小鱼干~");
                    }
                }
            }
        });

        dialog = new DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER);
        dialog.show();
        return this;

    }

    public interface onBtClick {
        void onConfirm(float fishNum);
    }
}


