package com.yayawan.sdk.account.widget;

import com.yayawan.sdk.utils.ResourceUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
    private Context mContext;

    private LayoutInflater inflater;

    private LayoutParams lp;

    private TextView loadtext;

    private ProgressBar mPb;

    public LoadingDialog(Context context) {
        super(context, ResourceUtil.getStyleId(context, "CustomProgressDialog"));

        this.mContext = context;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(
                ResourceUtil.getLayoutId(mContext, "prompt_loading_dialog"),
                null);
        loadtext = (TextView) layout.findViewById(ResourceUtil.getId(mContext,
                "tv_message"));
        mPb = (ProgressBar) layout.findViewById(ResourceUtil.getId(mContext,
                "pb_loading"));

        setContentView(layout);

        // 设置window属性
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = (float) 0.5; // 设置背景遮盖
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    public void setLoadText(String content) {
        loadtext.setText(content);
    }

    public void setMax(int max) {
        mPb.setMax(max);
    }

    public void setProgress(int progress) {
        mPb.setProgress(progress);
    }
}
