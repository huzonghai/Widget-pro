package com.ck.newssdk.ui.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ck.newssdk.R;


/**
 * Created by wy on 2017/8/29.
 */

public class MyProgressDialog extends ProgressDialog implements View.OnClickListener {
    private View progressLayout;

    public MyProgressDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.progress_layout);//loading的xml文件

        progressLayout = findViewById(R.id.progress_layout);
        progressLayout.setOnClickListener(this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
