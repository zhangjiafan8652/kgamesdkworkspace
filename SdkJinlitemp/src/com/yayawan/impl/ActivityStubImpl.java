package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.gionee.gamesdk.GamePlatform;
import com.yayawan.proxy.YYWActivityStub;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Handle;

public class ActivityStubImpl implements YYWActivityStub {

	public static String API_KEY = "BD0E5B15B17B4FCC91C6D6A9D41F38EF";

	public static String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIcURblD/6pREcFZWqPQlvnfJ9ReI5fOyGHYJLimdIYGnKggJQAvtRRHPZICZ2WDwQbOKdU4NOSQKQIHq1B4KIh7Sn5AWgUrsA3Hq52qz1ML41h/Xp6MWmwhAbTMVibwssA0lfHWiNpfGITzj3fiYguIFB5ilO0o7lDl6+3AwHojAgMBAAECgYBUpui3HR0pMFsPL581EDC9cRM17LUjmuxLHTiOG4rkv1oHpvVpM3yos6irSyRfIv2h9SuUOwAyyQFaC1JaQKMtSdwBxvLRhCZMCGJgG+k3WK+MBjkMPozXH0/RHcRVJjT2Wlht55GGU7a7FzaP+ipIF861n+d+TzQ7VEXWhsnqWQJBAPltCDs4LSe36kNk8LvFh1UI1BdZsmSjBJBRBPCV7jg3NkREFNfgVPlLtguYgtOyQTWBu8KesObiQ03scs0zGO8CQQCKo7N4+IciiAj/Lyepb/YGdRAN6B9jN5cpBxmf13chH5DZbuOocHLdebDhrxHTKkTwkEtiTZ9KoG9miYaz/yoNAkBqwjtatZHGRVcU35iWllZ1lCDLmc3ce1YRu0vk+heLoj4s/AL7334fckOKNQ4SNGLq1wuZorjPKhtquADvSaarAkAhgaTbhL0sNFJwUhY01hFQyDQEGGiVkxbeXI/t2KY7BCT+19T/nYW1TzbXpHcbYOjedxExBe+Mmq+xyWUb1Xx1AkBiTKI4OGWzOnHoeaBxOdoJurgmgNnh/PVDfNw2TzgSQWRzGgmHmWjFIVy+Vx4s0RAsWUTKBFr4Wmw1bg4ixh+h";

	public static GamePlatform mGamePlatform;

    @Override
    public void applicationInit(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(Activity paramActivity) {
        // TODO Auto-generated method stub

    	API_KEY = DeviceUtil.getGameInfo(paramActivity, "API_KEY");
    	PRIVATE_KEY = DeviceUtil.getGameInfo(paramActivity, "PRIVATE_KEY");


    	mGamePlatform = GamePlatform.getInstance(paramActivity);

    	mGamePlatform.init(ActivityStubImpl.API_KEY);


        Handle.active_handler(paramActivity);

    }

    @Override
    public void onStop(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPause(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestart(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy(Activity paramActivity) {

    }

    @Override
    public void applicationDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(Activity paramActivity, int paramInt1,
            int paramInt2, Intent paramIntent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNewIntent(Intent paramIntent) {
        // TODO Auto-generated method stub

    }

	@Override
	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

}
