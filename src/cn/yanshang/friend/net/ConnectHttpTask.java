package cn.yanshang.friend.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import cn.yanshang.friend.common.MyConstants;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//AsyncTask<String, Integer, String>
public class ConnectHttpTask extends AsyncTask<String, Integer, String> {

	private static final String TAG = "ConnectHttpTask";

	private static final int MAX_RETRY_TIME = 2;
	private int mRetryCount;

	private ConnectHttpListener mListener;

	// private ArrayList<NameValuePair> mKeyValueArray;
	private String mJsonstring;

	private boolean mIsHttpPost;

	private Context mContext;

	public ConnectHttpTask(Context context) {
		mContext = context;
	}

	// ArrayList<NameValuePair> keyValueArray
	public void doPost(ConnectHttpListener listener, String jsonstring,// ArrayList<NameValuePair>
																		// keyValueArray,
			String url) {

		// Map<String,String> keyValueMap
		// ArrayList<NameValuePair> keyValueArray = new
		// ArrayList<NameValuePair>();
		//
		// if(keyValueMap!=null && !keyValueMap.isEmpty()){
		// for(Map.Entry<String, String> entry : keyValueMap.entrySet()){
		// keyValueArray.add(new
		// BasicNameValuePair(entry.getKey(),entry.getValue()));
		// }
		// }

		this.mListener = listener;
		this.mIsHttpPost = true;
		this.mJsonstring = jsonstring;
		this.mRetryCount = 0;

		execute(url);
	}

	public void doGet(ConnectHttpListener listener, String url) {
		this.mListener = listener;
		this.mIsHttpPost = false;
		this.mRetryCount = 0;

		execute(url);
	}

	private ResponseStatus executeHttp(Context context, String uri)
			throws SSLHandshakeException, ClientProtocolException, IOException {
		return mIsHttpPost ? HttpUtils.post(context, uri, mJsonstring)// mKeyValueArray)
				: HttpUtils.get(context, uri);
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String response = null;
		while (response == null && mRetryCount < MAX_RETRY_TIME) {

			if (isCancelled())
				return null;

			try {
				String uri = params[0];
				// BufferedReader bufferedReader=null;//duoyu

				Log.d(TAG, this.toString() + "||mRetryCount=" + mRetryCount);
				Log.d(TAG, this.toString() + "||request=" + uri);

				ResponseStatus responseStatus = executeHttp(mContext, uri);
				HttpResponse httpResp = responseStatus.mHttpResponse;

				if (responseStatus.mStatus != MyConstants.RESPONSE_STATUS_OK) {
					response = MyConstants.RESPONSE_STR_TIMEOUT;
					return response;
				}

				if (httpResp != null && !isCancelled()) {

					int st = httpResp.getStatusLine().getStatusCode();
					Log.d(TAG, this.toString() + "||st=" + st);
					// if (st == HttpStatus.SC_OK) {
					HttpEntity entity = httpResp.getEntity();
					if (entity != null) {

						// try {
						// bufferedReader=new BufferedReader
						// (new InputStreamReader(entity.getContent(), "UTF-8"),
						// 8*1024);
						// String line=null;
						// while ((line=bufferedReader.readLine())!=null) {
						// entityStringBuilder.append(line+"/n");
						// }
						// //利用从HttpEntity中得到的String生成JsonObject
						// resultJsonObject=new
						// JSONObject(entityStringBuilder.toString());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//

						InputStream content = entity.getContent();

						if (content != null) {

							// byte[] btInput =
							// Base64AES.inputStream2byte(content);
							// byte[] btBase = Base64AES.decodeBase64(btInput);
							// byte[] btAES = Base64AES.decryptAES(btBase,
							// Constants.AES_CODE_KEY_ROAD);

							// response = new
							// String(btAES,"ISO-8859-1");//"UTF-8"
							response = convertStreamToString(content);
							break;
						}
					}
					// }
				}
			} catch (SSLHandshakeException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.d(TAG, this.toString() + "||response=" + response);

			mRetryCount++;
		}

		return response;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (mListener != null && !isCancelled()) {
			Log.d(TAG, this.toString() + "||onResponse");
			mListener.onResponse(result);// response
			mListener = null;
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);

		int proValue = values[0];
		Log.d(TAG, this.toString() + "onProgressUpdate " + proValue);

	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();

		if (mListener != null) {
			Log.d(TAG, this.toString() + "||onCancelled");
			mListener.onCancelled();
			mListener = null;
		}
	}

}
