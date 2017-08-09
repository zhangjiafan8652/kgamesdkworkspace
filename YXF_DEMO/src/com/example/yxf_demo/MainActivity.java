package com.example.yxf_demo;

import com.game.sdk.R;
import com.game.sdk.YXFAppService;
import com.game.sdk.YXFSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.OnPaymentListener;
import com.game.sdk.domain.PaymentCallback;
import com.game.sdk.domain.PaymentErrorMsg;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.RolecallBack;
import com.game.sdk.domain.onRoleListener;
import com.game.sdk.ui.mainUI.GetInfoActivity;
import com.game.sdk.util.Constants;
import com.game.sdk.util.LogUtil;
import com.lidroid.xutils.util.LogUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "-----MainActivity-----";
	private YXFSDKManager manager;
	private EditText et_money;
	private Button btn_login, btn_charger, get_roleInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = YXFSDKManager.getInstance(this);
		YXFSDKManager.setIsLandscape(false);// false: 竖屏 true：横屏
		if (YXFSDKManager.getInstance(MainActivity.this).isLandscape()) {
			setRequestedOrientation(Constants.ORIENTATION_LANDSPACE);// 横屏
		} else {
			setRequestedOrientation(Constants.ORIENTATION_PORTRAIT);// 竖屏
		}
		setContentView(R.layout.yxf_sdk_main);
		/**
		 * 设置logcat的输出日志等级 LOG_LEVEL_NONE:不输出日志
		 */
		LogUtil.setLogLevel(LogUtil.LOG_LEVEL_ALL);

		initView();

		btn_login.setOnClickListener(this);
		btn_charger.setOnClickListener(this);
		get_roleInfo.setOnClickListener(this);
	}

	private void initView() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_charger = (Button) findViewById(R.id.btn_charger);
		get_roleInfo = (Button) findViewById(R.id.btn_get_roleinfo);
		et_money = (EditText) findViewById(R.id.et_money);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_login:
			YXFSDKManager.getInstance(MainActivity.this).showLogin(
					MainActivity.this, true, new OnLoginListener() {
						@Override
						public void loginSuccess(LogincallBack logincallback) {
							Toast.makeText(getApplication(),
									logincallback.toString(),
									Toast.LENGTH_SHORT).show();
							LogUtil.getInstance(TAG).d(
									"登录成功-----" + logincallback.toString());
						}

						@Override
						public void loginError(LoginErrorMsg errorMsg) {
							Toast.makeText(getApplication(), errorMsg.msg,
									Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case R.id.btn_charger:
			String money_str = et_money.getText().toString().trim();
			String money = "1";
			if (!TextUtils.isEmpty(money_str) && !"".equals(money_str)) {
				money = money_str;
			}
			YXFSDKManager.getInstance(MainActivity.this).showPay(
					MainActivity.this, "1001", money, "10", "魔神", "金币",
					"0,Y201604201*11703*032985,517*550,100,10,1001*",
					new OnPaymentListener() {
						@Override
						public void paymentSuccess(PaymentCallback callbackInfo) {
							Toast.makeText(getApplication(),
									callbackInfo.toString(), Toast.LENGTH_SHORT)
									.show();
						}

						@Override
						public void paymentError(PaymentErrorMsg errorMsg) {
							Toast.makeText(getApplication(),
									"充值失败：" + errorMsg.toString(),
									Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case R.id.btn_get_roleinfo:
			if (!YXFAppService.isLogin) {
				Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(MainActivity.this, GetInfoActivity.class);
			startActivity(intent);
			YXFSDKManager.getInstance(MainActivity.this).removeFloatView();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// 游戏退出必须调用
		RoleInfo info = new RoleInfo();
		info.setRoleName("测试");// 角色名
		info.setRoleVIP("");// 角色VIP
		info.setRoleLevel("");// 角色等级
		info.setServerID("");// 区服id
		info.setServerName("测试服务器");// 区服名
		YXFSDKManager.getInstance(MainActivity.this).LoginOut(info,
				Constants.TYPE_EXIT_GAME, new onRoleListener() {
					@Override
					public void onSuccess(RolecallBack rolecallBack) {
						LogUtils.i(TAG + "提交用户信息成功 = "
								+ rolecallBack.toString());
					}

					@Override
					public void onError(RolecallBack rolecallBack) {
						LogUtils.i(TAG + "提交用户信息失败 = "
								+ rolecallBack.toString());
					}
				});
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		LogUtil.getInstance(TAG).d("-----onStop-----");
		YXFSDKManager.getInstance(this).removeFloatView();
		super.onStop();
	}

	@Override
	protected void onResume() {
		LogUtil.getInstance(TAG).d("-----onResume-----");
		YXFSDKManager.getInstance(this).showFloatView();
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogUtil.getInstance(TAG).d("-----onPause-----");
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		LogUtil.getInstance(TAG).d("-----onBackPressed-----");
		super.onBackPressed();
	}
}
