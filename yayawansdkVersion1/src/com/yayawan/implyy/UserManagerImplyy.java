package com.yayawan.implyy;

import android.app.Activity;
import android.widget.Toast;

import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWUserManager;
import com.yayawan.sdk.callback.YayaWanCallback;
import com.yayawan.sdk.main.YayaWan;

public class UserManagerImplyy implements YYWUserManager {

    @Override
    public void manager(Activity paramActivity) {
        // TODO Auto-generated method stub
        YayaWan.accountManager(paramActivity);
    }

    @Override
    public void login(Activity paramActivity, String paramString,
            Object paramObject) {
        // TODO Auto-generated method stub

    }

    @Override
    public void logout(Activity paramActivity, String paramString,
            Object paramObject) {
    	YayaWan.stop(paramActivity);
        if (YYWMain.mUserCallBack != null) {
            YYWMain.mUserCallBack.onLogout("logout");
        }
    }

    @Override
    public void setUserListener(Activity paramActivity,
            YYWUserManagerCallBack paramXMUserListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exit(final Activity paramActivity, final YYWExitCallback callback) {
        // TODO Auto-generated method stub
        //Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
    	paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				YayaWan.Exitgame(paramActivity, new YayaWanCallback() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						callback.onExit();
					}
				});
			}
		});
    	
    	
    }

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub
		YayaWan.setRoleData(arg0, YYWMain.mRole.getRoleId(), YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(), YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
	}

}
