package com.yayawan.sdk.account.engine;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.domain.Result;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jflogin.Login_success_dialog;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfutils.Logger;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.utils.CryptoUtil;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.HttpUtil;
import com.yayawan.sdk.utils.RSACoder;
import com.yayawan.sdk.utils.UrlUtil;

public class ObtainData {

	public static GetdataCallback getdatacallback;
	
	/**
	 * 登陆
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @param mPassword
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static User login(Context context, String name, String password)
			throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("data", encryptLoginData(context, name, password));
		String post = HttpUtil.post(UrlUtil.LOGIN, params, "UTF-8");
		User user = JSONParser.parserUser(post, context);

		return user;
	}

	/**
	 * 注册
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @param mPassword
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static User register(Context context, String name, String password)
			throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("data", encryptRegisterData(context, name, password));
		String post = HttpUtil.post(UrlUtil.REGISTER, params, "UTF-8");
		User user = JSONParser.parserUser(post, context);

		return user;
	}

	
	/**
	 * 验证码登录
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @param mPassword
	 *            密码
	 * @return 1失败0成功
	 * @throws Exception
	 */
	public static User loginSecurity(Context context, String phone, String code)
			throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("data", encryptLoginSecurityData(context, phone, code));
		String post = HttpUtil.post(UrlUtil.SECURITYLOGIN, params, "UTF-8");
		
		User user = JSONParser.parserSecurityLogin(post,context);

		return user;
	}

	/**
	 * 验证码登录
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @param mPassword
	 *            密码
	 * @return 1失败0成功
	 * @throws Exception
	 */
	public static User loginSecurity1(Context context, String phone, String code)
			throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("data", encryptLoginSecurityData(context, phone, code));
		String post = HttpUtil.post(UrlUtil.SECRET_KEYLOGIN, params, "UTF-8");
		User user = JSONParser.parserSecurityLogin(post,context);

		return user;
	}
	
	/**
	 * 完善资料
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @param mPassword
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static String completeInfo(Context context, String phone,
			String email, String qq) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		User user = AgentApp.mUser;
		params.put("uid", user.uid + "");
		params.put("app_id", DeviceUtil.getGameId(context));
		params.put("token", user.token);
		params.put("phone", phone);
		params.put("email", email);
		params.put("qq", qq);
		String post = HttpUtil.post(UrlUtil.COMPLETEINFO, params, "UTF-8");

		return JSONParser.parserComplete(post);

	}

	/**
	 * 获取短信注册信息
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @param mPassword
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static User fetchSms(Context context, String uuid) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reg_token", uuid);
		params.put("ads_id", DeviceUtil.getSourceId(context));
		params.put("app_id", DeviceUtil.getGameId(context));
		params.put("device", DeviceUtil.getIMEI(context));
		params.put("mac", DeviceUtil.getMAC(context));
		params.put("model", DeviceUtil.getModel());
		params.put("brand", DeviceUtil.getBrand());
		String params2 = encryptParams(params);
		params.clear();
		params.put("data", params2);

		String post = HttpUtil.post(UrlUtil.FETCHSMS, params, "UTF-8");

		// Log.e("fan", post);
		return JSONParser.parserUser(post, context);

	}

	/**
	 * 获取验证码
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @return
	 * @throws Exception
	 */
	public static String getAuthCode(String username) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		String post = HttpUtil.post(UrlUtil.GETPHONENUM, params, "UTF-8");
		return com.yayawan.sdk.account.engine.JSONParser.parserPhonenum(post);

	}

	/**
	 * 获取验证码
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @return
	 * @throws Exception
	 */
	public static Result getLoginCode(String phone) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		String info = "phone=" + phone;
		params.put("data", CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes())));
		String post = HttpUtil.post(UrlUtil.GETLOGINCODE, params, "UTF-8");

		return JSONParser.parserloginCode(post);

	}

	/**
	 * 判断验证码
	 * 
	 * @param context
	 * @param mName
	 *            用户名
	 * @return
	 * @throws Exception
	 */
	public static int checkCode(String username, String code) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("code", code);
		String post = HttpUtil.post(UrlUtil.CHECKCODE, params, "UTF-8");
		return JSONParser.parserCode(post);

	}

	/**
	 * 重设密码
	 * 
	 * @param username
	 * @param code
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String resetPassword(Context context, String username,
			String code, String password) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("data",
				encryptResetPassword(context, username, code, password));

		String post = HttpUtil.post(UrlUtil.RESETPASSWORD, params, "UTF-8");
		return JSONParser.parserResetPasswordCode(post);

	}

	/**
	 * 加密登录用户信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptLoginData(Context context, String name,
			String password) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("app_id=")
				.append(DeviceUtil.getGameId(context)).append("&ads_id=")
				.append(DeviceUtil.getSourceId(context)).append("&username=")
				.append(name).append("&password=").append(password).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return hexString;

	}

	/**
	 * 加密注册用户信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptRegisterData(Context context, String name,
			String password) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("app_id=")
				.append(DeviceUtil.getGameId(context)).append("&username=")
				.append(name).append("&password=").append(password)
				.append("&ads_id=").append(DeviceUtil.getSourceId(context))
				.append("&device=").append(DeviceUtil.getIMEI(context))
				.append("&mac=").append(DeviceUtil.getMAC(context))
				.append("&model=").append(DeviceUtil.getModel())
				.append("&brand=").append(DeviceUtil.getBrand()).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return hexString;

	}

	/**
	 * 加密验证码登录信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptLoginSecurityData(Context context,
			String phone, String code) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("app_id=")
				.append(DeviceUtil.getGameId(context)).append("&phone=")
				.append(phone).append("&code=").append(code).append("&ads_id=")
				.append(DeviceUtil.getSourceId(context)).append("&app_id=")
				.append(DeviceUtil.getGameId(context)).append("&device=")
				.append(DeviceUtil.getIMEI(context)).append("&mac=")
				.append(DeviceUtil.getMAC(context)).append("&model=")
				.append(DeviceUtil.getModel()).append("&brand=")
				.append(DeviceUtil.getBrand()).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return hexString;

	}

	/**
	 * 加密找回密码信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptResetPassword(Context context, String name,
			String code, String password) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("app_id=")
				.append(DeviceUtil.getGameId(context)).append("&username=")
				.append(name).append("&password=").append(password)
				.append("&code=").append(code).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return hexString;

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

	public interface GetdataCallback {
		public void sucess(User user);;

		public void fail(User user);;
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
     * 认证
     * 
     * @param context
     * @throws Exception
     */
    public static String encryptModifyRealAuth(Context context, User user,String relname,String identity
           ) throws Exception {
        StringBuffer infoBuffer = new StringBuffer();
        String info = infoBuffer.append("app_id=")
                .append(DeviceUtil.getGameId(context)).append("&uid=")
                .append(user.uid).append("&token=").append(user.token)
                .append("&relname=").append(relname)
                .append("&identity=").append(identity).toString();
        String hexString = CryptoUtil.encodeHexString(RSACoder
                .encryptByPublicKey(info.getBytes()));
        return hexString;

    }
}
