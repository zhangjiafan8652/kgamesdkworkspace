package cn.nubia.nbgame.demo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.nubia.nbgame.demo.util.ErrorMsgUtil;
import cn.nubia.nbgame.demo.util.NeoLog;
import cn.nubia.nbgame.sdk.GameSdk;
import cn.nubia.nbgame.sdk.entities.AppInfo;
import cn.nubia.nbgame.sdk.entities.ErrorCode;
import cn.nubia.nbgame.sdk.interfaces.CallbackListener;

/**
 * Created by liruchun on 2016/9/13.
 */

public class SdkApplication extends Application {
    private static final String TAG = SdkApplication.class.getSimpleName();
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        initSdk();
    }

    private void initSdk(){
        //如果当前进程不是主进程，则不执行初始化操作
        if(!isMainProcess(this)){
            return;
        }

        int appId = AppConfig.APP_ID; // 配置您自己的appid
        String appKey = AppConfig.APP_KEY;// 配置您自己 的appkey
        AppInfo appInfo = new AppInfo();
        appInfo.setAppId(appId);
        appInfo.setAppKey(appKey);
        appInfo.setChannelId(1); //选填，配置渠道,默认为1
        appInfo.setOrientation(1); //0：横屏；1：竖屏
        appInfo.setCanUseAdjunct(true);
        GameSdk.initSdk(this, appInfo,new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                String msg = "";
                if (responseCode == ErrorCode.SUCCESS) {
                    msg = "sdk初始化成功";
                    NeoLog.i(TAG, "init: " + msg);
                } else {
                    msg = "sdk初始化失败（" + ErrorMsgUtil.translate(responseCode) + "）";
                    NeoLog.e(TAG, "init: " + msg);
                }
                Toast.makeText(SdkApplication.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 假设当前APP的包名为cn.nubia.nbgame.sdk，则获取到的进程名称也为：cn.nubia.nbgame.sdk；
     * 如果在AndroidManifest.xml的某个组件(activity,service,receiver,provider)中申明了android:process=":remote"，则获取到的进程名为：cn.nubia.nbgame.sdk:remote
     *
     * 当申明了独立进程名的组件被打开时，Android系统认为一个新的进程被触发了，会首先执行SdkApplication的onCreate()方法，从而导致再次执行初始化，
     * 因此需要通过进程名来控制，确保初始化只被调用一次
     *
     * @param context
     * @return
     */
    String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 建议将初始化、登录、支付等与联运SDK相关的操作放到主进程(即进程名为APP包名cn.nubia.nbgame.sdk的进程)
     * 原因是：不同的进程之间数据是隔离的，而登录或者支付失败等业务需要用到初始化完成后的数据
     * @param context
     * @return
     */
    private boolean isMainProcess(Context context) {
        String pkgName = context.getPackageName();
        String curProcessName = getCurProcessName(context);
        if((null!=curProcessName)&&(curProcessName.equals(pkgName))) {
            NeoLog.d(TAG, "Application isMainProcess: " + curProcessName);
            return true;
        }else{
            NeoLog.d(TAG, "Application isSubProcess: " + curProcessName);
            return false;
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
