package com.example.newsdkecdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mumayi.paymentmain.business.FindUserDataListener;
import com.mumayi.paymentmain.business.ILoginCallback;
import com.mumayi.paymentmain.business.RequestFactory;
import com.mumayi.paymentmain.business.ResponseCallBack;
import com.mumayi.paymentmain.dao.HttpRequestFactory;
import com.mumayi.paymentmain.ui.*;

import com.mumayi.paymentmain.util.PaymentConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zeus.plugin.PluginManager;

/**
 * 演示demo
 *  @author DHY
 *  @time 2017-7-31 10:29:34
 */

public class WelcomeActivity extends Activity implements ILoginCallback {

    private Button btn_demo_login = null;
    private PaymentCenterInstance mInstance = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        mContext=this;

        // 初始化支付SDK用户中心
        mInstance = PaymentCenterInstance.getInstance(WelcomeActivity.this);

        //appkey   appName
        mInstance.initial("42bc87ca29a772d0mBj6Pxj1GtUIDqPfZg3ubUjpWeefTB8RPjVThxu4cKa5sB0", "倚天屠龙记 ");

        // 设置角色所在区服
        mInstance.setUserArea("A区");

        // 设置角色名
        mInstance.setUserName("漩涡鸣人");
        // 设置角色等级
        mInstance.setUserLevel(99);

        // 设置是否开启bug模式， true打开可以显示Log日志， false不显示
        mInstance.setTestMode(true);

        // 设置登陆回调
        mInstance.setLoginCallBack(this);

        // 确保每次在刚进入游戏都会检测本地数据
        // 在调用登陆之前调用
        mInstance.findUserInfo();

        initView();
        
//        mInstance.setChangeAccountAutoToLogin(false);
        
        //这个是检测是否具有开启悬浮窗的权限的方法  请开发者调用在登陆之前！！！
//        mInstance.checkFloatPermission();

    }

    private void initView() {
        btn_demo_login = (Button) findViewById(R.id.btn_demo_login);

        //btn_demo_login 这个相当于游戏界面上的登陆按钮
        btn_demo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInstance.go2Login(WelcomeActivity.this);

            }
        });
    }

    @Override
    public void onLoginSuccess(String loginSuccess) {
        Log.d("Welcome", "这是登陆成功的回调数据   ----->" + loginSuccess);

        if (!TextUtils.isEmpty(loginSuccess)) {
            try {
                JSONObject loginobject = new JSONObject(loginSuccess);
                String loginState = loginobject.getString(PaymentConstants.LOGIN_STATE);
                Log.d("登陆状态是：--->" , loginState);
                if (loginState != null && loginState.equals(PaymentConstants.STATE_SUCCESS)) {

                    /**  uname:用户名， uid:用户ID
                     * token:是用来服务器验证登录，注册是不是成功，用seesion来解签,解签方法见文档
                     * 所有注册，一键注册，登录的接口成功最后都会走这个回调接口
                     * 在这里进入游戏
                     */
                    String uname = loginobject.getString("uname");
                    String uid = loginobject.getString("uid");
                    String token = loginobject.getString("token");
                    String session = loginobject.getString("session");

                    Log.d("","用户的uid-->" + uid + " 用户名-->" + uname + " 获取token-->" + token + " 获取session-->" + session);

                    Intent go2play_intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(go2play_intent);
                    finish();
                }

            } catch (JSONException e) {
                Log.d("解析登陆回调异常" , e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoginFail(String status, String logiFail) {
        Log.d("Welcome", "这是登陆失败的回调数据  status ----->" + status);
        Log.d("Welcome", "这是登陆失败的回调数据  logiFail ----->" + logiFail);
        JSONObject statusobject = null;
        JSONObject logiFailobject = null;
        try {
            statusobject = new JSONObject(status);
            // 登录失败
            String loginStatus = statusobject.getString(PaymentConstants.LOGIN_STATE);
            if (!TextUtils.isEmpty(loginStatus) && loginStatus.equals(PaymentConstants.STATE_FAILED)) {

                Log.d("","登陆失败回调-----------");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
