package com.baidu.bdgamesdk.demo.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.bdgamesdk.demo.R;
import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.OnGameExitListener;
import com.baidu.gamesdk.ResultCode;
import com.baidu.platformsdk.PayOrderInfo;

@SuppressLint("InflateParams")
public class GameActivity extends BaseActivity {

	private AlertDialog alertDialog;
	private EditText amountText;
	// private GLSurfaceView surfaceView;
	// private DemoRenderer demoRenderer;
	protected static String uid = "";

	private int W;
	private int H;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		W = mDisplayMetrics.widthPixels;
		H = mDisplayMetrics.heightPixels;

		initGLSurfaceView();

		setSuspendWindowChangeAccountListener(); // 设置切换账号事件监听（个人中心界面切换账号）

		setSessionInvalidListener(); // 设置会话失效监听

		BDGameSDK.showFloatView(this); // 显示悬浮窗

	}

	/**
	 * @Description: 充值/支付
	 */
	private void pay() {
		PayOrderInfo payOrderInfo = buildOrderInfo();
		if (payOrderInfo == null) {
			return;
		}

		BDGameSDK.pay(GameActivity.this, payOrderInfo, null,
				new IResponse<PayOrderInfo>() {

					@Override
					public void onResponse(int resultCode, String resultDesc,
							PayOrderInfo extraData) {
						alertDialog.cancel();
						String resultStr = "";
						switch (resultCode) {
						case ResultCode.PAY_SUCCESS: // 支付成功
							resultStr = "支付成功:" + resultDesc;
							break;
						case ResultCode.PAY_CANCEL: // 订单支付取消
							resultStr = "取消支付";
							break;
						case ResultCode.PAY_FAIL: // 订单支付失败
							resultStr = "支付失败：" + resultDesc;
							break;
						case ResultCode.PAY_SUBMIT_ORDER: // 订单已经提交，支付结果未知（比如：已经请求了，但是查询超时）
							resultStr = "订单已经提交，支付结果未知";
							break;
						default:
							resultStr = "订单已经提交，支付结果未知";
							break;
						}
						Toast.makeText(getApplicationContext(), resultStr,
								Toast.LENGTH_LONG).show();

					}

				});

	}

	/**
	 * 构建订单信息
	 */
	private PayOrderInfo buildOrderInfo() {
		String cpOrderId = UUID.randomUUID().toString(); // CP订单号
		String goodsName = "金币";
		String totalAmount = amountText.getText().toString(); // 支付总金额 （以分为单位）
		int ratio = 1; // 该参数为非定额支付时生效 (支付金额为0时为非定额支付,具体参见使用手册)
		String extInfo = "第X号服务器，Y游戏分区充值"; // 扩展字段，该信息在支付成功后原样返回给CP

		if (TextUtils.isEmpty(totalAmount)) {
			totalAmount = "0";
		}

		PayOrderInfo payOrderInfo = new PayOrderInfo();
		payOrderInfo.setCooperatorOrderSerial(cpOrderId);
		payOrderInfo.setProductName(goodsName);
		long p = Long.parseLong(totalAmount);
		payOrderInfo.setTotalPriceCent(p); // 以分为单位
		payOrderInfo.setRatio(ratio);
		payOrderInfo.setExtInfo(extInfo); // 该字段将会在支付成功后原样返回给CP(不超过500个字符)
		payOrderInfo.setCpUid(uid); // 必传字段，需要验证uid是否合法,此字段必须是登陆后或者切换账号后保存的uid

		return payOrderInfo;
	}

	/**
	 * 此接口可实现自定义事件统计。 BDGameSDK.onTag(String eventId, Map<String,String> map);
	 * eventId：事件类型 ; map：自定义key键和自定义事件的值。
	 */
	private void onTag() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("custom key 1", "custom value 1");
		map.put("custom key 2", "custom value 2");
		map.put("custom key 3", "custom value 3");
		BDGameSDK.onTag("eventId", map);
	}

	private void setSuspendWindowChangeAccountListener() { // 设置切换账号事件监听（个人中心界面切换账号）
		BDGameSDK.setSuspendWindowChangeAccountListener(GameActivity.this,
				new IResponse<Void>() {

					@Override
					public void onResponse(int resultCode, String resultDesc,
							Void extraData) {
						switch (resultCode) {
						case ResultCode.LOGIN_SUCCESS:
							// TODO 登录成功，不管之前是什么登录状态，游戏内部都要切换成新的用户
							uid = BDGameSDK.getLoginUid(); // TODO
															// 切换账号成功后必须更新uid给调用支付api使用
							Toast.makeText(getApplicationContext(), "登录成功",
									Toast.LENGTH_LONG).show();
							break;
						case ResultCode.LOGIN_FAIL:
							// TODO
							// 登录失败，游戏内部之前如果是已经登录的，要清除自己记录的登录状态，设置成未登录。如果之前未登录，不用处理。
							Toast.makeText(getApplicationContext(), "登录失败",
									Toast.LENGTH_LONG).show();
							break;
						case ResultCode.LOGIN_CANCEL:
							// TODO 取消，操作前后的登录状态没变化
							Toast.makeText(getApplicationContext(), "登录取消",
									Toast.LENGTH_LONG).show();
							break;
						default:
							// TODO
							// 此时当登录失败处理，参照ResultCode.LOGIN_FAIL（正常情况下不会到这个步骤，除非SDK内部异常）
							Toast.makeText(getApplicationContext(), "登录失败",
									Toast.LENGTH_LONG).show();
							break;

						}
					}

				});
	}

	/**
	 * @Description: 监听session失效时重新登录
	 */
	private void setSessionInvalidListener() {
		BDGameSDK.setSessionInvalidListener(new IResponse<Void>() {

			@Override
			public void onResponse(int resultCode, String resultDesc,
					Void extraData) {
				if (resultCode == ResultCode.SESSION_INVALID) {
					// 会话失效，开发者需要重新登录或者重启游戏
					login();
				}

			}

		});
	}

	/**
	 * @Description: 登录
	 */
	private void login() {
		BDGameSDK.login(GameActivity.this, new IResponse<Void>() {

			@SuppressWarnings("unused")
			@Override
			public void onResponse(int resultCode, String resultDesc,
					Void extraData) {
				Log.d("login", "this resultCode is " + resultCode);
				String hint = "";
				switch (resultCode) {
				case ResultCode.LOGIN_SUCCESS:
					// 登录成功，此时玩家登录的账号有可能跟失效前登的账号不一致，开发者此时可通过BDGameSDK.getLoginUid获取当前登录的用户ID跟会话失效前的ID比对是否一致，
					// 如果一致则玩家继续游戏，如果不一致则开发者需要更改玩家在游戏中的游戏数据。
					hint = "登录成功";
					break;
				case ResultCode.LOGIN_CANCEL:
					hint = "取消登录";
					break;
				case ResultCode.LOGIN_FAIL:
				default:
					hint = "登录失败";
				}
			}
		});
	}

	private void recharge() {
		buildDialog();
		alertDialog.show();
	}

	private void buildDialog() {
		if (alertDialog != null) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogView = inflater.inflate(R.layout.chargedialog, null);
		amountText = (EditText) dialogView.findViewById(R.id.chargetype);
		amountText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String temp = s.toString();
				int d = temp.indexOf(".");
				if (d < 0) {
					return;
				}
				if (temp.length() - d - 1 > 2) {
					s.delete(d + 3, d + 4);
				} else if (d == 0) {
					s.delete(d, d + 1);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});
		dialogView.findViewById(R.id.confirm).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						pay();
						// Intent intent = new Intent();
						// intent.setClass(GameActivity.this,
						// TestActivity.class);
						// GameActivity.this.startActivity(intent);
					}
				});

		dialogView.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.cancel();
					}
				});

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(dialogView);
		alertDialog = builder.create();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void finish() {
		super.finish();
		BDGameSDK.closeFloatView(this); // 关闭悬浮窗
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			BDGameSDK.gameExit(this, new OnGameExitListener() {

				@Override
				public void onGameExit() {

					finish();
				}

			});
			break;
		default:
			break;
		}

		return true;
	}

	private void initGLSurfaceView() {
		findViewById(R.id.recharge).setVisibility(View.VISIBLE);
		findViewById(R.id.recharge).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recharge();
			}
		});
	}
}
