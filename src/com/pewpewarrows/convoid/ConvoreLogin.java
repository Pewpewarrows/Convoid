/**
 * 
 */
package com.pewpewarrows.convoid;

import com.pewpewarrows.convore.ConvoreUser;

import android.app.Activity;
import android.app.ProgressDialog;
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
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		new CheckLoginProgressTask().execute();
	}
	
	// TODO: Make this Public so we can check if logged-in on every Activity?
	private class CheckLoginProgressTask extends AsyncTask<Void, Integer, Boolean> {
		ProgressDialog checkProgress;
		
		@Override
		protected void onPreExecute() {
			checkProgress = ProgressDialog.show(ConvoreLogin.this, "", "Contacting Convore...", true);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean loggedIn = ConvoreUser.isLoggedIn();
			
			return loggedIn;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			checkProgress.dismiss();
			
			if (result) {
				// Spawn the main Convoid Activity and kill self
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
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			loginProgress.dismiss();
			
			if (result) {
				// Spawn the main Convoid Activity and kill self
			}
		}
	}
}