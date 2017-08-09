package com.yayawan.impl;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.kkgame.sdk.utils.Base64;
import com.kkgame.sdk.utils.MD5;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;



public class GuangdiantongUtils {

	
	
	
	/**
	 * 激活
	 * @param mcontext
	 */
	public static void guangDiantongActi(Context mcontext){
		String sPstring = Sputils.getSPstring("MOBILEAPP_ACTIVITE", "no", mcontext);
		if (sPstring.equals("no")) {
			getGuangdiantong(mcontext, "MOBILEAPP_ACTIVITE");
		}
		
		
	}
	
	/**
	 * 注册激活
	 * @param mcontext
	 */
	public static void guangDiantongRegister(Context mcontext){
		String sPstring = Sputils.getSPstring("MOBILEAPP_REGISTER", "no", mcontext);
		if (sPstring.equals("no")) {
			getGuangdiantong(mcontext, "MOBILEAPP_REGISTER");
		}
	}
	
	
	/**
	 * 上报激活到广点通
	 * @param conv_type
	 * @return
	 */
	public static void getGuangdiantong(final Context mcontext,final String conv_type){
		
		String gameInfo = DeviceUtil.getGameInfo(mcontext, "guangdiantong");
		if (!gameInfo.equals("yes")) {
			return;
		}
		long startPaintLogoTime=System.currentTimeMillis()/1000;
		System.out.println(startPaintLogoTime+"");
		String qqappid=DeviceUtil.getGameInfo(mcontext, "qqAppId");
		String muid= pingjieMuid(DeviceUtil.getIMEI(mcontext));
		String sign_key=DeviceUtil.getGameInfo(mcontext, "sign_key") ;
		String advertiser_id=DeviceUtil.getGameInfo(mcontext, "advertiser_id");
		
		String pingjieUrl = pingjieUrl(qqappid,muid, startPaintLogoTime+"", null,sign_key, conv_type, "ANDROID", advertiser_id, DeviceUtil.getGameInfo(mcontext, "encrypt_key"));
		//return pingjieUrl;
		Yibuhttputils yibuhttputils = new Yibuhttputils() {
			
			@Override
			public void sucee(String str) {
				// TODO Auto-generated method stub
				System.out.println("上报数据返回结果"+str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					int int1 = jsonObject.getInt("ret");
					if (int1==0) {
						Sputils.putSPstring(conv_type, "yes", mcontext);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void faile(String err, String rescode) {
				// TODO Auto-generated method stub
				System.out.println("上报数据失败返回结果"+err);
				
			}
		};
		yibuhttputils.runHttp(pingjieUrl, "", "GET", "");
		
	}
	
	
	/**
	 * 
	 * @param appid
	 *            应用id
	 * @param muid
	 *            IME的md5值
	 * @param conv_time
	 *            发生转化的时间
	 * @param client_ip
	 *            IP可以为空
	 * @param sign_key
	 *            平台提供的sign_key
	 * @param conv_type
	 *            上报什么数据
	 *            激活
	* 	MOBILEAPP_ACTIVITE
	*	注册
	*	MOBILEAPP_REGISTER
	*	加入购物车
	*	MOBILEAPP_ADDTOCART
	*	付费行为
	*	MOBILEAPP_COST
	 * @param apptype  填入 ANDROID
	 * 
	 * @param advertiser_id  广告主标识
	 * 
	 * @param encrypt_key  平台提供的encrypt_key
	 * @return 
	 */
	public static String pingjieUrl(String appid, String muid,
			String conv_time, String client_ip, String sign_key,
			String conv_type, String apptype, String advertiser_id,String encrypt_key) {
		String query_string;
		
		if (client_ip != null) {
			query_string = "muid=" + muid + "&conv_time=" + conv_time
					+ "&client_ip=" + client_ip;
		} else {
			query_string = "muid=" + muid + "&conv_time=" + conv_time;
		}

		String page = "http://t.gdt.qq.com/conv/app/" + appid + "/conv?"
				+ query_string;

		String encode_page = java.net.URLEncoder.encode(page);
		System.out.println("encode_page++++" + encode_page);

		String property = sign_key + "&GET&" + encode_page;
		System.out.println("property++++" + property);

		String md5_property = "123";
		try {
			md5_property = MD5.MD5(property);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("md5_property++++" + md5_property);

		String base_data = query_string + "&sign=" + md5_property;
		String en_base_data = xorEncryption(base_data, encrypt_key);
		System.out.println("base_data++++" + en_base_data);

		String finalurl = "http://t.gdt.qq.com/conv/app/" + appid + "/conv?v="
				+ java.net.URLEncoder.encode(en_base_data) + "&conv_type="
				+ conv_type + "&app_type=" + apptype + "&advertiser_id="
				+ advertiser_id;

		System.out.println("finalurl++++" + finalurl);
		return finalurl;
	}

	public static String xorEncryption(String info, String key) {
		byte[] result = new byte[info.length()];
		if (TextUtils.isEmpty(info) || TextUtils.isEmpty(key)) {
			return null;
		}
		for (int i = 0, j = 0; i < info.length(); ++i) {
			result[i] = (byte) (info.charAt(i) ^ key.charAt(j));
			j = (++j) % (key.length());
		}
		return Base64.encode(result);
	}

	public static String pingjieMuid(String IMEI) {
		String md5 = "123";
		try {
			md5 = MD5.MD5(IMEI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("muid++++" + md5);
		return md5.toLowerCase();
	}

}
