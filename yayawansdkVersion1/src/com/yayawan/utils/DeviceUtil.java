package com.yayawan.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class DeviceUtil {

    private static final String GAME_ID = "yayawan_game_id";

    private static final String UNION_GAME_ID = "union_game_id";

    private static final String UNION_ID = "union_id";

    private static final String SOURCE_ID = "yayawan_source_id";

    private static final String GAME_KEY = "yayawan_game_key";

    private static final String YAYAWAN_HELPER = "yayawan_helper";

    private static final String ISLANDSCAPE = "isLandscape";
    
    private static final String ISDEBUG = "isdebug";
    
    public static String gameid;

    /**
     * 获取清单文件中的<meta-data>标签信息
     *
     * @param paramContext
     * @return
     */
    public static Bundle getMetaDataInfo(Context paramContext) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = paramContext.getPackageManager()
                    .getApplicationInfo(paramContext.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo.metaData == null)
            return null;
        return appInfo.metaData;
    }


    /**
     * 获取gameid信息
     *
     * @param paramContext
     * @return
     */
    public static String getGameInfo(Context paramContext, String name) {
        Bundle dataInfo = getMetaDataInfo(paramContext);
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(name) == null)) {
            throw new IllegalArgumentException("must set the yayawan_game_id");
        }
        return dataInfo.getString(name).replace("string", "");
    }


    /**
     * 获取gameid信息
     *
     * @param paramContext
     * @return
     */
    public static String getGameId(Context paramContext) {
    	if (gameid!=null) {
			return gameid;
		}
        Bundle dataInfo = getMetaDataInfo(paramContext);
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(GAME_ID) == null)) {
            throw new IllegalArgumentException("must set the yayawan_game_id");
        }
        return dataInfo.getString(GAME_ID).replace("yaya", "");
    }

    /**
     * 获取UNION_ID
     *
     * @param paramContext
     * @return
     */
    public static String getUnionid(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(UNION_ID) == null)) {
            return null;
        }
        return dataInfo.get(UNION_ID).toString().replace("yaya", "");
    }
    /**
     * 获取UNION_GAME_ID
     *
     * @param paramContext
     * @return
     */
    public static String getUnionGameid(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(UNION_GAME_ID) == null)) {
            return null;
        }
        return dataInfo.get(UNION_GAME_ID).toString().replace("yaya", "");
    }

    /**
     * 获取gamekey
     *
     * @param paramContext
     * @return
     */
    public static String getGameKey(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(GAME_KEY) == null)) {
            return null;
        }
        return dataInfo.get(GAME_KEY).toString();
    }

    /**
     * 获取手机IMEI
     *
     * @param paramContext
     * @return
     */
    public static String getIMEI(Context paramContext) {
        // 获取设备的imei号
        String deviceId = ((TelephonyManager) paramContext
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        // 如果imei为空,获取mac地址
        if (deviceId == null || "0".equals(deviceId)) {
            WifiManager wifi = (WifiManager) paramContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            deviceId = getDEC(info.getMacAddress());
        }
        return deviceId;
    }

    /**
     * 判断是否是手机
     *
     * @param paramContext
     * @return
     */
    public static boolean isPhone(Context paramContext) {
        // 获取设备的imei号
        String deviceId = ((TelephonyManager) paramContext
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        // 如果imei为空,获取mac地址
        if (deviceId == null || "0".equals(deviceId)) {
            return false;
        }
        return true;
    }

    /**
     * 判断横竖屏
     *
     * @param paramContext
     * @return
     */
    public static boolean isLandscape(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (!dataInfo.getBoolean(ISLANDSCAPE))) {
            return false;
        }
        return dataInfo.getBoolean(ISLANDSCAPE);
    }
    
    /**
     * 判断是否debug模式
     *
     * @param paramContext
     * @return
     */
    public static boolean isDebug(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (!dataInfo.getBoolean(ISDEBUG))) {
            return false;
        }
        return dataInfo.getBoolean(ISDEBUG);
    }
    
    /**
     * 判断是否要退出框
     *
     * @param paramContext
     * @return
     */
    public static boolean isHaveexit(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (!dataInfo.getBoolean("isHaveexit"))) {
            return false;
        }
        return dataInfo.getBoolean("isHaveexit");
    }
    
    /**
     * 判断是否支持账号切换
     *
     * @param paramContext
     * @return
     */
    public static boolean changeAcount(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (!dataInfo.getBoolean("yayawan_nochangecount"))) {
            return false;
        }
        return dataInfo.getBoolean("yayawan_nochangecount");
    }

    /**
     * 获取程序包名
     *
     * @param paramContext
     * @return
     */
    public static String getPackageName(Context paramContext) {
        PackageManager localPackageManager = paramContext.getPackageManager();
        try {
            return localPackageManager.getPackageInfo(
                    paramContext.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取sim卡信息
     *
     * @param paramContext
     * @return
     */
    public static String getSim(Context paramContext) {
        return ((TelephonyManager) paramContext
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getSimSerialNumber();
    }

    /**
     * 获取MAC地址
     *
     * @param paramContext
     * @return
     */
    public static String getMAC(Context paramContext) {
        return (((WifiManager) paramContext
                .getSystemService(Context.WIFI_SERVICE)).getConnectionInfo())
                .getMacAddress();
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        String model = Build.MODEL;
        return model;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取mac地址的十进制
     *
     * @param mac
     * @return
     */
    public static String getDEC(String mac) {
        String[] split = mac.split(":");
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < split.length; i++) {
            int ii = Integer.parseInt(split[i], 16);
            buffer.append(ii);
        }
        return buffer.toString();
    }

}
