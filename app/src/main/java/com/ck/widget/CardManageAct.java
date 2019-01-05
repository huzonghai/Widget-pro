package com.ck.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class CardManageAct extends Activity implements View.OnClickListener {
    private CheckBox mCheckBoxSearck, mCheckBoxWeather;
    private ImageView mImageViewBack;
    public static final String CARD_FORM_ACT = "card_for_act";
    public static final String TAG = "host_card_manage_widget";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_manage);
        mImageViewBack = findViewById(R.id.imgv_back);
        mCheckBoxSearck = findViewById(R.id.select_cb_searck);
        mCheckBoxWeather = findViewById(R.id.select_cb_weather);
        mImageViewBack.setOnClickListener(this);
        mCheckBoxSearck.setOnClickListener(this);
        mCheckBoxSearck.setOnClickListener(this);
        initClick();
    }

    private void initClick() {
        mCheckBoxSearck.setChecked(SPUtils.getCbStateSearch(this));
        mCheckBoxWeather.setChecked(SPUtils.getCbStateWeather(this));

        mCheckBoxSearck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                SPUtils.saveCbStateSearch(CardManageAct.this, isCheck);
                NewAppWidget newAppWidget = new NewAppWidget();
                Intent intent = new Intent(CARD_FORM_ACT);
                intent.putExtra("type", 0);
                intent.putExtra("check", isCheck);
                newAppWidget.onReceive(CardManageAct.this, intent);
            }
        });
        mCheckBoxWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                SPUtils.saveCbStateWeather(CardManageAct.this, isCheck);
                NewAppWidget newAppWidget = new NewAppWidget();
                Intent intent = new Intent(CARD_FORM_ACT);
                intent.putExtra("type", 1);
                intent.putExtra("check", isCheck);
                newAppWidget.onReceive(CardManageAct.this, intent);
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgv_back:
                finish();
                break;
            case R.id.select_cb_searck:
                break;
            case R.id.select_cb_weather:
                break;
            default:
        }
    }


}
