package com.yayawan.sdk.callback;

import java.io.File;
import java.io.Serializable;

import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.User;

/**
 * 支付回调
 * 
 * @author wjy
 * 
 */
public interface YayaWanUpdateCallback extends Serializable {
    /**
     * 开始下载后回调
     * 
     * @param paramUser
     *            支付的用户信息
     * @param paramOrder
     *            支付的订单信息
     * @param paramInt
     */
    public abstract void onStart();

    /**
     * 下载成功时的回调
     * 
     * @param paramInt
     */
    public abstract void onSuccess(File file);

    /**
     * 取消下载时的回调
     */
    public abstract void onCancel();
}
