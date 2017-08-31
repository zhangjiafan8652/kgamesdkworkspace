package com.ddgame.proxy;

import com.ddgame.impl.ActivityStubImpl;
import com.ddgame.impl.AnimationImpl;
import com.ddgame.impl.ChargerImpl;
import com.ddgame.impl.LoginImpl;
import com.ddgame.impl.UserManagerImpl;



public class GameProxy extends CommonGameProxy {

    private static GameProxy proxy;

    private GameProxy(YYWLoginer login, YYWActivityStub stub, YYWUserManager userManager, YYWCharger charger) {
        super(login, stub, userManager, charger);
    }

    public static CommonGameProxy getInstent() {

        if (proxy == null) {
            proxy = new GameProxy(new LoginImpl(), new ActivityStubImpl(), new UserManagerImpl(), new ChargerImpl());
            proxy.setAnimation(new AnimationImpl());
        }

        return proxy;
    }

}
