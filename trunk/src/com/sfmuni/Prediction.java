package com.sfmuni;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

public class Prediction extends Activity {
	private String stopId = "";
	private String route = "";
	private String direction="";
	private ArrayList prediction_list;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.prediction);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		this.stopId = bundle.getString("stopid");
		this.route = bundle.getString("route");
		this.direction=bundle.getString("direction");
		tv = (TextView) findViewById(R.id.predicText);
		tv.setTextSize(18);
		refreshData();
		

	}
	public void refreshData()
	{
		 
		prediction_list=BasicFunctions.getPrediction(this.route,this.stopId);
		MuniPrediction pre = (MuniPrediction) prediction_list.get(0);
		
		String text = "";
		text += "Agency:      " + pre.getAgency() + "\n";
		text += "Direction:  " + pre.getDirectionTitle() + "\n";
		text += "Route:      " + pre.getRouteTitle() + "\n";
		text += "Stop:    	 " + pre.getStopTitle() + "\n";
		// Log.v("trackedInfo", "lalal "+(pre.getTrackedInfo().size()));
		text += "\n";
		text += "\n";
		if (pre.getTrackedInfo().size() == 0) {
			text += "no available prediction for this stop" + "\n";
		} else {
			text += "Tracked vehicles in:" + "\n";
			for (int i = 0; i < pre.getTrackedInfo().size(); i++) {
				text += "" + pre.getTrackedInfo().get(i).toString() + "\n";

			}
		}
		text += "\n";
		for (int i = 0; i < pre.getMessages().size(); i++) {
			text += pre.getMessages().get(i).toString() + "\n";
		}
	
		tv.setText(text);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST, Menu.NONE, R.string.prediction_menu_1);
		menu.add(0, Menu.FIRST + 1, Menu.NONE + 1, R.string.prediction_menu_2);
		menu.add(0, Menu.FIRST + 2, Menu.NONE + 2, R.string.prediction_menu_3);
		menu.add(0, Menu.FIRST + 3, Menu.NONE + 3, R.string.prediction_menu_4);
		menu.add(0, Menu.FIRST + 4, Menu.NONE + 4, R.string.prediction_menu_5);

		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.openOptionsMenu();
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (item.getItemId()) {
		case (Menu.FIRST):
			refreshData();
			break;
		case (Menu.FIRST + 1):
			intent.setClass(this, RouteMap.class);
			bundle.putString("route", this.route);
			bundle.putString("direction", this.direction);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case (Menu.FIRST + 2):
			intent.setClass(Prediction.this, RouteChooser.class);
			startActivity(intent);
			break;
		case (Menu.FIRST + 3):
			intent.setClass(this, Directions.class);
			bundle.putString("tag", this.route);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case (Menu.FIRST + 4):
			Intent stopintent = new Intent();
		stopintent.setClass(this, stops.class);			
			bundle.putString("direction tag", this.direction);
			bundle.putString("route", this.route);
			stopintent.putExtras(bundle);
			startActivity(stopintent);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	
}
