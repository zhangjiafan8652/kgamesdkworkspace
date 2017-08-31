package com.ddgame.impl;

import android.app.Activity;

import com.ddgame.callback.YYWExitCallback;
import com.ddgame.callback.YYWUserManagerCallBack;
import com.ddgame.main.YYWMain;
import com.ddgame.proxy.YYWUserManager;
import com.ddgame.sdk.bean.User;
import com.ddgame.sdk.callback.KgameSdkCallback;
import com.ddgame.sdkmain.DgameSdk;
import com.ddgame.utils.Yayalog;

public class UserManagerImpl implements YYWUserManager {

	@Override
	public void manager(Activity paramActivity) {
		// TODO Auto-generated method stub
		DgameSdk.accountManager(paramActivity);
	}

	@Override
	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		//KgameSdk.stop(paramActivity);

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
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

					@Override
					public void onSuccess(User paramUser, int paramInt) {
						// TODO Auto-generated method stub
						callback.onExit();
					}

					@Override
					public void onError(int paramInt) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onLogout() {
						// TODO Auto-generated method stub

					}

				});
			}
		});

	}

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub
		DgameSdk.setRoleData(arg0, YYWMain.mRole.getRoleId(),
				YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
				YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
	}

	// 3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,
			String ext) {
		
		Yayalog.loger("调用了impl中的usermanagerimpl中的setdata方法");
		
		if (Integer.parseInt(ext)==1) {
			DgameSdk.setRoleData(paramActivity, YYWMain.mRole.getRoleId(),
					YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
					YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
		}
	
	}

}
