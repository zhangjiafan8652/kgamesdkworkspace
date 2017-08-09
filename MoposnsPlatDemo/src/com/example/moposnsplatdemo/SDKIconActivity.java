package com.example.moposnsplatdemo;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.skymobi.moposnsplatsdk.plugins.account.ISnsAccountServerSupport;
import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.skymobi.snssdknetwork.outfunction.keeplibs.ISNSIconListener;

public class SDKIconActivity extends Activity {
	private final static String TAG = "DEBUG";
	private ISnsAccountServerSupport _sdkSupport = null;
//	private SurfaceView surfaceView = null;
//	private SurfaceHolder surfaceHolder = null;

	private ISnsAccountServerSupport getSupport() {
		if (_sdkSupport == null) {
			_sdkSupport = SnsAccountServerSupport.getInstance();
		}
		return _sdkSupport;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_activity_icon);
//		surfaceView = (SurfaceView) findViewById(R.id.surfaceview_test);
//		surfaceHolder = surfaceView.getHolder();
//		initSurfaceHolder();
		showSDKMainIcon();
		
	}
//
//	private void initSurfaceHolder() {
//		surfaceView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				Log.d(TAG, "onTouch surfaceView");
//				return false;
//			}
//		});
//		surfaceHolder.addCallback(new Callback() {
//
//			@Override
//			public void surfaceDestroyed(SurfaceHolder arg0) {
//				Log.d(TAG, "surfaceDestroyed");
//
//			}
//
//			@Override
//			public void surfaceCreated(SurfaceHolder arg0) {
//				Log.d(TAG, "surfaceCreated");
//				// Timer timer = new Timer();
//				// MyTimerTask timerTask = new MyTimerTask();
//				// timer.schedule(timerTask, 5, 5);
//			}
//
//			@Override
//			public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
//					int arg3) {
//				Log.d(TAG, "surfaceChanged");
//			}
//		});
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "dispatchTouchEvent activity");
//		return super.dispatchTouchEvent(ev);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		Log.d(TAG, "onTouchEvent activity");
//		return super.onTouchEvent(event);
//	}
//
//	private class MyTimerTask extends TimerTask {
//		@Override
//		public void run() {
//			Log.d(TAG, "run");
//			drawSurface();
//		}
//	}
//
//	private void drawSurface() {
//		Log.d(TAG, "drawSurface");
//		if (surfaceHolder != null) {
//			SimpleDraw();
//		}
//	}
//
//	void SimpleDraw() {
//		Log.d(TAG, "SimpleDraw");
//		Canvas canvas = surfaceHolder.lockCanvas(new Rect(0, 0,
//				getWindowManager().getDefaultDisplay().getWidth(),
//				getWindowManager().getDefaultDisplay().getHeight()));// 关键:获取画布
//
//		Paint paint = new Paint();
//		paint.setColor(Color.GREEN);// 画笔为绿色
//		paint.setStrokeWidth(2);// 设置画笔粗细
//		paint.setStrokeWidth((float) 3.0); // 线宽
//		paint.setStyle(Style.FILL_AND_STROKE);
//		canvas.drawRect(0, 0,
//				getWindowManager().getDefaultDisplay().getWidth(),
//				getWindowManager().getDefaultDisplay().getHeight(), paint);
//		surfaceHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
//	}

	private void showSDKMainIcon() {
		getSupport().showsSDKSNSIcon(this, new ISNSIconListener() {

			@Override
			public void onIconEvent(boolean isCatched) {
				Log.d(TAG, "showSDKMainIcon,onIconEvent:" + isCatched);
				if (isCatched) {
					// 有需要的情况下,屏蔽游戏的触屏事件处理.
					// 社区ICON正在处理触屏事件.
				} else {
					// 有需要的情况下,找开游戏的触屏事件处理.
					// 社区ICON当前不处理触屏事件.
				}
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
}
