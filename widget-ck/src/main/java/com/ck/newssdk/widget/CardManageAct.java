package com.ck.newssdk.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ck.newssdk.R;

public class CardManageAct extends Activity implements View.OnClickListener {
    private CheckBox mCheckBoxSearck, mCheckBoxWeather;
    private LinearLayout mLinearLayout;
    public static final String CARD_FORM_ACT = "card_for_act";
    public static final String TAG = "host_card_manage_widget";
    public static boolean isCheckSearch = true;
    public static boolean isCheckWeather = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_mng);
        mCheckBoxSearck = findViewById(R.id.select_cb_searck);
        mLinearLayout = findViewById(R.id.layout_back);
        mCheckBoxWeather = findViewById(R.id.select_cb_weather);
        mLinearLayout.setOnClickListener(this);
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
                isCheckSearch = isCheck;
            }
        });
        mCheckBoxWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                SPUtils.saveCbStateWeather(CardManageAct.this, isCheck);
                isCheckWeather = isCheck;
            }
        });
    }


    private void doCallback() {
        NewAppWidget newAppWidget = new NewAppWidget();
        Intent intent = new Intent(CARD_FORM_ACT);
        intent.putExtra("isCheckSearch", isCheckSearch);
        intent.putExtra("isCheckWeather", isCheckWeather);
        newAppWidget.onReceive(CardManageAct.this, intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.layout_back) {
            doCallback();
            finish();
        } else if (id == R.id.select_cb_searck) {
        } else if (id == R.id.select_cb_weather) {
        } else {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        doCallback();
        return super.onKeyDown(keyCode, event);
    }

}
