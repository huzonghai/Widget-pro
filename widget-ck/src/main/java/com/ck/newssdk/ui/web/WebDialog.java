package com.ck.newssdk.ui.web;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ck.newssdk.R;
import com.ck.newssdk.utils.SharePDataBaseUtils;


/**
 * Created by wy on 2017/8/29.
 */

public class WebDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout webDialogSelectFontLayout;
    private LinearLayout webDialogFontShareLayout;
    private  ImageView webDialogSmallerIv;
    private  ImageView webDialogNormalIv;
    private  ImageView webDialogLargerIv;
    private View dismiss;
    private Context context;
    private boolean isShare = true;
    private OnWebDialogListener listener;
    private ImageView font, share;
    private TextView cancel;

    public void setListener(OnWebDialogListener listener) {
        this.listener = listener;
    }

    public WebDialog(@NonNull Context context) {
        super(context, R.style.Transparent);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.web_dialog_layout);
        webDialogSelectFontLayout = findViewById(R.id.web_dialog_select_font_layout);
        webDialogFontShareLayout = findViewById(R.id.web_dialog_font_share_layout);
        webDialogSmallerIv = findViewById(R.id.web_dialog_smaller_iv);
        webDialogNormalIv = findViewById(R.id.web_dialog_normal_iv);
        webDialogLargerIv = findViewById(R.id.web_dialog_larger_iv);

        font = findViewById(R.id.web_dialog_font_btn);
        share = findViewById(R.id.web_dialog_share_btn);
        cancel = findViewById(R.id.web_dialog_cancel_btn);
        dismiss = findViewById(R.id.web_dialog_cancel_layout);

        font.setOnClickListener(this);
        share.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dismiss.setOnClickListener(this);
        webDialogSmallerIv.setOnClickListener(this);
        webDialogNormalIv.setOnClickListener(this);
        webDialogLargerIv.setOnClickListener(this);

        Window window = getWindow();
        window.setWindowAnimations(R.style.dialog_style);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);


        //保存修改的文字大小
        selectImg(SharePDataBaseUtils.getWebTextSize(context));
    }

    private void selectImg(int i) {
        switch (i) {
            case 1:
                webDialogSmallerIv.setImageResource(R.mipmap.btn_font_small_click);
                webDialogNormalIv.setImageResource(R.mipmap.btn_font_middle);
                webDialogLargerIv.setImageResource(R.mipmap.btn_font_big);
                break;
            case 2:
                webDialogSmallerIv.setImageResource(R.mipmap.btn_font_small);
                webDialogNormalIv.setImageResource(R.mipmap.btn_font_middle_click);
                webDialogLargerIv.setImageResource(R.mipmap.btn_font_big);
                break;
            case 3:
                webDialogSmallerIv.setImageResource(R.mipmap.btn_font_small);
                webDialogNormalIv.setImageResource(R.mipmap.btn_font_middle);
                webDialogLargerIv.setImageResource(R.mipmap.btn_font_big_click);
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        webDialogSelectFontLayout.setVisibility(View.GONE);
        webDialogFontShareLayout.setVisibility(View.VISIBLE);
        isShare = true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.web_dialog_font_btn) {
            isShare = false;
            webDialogSelectFontLayout.setVisibility(View.VISIBLE);
            webDialogFontShareLayout.setVisibility(View.GONE);

        } else if (i == R.id.web_dialog_share_btn) {
            listener.onWebShareListener();

        } else if (i == R.id.web_dialog_cancel_btn) {
            if (isShare) {
                dismiss();
            } else {
                webDialogSelectFontLayout.setVisibility(View.GONE);
                webDialogFontShareLayout.setVisibility(View.VISIBLE);
                isShare = true;
            }

        } else if (i == R.id.web_dialog_smaller_iv) {
            listener.onWebFontSelectListener(1);
            selectImg(1);

        } else if (i == R.id.web_dialog_normal_iv) {
            listener.onWebFontSelectListener(2);
            selectImg(2);

        } else if (i == R.id.web_dialog_larger_iv) {
            listener.onWebFontSelectListener(3);
            selectImg(3);

        } else if (i == R.id.web_dialog_cancel_layout) {
            dismiss();

        }

    }

    interface OnWebDialogListener {
        /**
         * @param fontSize 1小，2中，3大
         */
        void onWebFontSelectListener(int fontSize);

        void onWebShareListener();
    }

}
