package com.yayawan.sdk.floatwidget.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.Faq;
import com.yayawan.sdk.utils.ResourceUtil;
/**
 * 问题反馈
 * @author wjy
 *
 */
public class FaqAdapter extends BaseAdapter {

    private Context mContext;

    private LinkedList<Faq> mFaqs;

    
    class ViewHolder {

        TextView mContent;


    }
    class ViewHolder2 {

        TextView mContent;


    }
    public FaqAdapter(Context context, LinkedList<Faq> faqs) {
        super();
        this.mContext = context;
        this.mFaqs = faqs;
    }

    public int getCount() {
        return mFaqs.size();
    }

    public Object getItem(int position) {
        return mFaqs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    
    
    @Override
    public int getItemViewType(int position) {
        return mFaqs.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;
        Faq faq = mFaqs.get(position);
        int type = getItemViewType(position); 
        if (convertView == null) {
            if (type == 1){
                convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "server_item"), null);
                holder = new ViewHolder();
                holder.mContent = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_message"));
                convertView.setTag(holder);
            }else if (type == 2) {
                convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "person_item"), null);
                holder2 = new ViewHolder2();
                holder2.mContent = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_message"));
                convertView.setTag(holder2);
            }
        }else {
            if (type == 1) {
                holder = (ViewHolder) convertView.getTag();
            }else if(type == 2){
                holder2 = (ViewHolder2) convertView.getTag();
            }
        }
        if (type == 1) {
            holder.mContent.setText(faq.content);
        }else if(type == 2){
            holder2.mContent.setText(faq.content);
        }
        
        return convertView;
    }

}
