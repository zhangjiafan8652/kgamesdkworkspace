package com.ddgame.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.ddgame.callback.YYWUserCallBack;
import com.ddgame.domain.YYWUser;
import com.ddgame.main.YYWMain;
import com.ddgame.proxy.YYWLoginer;
import com.ddgame.sdk.bean.User;
import com.ddgame.sdk.callback.KgameSdkUserCallback;
import com.ddgame.sdkmain.DgameSdk;

public class LoginImpl implements YYWLoginer {

    @Override
    public void login(final Activity paramActivity, YYWUserCallBack userCallBack, String paramString) {

    	new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {


            	DgameSdk.login(paramActivity, new KgameSdkUserCallback() {

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
			                    YYWMain.mUserCallBack.onLoginSuccess(yywUser, "success");
			                }
			            }

			            @Override
			            public void onLogout() {
			            	//YayaWan.stop(paramActivity);
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

    }

}
