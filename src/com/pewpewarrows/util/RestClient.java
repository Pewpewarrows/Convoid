package com.pewpewarrows.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

/**
 * RestClient
 *
 * Inspired by: http://stackoverflow.com/questions/4250968/
 */
public class RestClient extends AsyncTask<String, Void, JSONObject> {
	AsyncTaskListener<JSONObject> callback;
	String reqType;
	List<NameValuePair> postData;
	String username;
	String password;
	
	public RestClient(AsyncTaskListener<JSONObject> callback) {
		this(callback, "get");
	}
	
	public RestClient(AsyncTaskListener<JSONObject> callback, String reqType) {
		this(callback, reqType, "", "");
	}
	
	public RestClient(AsyncTaskListener<JSONObject> callback, String reqType, String username, String password) {
		this(callback, reqType, null, "", "");
	}
	
	public RestClient(AsyncTaskListener<JSONObject> callback, String reqType, List<NameValuePair> postData, String username, String password) {
		this.callback = callback;
		this.reqType = reqType.toLowerCase();
		this.postData = postData;
		this.username = username;
		this.password = password;
		
		if ((this.reqType != "get") && (this.reqType != "post")) {
			// I really don't remember the proper way to handle this in Java...
		}
		
		if ((this.reqType == "post") && (postData == null)) {
			// Same as above, should yell about this or something.
		}
	}
	
    private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the
         * BufferedReader return null which means there's no more data to
         * read. Each line will appended to a StringBuilder and returned as
         * String.
         * 
         * (c) public domain: 
         * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
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
	
	public JSONObject connect(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		JSONObject jsonResult = null;

		if ((username != "") && (password != "")) {
			Credentials creds = new UsernamePasswordCredentials(username, password);
			httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(url, 80, AuthScope.ANY_REALM), 
				creds
			);
		}
		
		try {
			HttpResponse response = null;
			
			if (reqType == "get") {
				HttpGet httpRequest = new HttpGet(url);
				response = httpClient.execute(httpRequest);
			} else if (reqType == "post") {
				HttpPost httpRequest = new HttpPost(url);
				httpRequest.setEntity(new UrlEncodedFormEntity(postData)); 
				response = httpClient.execute(httpRequest);
			}

			HttpEntity entity = response.getEntity();
	
	        if (entity != null) {
	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            String result = convertStreamToString(instream);
	
	            // A Simple JSONObject Creation
	            jsonResult = new JSONObject(result);
	
	            // Closing the input stream will trigger connection release
	            instream.close();
	        	entity.consumeContent();
	        }	
		} catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return jsonResult;
	}

	@Override
	protected JSONObject doInBackground(String... urls) {
		return connect(urls[0]);
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		callback.onTaskComplete(result);
	}
}
