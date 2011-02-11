/**
 * 
 */
package com.pewpewarrows.convoid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Convore Login Activity
 * Only entry point for Convoid, requires a valid Convore account
 * 
 * @todo: Convore User Registration
 *
 */
public class ConvoreLogin extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convore_login);
		
		final Button loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ProgressDialog loginProgress = ProgressDialog.show(ConvoreLogin.this, "", "Logging in...", true);
			}
		});
	}
}
