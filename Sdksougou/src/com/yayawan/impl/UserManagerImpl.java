package com.yayawan.impl;

import android.app.Activity;
import android.widget.Toast;

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

	}

	@Override
	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		System.out.println("来这里了");

		YaYawanconstants.exit(paramActivity, callback);

	}

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub
		YaYawanconstants.setRoleData(arg0);
	}

	@Override
	public void setData(Activity arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, String arg7) {
		// TODO Auto-generated method stub

	}

}
