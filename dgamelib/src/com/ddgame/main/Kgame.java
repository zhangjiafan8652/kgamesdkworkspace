package com.ddgame.main;

import com.ddgame.proxy.CommonGameProxy;





public class Kgame {

	private static final CommonGameProxy mInstance = new CommonGameProxy();

	/**
	 * @param args
	 */
	public static CommonGameProxy getInstance() {
		// TODO Auto-generated method stub
		
		return mInstance;
	}

}
