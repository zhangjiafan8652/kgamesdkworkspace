package com.example.moposnsplatdemo;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.skymobi.moposnsplatsdk.pay.MoposnsPlatPayServer;

public class PayResultHandle extends Handler {
	public static final String STRING_MSG_CODE = "msg_code";
	public static final String STRING_PAY_STATUS = "pay_status";
	public static final String STRING_CHARGE_STATUS = "3rdpay_status";

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		System.out.println("demo======" + msg.what);
		if (msg.what == MoposnsPlatPayServer.MSG_WHAT_TO_APP) {
			// 形式：key-value
			String retInfo = (String) msg.obj;
			Map<String, String> map = new HashMap<String, String>();
			String[] keyValues = retInfo.split("&|=");
			for (int i = 0; i < keyValues.length; i = i + 2) {
				map.put(keyValues[i], keyValues[i + 1]);
			}

			int msgCode = Integer.parseInt(map.get(STRING_MSG_CODE));
			if (msgCode == 101) {
				/**
				 * 返回错误 retInfo格式：msg_code=101&error_code=*** error_code取值参考文档
				 */
				return;
			}

			if (map.get(STRING_PAY_STATUS) != null) {
				/**
				 * 短信付费结果返回 retInfo失败格式：msg_code=100&pay_status=101&pay_price=0&
				 * errorcode=***
				 * retInfo成功格式：msg_code=100&pay_status=102&pay_price=***
				 * 具体参数意义参考文档
				 */
			} else if (map.get(STRING_CHARGE_STATUS) != null) {
				/**
				 * 第三方付费结果返回
				 * retInfo格式：msg_code=100&3rdpay_status=***&pay_price=*
				 * **&skyChargeId=*** 具体参数意义参考文档
				 */
			}
		}
	}
}
