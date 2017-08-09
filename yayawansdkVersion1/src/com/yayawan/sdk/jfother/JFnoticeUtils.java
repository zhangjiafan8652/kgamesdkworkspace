package com.yayawan.sdk.jfother;

import android.app.Activity;

import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.proxy.Yibuhttputils;
import com.yayawan.sdk.jflogin.Announcement_dialog;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.utils.UrlUtil;

public class JFnoticeUtils {

	/**
	 * 获取公告信息，如果有公告则弹出公告，如果无公告就啥事都没有，一定要在主线程运行
	 * @param mActicity
	 */
	public void getNotice(final Activity mActicity){

		Yayalog.loger("获取公告："+UrlUtil.NOTICE);
		try {
			Yibuhttputils yibuhttputils = new Yibuhttputils() {
				
				@Override
				public void sucee(final String responseInfo) {
					// TODO Auto-generated method stub
					Yayalog.loger("获取公告返回："+responseInfo);
					// TODO Auto-generated method stub
					if (responseInfo.contains("error_code\":404")) {
						
					}else{
						mActicity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								new Announcement_dialog(mActicity, responseInfo).dialogShow();
							}
						});
						
					}
				}
				
				@Override
				public void faile(String err, String rescode) {
					// TODO Auto-generated method stub
					
				}
			};
			String pingjie="union_id="+com.yayawan.utils.DeviceUtil.getUnionid(mActicity)+"&game_id="+com.yayawan.utils.DeviceUtil.getGameId(mActicity);
			yibuhttputils.runHttp(UrlUtil.NOTICE+"/?"+pingjie, "", Yibuhttputils.GETMETHOD, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Yayalog.loger("获取公告发生异常："+e.toString());
			//e.printStackTrace();
		}

	}
}
