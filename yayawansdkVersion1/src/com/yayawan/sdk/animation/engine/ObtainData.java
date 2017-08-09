package com.yayawan.sdk.animation.engine;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.HttpUtil;
import com.yayawan.sdk.utils.UrlUtil;

public class ObtainData {
	/**
	 * 激活 激活在开场动画中,激活将获取getGameId:app_id,getSourceId:ads_id,getIMEI:device发送至服务器
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void active(Context context) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("app_id", DeviceUtil.getGameId(context));
		params.put("ads_id", DeviceUtil.getSourceId(context));
		params.put("device", DeviceUtil.getIMEI(context));

		String post = HttpUtil.post(UrlUtil.ACTIVE, params, "UTF-8");
		// {"success":0,"CMCC":"106900608888","UNICOM":"106900608888","TELECOM":"1069033301128","CMD":"qy"}
		JSONObject object = new JSONObject(post);
		if (object.optInt("success") == 0) {
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("CMCC", object.optString("CMCC"));
			edit.putString("UNICOM", object.optString("UNICOM"));
			edit.putString("TELECOM", object.optString("TELECOM"));
			edit.putString("CMD", object.optString("CMD"));
			edit.commit();
		} else {
			throw new Exception("game_id或source_id错误");
		}

	}

	/**
	 * 新版激活
	 * 激活在开场动画中,激活将获取getGameId:app_id,getSourceId:ads_id,getIMEI:device发送至服务器
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean newactive(Context context) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("app_id", DeviceUtil.getGameId(context));
		params.put("ads_id", DeviceUtil.getSourceId(context));
		params.put("device", DeviceUtil.getIMEI(context));

		String post = HttpUtil.post(UrlUtil.ACTIVE, params, "UTF-8");
		// {"success":0,"CMCC":"106900608888","UNICOM":"106900608888","TELECOM":"1069033301128","CMD":"qy"}
		JSONObject object = new JSONObject(post);
		Yayalog.loger("激活信息:"+post);
		if (object.optInt("success") == 0) {
		/*	SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("CMCC", object.optString("CMCC"));
			edit.putString("UNICOM", object.optString("UNICOM"));
			edit.putString("TELECOM", object.optString("TELECOM"));
			edit.putString("CMD", object.optString("CMD"));
			edit.commit();*/

			int login_type = object.optInt("login_type", 0);
			Sputils.putSPint("login_type", login_type, context);
			
		} else {
			throw new Exception("game_id或source_id错误");
		}

		return true;
	}
}
