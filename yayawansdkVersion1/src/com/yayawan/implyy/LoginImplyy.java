package com.yayawan.implyy;

import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWLoginer;
import com.yayawan.sdk.callback.YayaWanUserCallback;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class LoginImplyy implements YYWLoginer {

	
	public static boolean ISLOGIN=false;
    @Override
    public  void login(final Activity paramActivity, YYWUserCallBack userCallBack, String paramString) {

    	//下载文件
    	
    	new Handler(Looper.getMainLooper()).post(new Runnable() {

    		
    		
    		
            @Override
            public void run() {
            		Yayalog.loger("yy登陆");
            	
            	
			        YayaWan.login(paramActivity, new YayaWanUserCallback() {

			            @Override
			            public void onSuccess(User user, int arg1) {
			                if (YYWMain.mUserCallBack != null) {

			                    YYWUser yywUser = new YYWUser();

			                    yywUser.uid = user.uid + "";
			                    yywUser.icon = user.icon;
			                    yywUser.body = user.body;
			                    yywUser.lasttime = user.lasttime;
			                    yywUser.money = user.money;
			                    yywUser.nick = user.nick;
			                    yywUser.password = user.password;
			                    yywUser.phoneActive = user.phoneActive;
			                    yywUser.success = user.success;
			                    yywUser.token = user.token;
			                    yywUser.userName = user.userName;
			                    YYWMain.mUser=yywUser;
			                    ISLOGIN=true;
			                    YYWMain.mUserCallBack.onLoginSuccess(yywUser, "success");
			                }
			            }

			            @Override
			            public void onLogout() {
			            	YayaWan.stop(paramActivity);
			                if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onLogout("logout");
			                }
			                
			                
			            }

			            @Override
			            public void onError(int arg0) {
			            	
			                if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onLoginFailed("failed", "");
			                }
			            }

			            @Override
			            public void onCancel() {
			                // TODO Auto-generated method stub
			            	if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onCancel();
			                }
			            }
			        });

            	}

    	});

    }

    @Override
    public void relogin(Activity paramActivity, YYWUserCallBack userCallBack,
            String paramString) {
    		Yayalog.loger("yaya代理sdk注销");
    		YayaWan.logout(paramActivity);
    		
    		userCallBack.onLogout(null);
    		
    }

}
