package com.yayawan.impl;



import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import com.gionee.gamesdk.AccountInfo;
import com.gionee.gamesdk.GamePlatform.LoginListener;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWLoginer;
import com.yayawan.utils.Handle;
import com.yayawan.utils.HttpUtil;
import com.yayawan.utils.JSONUtil;

public class LoginImpl implements YYWLoginer {

    @Override
    public void login(final Activity paramActivity, YYWUserCallBack userCallBack, String paramString) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
            	System.err.println("login sttart");

            	ActivityStubImpl.mGamePlatform.loginAccount(111, true, new LoginListener() {

        			@Override
        			public void onSuccess(AccountInfo accountInfo) {
        				// 登录成功，处理自己的业务。

        				// 获取playerId
        				//String playerId = accountInfo.mPlayerId;

        				// 获取amigoToken
        				//String amigoToken = accountInfo.mToken;


        				YYWMain.mUser = new YYWUser();
                        YYWMain.mUser.uid = accountInfo.mPlayerId;
                        YYWMain.mUser.userName = accountInfo.mPlayerId;
                        YYWMain.mUser.nick = accountInfo.mNickName;
                        YYWMain.mUser.token = JSONUtil.formatToken( paramActivity, accountInfo.mToken, YYWMain.mUser.userName);

        				YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
                        Handle.login_handler(paramActivity, YYWMain.mUser.uid, YYWMain.mUser.userName);
        			}

        			@Override
        			public void onError(Exception e) {
        				YYWMain.mUserCallBack.onLoginFailed("", "");
        			}

        			@Override
        			public void onCancel() {
        				YYWMain.mUserCallBack.onLoginFailed("", "");
        			}
        		});


            }
        });

    }


    @Override
    public void relogin(Activity paramActivity, YYWUserCallBack userCallBack, String paramString) {

        System.err.println("relogin");
    }

}
