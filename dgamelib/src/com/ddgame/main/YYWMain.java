package com.ddgame.main;

import com.ddgame.callback.YYWAnimCallBack;
import com.ddgame.callback.YYWExitCallback;
import com.ddgame.callback.YYWPayCallBack;
import com.ddgame.callback.YYWUserCallBack;
import com.ddgame.callback.YYWUserManagerCallBack;
import com.ddgame.domain.YYWOrder;
import com.ddgame.domain.YYWRole;
import com.ddgame.domain.YYWUser;

public class YYWMain {

    public static YYWAnimCallBack mAnimCallBack;

    public static YYWUserCallBack mUserCallBack;

    public static YYWUserManagerCallBack mUserManagerCallBack;

    public static YYWPayCallBack mPayCallBack;

    public static YYWExitCallback mExitCallback;

    public static YYWUser mUser;

    public static YYWOrder mOrder;

    public static YYWRole mRole;

    private static final YYWMain mInstance = new YYWMain();

	/**
	 * @param args
	 */
	public static YYWMain getInstance() {
		// TODO Auto-generated method stub
		return mInstance;
	}

}
