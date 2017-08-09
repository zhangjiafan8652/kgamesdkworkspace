package com.yayawan.sdk.utils;

import java.io.File;
import java.text.DecimalFormat;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yayawan.sdk.R;
import com.yayawan.sdk.callback.YayaWanUpdateCallback;
import com.yayawan.sdk.domain.Update;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.widget.LoadDialog;
import com.yayawan.sdk.widget.UpdateDialog;

public class UpdateUtil {

	private static LoadDialog progressDialog;

	private static long downloadid;

	private static DownloadManager manager;

	private static ProgressBar mPb;

	private static YayaWanUpdateCallback mUpdateCallback;

	private static int current = 0;

	public static String getVersion(Context context) throws Exception {

		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packageInfo.versionName;

	}

	public static void isUpdate(final Activity context) {
		if (android.os.Build.VERSION.SDK_INT <= 9) {
			return;
		}
		mUpdateCallback = YayaWan.mUpdateCallback;
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				String checkupdate = null;
				try {
					checkupdate = checkupdate(context);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return checkupdate;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				try {
					Update update = parseUpdate(result);
					Yayalog.loger("+++++++++++++"+update.toString());
					int compareVersion = compareVersion(update.ver, getVersion(context));
					Yayalog.loger("+++++++++++++对比后"+compareVersion+"获取到游戏的version为"+getVersion(context));
					
					if (update != null
							&& compareVersion(update.ver, getVersion(context)) > 0) {
						//System.out.println();
						Yayalog.loger("+++++++++++++"+update.isUpdate);
						if (update.isUpdate) {
							Yayalog.loger("+++++++++++++我更新了"+update.isUpdate);
							showDialog(context, update);
						}
						Yayalog.loger("+++++++++++++我走了这里");
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.execute(null, null, null);
	}

	/**
	 * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 * 
	 * @param version1
	 * @param version2
	 * @return
	 */
	public static int compareVersion(String version1, String version2)
			throws Exception {
		if (version1 == null || version2 == null) {
			return 0;
		}
		String[] versionArray1 = version1.split("\\.");// 注意此处为正则匹配，不能用.；
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);// 取最小长度值
		int diff = 0;
		while (idx < minLength
				&& (diff = versionArray1[idx].length()
						- versionArray2[idx].length()) == 0// 先比较长度
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {// 再比较字符
			++idx;
		}
		// 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}

	public static void showDialog(final Activity context, final Update update) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setTitle("更新");

		final UpdateDialog updateDialog = new UpdateDialog(context);
		
		
		if (isWifi(context)) {
			updateDialog.setMessage(update.note + "\r\n文件大小: "
					+ getContentLengthValue(update.size));
		} else {
			updateDialog.setMessage(update.note + "\r\n温馨提示: 当前非WiFi网络,下载会消耗流量"
					+ "\r\n文件大小: " + getContentLengthValue(update.size));
		}
		updateDialog.setSubmit("立即更新", new android.view.View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// 开始下载
				manager = (DownloadManager) context
						.getSystemService(Context.DOWNLOAD_SERVICE);

				DownloadManager.Request request = new DownloadManager.Request(
						Uri.parse(update.pack_url));
				request.setVisibleInDownloadsUi(true);
				request.setShowRunningNotification(true);
				String substring = update.pack_url.substring(update.pack_url
						.lastIndexOf("/") + 1);
				request.setTitle(getApplicationName(context));
				File folder = Environment
						.getExternalStoragePublicDirectory("yayawan");
				if (!folder.exists()) {
					if (folder.isDirectory()) {
						folder.mkdirs();
					}
				}
				request.setDestinationInExternalPublicDir("yayawan", substring);

				downloadid = manager.enqueue(request);

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setCancelable(false);
				progressDialog = new LoadDialog(context);
				progressDialog.setCancelable(false);
				progressDialog.show();
				context.registerReceiver(receiver, new IntentFilter(
						DownloadManager.ACTION_DOWNLOAD_COMPLETE));
				timer.start();
				if (mUpdateCallback != null) {
					mUpdateCallback.onStart();
				}
				if (updateDialog.isShowing()) {
					updateDialog.dismiss();
				}
			}
		});
		updateDialog.setCancelable(false);
		if (update.isUpdate) {

			updateDialog.setCancle("退出游戏",
					new android.view.View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mUpdateCallback != null) {
								mUpdateCallback.onCancel();
							}
							System.exit(0);

						}
					});
		} else {
			updateDialog.setCancle("下次再说",
					new android.view.View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mUpdateCallback != null) {
								mUpdateCallback.onCancel();
							}
							if (updateDialog.isShowing()) {
								updateDialog.dismiss();
							}

						}
					});
		}
		updateDialog.show();
		// builder.create().show();

	}

	static CountDownTimer timer = new CountDownTimer(50000, 2000) {

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {
			DownloadManager.Query query = new DownloadManager.Query();
			int[] bytesAndStatus = new int[] { -1, -1, 0 };
			if (manager != null) {
				Cursor c = manager.query(query);

				if (c.moveToFirst()) {
					bytesAndStatus[0] = c
							.getInt(c
									.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
					bytesAndStatus[1] = c
							.getInt(c
									.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
					bytesAndStatus[2] = c.getInt(c
							.getColumnIndex(DownloadManager.COLUMN_STATUS));
					int speed = bytesAndStatus[0] - current;
					current = bytesAndStatus[0];

					if (progressDialog != null) {
						progressDialog.setProgress(bytesAndStatus[1],
								bytesAndStatus[0],
								getContentLengthValue(speed / 2));
					}

				}
			}

		}

		@Override
		public void onFinish() {
			timer.start();
		}

	};

	public static String checkupdate(Context context) throws Exception {

		String url = UrlUtil.GET_PACKNAME + "?packname="
				+ HEX.toHex(context.getPackageName().getBytes());
		// String url = UrlUtil.GET_PACKNAME + "?packname="
		// + HEX.toHex("com.chilijoy.lolex.yyw".getBytes());

		String post = HttpUtil.get(url, "UTF-8");
		return post;
	}

	public static Update parseUpdate(String result) throws Exception {

		Update update = new Update();

		JSONObject object = new JSONObject(result);

		update.pack_url = object.getString("pack_url");
		update.ver = object.getString("ver");
		update.isUpdate = object.getInt("is_update") == 0 ? false : true;
		update.note = object.getString("notes");
		update.size = object.getLong("size");
		return update;
	}

	private static BroadcastReceiver receiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			// 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				DownloadManager.Query query = new DownloadManager.Query()
						.setFilterById(intent.getLongExtra(
								DownloadManager.EXTRA_DOWNLOAD_ID, 0));
				DownloadManager manager = (DownloadManager) context
						.getSystemService(Context.DOWNLOAD_SERVICE);
				Cursor c = manager.query(query);

				if (c.moveToFirst()) {
					int status = c.getInt(c
							.getColumnIndex(DownloadManager.COLUMN_STATUS));
					String fileName = c
							.getString(c
									.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
					File file = new File(fileName);

					if (mUpdateCallback != null) {
						mUpdateCallback.onSuccess(file);
						timer.cancel();
					}
					install(context, file);
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
			}
			// queryDownloadStatus();
		}
	};

	/**
	 * 安装apk
	 * 
	 * @param file
	 */
	public static void install(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	/**
	 * 将字节 按照大小转换成 M K B
	 * 
	 * @param contentLength
	 * @return
	 */
	public static String getContentLengthValue(long contentLength) {
		// M K B
		DecimalFormat formate = new DecimalFormat("###.00");
		String sizeValue = "";
		if (contentLength > 1024 * 1024) {
			double size = contentLength / (1024 * 1024.00);
			sizeValue = formate.format(size) + "M";
		} else if (contentLength > 1024) {
			double size = contentLength / (1024.00);
			sizeValue = formate.format(size) + "K";
		} else {
			sizeValue = contentLength + "B";
		}
		return sizeValue;
	}

	public static boolean isWifi(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		boolean flag = false;
		if (wifiManager != null) {
			int wifiState = wifiManager.getWifiState();
			if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
				flag = true;
			}
		}
		return flag;
	}

	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}
}
