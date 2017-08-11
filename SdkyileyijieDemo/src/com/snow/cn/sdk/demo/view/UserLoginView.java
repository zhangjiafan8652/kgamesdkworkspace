/**
 * Copyright (c) 2013 DuoKu Inc.
 */

package com.snow.cn.sdk.demo.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.snow.cn.sdk.demo.utils.LoginHelper;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;
import com.snow.cn.sdk.demo.R;

public class UserLoginView extends BaseView implements OnClickListener, SFOnlineLoginListener {
	
	private View mAccountLoginView;
	static LoginHelper helper = null;
	
	private Activity getActivity(){
		return (Activity)mContext;
	}
	
	public UserLoginView(Context context) {
		super(context);
		mContext = context;
		setLoginListener();
	}
	
	public UserLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setLoginListener();
		
	}
	
	public void initView(Context context) {
		mAccountLoginView = findViewById(R.id.btn_account_login);
		mAccountLoginView.setOnClickListener(this);
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initView(mContext);
	}

	/* setLoginListener方法为登录的监听函数需要在调用login函数之前调用
	 * public static void setLoginListener(Activity context, SFOnlineLoginListener listener)
	 *  @param context   上下文Activity
	 *  @param listener  登陆回调
	*/
	private void setLoginListener() {
		SFOnlineHelper.setLoginListener(this.getActivity(), this);
		helper = LoginHelper.instance();
	}

	
	@Override
	public void onClick(View v) {
		if (v == mAccountLoginView) {
			doLogin();
		} 
	}
	/*易接登陆接口*/
	private void doLogin(){
	
		SFOnlineHelper.login(UserLoginView.this.getActivity(), "Login");
	}

	/**易接登出接口
	 * 若需要切换账户功能，请参考以下代码，在登出后调用登陆接口
	 * */
	@Override
	public void onLogout(Object customParams) {
		Log.e("ganga", "demo onLogout:" + customParams);
		Toast.makeText(this.getActivity(), "账户登出", Toast.LENGTH_SHORT).show();
		if(helper != null){
			helper.setOnlineUser(null);
			helper.setLogin(false);
			helper.getHandler(getActivity()).postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Log.e("ganga", "logout in postAtTime");
					SFOnlineHelper.login(getActivity(), "Login");	
				}
			}, 200);
		}
		goLoginView();
	}
/*
 * 登陆成功的回调函数
 * */
	@Override
	public void onLoginSuccess(SFOnlineUser user, Object customParams) {
		if(helper != null){
			helper.setOnlineUser(user);
		}
		LoginCheck(user);
		Log.e("ganga", "demo onLoginSuccess" );		
	}
	/**
	 * 登陆失败的回调函数
	 * */
	@Override
	public void onLoginFailed(String reason, Object customParams) {
		Log.e("ganga", "demo onLoginFailed:"+reason+", "+customParams );		
		Toast.makeText(this.getActivity(), "账户登陆失败", Toast.LENGTH_SHORT).show();
	}
	/**
     *  LoginCheck
     *  从服务器端验证用户是否登陆
     * @param user 登陆账户
     */
	public void LoginCheck(final SFOnlineUser user) {
        if(helper == null){
        	Toast.makeText(this.getActivity(), "Error, helper为null", Toast.LENGTH_SHORT).show();
        	return;
        }
		if (helper.isDebug()) {
			helper.setLogin(true);
			goChargeView();
			return;
		}
		Log.e("ganga", "LoginCheck user:"+user.toString());
		new Thread(new Runnable() {
			@Override
			public void run() { 
				try {
					String url = LoginHelper.CP_LOGIN_CHECK_URL + createLoginURL();
					if (url == null)
						return;

 					String result = LoginHelper.executeHttpGet(url);
 					Log.e("ganga", "LoginCheck result:"+result);
					if (result == null || !result.equalsIgnoreCase("SUCCESS")) {
						if(helper != null){
							helper.setLogin(false);
						}
						LoginHelper.showMessage("未登录", UserLoginView.this.getActivity());
					} else {
						if(helper != null){
							helper.setLogin(true);
						}
					  /* 部分渠道如UC渠道，要对游戏人物数据进行统计，而且为接入规范，调用时间：在游戏角色登录成功后调用
					   *  public static void setRoleData(Context context, String roleId，
					   *  	String roleName, String roleLevel, String zoneId, String zoneName)
					   *  
					   *  @param context   上下文Activity
					   *  @param roleId    角色唯一标识
					   *  @param roleName  角色名
					   *  @param roleLevel 角色等级
					   *  @param zoneId    区域唯一标识
					   *  @param zoneName  区域名称
					   *  */
						SFOnlineHelper.setRoleData(UserLoginView.this.getActivity(), "1",
								"猎人", "100", "1", "阿狸一区");
						
						JSONObject roleInfo = new JSONObject();
						roleInfo.put("roleId", "1");         //当前登录的玩家角色ID，必须为数字
						roleInfo.put("roleName", "猎人");  //当前登录的玩家角色名，不能为空，不能为null
						roleInfo.put("roleLevel", "100");   //当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
						roleInfo.put("zoneId", "1");       //当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
						roleInfo.put("zoneName", "阿狸一区");//当前登录的游戏区服名称，不能为空，不能为null
						roleInfo.put("balance", "0");   //用户游戏币余额，必须为数字，若无，传入0
						roleInfo.put("vip", "1");            //当前用户VIP等级，必须为数字，若无，传入1
						roleInfo.put("partyName", "无帮派");//当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
						roleInfo.put("roleCTime", "21322222");	 //单位为秒，创建角色的时间
						roleInfo.put("roleLevelMTime", "54456556");	//单位为秒，角色等级变化时间
						
						SFOnlineHelper.setData(mContext,"createrole",roleInfo.toString()); //  创建新角色时调用       必接
						SFOnlineHelper.setData(mContext,"levelup",roleInfo.toString());    //  玩家升级角色时调用     必接
						SFOnlineHelper.setData(mContext,"enterServer",roleInfo.toString());//  选择服务器进入时调用   必接
						
						LoginHelper.showMessage("已验证成功登录", UserLoginView.this.getActivity());
						UserLoginView.this.post(new Runnable() {
							
							@Override
							public void run() {
								goChargeView();								
							}
						});
					}
				} catch (Exception e) {
					Log.e("ganga", "LoginCheck ERROR:"+e.toString());
				}
			}
		}).start();
	}
    private void goChargeView(){
    	mChangeListener.ChangeViewListener(CHARGE_FRAGMENT_VIEW);
    }
    
    private void goLoginView(){
    	mChangeListener.ChangeViewListener(LOGIN_FRAGMENT_VIEW);
    }

	private String createLoginURL() throws UnsupportedEncodingException {
		if (helper == null || helper.getOnlineUser()  == null) {
			Toast.makeText(this.getActivity(), "未登录", Toast.LENGTH_SHORT).show();
			return null;
		}
		SFOnlineUser user = helper.getOnlineUser();
		StringBuilder builder = new StringBuilder();
		builder.append("?app=");
		builder.append(URLEncoder.encode(user.getProductCode(), "utf-8"));
		builder.append("&sdk=");
		builder.append(URLEncoder.encode(user.getChannelId(), "utf-8"));
		builder.append("&uin=");
		builder.append(URLEncoder.encode(user.getChannelUserId(), "utf-8"));
		builder.append("&sess=");
		builder.append(URLEncoder.encode(user.getToken(), "utf-8"));
		return builder.toString();
	}
}
