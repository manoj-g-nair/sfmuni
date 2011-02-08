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
import android.view.KeyEvent;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class StopMap extends MapActivity {
	private ArrayList stop_list = new ArrayList();
	private ArrayList path_list = new ArrayList();
	private String m_query = "";
	private String m_direction_tag = "";
	private Stop m_selectedStop = null;
	private Runnable viewStops;
	private ProgressDialog m_ProgressDialog = null;

	private MapView map;
	private MapController mc;

	private ViewGroup zoom;

	private MyLocationOverlay mylayer;
	private StopOverlay marklayer;
   private List<Overlay> overlays ;
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void findViews() {
		map = (MapView) findViewById(R.id.mapview);
		mc = map.getController();
		overlays= map.getOverlays();
		mylayer = new MyLocationOverlay(this, map);
		mylayer.runOnFirstFix(new Runnable() {
			public void run() {
				// Zoom in to current location
				map.setTraffic(true);
				// mc.setZoom(30);
				//mc.animateTo(mylayer.getMyLocation());
			}
		});
		overlays.add(mylayer);
		/*
		 * zoom = (ViewGroup) findViewById(R.id.zoom);
		 * zoom.addView(map.getZoomControls());
		 */

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
		m_direction_tag = bundle.getString("direction tag");
		m_query = bundle.getString("route");
		// stop_list = getStops(m_query);

		viewStops = new Runnable() {
			@Override
			public void run() {

				stop_list = BasicFunctions.getStops(m_query, m_direction_tag);
				path_list = BasicFunctions.getPaths(m_query);
				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewStops, "MagentoBackground");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);

	}

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (path_list != null && path_list.size() > 0) {
				setupMap();
				// Log.v("size", "i have "+path_list.size());
				Stop stop = (Stop) stop_list.get((stop_list.size() / 2));
				GeoPoint p = new GeoPoint((int) (stop.getLAT() * 1E6),
						(int) (stop.getLON() * 1E6));
				mc.animateTo(p);
				mc.setZoom(15);
			
				m_ProgressDialog.dismiss();
			}

		}
	};

	private void setupMap() {
		//Log.v("set up", "set up");
		
		StopOverlay pathLayer = new StopOverlay(path_list, 3, this
				.getResources());
		overlays.add(pathLayer);
	
		Drawable pin=getResources().getDrawable(R.drawable.busstop);
		pin.setBounds(0, 0, 12, 12);
		
        BusStopOverLay stopbus = new BusStopOverLay(pin);
        overlays.add(stopbus);
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
	
	private class BusStopOverLay extends ItemizedOverlay<OverlayItem> {
		 private List<OverlayItem> items = new ArrayList<OverlayItem>();
		public BusStopOverLay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			String text = "";
			for(int i=0;i<stop_list.size();i++)
			{
				Stop stop=(Stop)stop_list.get(i);
				GeoPoint gp = new GeoPoint(
						(int) (stop.getLAT() * 1000000), (int) ((stop
								.getLON() * 1000000)));
				
			
				items.add(new OverlayItem(gp,stop.getTitle(),"bus stop"));
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
			Stop stop=(Stop)stop_list.get(pIndex);
			ArrayList prediction_list=BasicFunctions.getPrediction(m_query,stop.getTag());
			
	MuniPrediction pre = (MuniPrediction) prediction_list.get(0);
			
			String text = "";
			if(pre.getDirectionTitle()!=null)
			{
				text += "Direction:  " + pre.getDirectionTitle() + "\n";
			}
			
			text += "Route:      " + pre.getRouteTitle() + "\n";
			text += "Stop:    	 " + pre.getStopTitle() + "\n";
			// Log.v("trackedInfo", "lalal "+(pre.getTrackedInfo().size()));
			text += "\n";
			text += "\n";
			if (pre.getTrackedInfo().size() == 0) {
				text += "no available prediction for this stop right now" + "\n";
			} else {
				text += "Tracked vehicles in:" + "\n";
				for (int i = 0; i < pre.getTrackedInfo().size(); i++) {
					text += "" + pre.getTrackedInfo().get(i).toString() + "\n";

				}
			}
		
		
		    Toast.makeText(StopMap.this,
		       text,
		        Toast.LENGTH_LONG).show();
		    return true;
		}

	}

}
