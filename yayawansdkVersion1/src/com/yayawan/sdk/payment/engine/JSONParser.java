package com.yayawan.sdk.payment.engine;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yayawan.sdk.domain.BankInfo;
import com.yayawan.sdk.domain.BillResult;
import com.yayawan.sdk.domain.ConfirmPay;
import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.PayResult;
import com.yayawan.sdk.domain.Question;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Yayalog;

public class JSONParser {

    /**
     * 解析第一次支付结果
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static PayResult parsePayResult(String post) throws Exception {

       PayResult result = new PayResult();

        JSONObject jsonObject = new JSONObject(post);
        result.success = jsonObject.getInt("success");
        if (result.success == 0) {
            result.method = jsonObject.getString("method");
            result.action = jsonObject.getString("action");
            JSONArray jsonArray = jsonObject.getJSONArray("post");
            int length = jsonArray.length();
            result.params = new HashMap<String, String>();
            for (int i = 0; i < length; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                result.params.put(object.getString("name"),
                        object.getString("value"));
            }
        } else if (result.success == 1) {
            result.error_code = jsonObject.getInt("error_code");
            result.error_msg = jsonObject.getString("error_msg");
        }

        System.out.println("返回处理json的数据"+post.toString());
        
        return result;
    }

    /**
     * 解析第二次确认支付结果
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static ConfirmPay parseConfirmPay(String post) throws Exception {
        ConfirmPay confirmPay = new ConfirmPay();
        JSONObject jsonObject = new JSONObject(post);

        confirmPay.success = jsonObject.getInt("success");
        if (confirmPay.success == 1) {
            confirmPay.error_code = jsonObject.getInt("error_code");
            confirmPay.error_msg = jsonObject.getString("error_msg");
        } else if (confirmPay.success == 0) {
            confirmPay.body = jsonObject.getString("body");
        }
        return confirmPay;
    }

    /**
     * 解析第二次确认支付结果
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static ArrayList<Order> parsePaylog(String post) throws Exception {

        ArrayList<Order> orders = new ArrayList<Order>();
        Order order = null;
        JSONObject jsonObject = new JSONObject(post);
        int success = jsonObject.getInt("success");
        if (success == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            int length = jsonArray.length();

            for (int i = 0; i < length; i++) {
                order = new Order();
                JSONObject obj = jsonArray.getJSONObject(i);
                order.orderId = obj.getString("id");
                order.mentId = obj.getInt("bank_id");
                order.money = obj.getLong("amount");
                order.time = obj.getString("date_time");
                order.status = obj.getInt("status");
                orders.add(order);
            }
        }

        return orders;
    }

    /**
     * 解析解析用户余额信息
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static User parseMoneyInfo(String post, User user) throws Exception {
       
    	Yayalog.loger("123"+post);
    	user.banks = new ArrayList<BankInfo>();
        user.cashbanks = new ArrayList<BankInfo>();
        BankInfo bank = null;
        JSONObject jsonObject = new JSONObject(post);
        int success = jsonObject.getInt("success");
        if (success == 0) {
            user.money = jsonObject.getString("amount");
            JSONArray jsonArray = jsonObject.getJSONArray("bindbank");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                bank = new BankInfo();
                bank.id = obj.getString("id");
                bank.bank_id = obj.getString("bank_id");
                bank.lastno = obj.getString("lastno");
                bank.bindvalid = obj.getString("bindvalid");
                bank.bankname = obj.getString("bankname");
                
                if (obj.getInt("bank_type") == 1) {
                    user.banks.add(bank);
                }else {
                    user.cashbanks.add(bank);
                }
                
            }
        }

        return user;
    }

    /**
     * 解析帮助中心
     * 
     * @param get
     * @return
     * @throws Exception
     */
    public static ArrayList<Question> parseHelp(String get) throws Exception {
        ArrayList<Question> questions = new ArrayList<Question>();
        Question question = null;
        JSONArray jsonArray = new JSONArray(get);
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            question = new Question();

            question.id = obj.getString("id");
            question.name = obj.getString("name");
            question.content = obj.getString("content");
            questions.add(question);
        }

        return questions;
    }
    /**
     * 解析订单查询结果
     * @param post
     * @return
     * @throws Exception
     */
    public static BillResult parseBillResult(String post) throws Exception {
        JSONObject object = new JSONObject(post);
        BillResult result = new BillResult();

        result.success = object.getInt("success");

        if (result.success == 0) {
            result.body = object.getString("body");
        } else {
            result.error_code = object.getInt("error_code");
            result.error_msg = object.getString("error_msg");
        }

        return result;
    }
}
