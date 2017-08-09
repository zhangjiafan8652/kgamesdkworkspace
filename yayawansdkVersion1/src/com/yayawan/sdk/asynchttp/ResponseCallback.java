package com.yayawan.sdk.asynchttp;

import java.io.InputStream;
import java.net.CookieStore;
import java.net.URL;

public abstract interface ResponseCallback {
	public abstract void onResponse(CookieStore paramCookieStore, InputStream paramInputStream, URL paramURL, int paramInt);

	public abstract void onResponse(CookieStore paramCookieStore, String paramString, URL paramURL, int paramInt);

	public abstract void onFailure(String paramString, URL paramURL, int paramInt);
}
