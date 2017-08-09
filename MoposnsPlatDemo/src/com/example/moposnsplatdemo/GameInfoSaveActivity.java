package com.example.moposnsplatdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.skymobi.snssdknetwork.outfunction.keeplibs.GameInfo;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IErrorCode;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IObjectResultListener;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IRemovable;

public class GameInfoSaveActivity extends Activity {

	private EditText _editRoleName;
	private EditText _editRoleLevel;
	private EditText _editRoleType;
	private EditText _editRoleMoney;
	private EditText _editServerInfo;
	private EditText _editPower;
	private EditText _editScore;
	private EditText _editParam1;
	private EditText _editParam1Tag;
	private EditText _editParam2;
	private EditText _editParam2Tag;
	private EditText _editParam3;
	private EditText _editParam3Tag;
	private TextView _textResult;

	private IRemovable _addInfoRemovable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gameinfo);
		initView();
	}

	private void initView() {
		_editRoleName = getAndSetEditText(R.id.edit_rolename, "Ralf");
		_editRoleLevel = getAndSetEditText(R.id.edit_rolelevel, "34");
		_editRoleType = getAndSetEditText(R.id.edit_roletype, "法师");
		_editRoleMoney = getAndSetEditText(R.id.edit_money, "10000");
		_editServerInfo = getAndSetEditText(R.id.edit_serverinfo, "SkyArea1");
		_editPower = getAndSetEditText(R.id.edit_power, "18000");
		_editScore = getAndSetEditText(R.id.edit_score, "300000");

		_editParam1 = getAndSetEditText(R.id.edit_param1, "param1");
		_editParam1Tag = getAndSetEditText(R.id.edit_param1_tag, "tag1");

		_editParam2 = getAndSetEditText(R.id.edit_param2, "param2");
		_editParam2Tag = getAndSetEditText(R.id.edit_param2_tag, "tag2");
		_editParam3 = getAndSetEditText(R.id.edit_param3, "param3");
		_editParam3Tag = getAndSetEditText(R.id.edit_param3_tag, "tag3");

		_textResult = getAndSetTextView(R.id.text_commit_result, "提交结果", null);
		getAndSetTextView(R.id.text_commit, "提交", new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				clickCommit();
			}
		});
	}

	private void clickCommit() {
		testUploadGameInfo();
	}

	private String getEditText(final EditText edit) {
		if (edit != null) {
			return edit.getText().toString();
		}
		return "";
	}

	private void clearRemovable() {
		if (_addInfoRemovable != null) {
			_addInfoRemovable.remove();
			_addInfoRemovable = null;
		}
	}

	private void testUploadGameInfo() {
		final GameInfo info = new GameInfo();

		info.setMoney(getEditText(_editRoleMoney))
				.setRoleLevel(getEditText(_editRoleLevel))
				.setRoleName(getEditText(_editRoleName))
				.setServerInfo(getEditText(_editServerInfo))
				.setTypeName(getEditText(_editRoleType))
				.setScore(getEditText(_editScore))
				.setPower(getEditText(_editPower))
				.setParam1(getEditText(_editParam1))
				.setParam1Tag(getEditText(_editParam1Tag))
				.setParam2(getEditText(_editParam2))
				.setParam2Tag(getEditText(_editParam2Tag))
				.setParam3(getEditText(_editParam3))
				.setParam3Tag(getEditText(_editParam3Tag));

//		Log.i("DEBUG", "testUploadGameInfo:" + JSON.toJSONString(info));

		sentResult("正在提交");
		clearRemovable();
		_addInfoRemovable = SnsAccountServerSupport.getInstance().addGameInfo(
				info, new IObjectResultListener() {

					@Override
					public void onResult(int result, Object reason) {
						// SDK底层回调回来执行线程不在UI线程,如果要刷新界面.需要做线程切换.
						clearRemovable();
						if (IErrorCode.RESULT_SUCCESS == result) {
							success();
						} else {
							String text = "提交失败";
							if ((reason != null) && (reason instanceof String)) {
								text += ":" + reason;
							}
							failed(text);
						}
					}
				});

		if (_addInfoRemovable == null) {
			failed("提交失败" + ":参数不对/当前没有登录帐号");
		}
	}

	private void success() {
		sentResult("提交成功!");
	}

	private void failed(final String reason) {
		sentResult(reason);
	}

	private void sentResult(final String text) {
		final Message msg = new Message();
		msg.obj = text;
		_handler.sendMessage(msg);
	}

	private void showResult(final String result) {
		if (_textResult != null) {
			_textResult.setText(result);
		}
	}

	private final Handler _handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			if ((msg != null) && (msg.obj != null)) {
				try {
					final String text = (String) msg.obj;
					showResult(text);
				} catch (Exception e) {
					e.printStackTrace();
					showResult("出错:" + e.toString());
				}
			}
		}
	};

	private EditText getAndSetEditText(final int id, final String text) {
		EditText edit = (EditText) findViewById(id);
		if (edit != null) {
			if (!TextUtils.isEmpty(text)) {
				edit.setText(text);
			}
		}
		return edit;
	}

	private TextView getAndSetTextView(final int id, final String text,
			final View.OnClickListener listener) {
		TextView view = (TextView) findViewById(id);
		if (view != null) {
			if (!TextUtils.isEmpty(text)) {
				view.setText(text);
			}
			if (listener != null) {
				view.setOnClickListener(listener);
			}
		}
		return view;
	}

}
