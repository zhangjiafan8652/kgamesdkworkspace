package com.yayawan.sdk.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.PayMethod;
import com.yayawan.sdk.domain.RoleData;
import com.yayawan.sdk.domain.User;



/**
 * 自定义application
 *
 * @author wjy
 *
 */
public class AgentApp {
    /**
     * 保存每一个打开的activity实例
     */
    // private static List<Activity> activities = new ArrayList<Activity>();

    public static User mUser;

    // public static void addActivity(Activity activity) {
    // activities.add(activity);
    // }

    // public static Activity payMainActivity;

    public static int mentid; // 支付类型

    public static Order mPayOrder; // 支付订单

    public static String mAuthNum; // 验证码

    public static String mSourceId; // 渠道号

    public static RoleData mRoleData;

    public static ArrayList<PayMethod> mPayMethods; // 支付方式

    public static HashMap<String, String> mCardInfos = new HashMap<String, String>();
    // @Override
    // public void onTerminate() {
    // super.onTerminate();
    // Intent floatService = new Intent(getApplicationContext(),
    // FloatService.class);
    // stopService(floatService);
    // for (Activity activity : activities) {
    // if (activity != null) {
    // activity.finish();
    // }
    // }
    // }

}
