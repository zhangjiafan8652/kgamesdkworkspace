package com.yayawan.sdk.floatwidget.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import com.yayawan.sdk.domain.Article;
import com.yayawan.sdk.domain.Faq;
import com.yayawan.sdk.domain.GameInfo;
import com.yayawan.sdk.domain.Gift;
import com.yayawan.sdk.domain.GiftInfo;
import com.yayawan.sdk.domain.PayStatusResult;
import com.yayawan.sdk.domain.Result;
import com.yayawan.sdk.domain.StrategyInfo;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.utils.CryptoUtil;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.HttpUtil;
import com.yayawan.sdk.utils.MD5;
import com.yayawan.sdk.utils.RSACoder;
import com.yayawan.sdk.utils.UrlUtil;

public class ObtainData {

    /**
     * 获取礼包列表
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static ArrayList<GiftInfo> getGameGiftList(Context context, int type)
            throws Exception {

        String url = UrlUtil.GAME_DETAIL + "?id="
                + DeviceUtil.getGameId(context) + "&type=" + type;

        String post = HttpUtil.get(url, "UTF-8");

        ArrayList<GiftInfo> giftInfoList = JSONParser.parserGameGift(post);

        return giftInfoList;

    }

    /**
     * 获取文章列表数据
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static ArrayList<Article> getArticleList(Context context, int type)
            throws Exception {

        String url = UrlUtil.GAME_DETAIL + "?id="
                + DeviceUtil.getGameId(context) + "&type=" + type;
        String post = HttpUtil.get(url, "UTF-8");
        ArrayList<Article> articleInfoList = JSONParser.parserArticle(post);

        return articleInfoList;

    }

    /**
     * 获取faq
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static LinkedList<Faq> getQuestionList(Context context, User user)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", user.uid + "");
        params.put("token", user.token);
        params.put("app_id", DeviceUtil.getGameId(context));

        String params2 = encryptParams(params);
        params.clear();
        params.put("data", params2);
        String post = HttpUtil.post(UrlUtil.FAQ, params, "UTF-8");

        return JSONParser.parserFaq(post);

    }

    /**
     * 添加faq
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static void addFaq(Context context, User user, String content)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", user.uid + "");
        params.put("token", user.token);
        params.put("user_name", user.userName);
        params.put("content", content);
        params.put("app_id", DeviceUtil.getGameId(context));

        String params2 = encryptParams(params);
        params.clear();
        params.put("data", params2);
        HttpUtil.post(UrlUtil.FAQADD, params, "UTF-8");

    }

    /**
     * 获取礼包
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static Gift getGameGift(Context context, User user, String cid)
            throws Exception {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", user.uid + "");
        params.put("token", user.token);
        params.put("app_id", DeviceUtil.getGameId(context));
        params.put("cid", cid);
        String post = HttpUtil.post(UrlUtil.GET_GIFT, params, "UTF-8");

        Gift gift = JSONParser.parserGift(post);

        return gift;

    }

    /**
     * 获取攻略详情数据
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static ArrayList<StrategyInfo> getGameStrategyList(Context context)
            throws Exception {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", DeviceUtil.getGameId(context));
        params.put("type", "2");
        String post = HttpUtil.post(UrlUtil.GAME_DETAIL, params, "UTF-8");

        ArrayList<StrategyInfo> strategyInfo = JSONParser
                .parserGameStrategy(post);

        return strategyInfo;

    }

    /**
     * 获取用户信息
     * 
     * @param context
     * @return
     * @throws Exception
     */
    public static User getUserInfo(Context context, User user) throws Exception {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app_id", DeviceUtil.getGameId(context));
        params.put("uid", user.uid + "");
        params.put("token", user.token);
        String post = HttpUtil.post(UrlUtil.GETUSERINFO, params, "UTF-8");

        user = JSONParser.parserUserInfo(post, user);

        return user;

    }

    /**
     * 获取更多游戏
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static ArrayList<GameInfo> getGameList() throws Exception {

        String get = HttpUtil.get(UrlUtil.MOREGAMES, "UTF-8");
        return JSONParser.parserGameList(get);
    }

    /**
     * 获取qq群
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static String getQQGroup(String id) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("type", "1");
        String post = HttpUtil.post(UrlUtil.GAME_DETAIL, params, "UTF-8");
        return JSONParser.parserQQGroup(post);
    }

    /**
     * 修改密码
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static Result modifyPassword(Context context, User user,
            String oldPass, String newPass) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("data",
                encryptModifyPassword(context, user, oldPass, newPass));

        String post = HttpUtil.post(UrlUtil.MODIFYPASSWORD, params, "UTF-8");
        return JSONParser.parserResult(post);
    }

    /**
     * 加密修改密码信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptModifyPassword(Context context, User user,
            String oldpassword, String newpassword) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer.append("app_id=")
                .append(DeviceUtil.getGameId(context)).append("&uid=")
                .append(user.uid).append("&token=").append(user.token)
                .append("&oldpassword=").append(oldpassword)
                .append("&newpassword=").append(newpassword).toString();
        String hexString = CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(info.getBytes()));
        return hexString;

    }

    /**
     * 修改绑定手机
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static Result modifyPhone(Context context, User user,
            String newPhone, String code) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", user.userName);
        params.put("app_id", DeviceUtil.getGameId(context));
        params.put("token", user.token);
        params.put("newphone", newPhone);
        params.put("code", code);
        String post = HttpUtil.post(UrlUtil.CHANGEPHONE, params, "UTF-8");
        return JSONParser.parserResult(post);
    }

    /**
     * 发送手机验证码
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static Result sendSms(Context context, User user, String phone)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("uid", user.uid + "");
        params.put("app_id", DeviceUtil.getGameId(context));
        params.put("token", user.token);
        params.put("sign",
                MD5.MD5(user.token + "|" + DeviceUtil.getGameKey(context)));
        params.put("type", "1");
        params.put("phone", phone);
        String post = HttpUtil.post(UrlUtil.SENDSMS, params, "UTF-8");
        return JSONParser.parserResult(post);
    }
    
    /**
     * 更换手机手机验证码
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static Result ResetPhoneSendSms(Context context, User user, String phone)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("uid", user.uid + "");
        params.put("app_id", DeviceUtil.getGameId(context));
        params.put("token", user.token);
        params.put("sign",
                MD5.MD5(user.token + "|" + DeviceUtil.getGameKey(context)));
        params.put("type", "1");
        params.put("phone", phone);
        String post = HttpUtil.post(UrlUtil.CHANGEPHONESENDSMS, params, "UTF-8");
        Yayalog.loger("验证码返回信息："+post);
        return JSONParser.parserResult(post);
    }
    
    /**
	 * 获取验证码
	 * 
	 * @param contex
	 * @param mName
	 *            用户名
	 * @return
	 * @throws Exception
	 */
	public static Result getAuthCode(User mUser) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", mUser.userName);
		String post = HttpUtil.post(UrlUtil.CHANGEPHONESENDSMS, params, "UTF-8");
		return JSONParser.parserResult(post);

	}
    

    /**
     * 绑定手机
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static Result bindPhone(Context context, User user, String phone,
            String code) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("data", encryptBindPhone(context, user, phone, code));
        String post = HttpUtil.post(UrlUtil.PHONEBIND, params, "UTF-8");
        return JSONParser.parserResult(post);
    }

    /**
     * 获取充值记录
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static PayStatusResult getRechargeLog(Context context, User user)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        String param = "uid=" + user.uid + "&token=" + user.token + "&app_id="
                + DeviceUtil.getGameId(context);
        params.put("data", CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(param.getBytes())));
        String post = HttpUtil.post(UrlUtil.RECHARGELOG, params, "UTF-8");
        return JSONParser.parserLog(post);
    }

    /**
     * 获取消费记录
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public static PayStatusResult getPayLog(Context context, User user)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        String param = "uid=" + user.uid + "&token=" + user.token + "&app_id="
                + DeviceUtil.getGameId(context);
        params.put("data", CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(param.getBytes())));
        String post = HttpUtil.post(UrlUtil.PAYLOG, params, "UTF-8");
        return JSONParser.parserLog(post);
    }

    /**
     * 加密绑定手机信息
     * 
     * @param context
     * @throws Exception
     */
    private static String encryptBindPhone(Context context, User user,
            String phone, String code) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer
                .append("app_id=")
                .append(DeviceUtil.getGameId(context))
                .append("&uid=")
                .append(user.uid)
                .append("&token=")
                .append(user.token)
                .append("&sign=")
                .append(MD5.MD5(user.token + "|"
                        + DeviceUtil.getGameKey(context))).append("&phone=")
                .append(phone).append("&code=").append(code).toString();
        String hexString = CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(info.getBytes()));
        return hexString;

    }

    /**
     * 获取已获取礼包列表
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public static ArrayList<GiftInfo> getGifts(Context context, User user)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", user.uid + "");
        params.put("token", user.token);
        params.put("game_id", DeviceUtil.getGameId(context));
        String post = HttpUtil.post(UrlUtil.MY_GIFTS, params, "UTF-8");
        ArrayList<GiftInfo> giftList = JSONParser.parserGifts(post);

        return giftList;
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
     * 更改绑定手机
     * @param mContext
     * @param mUser 玩家信息
     * @param mPhoneText 
     * @param authNum
     * @param newauthNum 
     * @param authNum2 
     * @return
     * @throws Exception
     */
	public static Result ChangebindPhone(Context mContext, User mUser,
			String mPhoneText, String mnewphonetext, String authNum2, String newauthNum) throws Exception {
		// TODO Auto-generated method stub
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("app_id", DeviceUtil.getGameId(mContext));
		params.put("username", mUser.userName);
		params.put("newphone", mPhoneText);
		params.put("code", mnewphonetext);
		params.put("token", mUser.token);
		params.put("newcode",newauthNum);
		Yayalog.loger("发送的post信息"+params.toString());
		String post = HttpUtil.post(UrlUtil.CHANGEPHONES, params, "UTF-8");
		Yayalog.loger("更换手机返回来的信息"+post);
		 return JSONParser.parserResult(post);
	}
}
