package com.yayawan.impl;

import android.app.Activity;
import android.widget.Toast;

import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.proxy.YYWUserManager;
import com.yayawan.sdk.callback.YayaWanCallback;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.utils.DeviceUtil;

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
        // TODO Auto-generated method stub

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
    	Yayalog.loger("进来了丫丫玩退出");
		if (DeviceUtil.getGameInfo(paramActivity, "addexit").equals("yes")) {
			paramActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					YayaWan.Exitgame(paramActivity, new YayaWanCallback() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							//exitCallBack.onExit();
							Yayalog.loger("进来了添加退出框的退出");
							callback.onExit();
							//paramActivity.finish();
						}
					});
				}
			});
			
		}else {
			
			//Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
			Yayalog.loger("进来了不添加退出框的退出");
			callback.onExit();
			//paramActivity.finish();
		}
    }

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub

	}

}
