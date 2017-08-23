package com.yayawan.proxy;

import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.rtp.RtpStream;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWLoginHandleCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWRole;
import com.yayawan.domain.YYWUser;
import com.yayawan.impl.ActivityStubImpl;
import com.yayawan.impl.AnimationImpl;
import com.yayawan.impl.ChargerImpl;
import com.yayawan.impl.LoginImpl;
import com.yayawan.impl.UserManagerImpl;
import com.yayawan.implyy.ChargerImplyy;
import com.yayawan.implyy.ChargerImplyylianhe;
import com.yayawan.implyy.LoginImplyy;
import com.yayawan.implyy.SingleChargerImplyy;
import com.yayawan.implyy.YYcontants;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.callback.YayaWanCallback;
import com.yayawan.sdk.callback.YayaWanUpdateCallback;
import com.yayawan.sdk.jfother.JFnoticeUtils;
import com.yayawan.sdk.jfother.JFupdateUtils;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Handle;
import com.yayawan.utils.JSONUtil;

public class CommonGameProxy implements YYWGameProxy {

	private YYWLoginer mLogin;

	private YYWCharger mCharger;

	private YYWActivityStub mStub;

	private YYWUserManager mUserManager;

	private YYWAnimation mAnimation;

	private Activity mActivity;
	
	private String templevel;

	public CommonGameProxy() {

		this(new LoginImpl(), new ActivityStubImpl(), new UserManagerImpl(),
				new ChargerImpl());

		setAnimation(new AnimationImpl());

	}

	public CommonGameProxy(YYWLoginer login, YYWActivityStub stub,
			YYWUserManager userManager, YYWCharger charger) {
		super();
		// new CommonGameProxy();
		this.mLogin = login;
		this.mStub = stub;
		this.mUserManager = userManager;
		this.mCharger = charger;
	}

	public void setLogin(YYWLoginer login) {
		this.mLogin = login;

	}

	public void setCharger(YYWCharger charger) {
		this.mCharger = charger;
	}

	public void setStub(YYWActivityStub stub) {
		this.mStub = stub;
	}

	public void setUserManager(YYWUserManager userManager) {
		this.mUserManager = userManager;
	}

	public void setAnimation(YYWAnimation animation) {
		this.mAnimation = animation;
	}

	public static boolean ISNEWPAY = false;// 是否使用只新登陆,用于控制第三种支付方式

	@Override
	public void login(final Activity paramActivity,
			final YYWUserCallBack userCallBack) {
		mActivity = paramActivity;
		// YYWMain.mUserCallBack=userCallBack;
		// 检测是否调用类
		GameApitest.getGameApitestInstants(paramActivity).sendTest("login");
		//判断本地登陆支付
		loca_login_type = Sputils.getSPint("loca_login_type", 4,
						paramActivity);
		
		YYWMain.mUserCallBack = new YYWUserCallBack() {

			@Override
			public void onLogout(Object paramObject) {
				// TODO Auto-generated method stub
				userCallBack.onLogout(paramObject);
			}

			@Override
			public void onLoginSuccess(final YYWUser paramUser,
					Object paramObject) {
				// TODO Auto-generated method stub

				Handle.login_handler(paramActivity, YYWMain.mUser.uid,
						YYWMain.mUser.userName, new YYWLoginHandleCallback() {

							private YYWUser yywUser;

							@Override
							public void onSuccess(String response, String temp) {
								// TODO Auto-generated method stub
								Yayalog.loger("loginhandle:" + response);
								// 登陆进来判断用1登陆还是0.
								try {
									JSONObject jsonObject = new JSONObject(
											response);
									JSONObject jsonObject2 = jsonObject
											.getJSONObject("bind_user");
									int yayatype = jsonObject2
											.getInt("bind_yaya");
									if (yayatype == 1&&!LoginImplyy.ISLOGIN) {
										// 如果是1，则返回丫丫玩平台的id和拼接的token返回给cp

										ISNEWPAY = true;

										// 拼接返回给cp的user开始
										yywUser = new YYWUser();
										yywUser.uid = jsonObject2
												.getString("uid");
										yywUser.userName = jsonObject2
												.getString("username");
										yywUser.yywtoken = jsonObject2
												.getString("token");
										yywUser.token = JSONUtil.formatToken(
												paramActivity, paramUser.token,
												paramUser.uid,
												paramUser.userName,
												yywUser.userName);
										// 拼接给cp的user结束

										// 拼接渠道的user，当调用渠道的支付，一定使用到渠道的YYWMain.mUser.uid
										YYWMain.mUser.uid = paramUser.uid;
										YYWMain.mUser.userName = paramUser.userName;
										YYWMain.mUser.yywuid = yywUser.uid;
										YYWMain.mUser.yywusername = yywUser.userName;
										YYWMain.mUser.yywtoken = yywUser.yywtoken;
										Yayalog.loger("+++++++++++++token"
												+ YYWMain.mUser.token);

										mActivity.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												userCallBack.onLoginSuccess(
														yywUser, "suceess");
											}
										});

									} else {
										if (com.yayawan.utils.DeviceUtil
												.getUnionid(paramActivity)
												.equals("1354981926")||LoginImplyy.ISLOGIN) {

										} else {
											String tempyywusername = jsonObject2
													.getString("username");
											YYWMain.mUser.token = JSONUtil
													.formatToken(
															paramActivity,
															paramUser.token,
															YYWMain.mUser.uid,
															YYWMain.mUser.userName,
															tempyywusername);

										}
										mActivity.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												userCallBack.onLoginSuccess(
														YYWMain.mUser, "success");
											}
										});

									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

									Yayalog.loger("json解析失败loginhandle:"
											+ e.toString());
								}

							}

							@Override
							public void onFail(String erro, String temp) {
								// TODO Auto-generated method stub
								Yayalog.loger("请求失败loginhandle:" + erro);
							}
						});
				//
			}

			@Override
			public void onLoginFailed(String paramString, Object paramObject) {
				// TODO Auto-generated method stub
				userCallBack.onLoginFailed("失败", "faile");
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				userCallBack.onCancel();
			}
		};

		// 获取配置文件的参数，指定登陆和支付方式，这是
		String templogin = "no";
		try {
			templogin = DeviceUtil.getGameInfo(paramActivity,
					"USE_DEFALUT_LOGIN");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Yayalog.loger("未定义USE_DEFALUT_LOGIN");
			// e.printStackTrace();
		}
		if (templogin.equals("yes")) {

			Sputils.putSPint("loca_login_type", 0, paramActivity);

			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
			Yayalog.loger("USE_DEFALUT_LOGIN");
			return;
		}
		Yayalog.loger("没有进去USE_DEFALUT_LOGIN");

		// 支付过程。。 启动游戏，获取线上的支付方式。
		// 获取本地支付方式 启动， 如果本地为 4--第一次启动获取线上支付方式，并且保存在本地

		//
		// 启动和支付方式 0 渠道登陆 渠道支付 1 丫丫玩登陆 丫丫玩支付 3 渠道登陆 丫丫玩支付
		//
		// 启动 0---3 如果之前为0可变为3
		// 启动 1不能改
		// 启动 3---0 如果之前为3可变为0

		// 判断本地支付方式
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);

		switch (loca_login_type) {
		case 0:
			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
			break;
		case 1:
			this.mLogin = new LoginImplyy();
			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
			break;
		case 2:
			this.mLogin = new LoginImplyy();
			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
			break;
		case 3:
			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
			break;
		case 4:
			// 默认4为第一次安装。从激活处获取网络获取的支付方式
			int login_type = Sputils.getSPint("login_type", 0, paramActivity);

			switch (login_type) {

			case 0:
				this.mLogin
						.login(paramActivity, YYWMain.mUserCallBack, "login");
				break;
			case 1:
				this.mLogin = new LoginImplyy();
				this.mLogin
						.login(paramActivity, YYWMain.mUserCallBack, "login");
				break;
			case 2:
				this.mLogin = new LoginImplyy();
				this.mLogin
						.login(paramActivity, YYWMain.mUserCallBack, "login");
				break;
			case 3:
				this.mLogin
						.login(paramActivity, YYWMain.mUserCallBack, "login");
				break;

			default:
				break;
			}
			Sputils.putSPint("loca_login_type", login_type, paramActivity);

			break;

		default:
			break;
		}
		// 如果第一次是用丫丫玩登陆则不能改变支付方式。所以出了本地登陆状态为1的还得检查一下是否为3.。如果为3.则调用单机支付
		if (loca_login_type != 1) {
			int login_type = Sputils.getSPint("login_type", 0, paramActivity);
			Yayalog.loger("我更改了loca：" + loca_login_type);
			if (login_type == 3 || login_type == 0) {
				Sputils.putSPint("loca_login_type", login_type, paramActivity);
			}
		}

	}

	@Override
	public void logout(Activity paramActivity, YYWUserCallBack userCallBack) {
		YYWMain.mUserCallBack = userCallBack;
		this.mLogin.relogin(paramActivity, userCallBack, "relogin");
	}

	public void logout(Activity paramActivity) {

		this.mUserManager.logout(paramActivity, null, null);
	}

	/**
	 * 检测更新
	 */
	public void update(Activity mActivity,
			YayaWanUpdateCallback mYayawanUpdatecallback) {
		GameApitest.getGameApitestInstants(mActivity).sendTest("update");
		YayaWan.update(mActivity, mYayawanUpdatecallback);

	}

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		YYWMain.mPayCallBack = payCallBack;
		YYWMain.mOrder = order;
		this.mCharger.charge(paramActivity, order, payCallBack);
	}

	@Override
	public void pay(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("pay");
		YYWMain.mPayCallBack = payCallBack;
		YYWMain.mOrder = order;
		
		
		// 获取配置文件的参数，指定切换支付的支付方式
				String temppay = "no";
				try {
					temppay = DeviceUtil.getGameInfo(paramActivity,
							"USE_YAYAPAY");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Yayalog.loger("未定义USE_YAYAPAY");
					// e.printStackTrace();
				}
				if (temppay.equals("yes")) {
					Yayalog.loger("payyy");
					if (ISNEWPAY) {
						Yayalog.loger("payyy");
						Yayalog.loger("CommoonGameproxy丫丫玩支付");
						this.mCharger = new ChargerImplyylianhe();
						this.mCharger.pay(paramActivity, order, payCallBack);

					} else {
					
						Yayalog.loger("CommoonGameproxy单机支付");
						this.mCharger = new SingleChargerImplyy();
						this.mCharger.pay(paramActivity, order, payCallBack);
					}
					return;
				}
				Yayalog.loger("没有进去USE_YAYAPAY");
		
		
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);
		Yayalog.loger("支付时候loca_login_type：" + loca_login_type);
		switch (loca_login_type) {
		case 0:
			
			this.mCharger.pay(paramActivity, order, payCallBack);
			break;
		case 1:
			
			this.mCharger = new ChargerImplyy();
			this.mCharger.pay(paramActivity, order, payCallBack);
			Yayalog.loger("丫丫玩登陆支付");
			
			break;
		case 2:
			this.mCharger.pay(paramActivity, order, payCallBack);
			break;
		case 3:
			String login_pay_level = Sputils.getSPstring("login_pay_level", "0", paramActivity);
			
			
			boolean compareLevel = compareLevel(templevel, login_pay_level);
			Yayalog.loger("compareLevel"+compareLevel);
			if (compareLevel) {
				if (ISNEWPAY) {
					Yayalog.loger("payyy");
					Yayalog.loger("CommoonGameproxy丫丫玩支付");
					this.mCharger = new ChargerImplyylianhe();
					this.mCharger.pay(paramActivity, order, payCallBack);

				} else {
				
					Yayalog.loger("CommoonGameproxy单机支付");
					this.mCharger = new SingleChargerImplyy();
					this.mCharger.pay(paramActivity, order, payCallBack);
				}

			}else {
				this.mCharger.pay(paramActivity, order, payCallBack);
			}
		
			break;

		default:
			break;
		}

	}

	@Override
	public void manager(Activity paramActivity) {
		this.mUserManager.manager(paramActivity);

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback exitCallBack) {
		YYWMain.mExitCallback = exitCallBack;
		GameApitest.getGameApitestInstants(paramActivity).sendTest("exit");
		if (DeviceUtil.isHaveexit(paramActivity)) {
			Yayalog.loger("ishaveexit");
			paramActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					YayaWan.Exitgame(paramActivity, new YayaWanCallback() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							// exitCallBack.onExit();
							paramActivity.finish();
						}
					});
				}
			});
			// 这这在执行一次。防止某些游戏在某些sdk中无法完全退出
			// this.mUserManager.exit(paramActivity, exitCallBack);
		} else {

			int loca_login_type = Sputils.getSPint("loca_login_type", 4,
					paramActivity);
			Yayalog.loger("loca_login_type" + loca_login_type + "退出");
			if (loca_login_type == 1) {
				YayaWan.Exitgame(paramActivity, new YayaWanCallback() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						paramActivity.finish();
					}
				});
			} else {
				this.mUserManager.exit(paramActivity, exitCallBack);
			}
		}
	}

	@Override
	public void anim(Activity paramActivity, YYWAnimCallBack animCallback) {
		YYWMain.mAnimCallBack = animCallback;
		GameApitest.getGameApitestInstants(paramActivity).sendTest("anim");
		this.mAnimation.anim(paramActivity);

	}

	@Override
	public void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"setRoleData");
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName);
		
		//设置临时的角色等级。用作支付时候判断是否切换支付
		templevel=roleLevel;
		this.mUserManager.setRoleData(paramActivity);
	}

	// 3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,
			String ext) {
		Yayalog.loger("调用了commongameproxy中setData");
		//设置临时的角色等级。用作支付时候判断是否切换支付
		templevel=roleLevel;
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName, roleCTime, ext);

		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"setData" + ext, YYWMain.mRole.toString());
		// 为了兼容老sdk判断是否有setdata接口方法再执行
		Method[] methods;
		try {
			methods = Class.forName("com.yayawan.impl.UserManagerImpl")
					.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("setData")) {
					// com.yayawan.proxy.YYWActivityStub
					Yayalog.loger(methods[i].getName());
					Yayalog.loger("有3.15版本设置角色信息接口setData");
					UserManagerImpl mani = (UserManagerImpl) this.mUserManager;
					mani.setData(paramActivity, roleId, roleName, roleLevel,
							zoneId, zoneName, roleCTime, ext);

				}
				// System.out.println("没有初始化方法");
				Yayalog.loger("UserManagerImpl中没有setdata方法");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void applicationInit(Activity paramActivity) {
		this.mStub.applicationInit(paramActivity);
	}

	boolean newactive = true;

	private int loca_login_type;

	@Override
	public void onCreate(final Activity paramActivity) {
		// 进行检查更新
		// Yayalog.loger("新版更新在oncreate中启动了");
		YYcontants.ISDEBUG = DeviceUtil.isDebug(paramActivity);
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onCreate");
		mActivity = paramActivity;
		//获取公告
		new JFnoticeUtils().getNotice(paramActivity);
		//获取更新
		new JFupdateUtils(paramActivity).startUpdate();
		
		mStub.onCreate(paramActivity);

	}

	@Override
	public void onStop(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onStop");
		this.mStub.onStop(paramActivity);

	}

	@Override
	public void onResume(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onResume");
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);
		if (loca_login_type == 1 || loca_login_type == 2) {
			YayaWan.init(paramActivity);
		} else {
			this.mStub.onResume(paramActivity);
		}

	}

	@Override
	public void onPause(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onPause");
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);
		if (loca_login_type == 1 || loca_login_type == 2) {
			YayaWan.stop(paramActivity);
		} else {
			this.mStub.onPause(paramActivity);
		}
	}

	@Override
	public void onRestart(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onRestart");
		this.mStub.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onDestroy");
		this.mStub.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {

		this.mStub.applicationDestroy(paramActivity);
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"onActivityResult");
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);

		this.mStub.onActivityResult(paramActivity, paramInt1, paramInt2,
				paramIntent);

	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		if (mActivity != null) {
			GameApitest.getGameApitestInstants(mActivity).sendTest(
					"onNewIntent");
		}

		this.mStub.onNewIntent(paramIntent);
	}

	@Override
	public void initSdk(Activity paramActivity) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants(paramActivity).sendTest("initSdk");
		// Class.forName("ActivityStubImpl").
		// 为了兼容老sdk判断是否有初始化方法再执行
		Method[] methods;
		try {
			methods = Class.forName("com.yayawan.impl.ActivityStubImpl")
					.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("initSdk")) {
					// com.yayawan.proxy.YYWActivityStub
					Yayalog.loger("有初始化方法com.yayawan.impl.ActivityStubImpl");

					/*
					 * if
					 * (DeviceUtil.getUnionid(paramActivity).equals("1876218605"
					 * )||
					 * DeviceUtil.getUnionid(paramActivity).equals("yaya1876218605"
					 * )) { this.mStub.initSdk(paramActivity); }
					 */

				}
				// System.out.println("没有初始化方法");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 *判断本地的等级与线上设定的等级谁大
	 * @return
	 */
	public static boolean compareLevel(String templevel,String xianshanglevel){
		int xian=Integer.parseInt(xianshanglevel);
		Yayalog.loger("xian"+xianshanglevel);
		if (xian<1) {
			return true;
		}
		try {
			int templ=Integer.parseInt(templevel);
			
			if (templ>xian) {
				return true;
				
			}else {
				return false;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Yayalog.loger("判断等级"+e.toString());
		}
		
		return false;
	}

}
