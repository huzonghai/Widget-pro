//package com.ck.newssdk.ui.web;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.TextView;
//
//import com.ck.newssdk.R;
//import com.ck.newssdk.utils.SharePDataBaseUtils;
//
//
///**
// * Created by wy on 2017/8/31.
// */
//
//@SuppressLint("ValidFragment")
//public class WebBrowsingSelectDialog extends DialogFragment implements View.OnClickListener {
//    private Context context;
//    private TextView cancelBtn;
//    private TextView okBtn;
//
//    private OnWebSelectDialogMiss dialogMiss;
//
//    public void setDialogMiss(OnWebSelectDialogMiss dialogMiss) {
//        this.dialogMiss = dialogMiss;
//    }
//
//    public WebBrowsingSelectDialog(Context context) {
//        this.context = context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        View view = inflater.inflate(R.layout.browsing_select_dialog_layout, container, false);
//
//        cancelBtn = (TextView) view.findViewById(R.id.browsing_select_cancel_btn);
//        okBtn = (TextView) view.findViewById(R.id.browsing_select_ok_btn);
//        cancelBtn.setOnClickListener(this);
//        okBtn.setOnClickListener(this);
//
//        return view;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.browsing_select_ok_btn) {
//            SharePDataBaseUtils.saveIsBrowseMode(context, true);
//            dialogMiss.onWebSelectDialogMiss();
//            dismiss();
//        } else if (i == R.id.browsing_select_cancel_btn) {
//            dismiss();
//        }
//    }
//
//    interface OnWebSelectDialogMiss {
//        void onWebSelectDialogMiss();
//    }
//}
