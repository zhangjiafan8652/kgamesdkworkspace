package com.yayawan.sdk.asynchttp;


import com.yayawan.sdk.asynchttp.support.ParamsWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpResponse;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

public class AsyncHttpConnection {
	String result = null;
	URL url = null;

	InputStreamReader in = null;
	int code = 0;

	public static AsyncHttpConnection getInstance() {
		return SingletonProvider.instance;
	}

	public void get(String url, ResponseCallback callback) {

		Oget(url, null, callback);
	}

	public void get(String url, ParamsWrapper params, ResponseCallback callback) {

		Oget(url, params, callback);
	}

	public void post(String url, ParamsWrapper params, ResponseCallback callback) {
		verifyUrl(url);

		Opost(url, params, callback);
	}

	public void Opost(final String url, final ParamsWrapper params, final ResponseCallback callback) {
		new Thread() {
			public void run() {
				HttpPost httpRequest = new HttpPost(url);
				String strResult = null;
				int Code = 0;
				try {
					httpRequest.setEntity(new UrlEncodedFormEntity(params.getparams1(), "UTF-8"));

					DefaultHttpClient client = new DefaultHttpClient();

					client.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
					client.getParams().setParameter("http.socket.timeout", Integer.valueOf(10000));
					HttpResponse httpResponse = client.execute(httpRequest);

					Code = httpResponse.getStatusLine().getStatusCode();

					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader( new InputStreamReader(httpResponse.getEntity().getContent()));
					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						builder.append(s);
					}

					AsyncHttpConnection.this.result = builder.toString();

					URL Ourl = new URL(url.toString());

					callback.onResponse(null, AsyncHttpConnection.this.result, Ourl, Code);
				} catch (ConnectTimeoutException ee) {
					try {
						URL Ourl = new URL(url.toString());
						//callback.onResponse(null, "timeout", Ourl, 1001);
						callback.onFailure("timeout", Ourl, 1001);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				} catch (SocketTimeoutException e) {
					try {
						URL Ourl = new URL(url.toString());
						callback.onFailure("timeout", Ourl, 1001);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					try {
						URL Ourl = new URL(url.toString());
						callback.onFailure("timeout", Ourl, 1002);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}

					e.printStackTrace();
				}
			}
		}.start();
	}

	public void Oget(final String url, final ParamsWrapper params, final ResponseCallback callback) {
		new Thread() {
			public void run() {
				StringBuilder buf = new StringBuilder(url);
				if (params != null) {
					buf.append("?");
					try {
						buf.append(params.getStringParams());
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				}

				HttpGet httpRequest = new HttpGet(buf.toString());

				int Code = 0;

				try {
					DefaultHttpClient client = new DefaultHttpClient();

					client.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));

					client.getParams().setParameter("http.socket.timeout", Integer.valueOf(10000));

					HttpResponse httpResponse = client.execute(httpRequest);

					Code = httpResponse.getStatusLine().getStatusCode();

					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader( new InputStreamReader(httpResponse.getEntity().getContent()));


					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						builder.append(s);
					}

					AsyncHttpConnection.this.result = builder.toString();

					URL Ourl = new URL(url.toString());
					callback.onResponse(null, AsyncHttpConnection.this.result, Ourl, Code);
				} catch (ConnectTimeoutException ee) {
					try {
						URL Ourl = new URL(url.toString());
						callback.onFailure("timeout", Ourl, 1001);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				} catch (SocketTimeoutException e) {
					try {
						URL Ourl = new URL(url.toString());
						callback.onFailure("timeout", Ourl, 1001);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					try {
						URL Ourl = new URL(url.toString());
						callback.onFailure("timeout", Ourl, 1002);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}

					e.printStackTrace();
				}
			}
		}.start();
	}

	private void verifyUrl(String url) {
		if (url == null)
			throw new IllegalArgumentException("Connection url cannot be null");
	}

	private static class SingletonProvider {
		private static AsyncHttpConnection instance = new AsyncHttpConnection();
	}
}
