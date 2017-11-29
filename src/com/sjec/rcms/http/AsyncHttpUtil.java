package com.sjec.rcms.http;

import java.security.KeyStore;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class AsyncHttpUtil {

	private static final int TIME_OUT_INTERVAL = 10000;

	/**
	 * Important! This method execute the get action!
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @param responseHandler
	 */
	public void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		try {
			AsyncHttpClient client = new AsyncHttpClient();

			// KeyStore trustStore =
			// KeyStore.getInstance(KeyStore.getDefaultType());
			// trustStore.load(null, null);
			// SSLSocketFactory sf = new SSLSocketFactory(trustStore);
			// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// client.setSSLSocketFactory(sf);

			client.setTimeout(TIME_OUT_INTERVAL);
			client.setMaxConnections(10);

			client.getHttpClient()
					.getParams()
					.setParameter("http.protocol.allow-circular-redirects",
							true);
			
			LogUtil.d("%s?%s", url, params.toString());

			RequestHandle rh = client.post(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Important! This method execute the post action!
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @param responseHandler
	 */
	public void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		try {
			AsyncHttpClient client = new AsyncHttpClient();

			// KeyStore trustStore =
			// KeyStore.getInstance(KeyStore.getDefaultType());
			// trustStore.load(null, null);
			// SSLSocketFactory sf = new SSLSocketFactory(trustStore);
			// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// client.setSSLSocketFactory(sf);

			client.setTimeout(TIME_OUT_INTERVAL);
			client.getHttpClient()
					.getParams()
					.setParameter("http.protocol.allow-circular-redirects",
							true);
			LogUtil.d("%s?%s", url, params.toString());

			client.get(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void getSync(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		try {
			SyncHttpClient client = new SyncHttpClient();

			// KeyStore trustStore =
			// KeyStore.getInstance(KeyStore.getDefaultType());
			// trustStore.load(null, null);
			// SSLSocketFactory sf = new SSLSocketFactory(trustStore);
			// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// client.setSSLSocketFactory(sf);

			client.setTimeout(TIME_OUT_INTERVAL);
			client.getHttpClient()
					.getParams()
					.setParameter("http.protocol.allow-circular-redirects",
							true);
			LogUtil.d("%s?%s", url, params.toString());

			client.get(url, params, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
