package com.yayawan.sdk.floatwidget.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.PayLog;
import com.yayawan.sdk.utils.ResourceUtil;

/**
 * 消费记录适配
 * 
 * @author wjy
 * 
 */
public class PayLogAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<PayLog> mPayLogs;

    private WindowManager mWindowManager;

    class ViewHolder {
        TextView time;

        TextView name;

        TextView money;

        TextView status;
    }

    public PayLogAdapter(Context context, ArrayList<PayLog> payLogs) {
        super();
        this.mContext = context;
        this.mPayLogs = payLogs;
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public int getCount() {
        if (mPayLogs == null) {
            return 0;
        }
        return mPayLogs.size();
    }

    public Object getItem(int position) {
        if (mPayLogs == null) {
            return null;
        }
        return mPayLogs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    ResourceUtil.getLayoutId(mContext, "paylog_item"), null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(ResourceUtil
                    .getId(mContext, "tv_time"));
            holder.name = (TextView) convertView.findViewById(ResourceUtil
                    .getId(mContext, "tv_name"));
            holder.money = (TextView) convertView.findViewById(ResourceUtil
                    .getId(mContext, "tv_money"));
            holder.status = (TextView) convertView.findViewById(ResourceUtil
                    .getId(mContext, "tv_status"));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayLog payLog = mPayLogs.get(position);
        int width = mWindowManager.getDefaultDisplay().getWidth() / 4;
        holder.time.getLayoutParams().width = width;
        holder.name.getLayoutParams().width = width;
        holder.money.getLayoutParams().width = width;
        holder.status.getLayoutParams().width = width;

        holder.time.setText(payLog.date_time);
        holder.name.setText(payLog.goods);
        holder.money.setText(payLog.amount);
        holder.status.setText(payLog.status_msg);

        return convertView;
    }

}
