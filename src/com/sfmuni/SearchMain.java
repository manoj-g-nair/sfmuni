package com.sfmuni;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchMain extends Activity {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private android.location.Location m_location;
	private ArrayList m_result;
	private String m_strLocationProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		getLocationProvider();
		locationListener = new LocationListener()

		{

			public void onProviderDisabled(String provider) {
				// called when the provider be disabled by user

			}

			public void onProviderEnabled(String provider) {
				// called when the provider be enabled
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(android.location.Location loc) {

				m_location = loc;
				Log.v("location changed", "location changed");
			}
		};
		locationManager
				.requestLocationUpdates(m_strLocationProvider, 2000, 0, locationListener);

		//
		Button btnSearch = (Button) findViewById(R.id.searchBtn);
		btnSearch.setOnClickListener(searchClick);
	}

	public void getLocationProvider() {
		try {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			
			m_strLocationProvider = locationManager.getBestProvider(criteria,
					true);
			Log.v("m_strLocationProvider", m_strLocationProvider);
			m_location = locationManager
					.getLastKnownLocation(m_strLocationProvider);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener searchClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			EditText ed = (EditText) findViewById(R.id.searchText);
			if (ed.getText().toString() != "") {
				Log.v("search", ed.getText().toString());
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("keyword", ed.getText().toString());

				// m_location =
				// locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Log.v("m_location", "location: " + (m_location == null));
				if (m_location != null) {
					bundle.putDouble("lat", m_location.getLatitude());
					bundle.putDouble("lng", m_location.getLongitude());// Log.v("location",
																		// "latitude: "+m_location.getLatitude());
					// m_result=BasicFunctions.googleSearch(m_location,ed.getText().toString());
					// /Log.v("search result","i have "+m_result.size());
					intent.setClass(SearchMain.this, SearchResult.class);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					// Log.v("location", "null");
				}
			}
			else
			{
				Toast.makeText(SearchMain.this, "input the search keyword", Toast.LENGTH_SHORT);
			}
			

		}

	};
}
