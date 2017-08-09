package com.yayawan.impl;

import android.app.Activity;
import android.widget.Toast;

import com.muzhiwan.sdk.core.MzwSdkController;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.proxy.YYWUserManager;

public class UserManagerImpl implements YYWUserManager {

	@Override
	public void manager(Activity paramActivity) {

		Toast.makeText(paramActivity, "个人中心", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		
		MzwSdkController.getInstance().doLogout();
	}

	@Override
	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(Activity paramActivity, YYWExitCallback callback) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		callback.onExit();
	}

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub

	}

}
