package com.samsung.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.iapppay.samsung.v403_10.R;
import com.samsung.interfaces.callback.ILoginResultCallback;
import com.samsung.interfaces.callback.IPayResultCallback;
import com.samsung.sdk.main.IAppPay;
import com.samsung.sdk.main.IAppPayOrderUtils;

import java.util.Map;

/**
 * 商品列表
 * 提供不用计费策略的演示
 */
public class GoodsListActivity extends Activity implements View.OnClickListener{

    private static final String TAG = GoodsListActivity.class.getSimpleName();

    private static final int waresid_with_times = 1;		//按次
    private static final int waresid_open_price = 2;		//开放价格

    /**三星账号相关**/
    private static final String CLIENT_ID = "92cl7nd225";
    private static final String CLIENT_SECRETE = "F3944297BCEEF82F237FCAD11C319B6B";

    /**
     * 以下参数在文档中有详细介绍
     */
    private String appuserid = "";
    private String cpprivateinfo= "cpprivateinfo123456";
    private String cporderid= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final int screenType = getIntent().getIntExtra("screentype", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏：固定方向，屏幕向左倾斜方向
        }else if (screenType == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//横屏：根据传感器横向切换
        }else if (screenType == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        String acid = getIntent().getStringExtra("acid");
        /**
         * SDK初始化 ，请放在游戏启动界面
         */
        IAppPay.init(GoodsListActivity.this, screenType, PayConfig.appid, acid, CLIENT_ID, CLIENT_SECRETE);//接入时！不要使用Demo中的appid

        setContentView(R.layout.iapppay_demo_activity_goods_list);

        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.button01).setOnClickListener(this);
        findViewById(R.id.pay_kaifangjiage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /**
         * appuserid代表用户的唯一标识，不能为空，且必须真实有效, 有帐号的情况下，请传帐号的标识。如果没有帐号，可以使用设备标识。
         * 这里演示没有帐号的情况，以DeviceId作为用户的标识。
         */
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        appuserid = getIntent().getStringExtra("appuserid");
        appuserid = TextUtils.isEmpty(appuserid) ? telephonyManager.getDeviceId() : appuserid;
        cporderid = System.currentTimeMillis()   + "";
        switch (v.getId()) {
            case R.id.button_login:
                goLogin();
                break;
            case R.id.button01://客户端下单
                startPay(GoodsListActivity.this, getTransdata(appuserid, cpprivateinfo , waresid_with_times , 1 , cporderid));
                break;
            case R.id.pay_kaifangjiage:
                final EditText etPrice = (EditText) findViewById(R.id.et_price);

                String price = etPrice.getText().toString().trim();

                if (TextUtils.isEmpty(price)) {
                    Toast.makeText(GoodsListActivity.this, "请输入收费金额",Toast.LENGTH_SHORT).show();
                    etPrice.requestFocus();
                } else if (".".equals(price)) {
                    Toast.makeText(GoodsListActivity.this, "请输入正确金额", Toast.LENGTH_LONG).show();
                    etPrice.requestFocus();
                } else if (Double.parseDouble(price) <= 0) {
                    Toast.makeText(GoodsListActivity.this, "收费金额应大于0",Toast.LENGTH_SHORT).show();
                    etPrice.requestFocus();
                } else {
                    double iprice = 0;
                    try {
                        iprice = Double.parseDouble(price) / 100.00;//UI上面输入的单位是分，传入后台需要转换成单位 元
                    } catch (Exception e) {
                        Toast.makeText(GoodsListActivity.this, "金额不合法",Toast.LENGTH_LONG).show();
                        return;
                    }

                    //关闭软键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    //启动收银台
                    startPay(GoodsListActivity.this, getTransdata(appuserid, cpprivateinfo , waresid_open_price , iprice , cporderid));
                }
                break;
            default:
                break;
        }
    }

    /** 获取收银台参数 */
    private String getTransdata( String appuserid, String cpprivateinfo, int waresid, double price, String cporderid) {
        //调用 IAppPayOrderUtils getTransdata() 获取支付参数
        IAppPayOrderUtils orderUtils = new IAppPayOrderUtils();
        orderUtils.setAppid(PayConfig.appid);
        orderUtils.setWaresid(waresid);
        orderUtils.setCporderid(cporderid);
        orderUtils.setAppuserid(appuserid);
        orderUtils.setPrice(price);//单位 元
        orderUtils.setWaresname("自定义名称");//开放价格名称(用户可自定义，如果不传以后台配置为准)
        orderUtils.setCpprivateinfo(cpprivateinfo);
        return orderUtils.getTransdata(PayConfig.privateKey);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 登录
     */
    private void goLogin(){
        /**
         * 获取登录参数
         * 1、登陆参数是 appid和package的签名值：
         * 	 String data = AppID +"&"+ 包名。
         *   String loginParams = sign(data);
         *
         * 2、重要说明：
         *   这里是为了方便演示在客户端生成 loginParams，所以Demo中加签过程直接放在客户端完成。
         *   真实App里，privateKey等数据严禁放在客户端，加签过程务必在服务端完成；（服务端代码示例有签名代码）
         *   防止商户私密数据泄露，造成不必要的损失。
         */
        String packageStr = getPackageName();
        String loginParams = IAppPayOrderUtils.getLoginParams(PayConfig.appid,packageStr, PayConfig.privateKey);

        IAppPay.startLogin(GoodsListActivity.this, loginParams, new ILoginResultCallback() {
            @Override
            public void onSuccess(String signValue, Map<String, String> resultMapStr) {
                Toast.makeText(GoodsListActivity.this, "获取到的signValue：" + signValue, Toast.LENGTH_SHORT).show();
                Log.d("GoodsListActivity","获取到的signValue:" + signValue);
                //接入方app ---signValue--> 接入方服务器 ---signValue--> 爱贝服务器
                //接入方app <---用户信息--  接入方服务器  <---用户信息-- 爱贝服务器
            }

            @Override
            public void onFaild(String errorCode, String errorMessage) {
                Toast.makeText(GoodsListActivity.this, "登录失败，错误信息:" + errorMessage + ",错误代码:" + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(GoodsListActivity.this, "您已取消登录", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 发起支付
     * @param activity
     * @param param 参数有两种 生成方式  <br/>
     * 1、客户端下单时拼接param， 如demo所展示 <br/>
     * 2、服务端下单时拼接param，首先从服务端获取transid，然后拼接param格式：transid=xxxx&appid=xxxx（xxx记得做URLEncoder.encode处理）
     * */
    public void startPay(Activity activity, String param) {
        IAppPay.startPay(activity, param, iPayResultCallback);
    }
    /**
     * 支付结果回调
     */
    IPayResultCallback iPayResultCallback = new IPayResultCallback() {

        @Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:
                    //调用 IAppPayOrderUtils 的验签方法进行支付结果验证
                    boolean payState = IAppPayOrderUtils.checkPayResult(signvalue, PayConfig.publicKey);
                    if(payState){
                        Toast.makeText(GoodsListActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                    }

                    break;
                case IAppPay.PAY_CANCEL:
                    Toast.makeText(GoodsListActivity.this, "支付取消", Toast.LENGTH_LONG).show();
                    break ;
                default:
                    Toast.makeText(GoodsListActivity.this, resultInfo, Toast.LENGTH_LONG).show();
                    break;
            }
            Log.d(TAG, "requestCode:"+resultCode + ",signvalue:" + signvalue + ",resultInfo:"+resultInfo);
        }
    };
}
