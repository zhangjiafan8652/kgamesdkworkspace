/**
 * Copyright (c) 2013 DuoKu Inc.
 */
package com.snow.cn.sdk.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.snowfish.cn.ganga.helper.SFOnlineUser;
/*  Class LoginHelper 该类用来管理登陆状态以及登陆账户信息
*/
public class LoginHelper {
	
	public class OderId {
		public int retry;
		public String orderId;
	}
	//自建服务器 测服
    //	static public final String CP_LOGIN_CHECK_URL = "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginZijian";
	//自建服务器 正服 
	//   static public final String CP_LOGIN_CHECK_URL = "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginZijianP";
	//test
    //static public final String CP_LOGIN_CHECK_URL = "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLogin";
	
	/*CP服务器地址:用作登录验证.因为有些渠道要求必须做登录验证，为了统一处理，所以只有登录验证成功之后才算真正的登陆成功
	 * 登陆验证是由CP服务器实现的，供游戏客户端调用验证用户登录状态的接口。 游戏客户端和游戏服务器之间的登陆验证接口由CP自己定义
	 */ 
	public static final String CP_LOGIN_CHECK_URL = "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginP";

	/*CP服务器地址，支付结果同步地址
	 * 如果客户端不设置，将以在易接后台创建游戏时设置的数据同步地址进行同步
	 * */
	static public final String CP_PAY_SYNC_URL = "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/sync";
	
	public static final boolean DEBUG	= false;
    private static LoginHelper instance = null;
    static SFOnlineUser user = null;
    static boolean bLogined = false;
    boolean isQuerying = false;
    Object lock = new Object();
	List<OderId> orderIds = new ArrayList<OderId>();
	static Handler handler  = null;
	static String message = "";
	
	public static LoginHelper instance() {
		if (instance == null) {
			try {
				instance = new LoginHelper();
			} catch (Throwable e) {
				new Exception("don't find the necessary configuration file").printStackTrace();
			}
		}
		return instance;
	}
	public boolean isLogin(){
		return bLogined;
	}
	public void setLogin(boolean login){
		bLogined = login;
	}
	public SFOnlineUser getOnlineUser(){
		return user;
	}
	public void setOnlineUser(SFOnlineUser u){
		user = u;
	}
	public boolean isDebug(){
		return DEBUG;
	}
	
	static public void showMessage(String msg, final Context context) {
		message = msg;
		prepareHandler(context);
		handler.sendEmptyMessage(1);
	}
	
	static void prepareHandler(final Context context) {
		if (handler == null) {
			handler = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {
						Toast.makeText(context, message,
								Toast.LENGTH_SHORT).show();
					}
				}
			};
		}
	}
	
	static public String executeHttpGet(String str) {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL(str);
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(5000);
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}
	public Handler getHandler(Context context){
		prepareHandler(context);
		return handler;
	}
}
