package com.sfmuni;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class Directions extends ListActivity {
	private Runnable viewDirections;
	private ArrayList direction_list = new ArrayList();

	private ProgressDialog m_ProgressDialog = null;
	private MyAdapter m_adapter;
	private String m_query = "";
	private Direction m_direction ;

	// private Routes routes;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST, Menu.NONE, R.string.stop_menu_1);
		menu.add(0, Menu.FIRST + 1, Menu.NONE + 1, R.string.stop_menu_2);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ((m_direction != null) && (m_query != "")) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			switch (item.getItemId()) {
			case (Menu.FIRST):
				Intent stopintent = new Intent();
			stopintent.setClass(this, stops.class);			
				bundle.putString("direction tag", m_direction.getTag());
				
				bundle.putString("route", m_query);
				
				stopintent.putExtras(bundle);
				startActivity(stopintent);
				break;
			case (Menu.FIRST + 1):	
				Intent mapintent = new Intent();
				mapintent.setClass(this, StopMap.class);				
				bundle.putString("direction tag", m_direction.getTag());
				bundle.putString("route", m_query);
				
				mapintent.putExtras(bundle);
				startActivity(mapintent);
				break;

			}

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.direction_list);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		m_query = bundle.getString("tag");
		// direction_list = getDirections(m_query);
		m_adapter = new MyAdapter(this, R.layout.direction_item, direction_list);
		setListAdapter(m_adapter);
		viewDirections = new Runnable() {
			@Override
			public void run() {

				direction_list = BasicFunctions.getDirections(m_query);
				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewDirections, "MagentoBackground");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);

	}

	

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (direction_list != null && direction_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < direction_list.size(); i++)
					m_adapter.add((Direction)direction_list.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		m_direction = (Direction)direction_list.get(position);
	
		this.openOptionsMenu();

	}

	private class MyAdapter extends ArrayAdapter<Direction> {

		private ArrayList<Direction> items;

		public MyAdapter(Context context, int textViewResourceId,
				ArrayList<Direction> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.direction_item, null);
			}
		
			String o = (String) items.get(position).getTitle();
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.direction_text);
				if (tt != null) {
					tt.setText(o);
				}

			}
			return v;
		}

	}
}
