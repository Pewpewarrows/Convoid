package com.pewpewarrows.convore;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class ConvoreUser {
	public String id;
	public String img;
	public String url;
	public String username;
	
	public static boolean isLoggedIn(HttpClient httpClient) throws ClientProtocolException, IOException {
		HttpGet httpRequest = new HttpGet(ConvoreAPI.ACCOUNT_URL);
	
		HttpResponse response = httpClient.execute(httpRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode == 200) {
			return true;
		}
		
		return false;
	}
	
	public static void login() {
	}
}
