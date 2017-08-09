package cn.nubia.nbgame.demo;

import java.util.HashMap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.nubia.componentsdk.constant.ConstantProgram;
import cn.nubia.nbgame.demo.util.ErrorMsgUtil;
import cn.nubia.nbgame.demo.util.MD5Signature;
import cn.nubia.nbgame.sdk.GameSdk;
import cn.nubia.nbgame.sdk.R;
import cn.nubia.nbgame.sdk.entities.ErrorCode;
import cn.nubia.nbgame.sdk.interfaces.CallbackListener;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_STORAGE_PERMISSION_LOGIN = 100;
    private static final int REQUEST_STORAGE_PERMISSION_PAY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnChangeAccount = (Button) findViewById(R.id.btn_change_account);
        Button btnCheckRealIdentity = (Button) findViewById(R.id.btn_check_real_identity);
        Button btnChangeNickname = (Button) findViewById(R.id.btn_change_nickname);
        Button btnChangeHead = (Button) findViewById(R.id.btn_change_head);
        Button btnFindPwd = (Button) findViewById(R.id.btn_find_pwd);
        Button btnPay = (Button) findViewById(R.id.btn_pay);
        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        Button btnUsageStats = (Button) findViewById(R.id.btn_usage_stats);

        tvVersion.setText("SDK版本号：" + GameSdk.getVersion());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnUsageStats.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(this);
        btnChangeAccount.setOnClickListener(this);
        btnCheckRealIdentity.setOnClickListener(this);
        btnChangeNickname.setOnClickListener(this);
        btnChangeHead.setOnClickListener(this);
        btnFindPwd.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        btnUsageStats.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.btn_change_account:
                changeAdjunctAccount();
                break;
            case R.id.btn_check_real_identity:
                checkRealIdentity();
                break;
            case R.id.btn_change_nickname:
                doChangeNickname();
                break;
            case R.id.btn_change_head:
                doChangeAvatar();
                break;
            case R.id.btn_find_pwd:
                doFindPwd();
                break;
            case R.id.btn_pay:
                doPay();
                break;
           case R.id.btn_usage_stats:
                openUsageStatsPermission();
                break;
            default:
                break;
        }
    }

    private void doLogin() {
        Log.i(TAG, "doLogin");
        GameSdk.openLoginActivity(this, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                switch (responseCode) {
                    case ErrorCode.SUCCESS:
                        //登陆成功，拿uid和sessionId去CP服务器完成角色创建或查询等操作
                        String msg = String.format("账号:%s 登录", GameSdk.getLoginGameId());
                        showText(responseCode, msg);
                        Log.i(TAG, "login success");
                        break;
                    case ErrorCode.NO_PERMISSION:
                        // Android6.0没允许安装和更新所需权限，需要运行时请求，主要是存储权限
                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_LOGIN);
//                        Toast.makeText(MainActivity.this, "登录需要安装努比亚联运中心服务，未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "login failure: " + "code=" + responseCode + ", message=未获得安装和更新所需权限");
                        break;
                    default:
                        // 登录失败，包含错误码和错误消息
                        Toast.makeText(MainActivity.this, "登录失败：" + ErrorMsgUtil.translate(responseCode), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "login failure: " + "code=" + responseCode + ", message=" + ErrorMsgUtil.translate(responseCode));
                        break;
                }
            }
        });
    }

    private void changeAdjunctAccount() {
        GameSdk.changeAdjunctAccount(this, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle) {
                switch (responseCode) {
                    case ErrorCode.SUCCESS:
                        //登陆成功，拿uid和sessionId去CP服务器完成角色创建或查询等操作
                        String msg = String.format("账号:%s 切换", GameSdk.getLoginGameId());
                        showText(responseCode, msg);
                        Log.i(TAG, "change account success");
                        break;
                    case ErrorCode.NO_PERMISSION:
                        // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                        Toast.makeText(MainActivity.this, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        // 登录失败，包含错误码和错误消息
                        Toast.makeText(MainActivity.this, "切换账号失败：" + ErrorMsgUtil.translate(responseCode), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "change account failure: " + "code=" + responseCode + ", message=" + ErrorMsgUtil.translate(responseCode));
                        break;
                }
            }
        });
    }

    private void checkRealIdentity() {
        GameSdk.checkRealIdentity(this, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle) {
                Toast.makeText(MainActivity.this, "实名认证：" + responseCode, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "实名认证：" + responseCode);
            }
        });
    }

    /**
     * 修改头像
     */
    private void doChangeAvatar() {
        GameSdk.openChangeAvatarActivity(MainActivity.this,
                new CallbackListener<Bundle>() {
                    @Override
                    public void callback(int responseCode, Bundle bundle) {
                        switch (responseCode) {
                            case ErrorCode.SUCCESS:
                                // 修改昵称成功
                                showText(responseCode, "头像修改");
                                break;
                            case ErrorCode.USER_CANCLE:
                                showUserCancel(responseCode);
                                break;
                            case ErrorCode.NO_PERMISSION:
                                // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                                Toast.makeText(MainActivity.this, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                showText(responseCode, "头像修改");
                                break;
                        }
                    }
                });
    }

    /**
     * 修改昵称
     */
    private void doChangeNickname() {
        GameSdk.openChangeNicknameActivity(MainActivity.this,
                new CallbackListener<Bundle>() {
                    public void callback(int responseCode, Bundle bundle) {
                        switch (responseCode) {
                            case ErrorCode.SUCCESS:
                                // 修改昵称成功
                                showText(responseCode, "昵称修改");
                                break;
                            case ErrorCode.USER_CANCLE:
                                // 用户主动取消修改或是关掉界面
                                showUserCancel(responseCode);
                                break;
                            case ErrorCode.NO_PERMISSION:
                                // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                                Toast.makeText(MainActivity.this, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                // 其他，包括修改失败
                                showText(responseCode, "昵称修改");
                                break;
                        }
                    }
                });
    }

    /**
     * 找回密码
     */
    private void doFindPwd() {
        GameSdk.openFindPwdActivity(MainActivity.this,
                new CallbackListener<Bundle>() {
                    @Override
                    public void callback(int responseCode, Bundle bundle) {
                        switch (responseCode) {
                            case ErrorCode.SUCCESS:
                                // 找回密码成功
                                showText(responseCode, "密码找回");
                                break;
                            case ErrorCode.USER_CANCLE:
                                // 用户主动取消找回或是关掉界面
                                showText(responseCode,"找回密码界面关闭");
//                                Toast.makeText(MainActivity.this, "找回密码界面关闭", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.NO_PERMISSION:
                                // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                                Toast.makeText(MainActivity.this, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                // 其他，包括找回密码失败
                                showText(responseCode, "密码找回");
                                break;
                        }
                    }
                });
    }

    /**
     * 支付
     */
    private void doPay() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ConstantProgram.TOKEN_ID, GameSdk.getSessionId());
        map.put(ConstantProgram.UID, GameSdk.getLoginUid());
        map.put(ConstantProgram.APP_ID, String.valueOf(AppConfig.APP_ID));
        map.put(ConstantProgram.APP_KEY, AppConfig.APP_KEY);
        map.put(ConstantProgram.AMOUNT, "0.01");
        map.put(ConstantProgram.PRICE, "0.01");
        map.put(ConstantProgram.NUMBER, "1");
        map.put(ConstantProgram.PRODUCT_NAME, "牛币");
        map.put(ConstantProgram.PRODUCT_DES, "牛币可交易");
        map.put(ConstantProgram.PRODUCT_ID, "A01");
        map.put(ConstantProgram.PRODUCT_UNIT, "个");
        String timeStamp = String.valueOf(System.currentTimeMillis());
        //由CP服务器返回的订单编号，订单号不能重复
        String cpOrderId = "nb" + timeStamp;
        map.put(ConstantProgram.CP_ORDER_ID, cpOrderId);
        // 为了安全性考虑，doSign最好在服务端执行, 时间戳在DATA_TIMESTAMP和签名两个地方传递的必须是一致的
        map.put(ConstantProgram.SIGN, MD5Signature.doSign(cpOrderId, "牛币", "1", "0.01", GameSdk.getLoginUid(), timeStamp));
        map.put(ConstantProgram.DATA_TIMESTAMP, timeStamp);
        map.put(ConstantProgram.CHANNEL_DIS, "1");
        map.put(ConstantProgram.GAME_ID, GameSdk.getLoginGameId());

        Log.i(TAG, "支付请求参数：" + map.toString());
        GameSdk.doPay(MainActivity.this, map, new CallbackListener<String>() {
            @Override
            public void callback(int responseCode, String message) {
                switch (responseCode) {
                    case 0:
                        // 支付完成
                        showPayResult(responseCode, "支付成功!");
                        break;
                    case -1:
                        // 本次支付失败
                        showPayResult(responseCode, "支付失败!"+ "{" + message + "}");
                        break;
                    case 10001:
                        // 用户取消了本次支付
                        showPayResult(responseCode, "您已经取消本次支付"+ "{" + message + "}");
                        break;
                    case 10002:
                        // 网络异常
                        showPayResult(responseCode, "网络异常，请检查网络设置"+ "{" + message + "}");
                        break;
                    case 10003:
                        // 订单结果确认中，建议客户端向自己业务服务器校验支付结果
                        showPayResult(responseCode, "支付结果确认中，请稍后查看"+ "{" + message + "}");
                        break;
                    case 10004:
                        // 支付服务正在升级
                        showPayResult(responseCode, "支付服务正在升级"+ "{" + message + "}");
                        break;
                    case 10005:
                        // 支付组件安装失败或是未安装
                        showPayResult(responseCode, "支付服务安装失败或未安装"+ "{" + message + "}");
                        break;
                    case 10006:
                        // 订单信息有误
                        showPayResult(responseCode, "订单信息有误"+ "{" + message + "}");
                        break;
                    case 10007:
                        // 获取支付渠道失败
                        showPayResult(responseCode, "获取支付渠道失败，请稍后重试"+ "{" + message + "}");
                        break;
                    case 10008:
                        // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                        Toast.makeText(MainActivity.this, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_PAY);
                        break;
                    default:
                        // 其他所有场景统一处理为支付失败
                        showPayResult(responseCode, "支付失败! " + "{" + message + "}");
                        break;
                }
            }
        });
    }

    private void openUsageStatsPermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "手机不支持此权限设置", Toast.LENGTH_SHORT).show();
        }

    }

    private void showText(int responseCode, String str) {
        String msg = str;
        if (responseCode == ErrorCode.SUCCESS) {
            msg += "成功";
        } else {
            msg += "失败（错误码：" + responseCode + "）";
        }
        Log.d(TAG, msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    void showUserCancel(int code) {
        String msg="";
        msg += "用户主动关掉界面（错误码：" + code + "）";
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    public void showPayResult(int code, String payResult) {
        String msg =  payResult + " (错误码:" + code + ")";
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void requestStoragePermission(int requestCode) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION_LOGIN:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doLogin();
                }
                break;
            case REQUEST_STORAGE_PERMISSION_PAY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    doPay();
                }
                break;
        }
    }
}
