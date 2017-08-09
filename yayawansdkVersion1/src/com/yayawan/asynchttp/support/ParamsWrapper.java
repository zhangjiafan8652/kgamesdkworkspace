package com.yayawan.asynchttp.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ParamsWrapper {
	public final ArrayList<NameValue> nameValueArray = new ArrayList();

	ArrayList<NameValuePair> params1 = new ArrayList();

	public ParamsWrapper put(String name, String value) {
		appendToParamsArray(name, value);
		return this;
	}

	public ParamsWrapper put(String name, int value) {
		appendToParamsArray(name, Integer.valueOf(value));
		return this;
	}

	public ParamsWrapper put(String name, boolean value) {
		appendToParamsArray(name, Boolean.valueOf(value));
		return this;
	}

	public ParamsWrapper put(String name, float value) {
		appendToParamsArray(name, Float.valueOf(value));
		return this;
	}

	public ParamsWrapper put(String name, long value) {
		appendToParamsArray(name, Long.valueOf(value));
		return this;
	}

	public ParamsWrapper put(String name, double value) {
		appendToParamsArray(name, Double.valueOf(value));
		return this;
	}

	private ParamsWrapper appendToParamsArray(String name, Object value) {
		if ((name != null) && (value != null) && (!"".equals(name))
				&& (!"".equals(value))) {
			this.nameValueArray.add(new NameValue(name, value));
			this.params1
					.add(new BasicNameValuePair(name, String.valueOf(value)));
		}
		return this;
	}

	public ArrayList<NameValuePair> getparams1() {
		return this.params1;
	}

	public String toString() {
		try {
			return getStringParams();
		} catch (UnsupportedEncodingException exp) {
			exp.printStackTrace();
		}
		return "Params contains unsupported encoding content!";
	}

	public String getStringParams() throws UnsupportedEncodingException {
		return getStringParams("utf-8");
	}

	public String getStringParams(String urlEncoding) throws UnsupportedEncodingException {
		StringBuilder buffer = new StringBuilder();
		for (NameValue param : this.nameValueArray) {
			buffer.append(param.name).append("=")
					.append(URLEncoder.encode(param.value, urlEncoding))
					.append("&");
		}
		if (buffer.length() > 0)
			buffer.deleteCharAt(buffer.length() - 1);
		return buffer.length() > 0 ? buffer.toString() : null;
	}

	public static class NameValue {
		public final String name;
		public final String value;

		public NameValue(String name, Object value) {
			this.name = name;
			this.value = String.valueOf(value);
		}
	}

	public static class PathParam {
		public final ParamsWrapper.NameValue param;
		public final String path;

		public PathParam(String name, Object value, String path) {
			this.param = new ParamsWrapper.NameValue(name, value);
			this.path = path;
		}
	}
}