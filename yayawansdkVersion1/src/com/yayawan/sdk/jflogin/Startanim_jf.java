package com.yayawan.sdk.jflogin;

import java.io.File;

import com.yayawan.sdk.account.dao.DataTransfermationDao;
import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.account.db.OldSDBHelper;
import com.yayawan.sdk.animation.engine.ObtainData;
import com.yayawan.sdk.callback.YayawanStartAnimationCallback;
import com.yayawan.sdk.jfxml.GetAssetsutils;
import com.yayawan.sdk.jfxml.Startanima_xml;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.utils.DeviceUtil;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.Theme;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Startanim_jf extends BaseView {

	private static final int ANIMSTOP = 10; // 动画播放完成

	private YayawanStartAnimationCallback mStartAnimationCallback; // 动画回调

	private SharedPreferences mSp;

	private static final String ACTIVE = "active";

	protected static final int ANIMERROR = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANIMSTOP:
				// mAnim.stop();
				Editor edit = mSp.edit();
				edit.putBoolean(ACTIVE, true);
				edit.commit();

				mActivity.finish();
				onSuccess();
				break;
			case ANIMERROR:
				// mAnim.stop();
				
				mActivity.finish();
				
				break;

			default:
				break;
			}
		}
	};

	private Startanima_xml mThisview;
	private ImageView iv_loading;

	private ImageView iv_text;

	public Startanim_jf(Activity mContext) {
		super(mContext);
	}

	@Override
	public View initRootview() {
		mThisview = new Startanima_xml(mActivity);

		return mThisview.initViewxml();
	}

	@Override
	public void initView() {

		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		mStartAnimationCallback = YayaWan.mStartAnimationCallback;
		
		//把主题换成全屏的
		Theme theme = mActivity.getTheme();
		theme.applyStyle(R.style.Theme_Holo_Light, true);
		
		iv_loading = mThisview.getIv_loading();
		iv_text = mThisview.getIv_text();
		
		iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_ani.png", mActivity));
		iv_text.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_logotext.png", mActivity));

		// 数据库添加一列
		UserDao.getInstance(mActivity).upDateclume();

		// 设置横竖屏tupian
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			// iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
			// "yaya_start_ho.jpg", mActivity));

		} else if ("portrait".equals(orientation)) {
			// iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
			// "yaya_start_po.jpg", mActivity));

		}

		/**
		 * 创建线程,给服务器发送数据激活,并延迟3秒,让动画播放完成
		 */
		new Thread() {
			@Override
			public void run() {
				try {
					long start = System.currentTimeMillis();
					boolean flag = mSp.getBoolean(ACTIVE, false);
					if (!flag) {
						ObtainData.active(mContext);
						initData();
					}
					long end = System.currentTimeMillis();

					if ((end - start) < 4000) {
						Thread.sleep(4000 - (end - start));
					}

					mHandler.sendEmptyMessage(ANIMSTOP);

				} catch (Exception e) {
					e.printStackTrace();
					onError();
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(mActivity, "请检查网络", Toast.LENGTH_LONG).show();
							
						}
					});
					mHandler.sendEmptyMessageDelayed(ANIMERROR,1000);
				}
			}
		}.start();

	}

	@Override
	public void logic() {

	}

	/**
	 * 迁移老版本sdk用户数据
	 */
	private void initData() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File dbFolder = new File(OldSDBHelper.DB_DIR);
			if (dbFolder.exists()) {
				// 将原数据库中的数据写入新数据库
				UserDao.getInstance(mActivity)
						.writeUsers(
								DataTransfermationDao.getInstance(mActivity)
										.getUsers());

			}
		}

	}

	public void onSuccess() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onSuccess();
		}
		mStartAnimationCallback = null;
	}

	public void onError() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onError();
		}
		mStartAnimationCallback = null;
	}

	public void onCancel() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onCancel();
		}
	}

	/**
	 * 监听用户点击返回键,
	 */
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) {
	 * 
	 * onCancel(); return true; } return super.onKeyDown(keyCode, event); }
	 */

	@Override
	public boolean onkeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			onCancel();
			return true;
		}
		return super.onkeyDown(keyCode, event);
	}

	public void onClick(View v) {

	}

}
