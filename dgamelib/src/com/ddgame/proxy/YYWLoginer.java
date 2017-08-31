package com.ddgame.proxy;

import com.ddgame.callback.YYWUserCallBack;

import android.app.Activity;

public interface YYWLoginer {

    public abstract void login(Activity paramActivity,
            YYWUserCallBack userCallBack, String paramString);

    public abstract void relogin(Activity paramActivity,
            YYWUserCallBack userCallBack, String paramString);

}
