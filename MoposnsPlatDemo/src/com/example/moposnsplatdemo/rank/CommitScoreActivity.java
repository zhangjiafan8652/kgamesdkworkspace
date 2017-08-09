package com.example.moposnsplatdemo.rank;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moposnsplatdemo.R;
import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IObjectResultListener;
import com.skymobi.snssdknetwork.outfunction.keeplibs.UpdateRankResponse;
import com.skymobi.snssdknetwork.outfunction.keeplibs.UpdateScoreListener;

public class CommitScoreActivity extends Activity {

	private EditText _editScore1 = null;
	private EditText _editScore2 = null;
	private EditText _editScore3 = null;

	private TextView _textResult = null;

	private TextView _textPackageName = null;

	private String _curName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);

		_editScore1 = (EditText) findViewById(R.id.edit_score1);
		_editScore2 = (EditText) findViewById(R.id.edit_score2);
		_editScore3 = (EditText) findViewById(R.id.edit_score3);

		_textResult = (TextView) findViewById(R.id.text_result);

		_textPackageName = (TextView) findViewById(R.id.text_packageName);
		_curName = getCurrentPackageName();
		showPackageName();

		Button top10 = (Button) findViewById(R.id.btn_commit);
		top10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickCommit();
			}
		});
	}

	private void showPackageName() {
		if ((_textPackageName != null) && (_curName != null)) {
			_textPackageName.setText("包名:" + _curName);
		}
	}

	private Long getScore1() {

		Long value = null;
		if (_editScore1 != null) {
			final String score = _editScore1.getText().toString();
			if (!TextUtils.isEmpty(score)) {
				try {
					value = Long.parseLong(score);
				} catch (Exception e) {

				}
			}
		}

		return value;
	}

	private boolean hasScore(final EditText edit) {
		boolean ret = false;
		if (edit != null) {
			final String score = edit.getText().toString();
			if (!TextUtils.isEmpty(score)) {
				ret = true;
			}
		}
		return ret;
	}

	private Long getScore2() {

		Long value = null;
		if (_editScore2 != null) {
			final String score = _editScore2.getText().toString();
			if (!TextUtils.isEmpty(score)) {
				try {
					value = Long.parseLong(score);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

		return value;
	}

	private Long getScore3() {

		Long value = null;
		if (_editScore3 != null) {
			final String score = _editScore3.getText().toString();
			if (!TextUtils.isEmpty(score)) {
				try {
					value = Long.parseLong(score);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

		return value;
	}

	private String getCurrentPackageName() {
		return this.getPackageName();
	}

	private void showResult(final String result) {
		if (_textResult != null) {
			_textResult.setText(result);
		}
	}

	private String getScoresString() {
		String text = "";
		if (getScore1() != null) {
			text += "分数1:" + Long.toString(getScore1());
		}
		if (getScore2() != null) {
			text += "分数2:" + Long.toString(getScore2());
		}
		if (getScore3() != null) {
			text += "分数3:" + Long.toString(getScore3());
		}
		return text;
	}

	private void clickCommit() {

		if ((_editScore1 != null) && (_editScore2 != null)
				&& (_editScore3 != null)) {

			final String text = "结果:正在提交......";
			showResult(text + getScoresString());

			if (getScore1() == null) {
				String textError = "结果:提交失败,分数1必须有值";

				if (hasScore(_editScore1)) {
					textError = "结果:提交失败,分数1数值已经溢出";
				}
				showResult(textError);
				return;
			}

			if ((getScore2() == null) && hasScore(_editScore2)) {

				String textError = "结果:提交失败,分数2数值已经溢出";
				showResult(textError);
				return;
			}

			if ((getScore3() == null) && hasScore(_editScore3)) {

				String textError = "结果:提交失败,分数3数值已经溢出";
				showResult(textError);
				return;
			}

			RankOperate.commitScore(getScore1(), getScore2(), getScore3(),
					this.getApplicationContext(), new UpdateScoreListener() {

						@Override
						public void onSuccessByServerCallBack(
								final UpdateRankResponse response) {
							Log.i("DEBUG", "commit score success");
							sentSuccessResponse(response);
						}

						@Override
						public void onFailedByServerCallBack() {
							Log.i("DEBUG", "commit score failed");
							final String text = "结果:失败......";
							sentResult(text + getScoresString());
						}
					});
		}

	}

	private void sentResult(final String text) {
		final Message msg = new Message();
		msg.obj = text;
		msg.what = MSG_SHOWTEXT;
		_handler.sendMessage(msg);
	}

	private void sentSuccessResponse(final UpdateRankResponse response) {
		final Message msg = new Message();
		msg.obj = response;
		msg.what = MSG_SHOWPOPUP;
		_handler.sendMessage(msg);
	}

	private Long getIndexScore(int index) {
		Long value = null;
		if (index == 1) {
			if (getScore1() != null) {
				value = getScore1();
			}
		} else if (index == 2) {
			if (getScore2() != null) {
				value = getScore2();
			}
		} else if (index == 3) {
			if (getScore3() != null) {
				value = getScore3();
			}
		}
		return value;
	}

	private void parseSuccessResponse(final Object object) {
		String text = "结果:成功......" + getScoresString();

		UpdateRankResponse response = null;
		if (object != null && object instanceof UpdateRankResponse) {
			response = (UpdateRankResponse) object;
			if (response != null) {
				text += ",lastRank:" + response.getLastRank() + ",nowRank:"
						+ response.getNowRank() + ",index:"
						+ response.getDataIndex();
			}
		}
		showResult(text);
		
		if ((response != null)
				&& (response.getLastRank() - response.getNowRank() > 0)) {

			if (getIndexScore(response.getDataIndex()) == null) {
				showResult("得瑟出错:" + "服务端配置统计的分数" + response.getDataIndex()
						+ "没有上传数据");
				return;
			}

			SnsAccountServerSupport.getInstance().showDefaultRankState(
					CommitScoreActivity.this, response.getLastRank(),
					response.getNowRank(),
					getIndexScore(response.getDataIndex()),
					new IObjectResultListener() {

						@Override
						public void onResult(int result, Object arg1) {
							Log.i("DEBUG", "upload data result:" + result);
							String text = "成功";
							if (result == 1) {
								text = "成功";
							} else if (result == 0) {
								text = "失败";
							} else if (result == 2) {
								text = "用户取消";
							} else {
								text = "失败:" + result;
							}
							sentResult("嘚瑟结果:" + text);
						}
					});
		}
	}

	private final static int MSG_SHOWTEXT = 0;
	private final static int MSG_SHOWPOPUP = 1;
	private final Handler _handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			if ((msg != null) && (msg.obj != null)) {
				if (msg.what == MSG_SHOWTEXT) {
					try {
						final String text = (String) msg.obj;
						showResult(text);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else if (msg.what == MSG_SHOWPOPUP) {
					parseSuccessResponse(msg.obj);
				}
			}
		}
	};
}
