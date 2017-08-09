package com.yayawan.sdk.account.engine;

import java.math.BigInteger;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yayawan.sdk.domain.ComentDiscuss;
import com.yayawan.sdk.domain.ComentRespose;
import com.yayawan.sdk.domain.Discuss;
import com.yayawan.sdk.domain.DiscussResponse;
import com.yayawan.sdk.domain.Game;
import com.yayawan.sdk.domain.Result;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.domain.Userkey;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;

public class JSONParser {

	/**
	 * 解析用户信息
	 * 
	 * @param post
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static User parserUser(String post, Context context)
			throws Exception {
		JSONObject object = new JSONObject(post);
		Yayalog.loger("登陆的信息+++++++" + post);

		int success = object.getInt("success");
		String body = object.getString("body");
		String uid = null;
		String username = null;
		String token = null;
		String money = null;
		User user = null;
		String secret = null;
		// ArrayList<Userkey> userkey = new ArrayList<Userkey>();
		if (success == 0) {
			uid = object.getString("uid");
			username = object.getString("username");
			token = object.getString("token");
			money = object.getString("money");

			user = new User(username, new BigInteger(uid), token, success,
					body, money);
			user.password = object.isNull("password") ? "" : object
					.getString("password");

			user.secret = object.isNull("secret") ? "" : object
					.getString("secret");
		} else {
			user = new User(success, body);
		}

		return user;
	}

	/**
	 * 解析完善资料返回数据
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static String parserComplete(String post) throws Exception {
		JSONObject object = new JSONObject(post);
		int su = object.getInt("su");
		String result = null;
		if (su == 0) {
			result = "操作成功";
		} else {
			result = object.getString("body");
		}
		return result;
	}

	/**
	 * 解析验证码登录信息
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static User parserSecurityLogin(String post, Context mContext)
			throws Exception {

		JSONObject object = new JSONObject(post);
		Yayalog.loger("登陆的信息+++++++" + post);
		int success = object.getInt("success");
		String body = object.getString("body");
		String uid = null;
		String username = null;
		String token = null;
		String money = null;
		String lasttime = null;
		String secret = null;
		User user = null;
		if (success == 0) {
			uid = object.getString("uid");
			username = object.getString("username");
			token = object.getString("token");
			money = object.getString("money");

			// lasttime
			user = new User(username, new BigInteger(uid), token, success,
					body, money);
			user.password = object.isNull("password") ? "" : object
					.getString("password");
			user.secret = object.isNull("secret") ? "" : object
					.getString("secret");
		} else if (success == 2) {
			uid = object.getString("uid");
			username = object.getString("username");
			token = object.getString("token");
			money = object.getString("money");
			lasttime = object.getString("lasttime");
			user = new User(username, new BigInteger(uid), token, success,
					body, money);
			user.password = object.isNull("password") ? "" : object
					.getString("password");
			user.secret = object.isNull("secret") ? "" : object
					.getString("secret");
		} else {
			user = new User(success, body);
		}

		return user;
	}

	/**
	 * 获取电话号码
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static String parserPhonenum(String post) throws Exception {
		JSONObject object = new JSONObject(post);
		return object.getString("body");
	}

	/**
	 * 获取返回值
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static int parserCode(String post) throws Exception {
		JSONObject object = new JSONObject(post);
		return object.getInt("success");
	}

	/**
	 * 获取修改密码返回值
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static String parserResetPasswordCode(String post) throws Exception {
		JSONObject object = new JSONObject(post);
		// int success = object.getInt("success");
		return object.getString("body");
	}

	/**
	 * 获取修改密码返回值
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static Result parserloginCode(String post) throws Exception {
		JSONObject object = new JSONObject(post);
		Result result = new Result();

		result.success = object.getInt("success");
		result.body = object.getString("body");
		return result;
	}

	/**
	 * 解析评论片段
	 * 
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	public static DiscussResponse parserDiscuss(String result)
			throws JSONException {
		JSONObject object = new JSONObject(result);
		DiscussResponse discussResponse = new DiscussResponse();
		ArrayList<Discuss> discusses = new ArrayList<Discuss>();
		int success = object.getInt("success");
		if (success == 0) {
			JSONArray jsonArray = object.getJSONArray("discuss");
			for (int i = 0; i < jsonArray.length(); i++) {
				Discuss discuss = new Discuss();
				// discuss.setAt(jsonArray.getJSONObject(i).getString("at"));
				discuss.setCount_c(jsonArray.getJSONObject(i).getString(
						"count_c"));
				discuss.setCreate_time(jsonArray.getJSONObject(i).getString(
						"create_time"));

				discuss.setIcon(jsonArray.getJSONObject(i).getString("icon"));
				discuss.setId(jsonArray.getJSONObject(i).getString("id"));
				discuss.setImg(jsonArray.getJSONObject(i).getString("img"));
				discuss.setLike(jsonArray.getJSONObject(i).getString("like"));
				discuss.setMessage(jsonArray.getJSONObject(i).getString(
						"message"));
				discuss.setTid(jsonArray.getJSONObject(i).getString("tid"));
				discuss.setUser(jsonArray.getJSONObject(i).getString("user"));
				discuss.setParent(jsonArray.getJSONObject(i)
						.getString("parent"));
				JSONObject jsonObject = jsonArray.getJSONObject(i)
						.getJSONObject("game");
				Game game = new Game();
				game.id = jsonObject.isNull("id") ? "" : jsonObject
						.getString("id");
				game.name = jsonObject.isNull("name") ? "" : jsonObject
						.getString("name");
				game.shortname = jsonObject.isNull("shortname") ? ""
						: jsonObject.getString("shortname");
				discuss.setGame(game);
				discusses.add(discuss);
			}
			discussResponse.setDiscuss(discusses);
			return discussResponse;
		} else {
			return discussResponse;
		}

	}

	/**
	 * 解析评论片段
	 * 
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	public static ComentRespose parserComentlist(String result)
			throws JSONException {
		JSONObject object = new JSONObject(result);
		ComentRespose discussResponse = new ComentRespose();
		ArrayList<ComentDiscuss> discusses = new ArrayList<ComentDiscuss>();
		int success = object.getInt("success");
		if (success == 0) {
			JSONArray jsonArray = object.getJSONArray("discuss");
			for (int i = 0; i < jsonArray.length(); i++) {
				ComentDiscuss discuss = new ComentDiscuss();
				// discuss.setAt(jsonArray.getJSONObject(i).getString("at"));

				discuss.setCreate_time(jsonArray.getJSONObject(i).getString(
						"create_time"));

				discuss.setIcon(jsonArray.getJSONObject(i).getString("icon"));
				discuss.setId(jsonArray.getJSONObject(i).getString("id"));
				discuss.setLike(jsonArray.getJSONObject(i).getString("like"));
				discuss.setMessage(jsonArray.getJSONObject(i).getString(
						"message"));
				discuss.setUser(jsonArray.getJSONObject(i).getString("user"));
				JSONObject jsonObject = jsonArray.getJSONObject(i)
						.getJSONObject("parent");
				String parent = jsonObject.isNull("username") ? "" : jsonObject
						.getString("username");
				discuss.setParent(parent);

				discusses.add(discuss);
			}
			discussResponse.setDiscuss(discusses);
			return discussResponse;
		} else {
			return discussResponse;
		}

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

}
