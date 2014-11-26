package cn.yanshang.friend.net;

//import java.io.BufferedReader;
//import java.util.ArrayList;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
//import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.*;

import cn.yanshang.friend.common.MyConstants;
//import org.apache.http.protocol.HTTP;

import android.content.Context;

public abstract class HttpUtils {

	public static ResponseStatus post(Context context, String uri, String json)
	// ArrayList<NameValuePair> keyValueArray
	{
		String encoding = "UTF-8";
		// HTTP.ASCII .UTF_8;
		if ("".equals(uri) || uri == null) {
			return null;
		}

		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.mHttpResponse = null;
		responseStatus.mStatus = MyConstants.RESPONSE_STATUS_OK;

		// UrlEncodedFormEntity entity =null;
		StringEntity entity = null;
		// HttpResponse httpResponse = null;
		HttpClient httpClient = getHttpClient();
		try {
			// entity = new UrlEncodedFormEntity(keyValueArray, encoding);
			entity = new StringEntity(json);// HTTP.UTF_8
			entity.setContentEncoding(encoding);// "UTF-8"
			entity.setContentType("application/json");
			HttpPost httpPost = new HttpPost(uri);

			httpPost.setEntity(entity);
			//
			responseStatus.mHttpResponse = httpClient.execute(httpPost);
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_OK;
		} catch (ConnectTimeoutException e) {
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_TIMEOUT;
		} catch (ClientProtocolException e) {
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_PRO_ERROR;
		} catch (IOException e) {
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_IO_ERROR;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return responseStatus;
	}

	public static ResponseStatus get(Context context, String uri) {
		if ("".equals(uri) || uri == null) {
			return null;
		}

		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.mHttpResponse = null;
		responseStatus.mStatus = MyConstants.RESPONSE_STATUS_OK;

		HttpClient httpClient = getHttpClient();

		StringBuilder urlStr = new StringBuilder(uri);
		// StringBuilder entityStr = new StringBuilder();

		HttpGet httpGet = new HttpGet(urlStr.toString());
		// BufferedReader bufferedReader = null;
		// HttpResponse httpResponse = null;
		try {
			responseStatus.mHttpResponse = httpClient.execute(httpGet);
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_OK;
		} catch (ConnectTimeoutException e) {
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_TIMEOUT;
		} catch (ClientProtocolException e) {
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_PRO_ERROR;
		} catch (IOException e) {
			responseStatus.mStatus = MyConstants.RESPONSE_STATUS_IO_ERROR;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return responseStatus;
	}

	private static HttpClient getHttpClient() {

		HttpParams mHttpParams = new BasicHttpParams();

		// set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(mHttpParams, 15 * 1000);
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(mHttpParams, 15 * 1000);
		HttpConnectionParams.setSocketBufferSize(mHttpParams, 8 * 1024);

		HttpClientParams.setRedirecting(mHttpParams, true);

		HttpClient httpClient = new DefaultHttpClient(mHttpParams);

		return httpClient;
	}

}
