package com.sfmuni;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class NearestStopMap extends MapActivity {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private android.location.Location m_location;

	private ArrayList stop_list ;
	private double m_lat = 0.0;
	private double m_lng = 0.0;
	private MapView map;
	private MapController mc;
	private MyLocationOverlay mylayer;

	private ProgressDialog m_ProgressDialog = null;
	private String m_strLocationProvider;
	private List<Overlay> overlays;
	private Runnable search;
	private int old_size = 0;
	private BusStopOverLay busstop;
	private Handler handler = new Handler();
	private boolean dismissed = false;
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.find_nearest);
		stop_list = new ArrayList();
		
		findViews();
		
		Log.v("create", "create");
		stop_list = new ArrayList();
		map.setBuiltInZoomControls(true);
		
		map.setClickable(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
		getLocationProvider();
		setupMap();
		locationListener = new LocationListener() {

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
				m_lat = m_location.getLatitude();
				m_lng = m_location.getLongitude();
			}
		};
		locationManager.requestLocationUpdates(m_strLocationProvider, 1000, 0,
				locationListener);
		m_lat = m_location.getLatitude();
		m_lng = m_location.getLongitude();

		search = new Runnable() {
			@Override
			public void run() {

				m_lat = m_location.getLatitude();
				m_lng = m_location.getLongitude();

				stop_list = BasicFunctions.getNearestStop(m_lat, m_lng);

			}
		};
		Timer updateTimer = new Timer("update nearest stops");
		updateTimer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				m_lat = m_location.getLatitude();
				m_lng = m_location.getLongitude();
				//if (!BasicFunctions.finishedParse) {
					Log.v("timer run", "timer run");
					stop_list = BasicFunctions.returnData();

					if (stop_list.size() > old_size) {
						handler.post(new Runnable() {
							public void run() {
								Log.v("start to dismiss", "start to dismiss");
								if(!dismissed)
								{
									m_ProgressDialog.dismiss();
									dismissed=false;
								}
								for (int i = old_size; i < stop_list.size(); i++) {
									Stop stop = (Stop) stop_list.get(i);
									GeoPoint gp = new GeoPoint((int) (stop
											.getLAT() * 1000000), (int) (stop
											.getLON() * 1000000));
									OverlayItem item = new OverlayItem(gp,
											"stop", null);
									busstop.addItem(item);

								}
								busstop.setStoplist(stop_list);
								old_size = stop_list.size();
								map.getOverlays().add(busstop);
								map.postInvalidate();
							
							}
						});

					}

			}
		}, 0, 1000);
	

		Thread thread2 = new Thread(null, search, "MagentoBackground");
		thread2.start();

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);
	

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

			m_location = locationManager
					.getLastKnownLocation(m_strLocationProvider);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setupMap() {

		Drawable pin = getResources().getDrawable(R.drawable.busstop);
		pin.setBounds(0, 0, 12, 12);

		busstop = new BusStopOverLay(pin);
		overlays.add(busstop);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("resumed", "resumed");
		mylayer.enableMyLocation();

	}

	private void findViews() {
		map = (MapView) findViewById(R.id.mapview);
		mc = map.getController();
		overlays = map.getOverlays();
		overlays.clear();
		
		mylayer = new MyLocationOverlay(this, map);
		mylayer.runOnFirstFix(new Runnable() {
			public void run() {
				// Zoom in to current location
				map.setTraffic(true);
				mc.setZoom(15);
				mc.animateTo(mylayer.getMyLocation());
			}
		});
		overlays.add(mylayer);
		map.postInvalidate();
	}

	@Override
	protected void onStop() {

		mylayer.disableMyLocation();

		super.onStop();
	}

	private class BusStopOverLay extends ItemizedOverlay<OverlayItem> {
		private ArrayList stop_list;
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker;
		private NearestStopMap stopmap;

		public void setStoplist(ArrayList stoplist) {
			this.stop_list = stoplist;
		}

		public BusStopOverLay(Drawable defaultMarker) {
			// super(defaultMarker);
			super(defaultMarker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub

			return items.get(i);

		}

		public void addItem(OverlayItem item) {
			items.add(item);
			populate();

		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// boundCenterBottom(marker);
			super.draw(canvas, mapView, shadow);

		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		protected boolean onTap(int pIndex) {
			Stop stop = (Stop) this.stop_list.get(pIndex);
			Log.v("stop.getRouteTag()", "111" + (stop.getRouteTag() == ""));
			Log.v("stop.getTag()", stop.getTag());
			ArrayList prediction_list = BasicFunctions.getPrediction(stop
					.getRouteTag(), stop.getTag());
			String text = "";
			if (prediction_list.size() >= 0) {
				if (prediction_list.size() > 0) {
					MuniPrediction pre = (MuniPrediction) prediction_list
							.get(0);

					if (pre.getDirectionTitle() != null) {
						text += "Direction:  " + pre.getDirectionTitle() + "\n";
					}
					text += "Route:      " + pre.getRouteTitle() + "\n";
					text += "Stop:    	 " + pre.getStopTitle() + "\n";
					// Log.v("trackedInfo",
					// "lalal "+(pre.getTrackedInfo().size()));
					text += "\n";
					text += "\n";
					if (pre.getTrackedInfo().size() == 0) {
						text += "no available prediction for this stop" + "\n";
					} else {
						text += "Tracked vehicles in:" + "\n";
						for (int i = 0; i < pre.getTrackedInfo().size(); i++) {
							text += "" + pre.getTrackedInfo().get(i).toString()
									+ "\n";

						}
					}
				} else {

					text += "Route:      " + stop.getRouteTitle() + "\n";
					text += "Stop:    	 " + stop.getTitle() + "\n";
				}

				Toast.makeText(NearestStopMap.this, text, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(NearestStopMap.this, "currently no data!",
						Toast.LENGTH_LONG).show();
			}

			return true;
		}

	}
}
