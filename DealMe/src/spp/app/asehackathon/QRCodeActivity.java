package spp.app.asehackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeActivity extends Activity {

	TextView formatText;
	TextView contextText;
	Button scanBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		scanBtn = (Button)findViewById(R.id.scanBtn);
        formatText = (TextView) findViewById(R.id.formatText);
        contextText = (TextView) findViewById(R.id.contextText);
        
        OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.scanBtn){
					//scan
					IntentIntegrator scanIntegrator=new IntentIntegrator(QRCodeActivity.this);
					scanIntegrator.initiateScan();
					}
			}
		};
        scanBtn.setOnClickListener(listener);
	}
	
	 public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		 IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		 if (scanningResult != null) {
			//we have a result
			 String content = scanningResult.getContents();
			 String format = scanningResult.getFormatName();
			 formatText.setText("FORMAT: " + content);
			 contextText.setText("CONTENT: " + format);
			}else{
			    Toast toast = Toast.makeText(QRCodeActivity.this, 
			            "Scan Data not recieved!", Toast.LENGTH_LONG);
			        toast.show();
			    }
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrcode, menu);
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
