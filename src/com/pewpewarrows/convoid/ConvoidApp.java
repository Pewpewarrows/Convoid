package com.pewpewarrows.convoid;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.app.Application;
import android.content.SharedPreferences;

public class ConvoidApp extends Application {
	private static String TAG = "Convoid";
	private HttpClient httpClient;
	private SharedPreferences settings;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		httpClient = createHttpClient();
		settings = getSharedPreferences(TAG, 0);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		
		shutdownHttpClient();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		shutdownHttpClient();
	}
	
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	public SharedPreferences getSettings() {
		return settings;
	}
	
	private HttpClient createHttpClient() {
	    DefaultHttpClient client = new DefaultHttpClient();
	    ClientConnectionManager mgr = client.getConnectionManager();
	    HttpParams params = client.getParams();

	    return new DefaultHttpClient(
	    		new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), 
	    		params);
	}
	
	private void shutdownHttpClient() {
		if ((httpClient != null) && (httpClient.getConnectionManager() != null)) {
			httpClient.getConnectionManager().shutdown();
		}
	}
}
