package com.yayawan.sdk.jflogin;

import java.util.UUID;
import java.util.zip.CRC32;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.account.engine.ObtainData;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.MybitmapUtils;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Utilsjf;
import com.yayawan.sdk.utils.CryptoUtil;
import com.yayawan.sdk.utils.ToastUtil;

public class AcountRegister {

	private String mName;
	private String mPassword;

	private User mUser;

	private Activity mActivity;

	public AcountRegister(Activity activity) {
		mActivity = activity;
	}

	private static final int REGISTER = 3;

	private static final int FETCHSMS = 4;

	protected static final int ERROR = 5;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			case REGISTER:
				if (mUser != null) {
					if (mUser.success == 0) {
						//密码以图片的形式保存起来
						MybitmapUtils.savePasswordtoBitmap(mPassword, mName,
								mActivity);
						// 将用户信息保存到全局变量
						AgentApp.mUser = mUser;
						
						//把第一次快速注册的原始密码保存起来
						Sputils.putSPstring(mUser.uid+"_password", mPassword, mActivity);
						
						// 将base64加密的用户信息保存到数据库
						UserDao.getInstance(mActivity).writeUser(mName,
								mPassword, mUser.secret);
						// System.out.println("打印一下拿到的是什么" + mUser);
						
						// 开启悬浮窗服务
						// YayaWan.init(mActivity);
						// allDismiss();
						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity,mPassword);
						mUser.password = "";
						mUser.secret = "";
						login_success_dialog.dialogShow();
					} else {
						startlogin();
						Toast.makeText(mActivity, mUser.body,
								Toast.LENGTH_SHORT).show();
						// onCancel();
					}
				}
				break;

			case ERROR:
				startlogin();
				Toast.makeText(mActivity, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}

	};

	private void startlogin() {

		//
		Login_ho_dialog login_ho_dialog = new Login_ho_dialog(mActivity);
		login_ho_dialog.dialogShow();

	}

	public void acountRregister() {
		UUID uuid = UUID.randomUUID();
		CRC32 crc32 = new CRC32();
		crc32.update(uuid.toString().getBytes());
		mName = "yy" + crc32.getValue();
		mPassword = CryptoUtil.getSeed();
		// mPassword="123456";

		Utilsjf.creDialogpro(mActivity, "正在注册...");

		new Thread() {

			@Override
			public void run() {
				try {
					mUser = ObtainData.register(mActivity, mName, mPassword);
					mHandler.sendEmptyMessage(REGISTER);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(ERROR);
					e.printStackTrace();
				}
			}
		}.start();
	}
}
