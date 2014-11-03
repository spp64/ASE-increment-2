package spp.app.asehackathon.places;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSLocation extends Service implements LocationListener {

	public static final int MIN_TIME = 1*60*1000; // Location gets updated every one
											// minute
	public static final int MIN_DIST = 10;// Location gets updated with a
											// minimum change of 10 metres
	private Context mContext;

	private Location location;
	double latitude;
	double longitude;

	private LocationManager locationManager;

	boolean isNetworkProviderEnabled = false;
	boolean isGPSEnabled = false;
	boolean canLockGPS = false;

	public GPSLocation(Context context) {
		mContext = context;
		getLocation();

	}

	private Location getLocation() {
		// TODO Auto-generated method stub
		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);

		// Check if Network Provider is enabled
		isNetworkProviderEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		// Check if GPS is enabled
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!isNetworkProviderEnabled && !isGPSEnabled) {
			
			AlertDialog.Builder builder = new Builder(mContext);
			//Takes you to settings to turn on GPS
			builder.setPositiveButton("Settings", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					mContext.startActivity(i);
				}
			});
			// Closes the dialog box
			builder.setNegativeButton("close", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			builder.setTitle("Cannot Lock Location");
			builder.setMessage("GPS is not enabled. Do you want to enable GPS?");

			AlertDialog dlg = builder.create();
			dlg.show();
		} else {
			canLockGPS = true;
			if (isNetworkProviderEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_DIST, MIN_TIME,
						this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}
			}
			if (isGPSEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, this);
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();

					}
				}
			}
		}
		return location;
	}

	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}
		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public boolean GPSLock() {
		return canLockGPS;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
