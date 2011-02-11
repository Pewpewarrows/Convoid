/**
 * 
 */
package com.pewpewarrows.convoid;

import com.pewpewarrows.convore.ConvoreUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
	AlertDialog.Builder mBuilder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convore_login);
		
		final Button loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new LoginProgressTask().execute();
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
				loggedIn = new Boolean(ConvoreUser.isLoggedIn()).toString();
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
	
	private class LoginProgressTask extends AsyncTask<Void, Integer, Boolean> {
		ProgressDialog loginProgress;
		
		@Override
		protected void onPreExecute() {
			loginProgress = ProgressDialog.show(ConvoreLogin.this, "", "Logging in...", true);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			loginProgress.dismiss();
			
			if (result) {
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