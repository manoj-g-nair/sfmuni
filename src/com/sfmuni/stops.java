package com.sfmuni;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class stops extends ListActivity {
	private ArrayList stop_list=new ArrayList();
	private String m_query="";
	private String m_direction_tag="";
	private MyAdapter m_adapter;
	private Runnable viewStops;
	private ProgressDialog m_ProgressDialog = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stop_list);
		Intent intent=this.getIntent();
		Bundle bundle=intent.getExtras();
		m_direction_tag=bundle.getString("direction tag");
		m_query=bundle.getString("route");
		//stop_list = getStops(m_query);
		m_adapter = new MyAdapter(this, R.layout.stop_item, stop_list);
		setListAdapter(m_adapter);
		viewStops = new Runnable() {
			@Override
			public void run() {
		
				stop_list = BasicFunctions.getStops(m_query,m_direction_tag);
				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewStops, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(this,
				"Please wait...", "Retrieving data ...", true);
	}
	
	
	

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (stop_list != null && stop_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < stop_list.size(); i++)
					m_adapter.add((Stop)stop_list.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Stop stop=(Stop)stop_list.get(position);
		
		String stopTag =  stop.getTag();
		Intent intent = new Intent();
		intent.setClass(this, Prediction.class);
		Bundle bundle = new Bundle();

		bundle.putString("stopid",stopTag);
		bundle.putString("route", m_query);
		bundle.putString("direction", m_direction_tag);

		intent.putExtras(bundle);

		startActivity(intent);

	}

	private class MyAdapter extends ArrayAdapter<Stop> {

		private ArrayList<Stop> items;

		public MyAdapter(Context context, int textViewResourceId,
				ArrayList<Stop> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.stop_item, null);
			}
			
			Stop o = (Stop)items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.stop_text);
				if (tt != null) {
					tt.setText(o.getTitle());
					//Log.v("stop", o.getTag());
				}

			}
			return v;
		}

	}
	
	}


