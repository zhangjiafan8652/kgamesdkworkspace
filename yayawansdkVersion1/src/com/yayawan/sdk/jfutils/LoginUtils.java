package com.yayawan.sdk.jfutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.account.engine.ObtainData;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.callback.YayaWanUserCallback;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jflogin.Login_ho_dialog;
import com.yayawan.sdk.jflogin.Login_success_dialog;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.main.YayaWan;

public class LoginUtils {

	private static final int FETCHSMS = 4;
	protected static final int FETCHSMS1 = 10;
	private static final int LOGINRESULT = 3; // 登陆返回
	protected static final int ERROR = 11;
	protected static final int SECRETLOGIN = 26;
	protected static final int LOGINSECURITYRESULT = 8;
	private static final int REGISTER = 5;

	private Activity mActivity;
	private Context mContext;
	private Basedialogview dialog;
	public static int STARTLOGIN = 1;
	public static int LOGINTYPE = 0;

	//
	public LoginUtils(Activity mActivity, Basedialogview dialog, int type) {
		this.mActivity = mActivity;
		this.mContext = mActivity;
		mUserCallback = YayaWan.mUserCallback;
		this.LOGINTYPE = type;
		this.dialog = dialog;
	}

	public LoginUtils(Activity mActivity, Basedialogview dialog) {
		this.mActivity = mActivity;
		this.mContext = mActivity;
		mUserCallback = YayaWan.mUserCallback;
		this.dialog = dialog;
	}

	public LoginUtils(Activity mActivity) {
		this.mActivity = mActivity;
		this.mContext = mActivity;
	}

	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			// TODO

			// dialogDismiss();
			Utilsjf.stopDialog();
			// DialogUtil.stopDialog();

			// dialog.dialogDismiss();

			switch (msg.what) {

			// 秘钥登录返回
			case SECRETLOGIN:
				User loginuser = (User) msg.obj;
				if (loginuser != null) {
					if (loginuser.success == 0) {
						// 登录成功
						/*Toast.makeText(mActivity, loginuser.body,
								Toast.LENGTH_SHORT).show();*/
						// 将用户信息保存到全局变量
						AgentApp.mUser = loginuser;
						// 将base64加密的用户信息保存到数据库
						UserDao.getInstance(mActivity).writeUser(
								loginuser.userName, loginuser.password,
								loginuser.secret);
						loginuser.secret = "";
						loginuser.password = "";
						// 开启悬浮窗服务
						//YayaWan.init(mActivity);
						//onSuccess(loginuser, 1);

						dialog.dialogDismiss();

						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity);
						login_success_dialog.dialogShow();

					} else {
						
						secretLogin( secretkey,  username);
						/*if (LOGINTYPE == STARTLOGIN) {
							startlogin();
						}

						Toast.makeText(mActivity, loginuser.body,
								Toast.LENGTH_SHORT).show();*/
					}
				}

				break;

			case LOGINRESULT:
				if (mUser != null) {
					if (mUser.success == 0) {
						// 登录成功
						/*Toast.makeText(mActivity, mUser.body,
								Toast.LENGTH_SHORT).show();*/
						// 将用户信息保存到全局变量
						AgentApp.mUser = mUser;
						// 将base64加密的用户信息保存到数据库
						UserDao.getInstance(mActivity).writeUser(mUsername,
								mPassword, mUser.secret);
						mUser.secret = "";
						mUser.password = "";
						// 开启悬浮窗服务
						//YayaWan.init(mActivity);
						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity);
						login_success_dialog.dialogShow();
						//onSuccess(mUser, 1);

						dialog.dialogDismiss();

					} else {
						if (LOGINTYPE == STARTLOGIN) {
							startlogin();
						}

						Toast.makeText(mActivity, mUser.body,
								Toast.LENGTH_SHORT).show();

					}
				}
				break;

			case ERROR:
				// DialogUtil.stopDialog();
				Toast.makeText(mActivity, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				if (LOGINTYPE == STARTLOGIN) {
					startlogin();
				}

				break;
			// 用户名手机验证码登录
			case LOGINSECURITYRESULT:
				User loginuser1 = (User) msg.obj;
				if (loginuser1.success == 0) {
					AgentApp.mUser = loginuser1;
					// System.out.println("手机注册接口返回数据"+loginuser.secret);
					// 将base64加密的用户信息保存到数据库
					UserDao.getInstance(mActivity).writeUser(
							loginuser1.userName, loginuser1.password,
							loginuser1.secret);
					// System.out.println("手机得到的账号密码是0"+loginuser);
					loginuser1.password = "";
					loginuser1.secret = "";
					// 开启悬浮窗服务
					//YayaWan.init(mActivity);
					//onSuccess(loginuser1, 1);
					Login_success_dialog login_success_dialog = new Login_success_dialog(
							mActivity);
					login_success_dialog.dialogShow();
					dialog.dialogDismiss();
				} else if (loginuser1.success == 1) {

				} else if (loginuser1.success == 2) {
					AgentApp.mUser = loginuser1;
					// 将base64加密的用户信息保存到数据库
					//System.out.println("手机得到的账号密码是" + loginuser1);
					UserDao.getInstance(mActivity).writeUser(
							loginuser1.userName, loginuser1.password,
							loginuser1.secret);
					loginuser1.password = "";
					loginuser1.secret = "";
					// 开启悬浮窗服务
					//YayaWan.init(mActivity);
					//onSuccess(loginuser1, 1);
					Login_success_dialog login_success_dialog = new Login_success_dialog(
							mActivity);
					login_success_dialog.dialogShow();
					dialog.dialogDismiss();
				}
				Toast.makeText(mActivity, loginuser1.body, Toast.LENGTH_SHORT)
						.show();

				break;

			case REGISTER:
				User loginuser3 = (User) msg.obj;
				if (mUser != null) {
					if (mUser.success == 0) {
						/*Toast.makeText(mContext, loginuser3.body,
								Toast.LENGTH_SHORT).show();*/
						// 将用户信息保存到全局变量
						AgentApp.mUser = loginuser3;
						// 将base64加密的用户信息保存到数据库
						UserDao.getInstance(mContext).writeUser(
								loginuser3.userName, loginuser3.password,
								loginuser3.secret);
						// 开启悬浮窗服务

						//YayaWan.init(mActivity);
						// onSuccess(mUser, 1);
						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity);
						login_success_dialog.dialogShow();
						//dialog.onSuccess(mUser, 1);
					} else {
						Toast.makeText(mContext, mUser.body, Toast.LENGTH_SHORT)
								.show();
					}
				}
				break;
			default:
				break;
			}
		}

	};

	private User mUser;
	private YayaWanUserCallback mUserCallback;
	private String mUsername;
	private String mPassword;

	/**
	 * 用key生成验证码登录
	 * 
	 * @param secretkey
	 * @param username
	 */
	private String secretkey;
	private String username;
	public void secretLogin(String secretkey, final String username) {

		this.secretkey=secretkey;
		this.username=username;
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}

		long t = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
		long create_code = ga.create_code(secretkey);
		final String code = create_code + "";
		//System.out.println(code);
		new Thread() {

			public void run() {
				try {
					User user = ObtainData.loginSecurity1(mContext, username,
							code);

					Message message = new Message();
					message.obj = user;
					message.what = SECRETLOGIN;
					mHandler.sendMessage(message);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					mHandler.sendEmptyMessage(ERROR);
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 用户名手机验证码登录
	 * 
	 * @param name
	 * @param password
	 */
	public void codeLogin(final String mphone, final String code) {
		// 输入的用户名和密码符合要求
		// Utilsjf.creDialogpro(mActivity, "正在登陆...");
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}
		// 网络访问要在线程中
		new Thread() {

			@Override
			public void run() {
				// 验证码登录
				new Thread() {
					@Override
					public void run() {

						try {
							User user = ObtainData.loginSecurity(mActivity,
									mphone, code);
							Message message = new Message();
							message.obj = user;
							message.what = LOGINSECURITYRESULT;
							mHandler.sendMessage(message);
						} catch (Exception e) {
							mHandler.sendEmptyMessage(ERROR);
							e.printStackTrace();
						}
					}

				}.start();
			}
		}.start();

	}

	/**
	 * 用户名密码登录
	 * 
	 * @param name
	 * @param password
	 */
	public void login(final String name, final String password) {
		// 输入的用户名和密码符合要求
		// Utilsjf.creDialogpro(mActivity, "正在登陆...");
		// 网络访问要在线程中
		mUsername = name;
		mPassword = password;
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}
		new Thread() {

			@Override
			public void run() {
				try {
					mUser = ObtainData.login(mActivity, name, password);
					mHandler.sendEmptyMessage(LOGINRESULT);
				} catch (Exception e) {
					// DialogUtil.stopProgressDlg();
					mHandler.sendEmptyMessage(ERROR);
					// e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 账号注册
	 * 
	 * @param name
	 * @param password
	 */
	public void register(final String mName, final String mPassword) {
		// 输入的用户名和密码符合要求
		// Utilsjf.creDialogpro(mActivity, "正在登陆...");
		// 网络访问要在线程中
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}
		new Thread() {

			@Override
			public void run() {
				try {

					User user = ObtainData.register(mContext, mName, mPassword);
					Message message = new Message();
					message.obj = user;
					message.what = REGISTER;
					mHandler.sendMessage(message);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(ERROR);
					e.printStackTrace();
				}
			}
		}.start();

	}

	private void startlogin() {

		if (dialog != null) {
			dialog.dialogDismiss();
		}

		Login_ho_dialog login_ho_dialog = new Login_ho_dialog(mActivity);
		login_ho_dialog.dialogShow();

	}

	private void onSuccess(User mUser, int i) {
		if (mUserCallback != null) {
			mUserCallback.onSuccess(mUser, i);
		}
		mUserCallback = null;

	}

}
