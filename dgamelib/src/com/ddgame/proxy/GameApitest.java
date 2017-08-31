package com.ddgame.proxy;

import java.io.File;

import android.app.Activity;
import android.os.Environment;

import com.ddgame.sdk.utils.FileUtils;
import com.ddgame.utils.FileIOUtils;

public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;

	public static String TestFilePath = "test";

	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "yayaUserData"
			+ File.separator
			+ TestFilePath + File.separator + "test.log";

	// DB_DIR

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
					+ TestFilePath;
		} else {
			DB_DIR = Environment.getRootDirectory().getPath() + File.separator
					+ "yayaUserData" + File.separator + TestFilePath;
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
	public static GameApitest getGameApitestInstants() {
		if (mGameapitest != null) {
			//mActivity = mactivity;

			return mGameapitest;
		} else {
			//mActivity = mactivity;

			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}
	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

		if (YYcontants.ISDEBUG) {

			File file = new File(DB_DIRPATH);

			if (file.exists()) {
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

			}

		}

	}

	public void sendTest(String type, String value) {

		if (YYcontants.ISDEBUG) {
			File file = new File(DB_DIRPATH);

			if (file.exists()) {
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

			}
		}

	}

}
