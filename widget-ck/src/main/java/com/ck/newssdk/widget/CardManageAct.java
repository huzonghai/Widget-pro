package com.ck.newssdk.widget;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ck.newssdk.R;

public class CardManageAct extends AppCompatActivity implements View.OnClickListener {
    private CheckBox mCheckBoxSearck, mCheckBoxWeather;
    private LinearLayout mLinearLayout;
    public static final String CARD_FORM_ACT = "card_for_act";
    public static final String TAG = "host_card_manage_widget";
    public static boolean isCheckSearch = true;
    public static boolean isCheckWeather = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bg));
        }
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
        isCheckSearch = SPUtils.getCbStateSearch(this);
        isCheckWeather = SPUtils.getCbStateWeather(this);

        mCheckBoxSearck.setChecked(isCheckSearch);
        mCheckBoxWeather.setChecked(isCheckWeather);
        mCheckBoxSearck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                isCheckSearch = isCheck;
            }
        });
        mCheckBoxWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                isCheckWeather = isCheck;
            }
        });
    }


    private void doCallback() {
        SPUtils.saveCbStateSearch(CardManageAct.this, isCheckSearch);
        SPUtils.saveCbStateWeather(CardManageAct.this, isCheckWeather);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doCallback();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            doCallback();
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint() {
        doCallback();
        super.onUserLeaveHint();
    }
}
