package com.sfmuni;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class RouteMap extends MapActivity {
	private ArrayList vehicle_list = new ArrayList();
	private ArrayList stop_list=new ArrayList();
	private ArrayList path_list=new ArrayList();
	private String m_direction="";
	private String m_query = "";

	private Runnable viewStops;
	private ProgressDialog m_ProgressDialog = null;

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
		overlays = map.getOverlays();
		mylayer = new MyLocationOverlay(this, map);
		mylayer.runOnFirstFix(new Runnable() {
			public void run() {
				// Zoom in to current location
				map.setTraffic(true);
				
				mc.setZoom(14);
			
			}
		});
		overlays.add(mylayer);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		findViews();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		m_query = bundle.getString("route");
		m_direction=bundle.getString("direction");
		// stop_list = getStops(m_query);

		viewStops = new Runnable() {
			@Override
			public void run() {

				vehicle_list = BasicFunctions.getVehicles(m_query);
				stop_list=BasicFunctions.getStops(m_query,m_direction);
				path_list=BasicFunctions.getPaths(m_query);
				
				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewStops, "MagentoBackground");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, Menu.FIRST, Menu.NONE, R.string.prediction_menu_1);
	

		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case(Menu.FIRST):
			vehicle_list.clear();
			vehicle_list = BasicFunctions.getVehicles(m_query);
		    setupMap();
		    //refreshMapview();
		    map.invalidate();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (stop_list != null && path_list.size() > 0) {
				setupMap();
				Log.v("size", "i have " + path_list.size());
				Stop stop = (Stop) stop_list.get((stop_list.size() / 2));
				GeoPoint p = new GeoPoint((int) (stop.getLAT() * 1E6),
						(int) (stop.getLON() * 1E6));
				mc.animateTo(p);
				mc.setZoom(14);
				// mc.animateTo(mylayer.getMyLocation());

				m_ProgressDialog.dismiss();
			}

		}
	};

	private void setupMap() {

		

		StopOverlay pathLayer=new StopOverlay(path_list,3,this.getResources());
		overlays.add(pathLayer);
		StopOverlay stopLayer=new StopOverlay(stop_list,1,this.getResources());
		overlays.add(stopLayer);
		marklayer = new StopOverlay(vehicle_list, 2,this.getResources());
		overlays.add(marklayer);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mylayer != null) {
			mylayer.enableMyLocation();
		}
		Log.v("resume", "resume");

	}

	@Override
	protected void onStop() {
		if (mylayer != null) {
			mylayer.disableMyLocation();
		}

		super.onStop();
	}

}
