package com.pewpewarrows.convore;

public class ConvoreAPI {
	public static final String ROOT_URL = "https://convore.com/api/";
	
	public static String accountUrl() {
		return ConvoreAPI.ROOT_URL.concat("account/verify.json");
	}

	public static String liveUrl() {
		return ConvoreAPI.ROOT_URL.concat("live.json");
	}
}
