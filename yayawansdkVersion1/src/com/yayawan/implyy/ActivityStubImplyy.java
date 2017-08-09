package com.yayawan.implyy;

import android.app.Activity;
import android.content.Intent;

import com.yayawan.proxy.YYWActivityStub;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.utils.Handle;

public class ActivityStubImplyy implements YYWActivityStub {

    @Override
    public void applicationInit(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(Activity paramActivity) {
        // TODO Auto-generated method stub
    	Yayalog.loger("oncreate");
    //	Handle.active_handler(paramActivity);
    	YayaWan.initSdk(paramActivity);
    	
    }

    @Override
    public void onStop(Activity paramActivity) {
        // TODO Auto-generated method stub
    	Yayalog.loger("onstop");
    }

    @Override
    public void onResume(Activity paramActivity) {
        YayaWan.init(paramActivity);
        Yayalog.loger("onresume");
    }

    @Override
    public void onPause(Activity paramActivity) {
        YayaWan.stop(paramActivity);
        Yayalog.loger("onpause");
    }

    @Override
    public void onRestart(Activity paramActivity) {
        // TODO Auto-generated method stub
    	Yayalog.loger("onrestart");
    }

    @Override
    public void onDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub
    	Yayalog.loger("ondestroy");
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
	public void initSdk(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

}
