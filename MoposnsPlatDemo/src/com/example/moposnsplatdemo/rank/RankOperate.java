package com.example.moposnsplatdemo.rank;

import android.content.Context;
import android.content.Intent;

import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.skymobi.snssdknetwork.outfunction.keeplibs.UpdateScoreListener;

public class RankOperate {
	public static void ShowRankActivity(final Context context) {
		//RankBiz.getRank();
		context.startActivity(new Intent(context, RankActivity.class));
	}

	public static void commitScore(final Long score1,final Long score2,final Long score3, final Context context, final UpdateScoreListener listener ) {
		SnsAccountServerSupport.getInstance().updateRank(score1,score2,score3,context, listener);
	}
}
