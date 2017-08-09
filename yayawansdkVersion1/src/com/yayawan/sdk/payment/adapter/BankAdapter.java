package com.yayawan.sdk.payment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.BankInfo;
import com.yayawan.sdk.utils.ResourceUtil;
/**
 * 银行适配
 * @author wjy
 *
 */
public class BankAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<BankInfo> mBankList;
    
    class ViewHolder{
        
        TextView mBank;
        TextView mNumber;
    }
    
    public BankAdapter(Context context, ArrayList<BankInfo> bankList) {
        super();
        this.mContext = context;
        this.mBankList = bankList;
    }

    public int getCount() {
        return mBankList.size();
    }

    public Object getItem(int position) {
        return mBankList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "bank_item"), null);
            holder.mBank = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_bank"));
            holder.mNumber = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_number"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankInfo bank = mBankList.get(position);
        
        holder.mBank.setText(bank.bankname);
        holder.mNumber.setText("(" +bank.lastno + ")");
        
        
        return convertView;
    }

}
