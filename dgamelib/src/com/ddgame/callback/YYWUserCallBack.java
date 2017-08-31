package com.ddgame.callback;

import com.ddgame.domain.YYWUser;

public interface YYWUserCallBack {

    public abstract void onLoginSuccess(YYWUser paramUser, Object paramObject);

    public abstract void onLoginFailed(String paramString, Object paramObject);

    public abstract void onLogout(Object paramObject);

    public  void onCancel();
    
    
}
