package com.yayawan.sdk.floatwidget.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.GiftInfo;
import com.yayawan.sdk.utils.ResourceUtil;
/**
 * 礼包适配器
 * @author Administrator
 *
 */
public class GiftAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<GiftInfo> mGiftList;
	
	class ViewHolder{
		TextView giftName;
	}
	
	public GiftAdapter(Context mContext, ArrayList<GiftInfo> mGiftList) {
		super();
		this.mContext = mContext;
		this.mGiftList = mGiftList;
	}

	public int getCount() {
		if (mGiftList != null && mGiftList.size() > 0) {
			return mGiftList.size();
		}
		return 0;
	}

	public Object getItem(int position) {
		if (mGiftList != null && mGiftList.size() > 0) {
			return mGiftList.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		GiftInfo giftInfo = mGiftList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "gift_item"), null);
			holder.giftName = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_gift_name"));
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.giftName.setText(giftInfo.name);
		
		return convertView;
	}

}
