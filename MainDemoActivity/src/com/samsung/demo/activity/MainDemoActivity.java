package com.samsung.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iapppay.samsung.v403_10.R;
import com.samsung.sdk.main.IAppPay;

/**
 * 完整接入流程 实例
 * 1：IAppPay.init（）//建议在APP的第一个界面调用
 * 2：IAppPay.startPay（）//发起支付
 */
public class MainDemoActivity extends Activity{
    int sdkType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.iapppay_demo_activity_main_demo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final RadioGroup gp = (RadioGroup)findViewById(R.id.orderBy);
        gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });

        final Button pAnci = (Button) findViewById(R.id.button1);
        final EditText userIdText = (EditText) findViewById(R.id.appuseridEdit);
        final EditText acidText = (EditText) findViewById(R.id.acidEdit);

        pAnci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appUserID = userIdText.getText().toString();//appUerID必须填写
                if (TextUtils.isEmpty(appUserID)) {
                    Toast.makeText(MainDemoActivity.this, "请输入AppUserID", Toast.LENGTH_SHORT).show();
                    userIdText.requestFocus();
                    return;
                }

                String acid = acidText.getText().toString();//acid 渠道标识，可以为空

                int id = gp.getCheckedRadioButtonId();
                if (id == R.id.orderBy1) {
                    sdkType = IAppPay.PORTRAIT;//竖屏
                }else if (id == R.id.orderBy2) {
                    sdkType = IAppPay.LANDSCAPE;//横屏
                }else if (id == R.id.orderBy3) {
                    sdkType = IAppPay.SENSOR_LANDSCAPE;//横屏自动切换
                }

                Intent intent = new Intent(MainDemoActivity.this, GoodsListActivity.class);
                intent.putExtra("appuserid", appUserID);//用户在商户应用的唯一标识，建议为用户帐号。对于游戏，需要区分到不同区服，#号分隔；比如游戏帐号abc在01区，则传入“abc#01”
                intent.putExtra("acid", acid);//渠道标识
                intent.putExtra("screentype", sdkType);
                startActivity(intent);
            }
        });
    }

}
