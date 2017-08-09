package com.yayawan.sdk.callback;

/**
 * 调用动画回调
 * @author wjy
 *
 */
public interface YayawanStartAnimationCallback {
    /*
     *动画调用成功 
     */
    public abstract void onSuccess();
    /*
     * 动画调用失败
     */
    public abstract void onError();
    /*
     * 动画调用退出
     */
    public abstract void onCancel();
}
