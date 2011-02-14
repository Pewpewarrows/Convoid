package com.pewpewarrows.convoid;

import com.pewpewarrows.convore.ConvoreUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Convore Login Activity
 * 
 * Only entry point for Convoid, requires a valid Convore account to continue
 * using the application.
 * 
 * TODO: Convore User Registration
 *
 */
public class ConvoreLogin extends Activity {
	private AlertDialog.Builder mBuilder;
	private ConvoidApp app;
	private String username;
	private String password;
	private Boolean rememberMe = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convore_login);
		
		app = (ConvoidApp) getApplication();
		
		final Context cx = this;
		
		this.username = app.getSettings().getString("username", "");
		this.password = app.getSettings().getString("password", "");
		
		if (this.username != "") {
			new LoginProgressTask(cx).execute(this.username, this.password);
		}
		
		final Button loginButton = (Button) findViewById(R.id.login_button);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		final CheckBox rememberMe = (CheckBox) findViewById(R.id.remember_me);
		
		// rememberMe.isChecked()
		
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (rememberMe.isChecked()) {
					setCredentials(username.getText().toString(), password.getText().toString());
				}
				
				new LoginProgressTask(cx).execute(username.getText().toString(), password.getText().toString());
			}
		});
		
		mBuilder = new AlertDialog.Builder(this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		/*
		 * We want this here instead of onCreate in case the User ever accidentally
		 * gets back to this Activity.
		 */
		new CheckLoginProgressTask().execute();
	}
	
	private void setCredentials(String username, String password) {
		// This will only be called if the rememberMe button is checked
		rememberMe = true;
		this.username = username;
		this.password = password;
	}
	
	public Boolean willRemember() {
		return rememberMe;
	}
	
	public void saveCredentials() {
		SharedPreferences.Editor editor = app.getSettings().edit();
		editor.putString("username", username);
		editor.putString("password", password);
		
		editor.commit();
	}
	
	private class CheckLoginProgressTask extends AsyncTask<Void, Integer, String> {
		ProgressDialog checkProgress;
		
		@Override
		protected void onPreExecute() {
			checkProgress = ProgressDialog.show(ConvoreLogin.this, "", "Contacting Convore...", true);
		}
		
		/*
		 * Check if the User is already logged into Convore, and kills the app
		 * if Convore is either offline or there is no network access.
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			String loggedIn = "false";
			
			try {
				loggedIn = new Boolean(ConvoreUser.isLoggedIn(app.getHttpClient())).toString();
			} catch (Exception e) {
				loggedIn = "error";
			}
			
			return loggedIn;
		}
		
		@Override
		protected void onPostExecute(String result) {
			checkProgress.dismiss();
			
			if (result == "true") {
				// Spawn the main Convoid Activity and kill self
				Intent i = new Intent(ConvoreLogin.this, Convoid.class);
				startActivity(i);
				finish();
			} else if (result == "error") {
				mBuilder.setMessage("Convore is either not available, or you currently don't have Internet access.")
					.setCancelable(false)
					.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							ConvoreLogin.this.finish();
						}
					})
					.show();
			}
		}
	}
	
	private class LoginProgressTask extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog loginProgress;
		Context cx;
		
		public LoginProgressTask(Context cx) {
			this.cx = cx;
		}
		
		@Override
		protected void onPreExecute() {
			loginProgress = ProgressDialog.show(ConvoreLogin.this, "", "Logging in...", true);
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			loginProgress.dismiss();
			
			if (result) {
				if (((ConvoreLogin) cx).willRemember()) {
					((ConvoreLogin) cx).saveCredentials();
				}
				
				// Spawn the main Convoid Activity and kill self
				Intent i = new Intent(ConvoreLogin.this, Convoid.class);
				startActivity(i);
				finish();
			} else {
				// Show clean Error Message
				mBuilder.setMessage("The Username or Password is incorrect.")
					.setCancelable(false)
					.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					})
					.show();
			}
		}
	}
}