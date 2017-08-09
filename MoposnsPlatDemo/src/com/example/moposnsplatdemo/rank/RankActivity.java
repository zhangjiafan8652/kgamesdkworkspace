package com.example.moposnsplatdemo.rank;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.moposnsplatdemo.R;
import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.skymobi.snssdknetwork.outfunction.keeplibs.GetRankingListener;
import com.skymobi.snssdknetwork.outfunction.keeplibs.IRankErrorCode;
import com.skymobi.snssdknetwork.outfunction.keeplibs.RankingInfoResponse;

public class RankActivity extends Activity {

	private ListView mListView;
	private RankListAdapter adapter;
	private TextView emptyView;
	private View loadingView;

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		setContentView(R.layout.rank_activity);

		init();
		showLoading();
		SnsAccountServerSupport.getInstance().getRank(getCurrentPackageName(), new GetRankingListener() {

			@Override
			public void onSuccessByServerCallBack(
					RankingInfoResponse rankingInfo) {
				final ArrayList<RankItem> list = new ArrayList<RankItem>();
				int listSize = 0;

				Log.i("DEBUG", "getRank success:");

				if (rankingInfo.getPosition() >= rankingInfo
						.getRankingItemList().size()) {
					listSize = rankingInfo.getRankingItemList().size() - 1;
					for (int i = 0; i < listSize; i++) {
						final RankItem item = new RankItem(i + 1, rankingInfo
								.getRankingItemList().get(i).getName(),
								rankingInfo.getRankingItemList().get(i)
										.getScore(), rankingInfo
										.getRankingItemList().get(i)
										.getIsSelf());
						list.add(item);
					}

					final RankItem item = new RankItem(rankingInfo
							.getPosition(), rankingInfo.getRankingItemList()
							.get(listSize).getName(), rankingInfo
							.getRankingItemList().get(listSize).getScore(),
							rankingInfo.getRankingItemList().get(listSize)
									.getIsSelf());
					list.add(item);
				} else {
					listSize = rankingInfo.getRankingItemList().size();
					for (int i = 0; i < listSize; i++) {
						final RankItem item = new RankItem(i + 1, rankingInfo
								.getRankingItemList().get(i).getName(),
								rankingInfo.getRankingItemList().get(i)
										.getScore(), rankingInfo
										.getRankingItemList().get(i)
										.getIsSelf());
						list.add(item);
					}
				}

				if (list.size() > 0) {
					final Message msg = new Message();
					msg.obj = list;
					handler.sendMessage(msg);
				} else {
					sendErrorMsg(IRankErrorCode.ERROR_HAS_NOT_DATA);
				}

			}

			@Override
			public void onFailedByServerCallBack(final int errorCode) {
				sendErrorMsg(errorCode);
			}
		});
	}

	private void sendErrorMsg(final int code) {
		final Message msg = new Message();
		msg.obj = null;
		msg.arg1 = code;
		handler.sendMessage(msg);
	}

	private String getCurrentPackageName() {
		String packageName = this.getPackageName();
		// 测试代码
		// packageName = "com.skymoposns.mtetris";
		return packageName;
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.rank_list);
		adapter = new RankListAdapter(this);
		mListView.setAdapter(adapter);
		emptyView = (TextView) findViewById(R.id.rank_empty);
		loadingView = findViewById(R.id.rank_loading);
	}

	private void showError(final int errorCode) {
		if (emptyView != null) {
			emptyView.setText("出错:" + errorCode);
		}
		emptyView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
	}

	private void showLoading() {

		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
	}

	private void showList(final ArrayList<RankItem> rankList) {
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		adapter.setListData(rankList);
	}

	private final Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (null == msg.obj) {
				showError(msg.arg1);
			} else {
				showList((ArrayList<RankItem>) msg.obj);
			}
		}
	};
}
