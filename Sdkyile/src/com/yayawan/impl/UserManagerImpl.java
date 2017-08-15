package com.yayawan.impl;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;

import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.proxy.YYWUserManager;

public class UserManagerImpl implements YYWUserManager {

	@Override
	public void manager(Activity paramActivity) {

		Toast.makeText(paramActivity, "个人中心", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		SFOnlineHelper.login(paramActivity, "LoginOut");
	}

	@Override
	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		System.out.println("来这里了");

		YaYawanconstants.exit(paramActivity, callback);

	}

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,
			String ext) {
		// TODO Auto-generated method stub
		YaYawanconstants.setData(paramActivity, roleId, roleName, roleLevel,
				zoneId, zoneName, roleCTime, ext);

		
		 /* 部分渠道如UC渠道，要对游戏人物数据进行统计，而且为接入规范，调用时间：在游戏角色登录成功后调用
		   *  public static void setRoleData(Context context, String roleId，
		   *  	String roleName, String roleLevel, String zoneId, String zoneName)
		   *  
		   *  @param context   上下文Activity
		   *  @param roleId    角色唯一标识
		   *  @param roleName  角色名
		   *  @param roleLevel 角色等级
		   *  @param zoneId    区域唯一标识
		   *  @param zoneName  区域名称
		   *  */
			
			JSONObject roleInfo = new JSONObject();
			try {
			roleInfo.put("roleId", roleId);         //当前登录的玩家角色ID，必须为数字
			roleInfo.put("roleName", roleName);  //当前登录的玩家角色名，不能为空，不能为null
			roleInfo.put("roleLevel", roleLevel);   //当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneId", zoneId);       //当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneName",zoneName);//当前登录的游戏区服名称，不能为空，不能为null
			
			roleInfo.put("balance", "0");
			  //用户游戏币余额，必须为数字，若无，传入0
			roleInfo.put("vip", "1");            //当前用户VIP等级，必须为数字，若无，传入1
			roleInfo.put("partyName", "无帮派");//当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
			roleInfo.put("roleCTime", roleCTime);	 //单位为秒，创建角色的时间
			roleInfo.put("roleLevelMTime", System.currentTimeMillis()/1000);	//单位为秒，角色等级变化时间
			
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
		
		int type = Integer.parseInt(ext);
		switch (type) {
		case 1:
			SFOnlineHelper.setRoleData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName);
			SFOnlineHelper.setData(paramActivity,"enterServer",roleInfo.toString());//  选择服务器进入时调用   必接
			break;
		case 2:
			SFOnlineHelper.setData(paramActivity,"createrole",roleInfo.toString()); //  创建新角色时调用       必接
			break;
		case 3:
			SFOnlineHelper.setData(paramActivity,"levelup",roleInfo.toString());    //  玩家升级角色时调用     必接
			break;

		default:
			break;
		}
	}

}
