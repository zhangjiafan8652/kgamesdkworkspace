package com.example.newsdkecdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mumayi.paymentmain.business.ILogoutCallback;
import com.mumayi.paymentmain.business.ITradeCallback;
import com.mumayi.paymentmain.ui.PaymentCenterDownLoadService;
import com.mumayi.paymentmain.ui.PaymentCenterInstance;
import com.mumayi.paymentmain.ui.PaymentUsercenterContro;
import com.mumayi.paymentmain.ui.pay.MMYInstance;
import com.mumayi.paymentmain.ui.usercenter.PaymentFloatInteface;
import com.mumayi.paymentmain.util.PaymentConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 测试的游戏主界面，在这里调用支付或者用户中心相关接口
 *
 * @author DHY
 * @time 2017-7-31 10:35:28
 */
public class MainActivity extends Activity implements ITradeCallback, ILogoutCallback {
    private PaymentCenterInstance mInstance = null;
    private PaymentUsercenterContro mUserCenter = null;

    private ImageView mImgUsercenter = null;

    private Button mBtnBuy = null;

  


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mInstance = PaymentCenterInstance.getInstance(MainActivity.this);

        //交易支付回调
        mInstance.setTradeCallback(this);

        //注销回调
        mInstance.setLogoutCallback(this);

        // 获取用户中心的实例
        mUserCenter = mInstance.getUsercenterApi(this);

       
        mUserCenter.checkLogin();
        mUserCenter.getForumInfo();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示悬浮窗
        mUserCenter.showFloat();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUserCenter.closeFloat();//关闭悬浮窗
    }

    private void initView() {
        mImgUsercenter = (ImageView) findViewById(R.id.iv_usercenter);
        mBtnBuy = (Button) findViewById(R.id.btn_buy);

        mImgUsercenter.setOnClickListener(onlickListener);
        mBtnBuy.setOnClickListener(onlickListener);
    }

    private View.OnClickListener onlickListener = new MyOnclick();


    class MyOnclick implements View.OnClickListener {
        @SuppressLint("NewApi")
        @Override
        public void onClick(View v) {
            if (v == mImgUsercenter) {
                mUserCenter.go2Ucenter();
            } else if (v == mBtnBuy) {
                //注：请在支付之前确认已设置区服，角色名，角色等级等信息
                mUserCenter.pay(MainActivity.this, "屠龙刀", "1.0", "谢逊的刀");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 检查用户是否登录。没登陆就跳转到登录页面
        Log.e("mumayi1", "检查是否登录");
        mUserCenter.checkLogin();
    }

    /**
     * @param tradeType
     * @param tradeCode
     * @param intent
     */
    @Override
    public void onTradeSuccess(String tradeType, int tradeCode, Intent intent) {
        // 可在此处获取到提交的商品信息
        Bundle bundle = intent.getExtras();
        String orderId = bundle.getString("orderId");
        String productName = bundle.getString("productName");
        String productPrice = bundle.getString("productPrice");
        String productDesc = bundle.getString("productDesc");

        Log.e("mumayi1", "这是orderid-->" + orderId);
        Log.e("mumayi1", "这是productName-->" + productName);
        Log.e("mumayi1", "这是productPrice-->" + productPrice);
        Log.e("mumayi1", "这是productDesc-->" + productDesc);
        Log.e("mumayi1", "这是tradeType-->" + tradeType);

        if (tradeCode == MMYInstance.PAY_RESULT_SUCCESS) {
            // 在每次支付回调结束时候，调用此接口判断用户是否完善了资料
            mUserCenter.checkUserState(MainActivity.this);
            Toast.makeText(this, productName + "支付成功 支付金额:" + productPrice, Toast.LENGTH_SHORT).show();
        } else if (tradeCode == MMYInstance.PAY_RESULT_FAILED) {
            Toast.makeText(this, productName + "支付失败 支付金额:" + productPrice, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTradeFail(String tradeType, int tradeCode, Intent intent) {

    }

    /**
     * 悬浮窗   以及  用户中心的  注销账号的回调
     *
     * @param logoutSuccess
     */
    @Override
    public void onLogoutSuccess(String logoutSuccess) {
        JSONObject mSuccJson = null;
        try {
            mSuccJson = new JSONObject(logoutSuccess);
            String code = mSuccJson.getString("loginOutCode");
            if (code.equals(PaymentConstants.LOGINOUT_SUCCESS)) {
                String uid = mSuccJson.getString("uid");
                String name = mSuccJson.getString("uname");
                Log.d("注销成功账号信息", "注销账号：" + name + " uid:" + uid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLogoutFail(String logoutFail) {
        if (logoutFail.equals(PaymentConstants.LOGINOUT_FAILED)) {
            Log.d("注销失败账号信息", "失败了~~~~~");
        }
    }


    /**
     * 这个回调 主要是处理悬浮窗 请求权限后 的处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (requestCode == PaymentConstants.FLOAT_REQUEST_CODE) {
//                if (Settings.canDrawOverlays(this)) {
//                    Log.d("悬浮窗有权限了", "回调里处理~~~~~");
//                    mUserCenter.showFloat();
//                }
//            }
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("游戏提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出此游戏吗");
            // 添加选择按钮并注册监听
            isExit.setButton(DialogInterface.BUTTON_POSITIVE, "确定", listener);
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listener);
            // 显示对话框
            isExit.show();
        }

        return false;
    }


    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序

                    /** 这两句代码是在  完全退出游戏  的时候调用，其他地方不需要调用
                     *  mUserCenter.closeFloat()
                     *  mInstance.finish()
                     *
                     * 关闭悬浮框  mUserCenter.closeFloat()
                     * 释放资源   mInstance.finish()
                     */
                	  mUserCenter.closeFloat();
                    mInstance.finish();

                    System.exit(0);
                    
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    if (dialog != null){
                        dialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity  onDestroy", "走这个吗");
    }

}
