package com.yayawan.sdk.floatwidget.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.StrategyInfo;
import com.yayawan.sdk.utils.ResourceUtil;

/**
 * 攻略适配器
 * 
 * @author Administrator
 * 
 */
public class StrategyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<StrategyInfo> mStrategyList;
	private WindowManager mWindow;
	class ViewHolder{
		TextView strategyName;
		TextView strategyTime;
	}
	
	public StrategyAdapter(Context mContext, ArrayList<StrategyInfo> mGiftList) {
		super();
		this.mContext = mContext;
		this.mStrategyList = mGiftList;
		mWindow = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	}

	public int getCount() {
		if (mStrategyList != null && mStrategyList.size() > 0) {
			return mStrategyList.size();
		}
		return 0;
	}

	public Object getItem(int position) {
		if (mStrategyList != null && mStrategyList.size() > 0) {
			return mStrategyList.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		StrategyInfo strategyInfo = mStrategyList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "strategy_item"), null);
			int width = mWindow.getDefaultDisplay().getWidth() * 2/3;
			holder.strategyName = (TextView) convertView
					.findViewById(ResourceUtil.getId(mContext, "tv_strategy_name"));
			holder.strategyName.setMaxWidth(width);
			holder.strategyTime = (TextView) convertView
					.findViewById(ResourceUtil.getId(mContext, "tv_strategy_time"));
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.strategyName.setText(strategyInfo.name);
		holder.strategyTime.setText(strategyInfo.time);

		return convertView;
	}

}
