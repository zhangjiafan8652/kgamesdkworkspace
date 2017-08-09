package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Handle;
import com.yayawan.proxy.YYWActivityStub;


public class ActivityStubImpl implements YYWActivityStub {

	
	
	public static Activity mactivity;

    @Override
    public void applicationInit(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(Activity paramActivity) {
        // TODO Auto-generated method stub
    	GuangdiantongUtils.guangDiantongInit(paramActivity.getApplicationContext());
    	//广点通激活
		//GuangdiantongUtils.guangDiantongActi(paramActivity);
    	KgameSdk.initSdk(paramActivity);
    	Handle.active_handler(paramActivity);
    }

    @Override
    public void onStop(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume(Activity paramActivity) {
    	
    
    	
        KgameSdk.init(paramActivity);
    }

    @Override
    public void onPause(Activity paramActivity) {
        KgameSdk.stop(paramActivity);
    }

    @Override
    public void onRestart(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void applicationDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(Activity paramActivity, int paramInt1,
            int paramInt2, Intent paramIntent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNewIntent(Intent paramIntent) {
        // TODO Auto-generated method stub

    }

	@Override
	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(Activity arg0) {
		// TODO Auto-generated method stub
		
	}
	
	 

}
