package com.yayawan.implyy;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWAnimation;
import com.yayawan.sdk.callback.YayawanStartAnimationCallback;
import com.yayawan.sdk.main.YayaWan;

public class AnimationImplyy implements YYWAnimation {

    @Override
    public void anim(final Activity paramActivity) {
        // TODO Auto-generated method stub
        //Toast.makeText(paramActivity, "播放动画", Toast.LENGTH_SHORT).show();

    	new Handler(Looper.getMainLooper()).post(new Runnable() {

             @Override
             public void run() {

		        YayaWan.animation(paramActivity, new YayawanStartAnimationCallback() {

		            @Override
		            public void onSuccess() {
		                if (YYWMain.mAnimCallBack != null) {
		                    YYWMain.mAnimCallBack.onAnimSuccess("success", "");
		                }
		            }

		            @Override
		            public void onError() {
		                if (YYWMain.mAnimCallBack != null) {
		                    YYWMain.mAnimCallBack.onAnimFailed("failed", "");
		                }
		            }

		            @Override
		            public void onCancel() {
		                if (YYWMain.mAnimCallBack != null) {
		                    YYWMain.mAnimCallBack.onAnimCancel("cancel", "");
		                }
		            }
		        });

             }

             });

    }

}
