package com.yayawan.sdk.jfutils;

import com.yayawan.implyy.YYcontants;
import com.yayawan.sdk.jflogin.ViewConstants;

import android.util.Log;

public class Yayalog {

	
	
	
	public static void  loger(String msg){
		
		//DeviceUtil.isDebug(paramContext)
		if (YYcontants.ISDEBUG) {
			Log.e("Yayalog", msg);
		}
			
		
		
	}
	
	public static void setCanlog(boolean msg){
		//canlog=msg;
	}
}
