package com.example.moposnsplatdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skymobi.moposnsplatsdk.plugins.account.ISnsAccountServerSupport;
import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IObjectResultListener;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IRemovable;
import com.skymobi.snssdknetwork.outfunction.keeplibs.UserParam;

public class RegloginUIActivity extends Activity {

	private ISnsAccountServerSupport _sdkSupport = null;
	private TextView _textInfo = null;
	private TextView _textResult = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_activity_sdk20);

		initViews();
		initButtons();
	}

	private TextView initTextView(final int id, final String text) {
		TextView view = (TextView) findViewById(id);
		if ((view != null) && (text != null)) {
			view.setText(text);
		}
		return view;
	}

	private void initViews() {

		_textInfo = initTextView(R.id.text_info,
				getString(R.string.account_info));
		_textResult = initTextView(R.id.text_result, getLoginState());
	}

	private String getLoginState() {
		UserParam params = getSupport().getparam();
		if (params != null) {
			return "登录结果:已经登录";
		} else {
			return "登录结果:未登录";
		}
	}

	private void initButtons() {
		final Button btnNewSdk = (Button) findViewById(R.id.gotoNewSDK);
		if (btnNewSdk != null) {
			btnNewSdk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					clickGotoNewSDK();
				}
			});
		}

		final Button btnShowInfo = (Button) findViewById(R.id.show_info);
		if (btnShowInfo != null) {
			btnShowInfo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					clickShowInfo();
				}
			});
		}

		final Button btnShowButton = (Button) findViewById(R.id.show_button);
		if (btnShowButton != null) {
			btnShowButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(RegloginUIActivity.this,
							SDKIconActivity.class));
				}
			});
		}

	}

	private IRemovable _loginRemovable = null;

	private void clearRemovable() {
		if (_loginRemovable != null) {
			_loginRemovable.remove();
			_loginRemovable = null;
		}
	}

	private void clickGotoNewSDK() {
		if (setClick()) {
			_loginRemovable = getSupport().login(this,
					new IObjectResultListener() {

						@Override
						public void onResult(int result, Object arg1) {
							clearRemovable();
							// 返回值1:成功,2:用户取消登录
							if (result == 1) {
								_textResult.setText("登录结果:成功");
							} else {
								_textResult.setText("登录结果:用户取消登录");
							}
						}
					});
		}

	}

	private void clickShowInfo() {
		if (setClick()) {
			// 读取并显示帐号信息
			UserParam params = getSupport().getparam();
			String text = getString(R.string.account_info) + ":";
			if (params != null) {

				text += "\n  帐号:" + params.getAccountName();
				text += "\n  昵称:" + params.getNickName();
				text += "\n  头像:" + params.getHeadIcon();
				text += "\n  性别:" + params.getSex();
				text += "\n  索引:" + params.getIndex();
				text += "\n  token:" + params.getToken();
			} else {
				text += "\n" + "没有获取到帐号信息!";
			}

			if (_textInfo != null) {
				_textInfo.setText(text);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private ISnsAccountServerSupport getSupport() {
		if (_sdkSupport == null) {
			_sdkSupport = SnsAccountServerSupport.getInstance();
		}
		return _sdkSupport;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private boolean isClicked() {
		return _isClicked;
	}

	private boolean _isClicked = false;

	private boolean setClick() {
		if (isClicked()) {
			return false;
		} else {
			_isClicked = true;

			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					_isClicked = false;
				}
			}, 1000);
			return true;
		}
	};
}
