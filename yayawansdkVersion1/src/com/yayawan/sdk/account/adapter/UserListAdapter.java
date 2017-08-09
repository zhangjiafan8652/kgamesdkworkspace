package com.yayawan.sdk.account.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.utils.ResourceUtil;

public class UserListAdapter extends BaseAdapter {

    private ArrayList<String> mNames;
    
    private Context mContext;
   
    class ViewHolder{
        
        TextView mName;
        ImageView mDelete;
    }
    
    public UserListAdapter(Context context, ArrayList<String> names) {
        super();
        this.mContext = context;
        this.mNames = names;
    }

    public int getCount() {
        return mNames.size();
    }

    public Object getItem(int position) {
        return mNames.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "userlist_item"), null);
            holder.mName = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_username"));
            holder.mDelete = (ImageView) convertView.findViewById(ResourceUtil.getId(mContext, "iv_delete"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String name = mNames.get(position);
        holder.mName.setText(name);
        
        holder.mDelete.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                mNames.remove(name);
                UserDao.getInstance(mContext).removeUser(name);
                UserListAdapter.this.notifyDataSetChanged();
            }
        });
        
        return convertView;
    }

}
