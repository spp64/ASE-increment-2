package spp.app.asehackathon;

import java.util.ArrayList;

import spp.app.asehackathon.FoursquareApp.FsqAuthListener;
import spp.app.asehackathon.places.PlaceMapActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button map;
	Button qrCode;
	
	//Foursquare 
	private FoursquareApp mFsqApp;
	private NearbyAdapter mAdapter;
	private ArrayList<FsqVenue> mNearbyList;
	private ProgressDialog mProgress;
	Button authenticate;
	
	public static final String CLIENT_ID = "2GZLBZMS0U2L0MCKGWL5SGXZHAXW2FIMQOQ0IRQMF134GAKT";
	public static final String CLIENT_SECRET = "CTHR0EBVWZZW1ASVCD0WP24HDR1SM3SEZDGPJJXJ3CVXVZHD";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = (Button) findViewById(R.id.map);
		qrCode = (Button) findViewById(R.id.qrCode);

		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, PlaceMapActivity.class);
				startActivity(i);
			}
		});

		qrCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, QRCodeActivity.class);
				startActivity(i);
			}
		});
		
		//Foursquare
		authenticate = (Button)findViewById(R.id.authenticateFsq);
		mFsqApp         = new FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
		 
        //mAdapter        = new NearbyAdapter(this);
        mNearbyList     = new ArrayList<FsqVenue>();
        mProgress       = new ProgressDialog(this);
        mProgress.setMessage("Loading data ...");
        
        FsqAuthListener listener = new FsqAuthListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Connected as " + mFsqApp.getUserName(), Toast.LENGTH_LONG).show();
            }
 
            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };
 
        mFsqApp.setListener(listener);
        
        //get access token and user name from foursquare
        authenticate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFsqApp.authorize();
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
