package com.yayawan.proxy;

import java.io.File;

import android.app.Activity;
import android.os.Environment;

import com.kkgame.sdk.utils.FileUtils;

public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;

	

	public static String TestFilePath = "test";

	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "yayaUserData"
			+ File.separator
			+ TestFilePath
			+ File.separator
			+ "test.log";
	
	//DB_DIR
	
	public static String DB_DIR = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "yayaUserData"
			+ File.separator
			+ TestFilePath;
	static {
		
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			DB_DIR = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + "yayaUserData" + File.separator
					+TestFilePath;
		} else {
			DB_DIR = Environment.getRootDirectory().getPath() + File.separator
					+ "yayaUserData" + File.separator
					+TestFilePath;
		}

		File dbFolder = new File(DB_DIR);
		if (!dbFolder.exists()) {
			dbFolder.mkdirs();
		}
	}

	public static GameApitest getGameApitestInstants(Activity mactivity) {
		if (mGameapitest != null) {
			mActivity = mactivity;

			return mGameapitest;
		} else {
			mActivity = mactivity;

			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}

	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

	/*	if (YYcontants.ISDEBUG) {
			//Toast.makeText(mActivity, "测试环境调用接口：", duration)
			if (!FileUtils.isFileExists(DB_DIRPATH)) {
				File file = new File(DB_DIRPATH);
			}
			FileUtils.writeFileFromString(DB_DIRPATH, type+"\r\n", true);
		}*/

	}

	public void sendTest(String type, String value) {

		/*if (YYcontants.ISDEBUG) {
			if (!FileUtils.isFileExists(DB_DIRPATH)) {
				File file = new File(DB_DIRPATH);
			}
			FileUtils.writeFileFromString(DB_DIRPATH, type+":"+value+"\r\n", true);
				}
*/
	}

}
