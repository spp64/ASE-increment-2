package spp.app.asehackathon.places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import spp.app.asehackathon.FoursquareApp;
import spp.app.asehackathon.FsqVenue;
import spp.app.asehackathon.NearbyAdapter;
import spp.app.asehackathon.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


public class PlaceMapActivity extends Activity implements OnCameraChangeListener {

	GooglePlaces googlePlaces;

	PlacesList placesList;
	GoogleMap googleMap;

	AlertDialogManager alert = new AlertDialogManager();

	GPSLocation gpsLocation;

	double localLatitude;
	double localLongitude;

	ProgressDialog pDialog;


	AddItemizedOverlay itemizedOverlay;

	double latitude;
	double longitude;
	EditText searchText;

	ArrayList<HashMap<String, String>> placesArrayList = new ArrayList<HashMap<String, String>>();

	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	
	//Speech
	private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    
    //FourSquare
    private NearbyAdapter mAdapter;
    private ArrayList<FsqVenue> mNearbyList;
    private ProgressDialog mProgress;
    private FoursquareApp mFsqApp; 
    

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_map);
		
		//FourSquare
        mNearbyList     = new ArrayList<FsqVenue>();
        mProgress       = new ProgressDialog(this);

		gpsLocation = new GPSLocation(this);
		localLatitude = gpsLocation.getLatitude();
		localLongitude = gpsLocation.getLongitude();
		Log.d("Your Location", "latitude:" + localLatitude + ", longitude: " + localLongitude);

		loadNearbyPlaces(localLatitude, localLongitude);
		Log.d("FourSquare Places","Size :"+ mNearbyList.size());
		
		new LoadPlaces().execute();
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		searchText = (EditText) findViewById(R.id.enterAddress);
		 
        getActionBar().hide();
 
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
		// /PP add to Infobox
		/* *//**
		 * ListItem click event On selecting a listitem SinglePlaceActivity
		 * is launched
		 * */
		/*
		 * lv.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { // getting values from selected ListItem
		 * String reference = ((TextView)
		 * view.findViewById(R.id.reference)).getText().toString();
		 * 
		 * // Starting new intent Intent in = new
		 * Intent(getApplicationContext(), SinglePlaceActivity.class);
		 * 
		 * // Sending place refrence id to single place activity // place
		 * refrence id used to get "Place full details"
		 * in.putExtra(KEY_REFERENCE, reference); startActivity(in); } });
		 */

		
		googleMap= ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		Log.d("Points","three");
		//maps v2
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            
		     public void onCameraChange(CameraPosition arg0) {
		    googleMap.animateCamera(CameraUpdateFactory.zoomTo(8));
		        googleMap.setOnCameraChangeListener(PlaceMapActivity.this);
		      }
		});
		Log.d("Points","three.1");
		//Speech
		
        Log.d("Points","three.2");


        
        //Overlay ApiV2
        LatLng youLocation = new LatLng(localLatitude,localLongitude);
        
        Log.d("Points","four.0");
        googleMap.addMarker(new MarkerOptions().position(youLocation).title("Your Location").snippet("That is you!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Log.d("Points","four");
        CameraUpdate updateCamera = CameraUpdateFactory.newLatLngZoom(youLocation, 10);
        googleMap.animateCamera(updateCamera);
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        // check for null in case it is null
        Log.d("Points","five");
      
	}

	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PlaceMapActivity.this);
			pDialog.setMessage(Html
					.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();

			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				//
				String types = "cafe|restaurant"; // Listing places only cafes, restaurants

				// Radius in meters - increase this value if you don't find any
				// places
				double radius = 5000; // 1000 meters

				// get nearest places
				placesList = googlePlaces.getPlaces(localLatitude,
						localLongitude, radius, types);
			
			} catch (Exception e) {
				Log.d("Exception","Exception:" +e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = placesList.status;
					Log.d("Points","four.10000");
					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						if (placesList.results != null) {
							// loop through each place
							for (PlaceObjects p : placesList.results) {
								HashMap<String, String> map = new HashMap<String, String>();

								// Place reference won't display in listview -
								// it will be hidden
								// Place reference is used to get
								// "place full details"
								map.put(KEY_REFERENCE, p.reference);

								// Place name
								map.put(KEY_NAME, p.name);

								// adding HashMap to ArrayList
								placesArrayList.add(map);
							}
							// /PP add to infobox
							/*
							 * // list adapter ListAdapter adapter = new
							 * SimpleAdapter(PlaceMapActivity.this,
							 * placesListItems, R.layout.list_item, new String[]
							 * { KEY_REFERENCE, KEY_NAME}, new int[] {
							 * R.id.reference, R.id.name });
							 * 
							 * // Adding data into listview
							 * lv.setAdapter(adapter);
							 */
						}
					} else if (status.equals("ZERO_RESULTS")) {
						// Zero results found
						alert.showAlertDialog(PlaceMapActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places");
					} else if (status.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error", "Sorry unknown error occured.");
					} else if (status.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error",
								"Sorry query limit to google places is reached");
					} else if (status.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error",
								"Sorry error occured. Request is denied");
					} else if (status.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error",
								"Sorry error occured. Invalid Request");
					} else {
						alert.showAlertDialog(PlaceMapActivity.this,
								"Places Error", "Sorry error occured.");
					}
				}
			});
			
			 if (placesList.results != null) {
		            // loop through all the places
		            for (PlaceObjects place : placesList.results) {
		                latitude = place.geometry.location.lat; // latitude
		                longitude = place.geometry.location.lng; // longitude
		                 
		                
		                
		                //API v2
		                LatLng location = new LatLng(latitude, longitude);
		                googleMap.addMarker(new MarkerOptions().position(location).title(place.name).snippet(place.vicinity).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		                 
		                 
		              
		            }
		          
		        }
			if(mNearbyList.size() != 0){
				for(FsqVenue venue : mNearbyList){
					Location mlocation = venue.location;
					double latitude = mlocation.getLatitude();
					double longitude = mlocation.getLongitude();
					
					LatLng location = new LatLng(latitude, longitude);
					googleMap.addMarker(new MarkerOptions().position(location).title(venue.name).snippet("by Foursquare").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
				}
			}
		}
	}

	class AlertDialogManager {
		public void showAlertDialog(Context context, String title,
				String message) {
			AlertDialog.Builder builder = new Builder(context);

			builder.setPositiveButton("Okay", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			builder.setTitle(title);
			builder.setMessage(message);

			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}
	
	//Speech
	  private void promptSpeechInput() {
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
	                getString(R.string.speech_prompt));
	        try {
	            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
	        } catch (ActivityNotFoundException a) {
	            Toast.makeText(getApplicationContext(),
	                    getString(R.string.speech_not_supported),
	                    Toast.LENGTH_SHORT).show();
	        }
	    }
	 
	    /**
	     * Receiving speech input
	     * */
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	 
	        switch (requestCode) {
	        case REQ_CODE_SPEECH_INPUT: {
	            if (resultCode == RESULT_OK && null != data) {
	 
	                ArrayList<String> result = data
	                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	                //PP use the result
	                searchText.setText(result.get(0));
	                
	            }
	            break;
	        }
	 
	        }
	    }

	 //FourSquare 
	    private void loadNearbyPlaces(final double latitude, final double longitude) {
	        mProgress.show();
	 
	        new Thread() {
	            @Override
	            public void run() {
	                int what = 0;
	 
	                try {
	 
	                    
						mNearbyList = mFsqApp.getNearby(latitude, longitude);
	                } catch (Exception e) {
	                    what = 1;
	                    e.printStackTrace();
	                }
	 
	            }
	        }.start();
	    }
	 
	    
	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_map, menu);
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

	
	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		
	}
}
