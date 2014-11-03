package spp.app.asehackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	Button mainActivity;
	TextView register;
	boolean loginSuccess = true;
	EditText username;
	EditText password;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mainActivity = (Button) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);
		username = (EditText)findViewById(R.id.username);
		password =(EditText)findViewById(R.id.password);
		
		
		
		mainActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(loginSuccess){
				Intent i = new Intent(LoginActivity.this,MainActivity.class );
				startActivity(i);
				}
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
