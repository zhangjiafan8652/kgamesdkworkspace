package com.example.moposnsplatdemo.rank;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.moposnsplatdemo.R;

public class RankListAdapter extends BaseAdapter {

	private final ArrayList<RankItem> list = new ArrayList<RankItem>();// 数据源
	private final Context context;// 上下文

	public RankListAdapter(final Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return null == list ? 0 : list.size();
	}

	@Override
	public RankItem getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RankItem item = getItem(position);
		final ViewHolder holder;
		convertView = LayoutInflater.from(context).inflate(
				R.layout.rank_list_item, null);
		holder = new ViewHolder();
		holder.rank = (TextView) convertView.findViewById(R.id.list_item_rank);
		holder.nickname = (TextView) convertView
				.findViewById(R.id.list_item_name);
		holder.score = (TextView) convertView
				.findViewById(R.id.list_item_score);

		convertView.setTag(holder);
		holder.rank.setText(Integer.toString(item.rank));
		if (null == item.nickname) {
			holder.nickname.setText("");
		} else {
			holder.nickname.setText(item.nickname);
		}
		holder.score.setText(Long.toString(item.score));
		if (item.getIsSelf()) {
			convertView.setBackgroundColor(Color.RED);
		} else {
			convertView.setBackgroundResource(R.drawable.list_item_selected);
		}
		return convertView;
	}

	class ViewHolder {
		TextView rank;
		TextView nickname;
		TextView score;
	}

	public void setListData(final ArrayList<RankItem> rankList) {
		if (null == rankList) {
			return;
		}
		this.list.clear();
		this.list.addAll(rankList);
		notifyDataSetChanged();
	}
}
