package com.yayawan.proxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.yayawan.implyy.YYcontants;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.utils.DeviceUtil;

public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;

	public static Yibuhttputils yibuhttputils;

	public static GameApitest getGameApitestInstants(Activity mactivity) {
		if (mGameapitest != null) {
			mActivity = mactivity;

			return mGameapitest;
		} else {
			mActivity = mactivity;

			mGameapitest = new GameApitest();
			yibuhttputils = new Yibuhttputils() {

				@Override
				public void sucee(String str) {
					// TODO Auto-generated method stub
					Yayalog.loger(str);
				}

				@Override
				public void faile(String err, String rescode) {
					// TODO Auto-generated method stub

				}
			};
			return mGameapitest;
		}
	}

	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

		if (YYcontants.ISDEBUG) {
			String url = "http://danjiyou.duapp.com/Api/Gametest/save?yayawan_game_id="
					+ DeviceUtil.getGameId(mActivity) + "&type=" + type;

			yibuhttputils.runHttp(url, "", Yibuhttputils.GETMETHOD, "");
		}

	}

	public void sendTest(String type, String value) {

		if (YYcontants.ISDEBUG) {
			String gamevalue = value.replace(",", "___").replace("=", "_")
					.replace(" ", "");

			String url = "http://danjiyou.duapp.com/Api/Gametest/save?yayawan_game_id="
					+ DeviceUtil.getGameId(mActivity)
					+ "&type="
					+ type
					+ "&value=" + gamevalue;

			Yayalog.loger(url);
			yibuhttputils.runHttp(url, "", Yibuhttputils.GETMETHOD, "");
//http://www.then9.com/forum.php?mod=post&action=reply&fid=40&tid=32118&extra=page=1&replysubmit=yes&infloat=yes&handlekey=fastpost&inajax=1			
		}

	}

}
