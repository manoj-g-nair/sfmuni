package com.sfmuni;

import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;
import android.widget.Toast;
import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SearchResultMap extends MapActivity {
	private Runnable viewSearchResult;
	private ArrayList search_result_list = new ArrayList();

	private ProgressDialog m_ProgressDialog = null;

	private String m_keyword = "";
	private double m_lat = 0.0;
	private double m_lng = 0.0;

	private MapView map;
	private MapController mc;

	private ViewGroup zoom;

	private MyLocationOverlay mylayer;
	private StopOverlay marklayer;
	private List<Overlay> overlays;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void findViews() {
		map = (MapView) findViewById(R.id.mapview);
		mc = map.getController();
		mylayer = new MyLocationOverlay(this, map);
		/*
		 * zoom = (ViewGroup) findViewById(R.id.zoom);
		 * zoom.addView(map.getZoomControls());
		 */
		overlays = map.getOverlays();
		// mylayer = new MyLocationOverlay(this, map);
		mylayer.runOnFirstFix(new Runnable() {
			public void run() {
				// Zoom in to current location
				map.setTraffic(true);
				// mc.setZoom(30);

				// mylayer.draw(canvas, mapView, shadow)
				mc.animateTo(mylayer.getMyLocation());
			}
		});
		overlays.add(mylayer);
		mylayer.enableMyLocation();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stop_map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		findViews();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		m_keyword = bundle.getString("keyword");
		m_lat = bundle.getDouble("lat");
		m_lng = bundle.getDouble("lng");
		// search_result_list=bundle.getParcelableArrayList("result");
		// stop_list = getStops(m_query);
	
		viewSearchResult = new Runnable() {
			@Override
			public void run() {

				search_result_list = BasicFunctions.googleSearch(m_lat, m_lng,
						m_keyword);

				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewSearchResult, "MagentoBackground");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);

	}

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (search_result_list != null && search_result_list.size() > 0) {
				setupMap();
				// Log.v("size", "i have "+path_list.size());
				GoogleSearchResult result = (GoogleSearchResult) search_result_list
						.get((search_result_list.size() / 2));
				GeoPoint p = new GeoPoint((int) (result.getLat() * 1E6),
						(int) (result.getLng() * 1E6));
				// mc.animateTo(p);
				mc.setZoom(15);
				// mc.animateTo(mylayer.getMyLocation());

				m_ProgressDialog.dismiss();
			}

		}
	};

	private void setupMap() {

		Drawable pin = getResources().getDrawable(R.drawable.stop);
		pin.setBounds(-8, -8, 8, 8);
		SearchResultOverLay result = new SearchResultOverLay(pin);
		overlays.add(result);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mylayer.enableMyLocation();

	}

	@Override
	protected void onStop() {
		mylayer.disableMyLocation();

		super.onStop();
	}

	private class SearchResultOverLay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();

		public SearchResultOverLay(Drawable defaultMarker) {
			super(defaultMarker);
			String text = "";
			for (int i = 0; i < search_result_list.size(); i++) {
				GoogleSearchResult result = (GoogleSearchResult) search_result_list
						.get(i);
				GeoPoint gp = new GeoPoint((int) (result.getLat() * 1000000),
						(int) ((result.getLng() * 1000000)));

				items.add(new OverlayItem(gp, result.getTitle(),
						"Search Result"));
			}
			populate();
			// TODO Auto-generated constructor stub
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return items.get(i);

		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		protected boolean onTap(int pIndex) {
			GoogleSearchResult result = (GoogleSearchResult) search_result_list
					.get(pIndex);

			String text = "";

			text += result.getTitle() + "\n";
			text += result.getStreetAddress() + " " + result.getCity() + " "
					+ result.getRegion() + "\n";
			ArrayList phone = result.getPhonenumbers();
			text += "phone number: ";
			for (int i = 0; i < phone.size(); i++) {
				text += " " + phone.get(i).toString();
			}

			Toast.makeText(SearchResultMap.this, text, Toast.LENGTH_LONG)
					.show();
		//	GeoPoint fromGeoPoint=new GeoPoint((int)(m_lat*1000000),(int)(m_lng*1000000));
		//	GeoPoint toGeoPoint=new GeoPoint((int)(result.getLat()*1000000),(int)(result.getLng()*1000000));
		//	Intent intent=new Intent();
		//	intent.setAction(android.content.Intent.ACTION_VIEW);
		//	intent.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr="+GeoPointToString(fromGeoPoint)+"&daddr="+GeoPointToString(toGeoPoint)+"&hl=en"));
		//	SearchResultMap.this.startActivity(intent);
			return true;
		}

		private String GeoPointToString(GeoPoint gp) {
			String strReturn = "";
			try {
				if (gp != null) {
					double geoLatitude = (int) gp.getLatitudeE6() / 1000000;
					double geoLongitude = (int) gp.getLongitudeE6() / 1000000;
					strReturn = String.valueOf(geoLatitude) + ","
							+ String.valueOf(geoLongitude);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return strReturn;
		}

	}

}
