package com.yayawan.sdk.floatwidget.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.text.Spanned;

import com.yayawan.sdk.domain.Article;
import com.yayawan.sdk.domain.Faq;
import com.yayawan.sdk.domain.GameInfo;
import com.yayawan.sdk.domain.Gift;
import com.yayawan.sdk.domain.GiftInfo;
import com.yayawan.sdk.domain.PayLog;
import com.yayawan.sdk.domain.PayStatusResult;
import com.yayawan.sdk.domain.Result;
import com.yayawan.sdk.domain.StrategyInfo;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Yayalog;

public class JSONParser {

    /**
     * 解析礼包信息json
     * 
     * @param post
     * @return
     * @throws JSONException
     */
    public static ArrayList<GiftInfo> parserGameGift(String post)
            throws JSONException {
        ArrayList<GiftInfo> giftList = new ArrayList<GiftInfo>();

        JSONObject jsonObject = new JSONObject(post);

        JSONArray jsonArray = jsonObject.getJSONArray("gifts");

        GiftInfo giftInfo = null;
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {

            JSONObject obj = jsonArray.getJSONObject(i);
            String gift_id = obj.getString("id");
            String game_id = obj.getString("game_id");
            String name = obj.getString("name");
            String description = obj.getString("description");
            String howto = obj.getString("howto");
            String create_time = obj.getString("create_time");
            String end_time = obj.getString("end_time");

            giftInfo = new GiftInfo(gift_id, game_id, name, description, howto,
                    create_time, end_time);
            giftList.add(giftInfo);
        }

        return giftList;
    }

    /**
     * 解析礼包信息json
     * 
     * @param post
     * @return
     * @throws JSONException
     */
    public static ArrayList<Article> parserArticle(String post)
            throws JSONException {
        ArrayList<Article> articleList = new ArrayList<Article>();

        JSONObject jsonObject = new JSONObject(post);

        JSONArray jsonArray = jsonObject.getJSONArray("news");

        Article info = null;
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            info = new Article();
            JSONObject obj = jsonArray.getJSONObject(i);
            info.id = obj.getString("id");
            info.name = obj.getString("name");
            info.description = obj.getString("description");
            info.create_time = obj.getString("create_time").split(" ")[0];
            info.clicknum = obj.getString("clicknum");
            info.upfile = obj.getString("upfile");
            if (info.upfile != null && !"".equals(info.upfile)) {
                info.upfile = info.upfile + "?imageView/1/w/160/h/120/q/85";
            }
            if (info.upfile.contains("!logosy")) {
                info.upfile = info.upfile.replace("!logosy", "");
            }

            articleList.add(info);
        }

        return articleList;
    }

    /**
     * 解析攻略信息json
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static ArrayList<StrategyInfo> parserGameStrategy(String post)
            throws Exception {

        ArrayList<StrategyInfo> strategyList = new ArrayList<StrategyInfo>();

        JSONObject jsonObject = new JSONObject(post);
        StrategyInfo strategy = null;
        JSONArray jsonArray = jsonObject.getJSONArray("news");
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            String id = obj.getString("id");
            
            String name = obj.getString("name");
            
            String game_id = obj.getString("game_id");
            String time = obj.getString("update_time").split(" ")[0]
                    .substring(5);
            strategy = new StrategyInfo(id, name, game_id, time);
            strategyList.add(strategy);
        }
        return strategyList;
    }

    /**
     * 解析礼包cdkey
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static Gift parserGift(String post) throws Exception {
        JSONObject obj = new JSONObject(post);
        int is_success = obj.getInt("is_success");
        Gift gift = null;
        Yayalog.loger("我是领取礼包"+post);
        if (is_success == -1) {
            gift = new Gift();
            gift.is_success = is_success;
//            gift.body = "你已领取过该礼包";
            gift.body = obj.optString("body");
            String cdkey = obj.getString("cdkey");
            gift.cdkey=cdkey;
        } else if (is_success == 0) {
            gift = new Gift();
            gift.is_success = is_success;
//            gift.body = "领取礼包失败";
            gift.body = obj.optString("body");
        } else if (is_success == 1) {
            //String id = obj.getString("id");
        	String id = obj.isNull("id") ? "" : obj
                     .getString("id");
            String cdkey = obj.getString("cdkey");
            
           // String release_time = obj.getString("release_time");
            String release_time = obj.isNull("release_time") ? "" : obj
                    .getString("release_time");
            
            String get_time = obj.getString("get_time");
            gift = new Gift(id, cdkey, is_success, release_time, get_time);
            gift.body = "领取礼包成功";
        }
        return gift;
    }

    /**
     * 解析游戏列表信息json
     * 
     * @param post
     * @return
     * @throws JSONException
     */
    public static ArrayList<GameInfo> parserGameList(String post)
            throws JSONException {
        ArrayList<GameInfo> gameList = new ArrayList<GameInfo>();

        JSONObject jsonObject = new JSONObject(post);

        int success = jsonObject.getInt("success");
        if (success == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            GameInfo game = null;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                game = new GameInfo();
                game.id = obj.getString("id");
                game.size = obj.getLong("size");
                game.name = obj.getString("name");
                game.category = obj.getString("category_name");
                game.description = obj.getString("description");
                game.upfile = obj.getString("upfile");
                game.url_id = obj.getString("url_id");
                gameList.add(game);
            }
        }

        return gameList;
    }

    /**
     * 获取qq群
     * 
     * @param get
     * @return
     * @throws Exception
     */
    public static String parserQQGroup(String post) throws Exception {
        JSONObject object = new JSONObject(post);

        return object.getString("qqgroup");
    }

    /**
     * 解析操作结果
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static Result parserResult(String post) throws Exception {

        Result result = new Result();
        JSONObject object = new JSONObject(post);
        result.success = object.getInt("success");
        if (!object.isNull("body")) {
            result.body = object.getString("body");
        }
        return result;
    }

    /**
     * 获取已获取礼包列表
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static ArrayList<GiftInfo> parserGifts(String post) throws Exception {
        ArrayList<GiftInfo> giftList = new ArrayList<GiftInfo>();
        JSONObject jsonObject = new JSONObject(post);
        JSONArray jsonArray = jsonObject.getJSONArray("gifts");
        GiftInfo gift = null;
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            gift = new GiftInfo();
            gift.gift_id = obj.getString("category_id");
            gift.cdkey = obj.getString("cdkey");
            gift.name = obj.getString("name");
            gift.end_time = obj.getString("release_time");
            gift.create_time = obj.getString("get_time");
            giftList.add(gift);
        }
        return giftList;
    }

    /**
     * 获取用户信息
     * 
     * @param post
     * @param user
     * @return
     * @throws Exception
     */
    public static User parserUserInfo(String post, User user) throws Exception {

        JSONObject obj = new JSONObject(post);
        user.lasttime = obj.getString("last_login");
        user.money = obj.getString("amount");
        user.icon = obj.getString("icon");
        user.nick = obj.getString("nick");
        user.phoneActive = obj.getInt("phone_active");
        return user;
    }

    /**
     * 解析充值和消费记录
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static PayStatusResult parserLog(String post) throws Exception {
        JSONObject jsonObject = new JSONObject(post);
        PayStatusResult statusResult = new PayStatusResult();

        statusResult.success = jsonObject.getInt("success");

        if (statusResult.success == 0) {
            statusResult.total = jsonObject.getInt("total");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            statusResult.payLogs = new ArrayList<PayLog>();
            PayLog log = null;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                log = new PayLog();
                log.id = obj.getString("id");

                long money = Long.valueOf(obj.getString("amount"));
                if (money % 100 == 0) {
                    log.amount = (money / 100) + "";
                } else {
                    // 除数
                    BigDecimal bd = new BigDecimal(money);
                    // 被除数
                    BigDecimal bd2 = new BigDecimal(100);
                    // 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
                    BigDecimal result = bd.divide(bd2, 2,
                            BigDecimal.ROUND_HALF_UP);
                    log.amount = result.toString();
                }
                log.goods = obj.isNull("goods") ? "" : obj.getString("goods");
                log.date_time = obj.getString("date_time");
                log.status = obj.getInt("status");
                log.status_msg = obj.getString("status_msg");
                log.bank_id = obj.isNull("bank_id") ? "" : obj
                        .getString("bank_id");
                log.bank_name = obj.isNull("bank_name") ? "" : obj
                        .getString("bank_name");
                statusResult.payLogs.add(log);
            }

        }

        return statusResult;
    }

    /**
     * 解析Faq
     * 
     * @param post
     * @return
     * @throws Exception
     */
    public static LinkedList<Faq> parserFaq(String post) throws Exception {
        LinkedList<Faq> list = new LinkedList<Faq>();
        Faq faq = null;
        JSONObject jsonObject = new JSONObject(post);

        JSONArray helps = jsonObject.getJSONArray("helps");
        int length = helps.length();
        if (length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                JSONObject object = helps.getJSONObject(i);

                String text = "<b>" + object.getString("name") + "</b>";
                Spanned fromHtml = Html.fromHtml(text);
                sb.append(fromHtml).append("\n")
                        .append("    " + object.getString("content"))
                        .append("\n\n");
            }
            faq = new Faq();
            faq.content = sb.toString();
            faq.type = 1;
            list.add(faq);
        }
        JSONArray questions = jsonObject.getJSONArray("questions");
        int length2 = questions.length();
        for (int i = 0; i < length2; i++) {
            JSONObject object = questions.getJSONObject(i);
            faq = new Faq();
            faq.content = object.getString("content");
            faq.type = 2;
            list.add(faq);
            JSONArray array = object.getJSONArray("replay");
            int length3 = array.length();
            if (length3 > 0) {
                for (int j = 0; j < length3; j++) {
                    JSONObject object2 = array.getJSONObject(j);
                    faq = new Faq();
                    faq.content = object2.getString("content");
                    faq.type = 1;
                    list.add(faq);
                }
            }
        }

        return list;
    }
}
