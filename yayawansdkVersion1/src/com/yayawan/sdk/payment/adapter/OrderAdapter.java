package com.yayawan.sdk.payment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.utils.ResourceUtil;
/**
 * 历史订单适配
 * @author wjy
 *
 */
public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Order> mOrderList;
    
    public OrderAdapter(Context context, ArrayList<Order> orderList) {
        super();
        this.mContext = context;
        this.mOrderList = orderList;
    }

    public int getCount() {
        if (mOrderList == null || mOrderList.size() == 0) {
            return 0;
        }
        return mOrderList.size();
    }

    public Object getItem(int position) {
        if (mOrderList == null || mOrderList.size() == 0) {
            return null;
        }
        return mOrderList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView mTime;
        TextView mOrderid;
        TextView mStatus;
        TextView mMentid;
        TextView mMoney;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(ResourceUtil.getLayoutId(mContext, "order_item"), null);
            
            holder.mTime = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_order_time"));
            holder.mOrderid = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_order_orderid"));
            holder.mStatus = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_order_status"));
            holder.mMentid = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_order_mentid"));
            holder.mMoney = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_order_money"));
            
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Order order = mOrderList.get(position);
        
        holder.mTime.setText(order.time);
        holder.mOrderid.setText(order.orderId);
        holder.mStatus.setText(getStatus(order.status));
        holder.mMentid.setText(getMentid(order.mentId));
        holder.mMoney.setText((order.money / 100) + ""); 
        return convertView;
    }

    private String getStatus(int status) {
        switch (status) {
        case 0:
            return "等待付款";
        case 1:
            return "等待游戏到账";
        case 2:
            return "充值成功";
         default:
             return "";
        }
    }
    
    
    private String getMentid(int mentid) {
        switch (mentid) {
        case 1:
            return "支付宝";
        case 11:
            return "易宝神州行";
        case 12:
            return "易宝联通卡";
        case 15:
            return "易宝电信卡";
        case 6:
            return "信用卡";
        case 13:
            return "易宝盛大点卡";
        case 14:
            return "易宝征途卡";
        case 16:
            return "骏网一卡通";
         default:
             return "";
        }
    }
}
