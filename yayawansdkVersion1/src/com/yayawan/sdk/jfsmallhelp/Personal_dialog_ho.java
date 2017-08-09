package com.yayawan.sdk.jfsmallhelp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yayawan.sdk.jfutils.Basedialogview;
import com.yayawan.sdk.jfutils.Myviewpage;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.jfxml.GetAssetsutils;
import com.yayawan.sdk.jfxml.MachineFactory;
import com.yayawan.sdk.utils.DeviceUtil;

/**
 * 个人中心主页面
 * @author zjf
 *
 */
public class Personal_dialog_ho extends Basedialogview{

	private LinearLayout ll_title;
	private LinearLayout ll_mHome;
	private LinearLayout ll_mPersonal;
	private LinearLayout ll_mRecommendation;
	private LinearLayout ll_mGuide;
	private LinearLayout ll_mGift;
	private ArrayList<BaseContentView> views;
	private MyViewpagerAdapter myViewpagerAdapter;
	private int templocation=0;
	private TextView tv_mHome;
	private ImageView iv_mHome;
	private ImageView iv_mPersonal;
	private TextView tv_mPersonal;
	private ImageView iv_mRecommendation;
	private TextView tv_mRecommendation;
	private ImageView iv_mGuide;
	private TextView tv_mGuide;
	private ImageView iv_mGift;
	private TextView tv_mGift;
	private Myviewpage vp_mContentview;
	private PersonalView personalView;

	
	public Personal_dialog_ho(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(final Activity mActivity) {
		
		dialog = new Dialog(mContext);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 900;
		int po_with = 650;

		int height=0;
		int with=0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height=ho_height;
			with=ho_with;
			
		} else if ("portrait".equals(orientation)) {
			height=po_height;
			with=po_with;
		}
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout",2,25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 选项栏
		ll_title = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_title, MATCH_PARENT, 140, mLinearLayout);
		ll_title.setBackgroundColor(Color.parseColor("#f1f1f1"));

		// 首页
		ll_mHome = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mHome, 0, MATCH_PARENT,1, mLinearLayout);
		ll_mHome.setGravity(Gravity_CENTER);
		ll_mHome.setOrientation(LinearLayout.VERTICAL);

		iv_mHome = new ImageView(mActivity);
		machineFactory.MachineView(iv_mHome, 60, 60, mLinearLayout);
		iv_mHome.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_1home.png", mActivity));

		tv_mHome = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mHome, MATCH_PARENT, WRAP_CONTENT, 0,
				"首页", 26, mLinearLayout, 0, 6, 0, 0);
		tv_mHome.setTextColor(Color.parseColor("#666666"));
		tv_mHome.setGravity(Gravity_CENTER);
		
		// TODO
		ll_mHome.addView(iv_mHome);
		ll_mHome.addView(tv_mHome);

		// 个人中心
		ll_mPersonal = new LinearLayout(mActivity);
		machineFactory
				.MachineView(ll_mPersonal, 0, MATCH_PARENT,1, mLinearLayout);
		ll_mPersonal.setGravity(Gravity_CENTER);
		ll_mPersonal.setOrientation(LinearLayout.VERTICAL);

		iv_mPersonal = new ImageView(mActivity);
		machineFactory.MachineView(iv_mPersonal, 60, 60, mLinearLayout);
		iv_mPersonal.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_1personal.png", mActivity));

		tv_mPersonal = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mPersonal, MATCH_PARENT,
				WRAP_CONTENT, 0, "个人中心", 26, mLinearLayout, 0, 6, 0, 0);
		tv_mPersonal.setTextColor(Color.parseColor("#666666"));
		tv_mPersonal.setGravity(Gravity_CENTER);
		
		// TODO
		ll_mPersonal.addView(iv_mPersonal);
		ll_mPersonal.addView(tv_mPersonal);

		// 精品推荐
		ll_mRecommendation = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mRecommendation, 0, MATCH_PARENT,1,
				mLinearLayout);
		ll_mRecommendation.setGravity(Gravity_CENTER);
		ll_mRecommendation.setOrientation(LinearLayout.VERTICAL);

		iv_mRecommendation = new ImageView(mActivity);
		machineFactory.MachineView(iv_mRecommendation, 60, 60, mLinearLayout);
		iv_mRecommendation.setImageBitmap(GetAssetsutils
				.getImageFromAssetsFile("yaya_1recommendation.png", mActivity));

		tv_mRecommendation = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mRecommendation, MATCH_PARENT,
				WRAP_CONTENT, 0, "精品推荐", 26, mLinearLayout, 0, 6, 0, 0);
		tv_mRecommendation.setTextColor(Color.parseColor("#666666"));
		tv_mRecommendation.setGravity(Gravity_CENTER);
		
		// TODO
		ll_mRecommendation.addView(iv_mRecommendation);
		ll_mRecommendation.addView(tv_mRecommendation);

		// 活动攻略
		ll_mGuide = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mGuide, 0, MATCH_PARENT,1, mLinearLayout);
		ll_mGuide.setGravity(Gravity_CENTER);
		ll_mGuide.setOrientation(LinearLayout.VERTICAL);

		iv_mGuide = new ImageView(mActivity);
		machineFactory.MachineView(iv_mGuide, 60, 60, mLinearLayout);
		iv_mGuide.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_1game.png", mActivity));

		tv_mGuide = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mGuide, MATCH_PARENT, WRAP_CONTENT,
				0, "活动攻略", 26, mLinearLayout, 0, 6, 0, 0);
		tv_mGuide.setTextColor(Color.parseColor("#666666"));
		tv_mGuide.setGravity(Gravity_CENTER);
		
		// TODO
		ll_mGuide.addView(iv_mGuide);
		ll_mGuide.addView(tv_mGuide);

		// 礼包
		ll_mGift = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mGift, 0, MATCH_PARENT,1, mLinearLayout);
		ll_mGift.setGravity(Gravity_CENTER);
		ll_mGift.setOrientation(LinearLayout.VERTICAL);

		iv_mGift = new ImageView(mActivity);
		machineFactory.MachineView(iv_mGift, 60, 60, mLinearLayout);
		iv_mGift.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_1gift.png", mActivity));

		tv_mGift = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mGift, MATCH_PARENT, WRAP_CONTENT,
				0, "礼包", 26, mLinearLayout, 0, 6, 0, 0);
		tv_mGift.setTextColor(Color.parseColor("#666666"));
		tv_mGift.setGravity(Gravity_CENTER);
		
		// TODO
		ll_mGift.addView(iv_mGift);
		ll_mGift.addView(tv_mGift);
		
		vp_mContentview = new Myviewpage(mActivity);
		machineFactory.MachineView(vp_mContentview, MATCH_PARENT, MATCH_PARENT, mLinearLayout);
		
		//TODO
		//ll_title.addView(ll_mHome);
		ll_title.addView(ll_mPersonal);
		ll_title.addView(ll_mRecommendation);
		ll_title.addView(ll_mGuide);
		ll_title.addView(ll_mGift);
		
		
		
		
		ll_content.addView(ll_title);
		ll_content.addView(vp_mContentview);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		
		//配置viewpage
		views = new ArrayList<BaseContentView>();
		personalView = new PersonalView(mActivity,this);
		//views.add(new HomeView(mActivity));
		views.add(personalView);
		views.add(new RecommendationView(mActivity));
		views.add(new GuideView(mActivity));
		views.add(new GiftView(mActivity));
		myViewpagerAdapter = new MyViewpagerAdapter();
		// 设置adapter
		vp_mContentview.setAdapter(myViewpagerAdapter);
		vp_mContentview.setCurrentItem(0);
		//vp_mContentview.setScanScroll(false);
		views.get(0).initdata();
		chargeColor(0);
		//设置viewpage的滑动监听
		vp_mContentview.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int location) {
				resetColor(templocation);
				templocation=location;
				chargeColor(location);
				views.get(location).initdata();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		ll_mHome.setClickable(true);
		ll_mHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vp_mContentview.setCurrentItem(0);
			}
		});
		ll_mPersonal.setClickable(true);
		ll_mPersonal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vp_mContentview.setCurrentItem(0);
			}
		});
		ll_mRecommendation.setClickable(true);
		ll_mRecommendation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vp_mContentview.setCurrentItem(1);
			}
		});
		ll_mGuide.setClickable(true);
		ll_mGuide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vp_mContentview.setCurrentItem(2);
			}
		});
		ll_mGift.setClickable(true);
		ll_mGift.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vp_mContentview.setCurrentItem(3);
			}
		});
		
		
		//设置
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Yayalog.loger("我退出了dialogdissmis");
				mActivity.finish();
			}
		});
		
		
		
	}
	
	@Override
	public void logic(Activity mctivity) {
		// TODO Auto-generated method stub
		super.logic(mActivity);
		
		
	}
	
	private void chargeColor(int location) {
		switch (location) {
//		case 0:
//			tv_mHome.setTextColor(Color.parseColor("#f24b6a"));
//			iv_mHome.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
//					"yaya_home.png", mActivity));
//
//			break;
		case 0:
			tv_mPersonal.setTextColor(Color.parseColor("#3dc0d5"));
			iv_mPersonal.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_personal.png", mActivity));
			

			break;
		case 1:
			tv_mRecommendation.setTextColor(Color.parseColor("#66d57a"));
			iv_mRecommendation.setImageBitmap(GetAssetsutils
					.getImageFromAssetsFile("yaya_recommendation.png", mActivity));

			break;
		case 2:
			tv_mGuide.setTextColor(Color.parseColor("#f7d06c"));
			iv_mGuide.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_game.png", mActivity));

			break;
		case 3:
			tv_mGift.setTextColor(Color.parseColor("#e78959"));
			iv_mGift.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_gift.png", mActivity));

			break;

		default:
			break;
		}
	}
	private void resetColor(int location) {
		switch (location) {
//		case 0:
//			tv_mHome.setTextColor(Color.parseColor("#666666"));
//			iv_mHome.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
//					"yaya_1home.png", mActivity));
//
//			break;
		case 0:
			tv_mPersonal.setTextColor(Color.parseColor("#666666"));
			iv_mPersonal.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_1personal.png", mActivity));
			

			break;
		case 1:
			tv_mRecommendation.setTextColor(Color.parseColor("#666666"));
			iv_mRecommendation.setImageBitmap(GetAssetsutils
					.getImageFromAssetsFile("yaya_1recommendation.png", mActivity));

			break;
		case 2:
			tv_mGuide.setTextColor(Color.parseColor("#666666"));
			iv_mGuide.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_1game.png", mActivity));

			break;
		case 3:
			
			tv_mGift.setTextColor(Color.parseColor("#666666"));
			iv_mGift.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_1gift.png", mActivity));

			break;

		default:
			break;
		}
	}

	class MyViewpagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			BaseContentView pager = (BaseContentView) views.get(position);
			View rootView = pager.getRootview();
			container.addView(rootView);
			// 初始化数据
			// pager.initdata();

			return rootView;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	public void onResume() {
		
			views.get(vp_mContentview.getCurrentItem()).onResume();
		
		
		
	}

	
	
}
