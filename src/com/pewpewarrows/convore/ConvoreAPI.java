package com.pewpewarrows.convore;

import com.pewpewarrows.util.AsyncTaskListener;
import com.pewpewarrows.util.RestClient;

import org.json.JSONObject;

public class ConvoreAPI implements AsyncTaskListener<JSONObject> {
	public static final String ROOT_URL = "https://convore.com/api/";
	public static final String ACCOUNT_URL = ROOT_URL.concat("account/verify.json");
	public static final String LIVE_URL = ROOT_URL.concat("live.json");
	
	private String username;
	private String password;
	
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public void getAccount() {
		new RestClient(this, "get", username, password).execute(ACCOUNT_URL);
	}

	public void onTaskCancel() {
	}

	public void onTaskComplete(JSONObject result) {
	}
}
