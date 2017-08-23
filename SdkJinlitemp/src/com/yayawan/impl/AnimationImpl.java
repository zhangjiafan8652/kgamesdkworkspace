package com.yayawan.impl;

import android.app.Activity;
import android.widget.Toast;

import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWAnimation;

public class AnimationImpl implements YYWAnimation {

    @Override
    public void anim(Activity paramActivity) {
        // TODO Auto-generated method stub
        //Toast.makeText(paramActivity, "播放动画", Toast.LENGTH_SHORT).show();
    	YYWMain.mAnimCallBack.onAnimSuccess("", "");
    }

}
