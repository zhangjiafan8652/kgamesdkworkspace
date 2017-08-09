package com.yayawan.sdk.asynchttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.URL;

public abstract class StringResponseHandler implements ResponseCallback {
	protected CookieStore cookieStore;

	public final void onResponse(CookieStore cookieStore, InputStream response, URL url, int code) {
		String data = null;

		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(response));
		try {
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = builder.toString();
		onResponse(data, url, code);
	}

	public final void onResponse(CookieStore cookieStore, String data, URL url, int code) {
		onResponse(data, url, code);
	}

	protected abstract void onResponse(String paramString, URL paramURL, int paramInt);

	//public abstract void onFailure(String paramString, URL paramURL, int paramInt);
}