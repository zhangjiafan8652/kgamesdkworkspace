package com.yayawan.sdk.payment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yayawan.sdk.domain.PayMethod;
import com.yayawan.sdk.utils.ResourceUtil;

public class PayMethodAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<PayMethod> mMethods;

    public PayMethodAdapter(Context context, ArrayList<PayMethod> methods) {
        super();
        this.mContext = context;
        this.mMethods = methods;
    }

    public class ViewHolder {

        ImageView mIcon;
    }

    @Override
    public int getCount() {
        return mMethods.size();
    }

    @Override
    public Object getItem(int position) {
        return mMethods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "paymethod_item"), null);
            holder.mIcon = (ImageView) convertView.findViewById(ResourceUtil.getId(mContext, "iv_icon"));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayMethod payMethod = mMethods.get(position);
     //   Picasso.with(mContext).load(payMethod.iconId).into(holder.mIcon);

        return convertView;
    }

}
