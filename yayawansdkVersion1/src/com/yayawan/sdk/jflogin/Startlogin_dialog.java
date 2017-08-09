package com.yayawan.sdk.jflogin;

import java.util.ArrayList;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.account.engine.ObtainData;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Basedialogview;
import com.yayawan.sdk.jfutils.HorizontalProgressBarWithNumber;
import com.yayawan.sdk.jfutils.LoginUtils;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfxml.GetAssetsutils;
import com.yayawan.sdk.jfxml.MachineFactory;
import com.yayawan.sdk.utils.MD5;
import com.yayawan.sdk.utils.SIMCardUtil;

public class Startlogin_dialog extends Basedialogview {

	private ImageView iv_loading;
	private TextView tv_message;
	private ArrayList<String> mNames;
	private String mSelectUser;
	private String mPassword;
	private SmsManager mSmsManager;

	private String mUUID;
	private static final int FETCHSMS = 4;
	protected static final int FETCHSMS1 = 10;
	private static final int LOGINRESULT = 3; // 登陆返回
	protected static final int ERROR = 11;
	protected static final int SECRETLOGIN = 20;
	private static final String CMCC = "106900608888";

	private static final String TELECOM = "1069033301128";
	private static final int PROGRESS = 400;
	private User mUserLoading;

	private int processtime = 0;
	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			// TODO
			if (msg.what != PROGRESS) {
				dialogDismiss();
			}

			switch (msg.what) {

			case FETCHSMS:

				User user = (User) msg.obj;
				if (user.success == 0) {
					// 弹出服务器返回的登录成功提示
					// 将用户信息保存到全局变量
					AgentApp.mUser = user;
					// 将base64加密的用户信息保存到数据库
					UserDao.getInstance(mActivity).writeUser(user.userName,
							user.password, user.secret);
					user.secret = "";
					user.password = "";
					// 成功的回调接口 sb.zhangjiafan
					// onSuccess(user, 1);
					dialogDismiss();
					Login_success_dialog login_success_dialog = new Login_success_dialog(
							mActivity);
					login_success_dialog.dialogShow();
				} else {

					ViewConstants.tempisFastregist = true;
					// 开启登录窗口
					dialogDismiss();
					new AcountRegister(mActivity).acountRregister();

					Toast.makeText(mActivity, "快速注册失败...", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case FETCHSMS1:

				User user1 = (User) msg.obj;

				if (user1.success == 2) {
					Toast.makeText(mActivity, user1.body, Toast.LENGTH_SHORT)
							.show();
					startlogin();
				}
				break;

			case ERROR:
				// DialogUtil.stopDialog();
				Toast.makeText(mActivity, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				// 开启登录窗口
				dialogDismiss();
				new AcountRegister(mActivity).acountRregister();
				// startlogin();
				break;
			case PROGRESS:

				if (processtime < 100) {
					// System.out.println(processtime);
					processtime = processtime + 1;
					if (pb_hori.getVisibility() != View.VISIBLE) {
						pb_hori.setVisibility(View.VISIBLE);
					}

					pb_hori.incrementProgressBy(1);
					mHandler.sendEmptyMessageDelayed(PROGRESS, 450);
				} else {
					processtime = 0;
				}

				break;

			default:
				break;
			}
		}

	};
	private boolean phonelogin;
	private String secretkey;
	private HorizontalProgressBarWithNumber pb_hori;

	public Startlogin_dialog(Activity activity) {
		super(activity);

	}

	/**
	 * 打开登录对话框
	 */
	private void startlogin() {

		dialogDismiss();
		Login_ho_dialog login_ho_dialog = new Login_ho_dialog(mActivity);
		login_ho_dialog.dialogShow();

	}

	@Override
	public void dialogShow() {
		// TODO Auto-generated method stub
		super.dialogShow();
		initlog();
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 450, 150, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 450, 150, 0, mLinearLayout, 0,
				0, 0, 0, 100);

		// ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mActivity));
		ll_content.setGravity(Gravity.CENTER_VERTICAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content2, 450, 100, 0, mLinearLayout, 0,
				0, 0, 0, 100);
		ll_content2.setGravity(Gravity.CENTER);

		iv_loading = new ImageView(mActivity);
		machineFactory.MachineView(iv_loading, 100, 100, mLinearLayout, 1, 10);
		iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_loading(1).png", mActivity));

		RotateAnimation rotateAnimation = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setDuration(1000);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);

		iv_loading.setAnimation(rotateAnimation);
		iv_loading.startAnimation(rotateAnimation);

		tv_message = new TextView(mActivity);
		machineFactory.MachineTextView(tv_message, WRAP_CONTENT, WRAP_CONTENT,
				0, "", 36, mLinearLayout, 10, 0, 0, 0);
		tv_message.setTextColor(Color.parseColor("#666666"));

		pb_hori = new HorizontalProgressBarWithNumber(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		machineFactory.MachineView(pb_hori, MATCH_PARENT, 30, 0, mLinearLayout,
				30, 0, 30, 10, 100);
		pb_hori.setBackgroundColor(Color.WHITE);

		pb_hori.incrementProgressBy(0);

		pb_hori.setVisibility(View.GONE);

		// TODO
		ll_content2.addView(iv_loading);
		ll_content2.addView(tv_message);
		ll_content.addView(ll_content2);
		ll_content.addView(pb_hori);

		baselin.addView(ll_content);
		// baselin.addView(pb_hori);

		dialog.setContentView(baselin);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度

		dialogWindow.setAttributes(lp);

		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		// dialog.setCanceledOnTouchOutside(true);

		// initlog();

	}

	private void initlog() {
		// mHandler.sendEmptyMessageDelayed(PROGRESS, 300);
		// 判断是否点击过切换账号或者
		if (ViewConstants.HADLOGOUT
				|| Sputils.getSPint("ischanageacount", 1, mActivity) == 0) {
			startlogin();
			return;
		}

		mNames = new ArrayList<String>();
		initDBData();

	}

	private void initDBData() {

		// TODO Auto-generated method stub
		// 每次从数据库获取数据时都清空下列表,否则会有很多重复的数据
		if (mNames != null && mNames.size() > 0) {
			mNames.clear();
		}
		// 数据库添加一列
		UserDao.getInstance(mActivity).upDateclume();
		mNames = UserDao.getInstance(mActivity).getUsers();

		// 如果数据库里没有任何账号注册过.则进行快速注册
		if (mNames.size() == 0) {
			startFirstregister();
			tv_message.setText("尝试自动登录中...");
			return;
		}
		// 是否把切换账号取消了
		if (ViewConstants.nochangeacount) {
			startlogin();
			return;
		}

		if (mNames != null && mNames.size() > 0) {

			mSelectUser = mNames.get(0);
			mPassword = UserDao.getInstance(mActivity).getPassword(mSelectUser);

			secretkey = UserDao.getInstance(mActivity).getSecret(mSelectUser);

			if (!TextUtils.isEmpty(mPassword)
					&& !mPassword.equals("yayawan-zhang")) {
				// 选择第一项进行登录
				LoginUtils loginUtils = new LoginUtils(mActivity, this,
						LoginUtils.STARTLOGIN);
				loginUtils.login(mSelectUser, mPassword);

				tv_message.setText("快速登录中...");
			} else if (!TextUtils.isEmpty(secretkey)) {

				// secretLogin(secretkey, mSelectUser);
				LoginUtils loginUtils = new LoginUtils(mActivity, this,
						LoginUtils.STARTLOGIN);
				loginUtils.secretLogin(secretkey, mSelectUser);
				tv_message.setText("快速登录中...");
			} else {
				dialogDismiss();
				startlogin();
			}

		}

	}

	// 快速注册
	private void startFirstregister() {

		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Service.TELEPHONY_SERVICE);
		int state = tm.getSimState();
		System.out.println("得到的state" + state);
		int absent = TelephonyManager.SIM_STATE_ABSENT;
		// 判断是否有sim卡..没有就直接打开登录界面
		if (state != TelephonyManager.SIM_STATE_READY) {
			dialogDismiss();
			new AcountRegister(mActivity).acountRregister();

			return;
			// / Log.d(TAG,"请确认sim卡是否插入或者sim卡暂时不可用！");
		}
		// 已经尝试过快速注册登录
		if (ViewConstants.tempisFastregist) {
			startlogin();
			return;
		}
		// DialogUtil.createDialog(mActivity, "快速注册登录中...");
		// 一键注册
		// 注册广播 发送消息
		mSmsManager = SmsManager.getDefault();
		UUID uuid = UUID.randomUUID();
		try {
			mUUID = MD5.MD5(uuid.toString());
			String providersName = SIMCardUtil.getProvidersName(mActivity);
			if ("CMCC".equals(providersName)) {
				mSmsManager.sendTextMessage(mSp.getString("CMCC", CMCC), null,
						mSp.getString("CMD", "qy") + mUUID, null, null);
			} else if ("UNICOM".equals(providersName)) {
				mSmsManager.sendTextMessage(mSp.getString("UNICOM", CMCC),
						null, mSp.getString("CMD", "qy") + mUUID, null, null);
			} else if ("TELECOM".equals(providersName)) {
				mSmsManager.sendTextMessage(mSp.getString("TELECOM", TELECOM),
						null, mSp.getString("CMD", "qy") + mUUID, null, null);
			}
		} catch (Exception e) {
			dialogDismiss();
			new AcountRegister(mActivity).acountRregister();

			e.printStackTrace();
		}
		// DialogUtil.showDialog(mActivity, "正在注册...");

		// 这里的策略好像是循环向服务器请求数据.看用户是否注册成功 sb.zhang
		new Thread() {

			@Override
			public void run() {
				// 访问网络,获取用户名密码
				try {
					mHandler.sendEmptyMessageDelayed(PROGRESS, 300);
					// TODO
					mUserLoading = ObtainData.fetchSms(mActivity, mUUID);
					int count = 0;
					// 每隔3秒访问一次.总共10次..30秒哦 sb.zhang
					// 2为已经注册过
					while (mUserLoading.success == 1 && count < 10) {
						count++;
						Thread.sleep(3 * 1000);
						mUserLoading = ObtainData.fetchSms(mActivity, mUUID);

						if (mUserLoading.success == 2) {
							Message message = new Message();
							message.obj = mUserLoading;
							message.what = FETCHSMS1;
							mHandler.sendMessage(message);
							break;
						}
					}

					Message message = new Message();
					message.obj = mUserLoading;
					message.what = FETCHSMS;
					mHandler.sendMessage(message);

				} catch (Exception e) {
					mHandler.sendEmptyMessage(ERROR);
					e.printStackTrace();
				}

			}
		}.start();

	}

}
