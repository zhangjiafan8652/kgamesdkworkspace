package com.yayawan.sdk.floatwidget.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.PayLog;
import com.yayawan.sdk.utils.ResourceUtil;
/**
 * 充值记录适配
 * @author wjy
 *
 */
public class RechargeLogAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PayLog> mPayLogs; 
    
    
    class ViewHolder {
        TextView time;
        TextView name;
        TextView money;
        TextView status;
    }
    
    public RechargeLogAdapter(Context context, ArrayList<PayLog> payLogs) {
        super();
        this.mContext = context;
        this.mPayLogs = payLogs;
    }

    public int getCount() {
        
        return mPayLogs.size();
    }

    public Object getItem(int position) {
        return mPayLogs.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "paylog_item"), null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_time"));
            holder.name = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_name"));
            holder.money = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_money"));
            holder.status = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_status"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayLog payLog = mPayLogs.get(position);
        holder.time.setText(payLog.date_time);
        holder.name.setText(payLog.bank_name);
        holder.money.setText(payLog.amount);
        holder.status.setText(payLog.status_msg);
        
        return convertView;
    }

}
