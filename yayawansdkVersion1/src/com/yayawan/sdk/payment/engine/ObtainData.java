package com.yayawan.sdk.payment.engine;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.domain.BillResult;
import com.yayawan.sdk.domain.ConfirmPay;
import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.PayResult;
import com.yayawan.sdk.domain.Question;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.utils.CryptoUtil;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.HttpUtil;
import com.yayawan.sdk.utils.RSACoder;
import com.yayawan.sdk.utils.UrlUtil;

public class ObtainData {

    /**
     * 第一次支付方法(加密)
     * 
     * @param context
     * @param order
     * @param user
     * @throws Exception
     */
    public static PayResult payment(Context context, Order order, User user,
            int paytype) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
      
       
        
        int issinglepay = Sputils.getSPint("issinglepay", 0,
				context);
        String post;
        if (issinglepay==1) {
        	//单机
        	params.put("data", encryptpaysingle(context, order, user, paytype));
        	post = HttpUtil.post(UrlUtil.SINGLE_PAY, params, "UTF-8");
        	
		}else {
			 params.put("data", encryptpay(context, order, user, paytype));
			 post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
			// System.out.println(params.toString());
			
		}
      
      
        //System.out.println("++++++++++++++"+post);
        return JSONParser.parsePayResult(post);
    }
    
    
    
    /**
     * 第一次微信支付方法(加密)
     * 
     * @param context
     * @param order
     * @param user
     * @throws Exception
     */
    public static PayResult wxpayment(Context context, Order order, User user,
            int paytype) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
      
       
        
        int issinglepay = Sputils.getSPint("issinglepay", 0,
				context);
        String post;
        if (issinglepay==1) {
        	//单机
        	 params.put("data", encryptpaysingle(context, order, user, paytype));
        	 
        	post = HttpUtil.post(UrlUtil.SINGLE_PAY, params, "UTF-8");
        	Yayalog.loger("botanin单机："+post);
        	
		}else {
			
			 params.put("data", encryptpay(context, order, user, paytype));
			// Yayalog.loger("微信支付参数："+encryptpay(context, order, user, paytype));
			 post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
			// System.out.println(post.toString());
			 Yayalog.loger("微信支付返回："+post);
		}
      
      
       // System.out.println("++++++++++++++"+post);
        return JSONParser.parsePayResult(post);
    }
    
    /**
     * 第一次支付方法(加密)
     * 
     * @param context
     * @param order
     * @param user
     * @throws Exception
     */
    public static PayResult yayawanWeixinPayment(Context context, Order order, User user,
            int paytype) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
      
       
        
        int issinglepay = Sputils.getSPint("issinglepay", 0,
				context);
        String post;
        if (issinglepay==1) {
        	//单机
        	params.put("data", encryptpaysingle(context, order, user, paytype));
        	post = HttpUtil.post(UrlUtil.SINGLE_PAY, params, "UTF-8");
        	
		}else {
			 params.put("data", encryptpay(context, order, user, paytype));
			 post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
			 System.out.println("请求url："+UrlUtil.PAYMENT+"请求参数："+params.toString());
			
		}
      
      
        Yayalog.loger("yayawanWeixinPayment++++++++++++++"+post);
        return JSONParser.parsePayResult(post);
    }
    
    /**
     * 银联支付返回
     * 
     * @param context
     * @param order
     * @param user
     * @throws Exception
     */
    public static String payment_yinlian(Context context, Order order, User user,
            int paytype) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        
        int issinglepay = Sputils.getSPint("issinglepay", 0,
				context);
        String post;
        if (issinglepay==1) {
        	//单机支付
        	 params.put("data", encryptpaysingle(context, order, user, paytype));
        	post = HttpUtil.post(UrlUtil.SINGLE_PAY, params, "UTF-8");
		}else {
			params.put("data", encryptpay(context, order, user, paytype));
			 post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
		}
       // String post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
        
        
        //System.out.println("++++++++++++++"+post);
        return post;
    }

    /**
     * 加密支付信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptpay(Context context, Order order, User user,
            int paytype) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer
                .append("app_id=")
                .append(DeviceUtil.getGameId(context))
                .append("&uid=")
                .append(user.uid)
                .append("&username=")
                .append(user.userName)
                .append("&token=")
                .append(user.token)
                .append("&mentid=")
                .append(AgentApp.mentid + "")
                .append("&money=")
                .append(order.money)
                .append("&goods=")
                .append(order.goods != null ? URLEncoder.encode(order.goods)
                        : "").append("&serverid=").append(order.serverId)
                .append("&orderid=").append(order.orderId).append("&ext=")
                .append(order.ext != null ? URLEncoder.encode(order.ext) : "")
                .append("&paytype=").append(paytype).toString();
        Yayalog.loger(info.toString());
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < (int) Math.ceil(((double) (info.length()) / 117)); i++) {
            if (((i + 1) * 117) <= info.length()) {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117,
                                (i + 1) * 117).getBytes())));
            } else {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117).getBytes())));
            }
        }
        return hex.toString();

    }

    
    /**
     * 单机加密支付信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptpaysingle(Context context, Order order, User user,
            int paytype) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        
        String info = infoBuffer
                .append("app_id=")
                .append(DeviceUtil.getGameId(context))
                .append("&uid=")
                .append(YYWMain.mUser.uid)
                .append("&username=")
                .append(YYWMain.mUser.userName)
                .append("&token=")
                .append(""+YYWMain.mUser.token)
                .append("&mentid=")
                .append(AgentApp.mentid + "")
                .append("&money=")
                .append(order.money)
                .append("&goods=")
                .append(order.goods != null ? URLEncoder.encode(order.goods)
                        : "").append("&serverid=").append(order.serverId)
                .append("&orderid=").append(order.orderId).append("&ext=")
                .append(order.ext != null ? URLEncoder.encode(order.ext) : "")
                .append("&paytype=").append(paytype).toString();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < (int) Math.ceil(((double) (info.length()) / 117)); i++) {
            if (((i + 1) * 117) <= info.length()) {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117,
                                (i + 1) * 117).getBytes())));
            } else {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117).getBytes())));
            }
        }
        return hex.toString();

    }

    /**
     * 信用卡第一次支付方法(加密)
     * 
     * @param context
     * @param order
     * @param user
     * @param mPhoneNumText
     * @throws Exception
     */
    public static PayResult creditCardPayment(Context context, Order order,
            User user, String phoneNumText, int paytype) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        
        int issinglepay = Sputils.getSPint("issinglepay", 0,
				context);
        String post;
        if (issinglepay==1) {
        	//单机支付
        	params.put("data",
        			encryptCreditPaysingle(context, order, user, phoneNumText, paytype));
	       post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
		}else {
			params.put("data",
	                encryptCreditPay(context, order, user, phoneNumText, paytype));
	       post = HttpUtil.post(UrlUtil.PAYMENT, params, "UTF-8");
	        
		}
        
        return JSONParser.parsePayResult(post);
    }

    /**
     * 加密信用卡支付信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptCreditPay(Context context, Order order,
            User user, String phoneNumText, int paytype) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer
                .append("app_id=")
                .append(DeviceUtil.getGameId(context))
                .append("&uid=")
                .append(user.uid)
                .append("&token=")
                .append(user.token)
                .append("&username=")
                .append(user.userName)
                .append("&mentid=")
                .append(AgentApp.mentid + "")
                .append("&money=")
                .append(order.money)
                .append("&goods=")
                .append(order.goods != null ? URLEncoder.encode(order.goods)
                        : "").append("&serverid=").append(order.serverId)
                .append("&orderid=").append(order.orderId).append("&ext=")
                .append(order.ext != null ? URLEncoder.encode(order.ext) : "")
                .append("&paytype=").append(paytype).append("&mobile=")
                .append(phoneNumText).toString();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < (int) Math.ceil(((double) (info.length()) / 117)); i++) {
            if (((i + 1) * 117) <= info.length()) {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117,
                                (i + 1) * 117).getBytes())));
            } else {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117).getBytes())));
            }
        }
        return hex.toString();

    }
    
    /**
     * 单机加密信用卡支付信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptCreditPaysingle(Context context, Order order,
            User user, String phoneNumText, int paytype) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer
                .append("app_id=")
                .append(DeviceUtil.getGameId(context))
                .append("&uid=")
                .append(YYWMain.mUser.uid)
                .append("&token=")
                .append(YYWMain.mUser.token)
                .append("&username=")
                .append(YYWMain.mUser.userName)
                .append("&mentid=")
                .append(AgentApp.mentid + "")
                .append("&money=")
                .append(order.money)
                .append("&goods=")
                .append(order.goods != null ? URLEncoder.encode(order.goods)
                        : "").append("&serverid=").append(order.serverId)
                .append("&orderid=").append(order.orderId).append("&ext=")
                .append(order.ext != null ? URLEncoder.encode(order.ext) : "")
                .append("&paytype=").append(paytype).append("&mobile=")
                .append(phoneNumText).toString();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < (int) Math.ceil(((double) (info.length()) / 117)); i++) {
            if (((i + 1) * 117) <= info.length()) {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117,
                                (i + 1) * 117).getBytes())));
            } else {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117).getBytes())));
            }
        }
        return hex.toString();

    }

    /**
     * 加密参数信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptParams(HashMap<String, String> params)
            throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        Set<Entry<String, String>> entrySet = params.entrySet();
        for (Entry<String, String> entry : entrySet) {
        	Yayalog.loger(entry.getKey() + "="+entry.getValue());
            infoBuffer.append("&" + entry.getKey() + "=").append(
                    entry.getValue());
        }
        String info = infoBuffer.toString().substring(1);
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < (int) Math.ceil(((double) (info.length()) / 117)); i++) {
            if (((i + 1) * 117) <= info.length()) {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117,
                                (i + 1) * 117).getBytes())));
            } else {
                hex.append(CryptoUtil.encodeHexString(RSACoder
                        .encryptByPublicKey(info.substring(i * 117).getBytes())));
            }
        }
        return hex.toString();

    }

    /**
     * 确认支付
     * 
     * @param payResult
     * @throws Exception
     */
    public static ConfirmPay confirmPay(PayResult payResult) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("data", encryptParams(payResult.params));
        String post = "";
        if ("post".equals(payResult.method)) {
            post = HttpUtil.post(payResult.action, params, "UTF-8");
        }

        return JSONParser.parseConfirmPay(post);
    }

    /**
     * 获取用户余额信息
     * 
     * @param payResult
     * @throws Exception
     */
    public static User getMoneyInfo(Context context, User user)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        String param = "uid=" + user.uid + "&token=" + user.token + "&app_id="
                + DeviceUtil.getGameId(context);
        params.put("data", CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(param.getBytes())));
        String post = HttpUtil.post(UrlUtil.MONEYINFO, params, "UTF-8");
        return JSONParser.parseMoneyInfo(post, user);
    }

    /**
     * 余额支付
     * 
     * @param payResult
     * @throws Exception
     */
    public static ConfirmPay yayapay(Context context, Order order, User user)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("data", encryptYayapay(context, order, user));
        String post = HttpUtil.post(UrlUtil.YAYAPAY, params, "UTF-8");
        return JSONParser.parseConfirmPay(post);
    }

    /**
     * 加密余额支付信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptYayapay(Context context, Order order, User user)
            throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer.append("app_id=")
                .append(DeviceUtil.getGameId(context)).append("&uid=")
                .append(user.uid).append("&token=").append(user.token)
                .append("&mentid=").append(4).append("&money=")
                .append(order.money).append("&serverid=")
                .append(order.serverId).append("&orderid=")
                .append(order.orderId).append("&goods=").append(order.goods)
                .append("&ext=").append(order.ext).toString();
        String hexString = CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(info.getBytes()));
        return hexString;

    }

    /**
     * 获取帮助中心信息
     * 
     * @param payResult
     * @throws Exception
     */
    public static ArrayList<Question> getHelp(Context context) throws Exception {
        String get = HttpUtil.get(UrlUtil.HELP, "UTF-8");
        return JSONParser.parseHelp(get);
    }

    /**
     * 获取订单查询结果信息
     * 
     * @param context
     * @param user
     * @param order
     * @return
     * @throws Exception
     */
    public static BillResult getBillResult(Context context, User user,
            Order order) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        
        
        
        int issinglepay = Sputils.getSPint("issinglepay", 0,
				context);
        String post;
        if (issinglepay==1) {
        	//单机支付
        	   params.put("uid", YYWMain.mUser.uid + "");
               params.put("app_id", DeviceUtil.getGameId(context));
               params.put("token","");
               params.put("id", order.id);
               String params2 = encryptParams(params);
               params.clear();
               params.put("data", params2); 
               post = HttpUtil.post(UrlUtil.BILL_STATUS_SINGLE, params, "UTF-8");
               
		}else {
			   params.put("uid", user.uid + "");
		        params.put("app_id", DeviceUtil.getGameId(context));
		        params.put("token", user.token);
		        params.put("id", order.id);
		        String params2 = encryptParams(params);
		        params.clear();
		        params.put("data", params2); 
		        post = HttpUtil.post(UrlUtil.BILL_STATUS, params, "UTF-8");
		}
        
     
        
      
        
      
        Yayalog.loger(post);
        BillResult bill = JSONParser.parseBillResult(post);

        return bill;
    }

    /**
     * 初始化支付方式
     */
    public static void initPayMethod() {

    }
}
