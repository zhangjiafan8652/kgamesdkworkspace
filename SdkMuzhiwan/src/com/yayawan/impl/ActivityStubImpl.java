package com.yayawan.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwInitCallback;

import com.yayawan.proxy.YYWActivityStub;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Handle;

public class ActivityStubImpl implements YYWActivityStub {

	@Override
	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}
	private ProgressDialog pd;
	@Override
	public void onCreate(final Activity paramActivity) {
		// TODO Auto-generated method stub
		MzwSdkController.getInstance().doUpdateAuto(MzwSdkController.UPDATE_ISFORCED_YES);
		Handle.active_handler(paramActivity);
		
		pd = new ProgressDialog(paramActivity);
		pd.setTitle("提示");
		pd.setMessage("正在初始化资源");
		pd.show();
		int orientation=0;
		if (DeviceUtil.isLandscape(paramActivity)) {
			orientation=MzwSdkController.ORIENTATION_HORIZONTAL;
		}else {
			orientation=MzwSdkController.ORIENTATION_VERTICAL;
			
		}
		MzwSdkController.getInstance().init(paramActivity,
				orientation, new MzwInitCallback() {
					

					@Override
					public void onResult(final int code, String arg1) {
						// TODO Auto-generated method stub
						paramActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {

								if (!paramActivity.isFinishing() && pd.isShowing()) {
									pd.dismiss();
								}
								if (code == 1) {
									System.out.println("拇指玩初始化成功");
									Myconstant.ISINIT=true;
								} else {
									System.out.println("拇指玩初始化失败");
									Myconstant.ISINIT=false;
								}
							}
						});
					}

				
				});

	}

	@Override
	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		//MzwSdkController.getInstance().setError(paramActivity);
	}

	@Override
	public void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy(Activity paramActivity) {
	 MzwSdkController.getInstance().destory();
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

}
