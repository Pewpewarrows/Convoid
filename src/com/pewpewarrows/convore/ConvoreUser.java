package com.pewpewarrows.convore;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ConvoreUser {
	public int id;
	public String img;
	public String url;
	public String username;
	
	public static boolean isLoggedIn() throws ClientProtocolException, IOException {
		HttpGet httpRequest = new HttpGet(ConvoreAPI.accountUrl());
		HttpClient httpclient = new DefaultHttpClient();
	
		HttpResponse response = httpclient.execute(httpRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode == 200) {
			return true;
		}
		
		return false;
	}
}
