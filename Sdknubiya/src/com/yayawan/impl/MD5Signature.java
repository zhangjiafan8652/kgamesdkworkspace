package com.yayawan.impl;



import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;


public class MD5Signature {
    private static final String TAG = MD5Signature.class.getSimpleName();

    public static String sign(String content, String key) throws Exception {

        String tosign = (content == null ? "" : content) + key;

        try {
            return MD5Util.getMD5CodeHex(getContentBytes(tosign, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("MD5签名[content = " + content + "; charset = utf-8"
                    + "]发生异常!", e);
        }

    }

    protected static byte[] getContentBytes(String content, String charset)
            throws UnsupportedEncodingException {
        if (isEmpty(charset)) {
            return content.getBytes();
        }

        return content.getBytes(charset);
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    /**
     * 生成签名，此过程建议在服务端调用
     */
    public static String doSign(String orderId, String productName, String number, String amount, String uid, String timeStamp) {
        String sign = null;
        try {
            Map<String, String> parameterMap = new HashMap<String, String>();
            parameterMap.put("app_id", String.valueOf(AppConfig.APP_ID));
            parameterMap.put("uid", uid);
            parameterMap.put("cp_order_id", orderId);
            parameterMap.put("amount", amount);
            parameterMap.put("product_name", productName);
            parameterMap.put("product_des", "牛币可交易");
            parameterMap.put("number", number);
            parameterMap.put("data_timestamp", timeStamp);
            String verifyData = ParameterUtil.getSignData(parameterMap);

            sign = MD5Signature.sign(verifyData, ":" + AppConfig.APP_ID + ":" + AppConfig.APP_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

}