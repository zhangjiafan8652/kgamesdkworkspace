package com.yayawan.sdk.jflogin;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.jxutils.http.client.RetryHandler;
import com.yayawan.sdk.domain.BankInfo;
import com.yayawan.sdk.domain.PayMethod;
import com.yayawan.sdk.domain.PayResult;
import com.yayawan.sdk.jfpay.Yayapaymain_jf;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.utils.DeviceUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;

public class ViewConstants {

	/**
	 * 3.04. 1修改了logo白边. 2.修改了储蓄卡支付bug 3.增加了储蓄卡.信用卡提示
	 */

	/**
	 * 3.05. 1修改了登录方式..现在默认给用户自动注册一个账号 2.长按小兔子隐藏,摇一摇出现
	 * 
	 */

	/**
	 * 3.06. 1.解决切换账号未回调bug 2.修改了当在登录时切换账号和小助手中切换账号的不同情况 3.增加了兔子隐藏提示功能
	 */

	/**
	 * 3.07. 2015.8.3 1.解决了小助手在小米手机上的显示问题 2.礼包code可以再次显示 3.关于我们加上了微信公众号
	 * 4.修改首次登陆快速注册的等待ui.现为进度条形式 5.在个人中心上关于我们增加显示sdk版本号
	 */

	/**
	 * 3.08. 2015.8.20 1.添加是否支持进入游戏后切换账号功能 2.添加银联支付 3.修改支付宝icon 4.登录界面字体颜色统一黑色
	 */
	/**
	 * 3.09 2015.10.6 1.小兔子在隐藏后..切换界面不再出现 2.小兔子靠边半透明露头 3.消息列表玩家头像如果没有随机分配一个
	 */

	/**
	 * 3.10 2015.10.19 1.优化支付时窗口点击事件 2.优化更新机制
	 */
	/**
	 * 3.11 2015.11.19 1.优化丫丫玩网页链接问题 2.解决使用第三方登录时先调用登录取消的问题
	 */

	/**
	 * 3.11V1 2015.12.7 1.支付中卡密填写错误后.重新点击支付不响应
	 * 
	 */

	/**
	 * 3.11V2 2015.12.9 1.修改了退出提示文字，
	 * 
	 */
	/**
	 * 3.12 2015.12.14 1.打开应用无网络会卡在闪屏页面 2.取消了游戏切换账号功能的，新玩家第一次进入游戏还是会自动注册生成账号登陆
	 * 3.改了重设密码的文字描述
	 */
	/**
	 * 3.13 2016.1.18 1.解决了登陆改了账号没改密码时的登陆错误 2.增加了控制是否加入退出框判断。在配置文件中添加meta
	 * ishaveexit
	 * 
	 */

	/**
	 * 3.14 2016.2.18 1.解决了cp更改支付方式后。点击qq支付崩溃的问题 2.超级大改动，后台支付方式切换
	 * 
	 */

	/**
	 * 3.14.1 2016.3.29 1.联合sdk格式化token等玩家数据的时候加入了appid
	 * 
	 * 
	 */

	/**
	 * 3.14.2 2016.4.25 1.增加了新的设置玩家数据方法 2.加入了微信支付ui
	 * 更改了支付可显示方式，在AndroidManifest.xml清单文件中设置并且为true才显示
	 * 
	 */

	/**
	 * 3.15 2016.5.16 1.增加了将快速注册玩家的账号密码保存在手机里 2.解决了按返回键后。点击游戏进入崩溃的问题
	 * 
	 */

	/**
	 * 3.15.1 2016.5.23 1.点击个人中心崩溃问题
	 * 
	 * 
	 */

	/**
	 * 3.15.2 2016.6.21 1.闪屏偏离角度
	 * 
	 * 
	 */

	/**
	 * 3.16 2016.7.5 1.保存显示初始密码 2.添加客服qq 3.修复无网qq登陆问题
	 * 
	 */
	/**
	 * 3.16.1 2016.8.4 1.全面更新调试模式，联合sdk加入调试接口，可查询所有接口调用状态
	 * 
	 * 
	 * 
	 */
	/**
	 * 3.16.2 2016.8.4 1.去掉了消息流 2.在个人中心添加了丫丫币充值 3.在游戏资料中添加了游戏论坛
	 * 4.添加了客服中心。删除了关于我们
	 * 
	 */
	/**
	 * 3.17 2016.9.26 1.增加了清单文件设置默认为渠道登陆支付
	 * 
	 */
	/**
	 * 3.18 2016.10.27 1.增加了清单文件设置默认为渠道登陆支付 2.增加了yyu id功能
	 */
	/**
	 * 3.18 2016.11.5 1.增加了等级判断给钱
	 */
	/**
	 * 3.19 2016.12.5 1.充值丫丫币后闪退问题
	 */
	/**
	 * 3.20 2016.12.7 1.充值九五折 2.激活设置折扣九五折等信息
	 */
	/**
	 * 3.21 2016.1.17 1.激活添加了是否为ios
	 */

	/**
	 * 3.22 2016.2.7 1.多宝通微信支付更新
	 */
	/**
	 * 3.23 2016.2.24 1.多宝通微信支付更新2
	 */
	/**
	 * 3.24 2016.3.30 1.添加了实名认证
	 */
	
	/**
	 * 3.25 2016.4.6 1.优化了微信支付ui
	 */
	
	/**
	 * 3.26 2016.4.19 
	 * 
	 * 1.微信插件支付
	 * 
	 * 2.lianhe丫丫玩支付创建订单后台可查
	 * 
	 */

	public static final String SDKVERSION = 3.2 + "6";
	public static final int LOGIN_VIEW = 1;
	public static final int REGISTER_VIEW = 2;
	public static final int REGISTERACCOUNT_VIEW = 3;
	public static final int WEIBOLOGIN_VIEW = 4;
	public static final int QQLOGIN_VIEW = 5;
	public static final int YAYAPAYMAIN = 6;

	public static final int YINLIANPAY_ACTIVITY = 12;
	// 支付宝第二次确认页面
	public static final int PAYMENT_JF = 7;
	// 重置密码界面
	public static final int RESETPASSWORD = 8;
	// 重置密码界面
	public static final int ACCOUNTMANAGER = 9;
	// 启动页
	public static final int STARTANIMATION = 11;
	// isfirstlogin是否第一次进来登录页面
	public static final int FIRSTLOGIN = -1;
	public static final int NOFIRSTLOGIN = 0;
	
	//支付插件版本
	public static final int PLUINVERSIONCODE = 2;
	// dialog集合
	public static ArrayList<Dialog> mDialogs = new ArrayList<Dialog>();

	// 激活得到的bank集合
	public static SparseArray<PayMethod> banks = new SparseArray<PayMethod>(21);

	// 用户信息接口
	public static String USERINFO_URL = "http://passport.yayawan.com/api/userinfo";
	
	// 实名认证
	public static String REALAUTH ="http://passport.yayawan.com/apiv3/idcard";

	public static String shortname = null;

	// 控制第一次快速注册
	public static Boolean tempisFastregist = false;

	// 控制第一次快速登陆
	public static Boolean tempisFastlogin = true;

	// 临时的弹窗窗口
	public static Dialog TEMPLOGIN_HO = null;

	// 主界面activity
	public static Activity mMainActivity = null;

	// 是否弹出手机登陆
	public static Boolean OPENPHONELOGIN = true;

	// 控制是否手动退出
	public static Boolean HADLOGOUT = false;

	// 支付activity
	public static Activity mPayActivity = null;

	// 验证码倒计时
	public static long SENDMESSAGETIME = 60000;

	// 关闭小助手时候是否显示提示
	public static Boolean ISSHOWDISMISSHELP = true;

	// 是否为miui系统
	public static Boolean ismiui = false;

	// 是否不需要切换账号
	public static Boolean nochangeacount = false;

	// 用户是否打开了yywan兔子logo
	public static boolean iscloseyylog = false;

	public static boolean demoyayalogin = false;
	
	public static PayResult mPayResult;

	// public static String USER_FIRST_PASSWORD_SAVE=;

	public static int getHoldActivityHeight(Context mContext) {
		int ho_height = 650;
		int po_height = 850;
		int height = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
		}
		return height;
	}

	public static int getHoldActivityWith(Context mContext) {
		int ho_with = 1080;

		int po_with = 650;

		int with = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {
		} else if ("landscape".equals(orientation)) {
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			with = po_with;
		}
		return with;
	}

	public static int getHoldDialogHeight(Context mContext) {
		int ho_height = 600;
		int po_height = 600;
		int height = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
		}
		return height;
	}

	public static int getHoldDialogWith(Context mContext) {
		int ho_with = 760;

		int po_with = 600;

		int with = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {
		} else if ("landscape".equals(orientation)) {
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			with = po_with;
		}
		return with;
	}

}
