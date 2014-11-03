package spp.app.asehackathon;

import android.content.Context;
import android.os.AsyncTask;

public class LoginResult extends AsyncTask<String, Void, String>{

	private Context mContext;
	
	public static final String MAIN_URL ="";
	
	public LoginResult(Context context){
		mContext = context;
	}
	 
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
