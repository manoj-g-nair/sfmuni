package com.sfmuni;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.net.URL;
import java.util.*;



public class RouteChooser extends ListActivity {
	private Runnable viewRoutes;
	private ArrayList route_list=new ArrayList();

	private ProgressDialog m_ProgressDialog = null;
	private MyAdapter m_adapter;

	// private Routes routes;
	@Override
	public void onCreate(Bundle savedInstanceState) {

	
		super.onCreate(savedInstanceState);

		setContentView(R.layout.route_list);
		//route_list = new Routes().getAllRoutes();
		m_adapter = new MyAdapter(this, R.layout.list_item, route_list);
		setListAdapter(m_adapter);
		viewRoutes = new Runnable() {
			@Override
			public void run() {
				
				route_list = BasicFunctions.getAllRoutes();
				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewRoutes, "MagentoBackground");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(RouteChooser.this,
				"Please wait...", "Retrieving data ...", true);

	}
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (route_list != null && route_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < route_list.size(); i++)
					m_adapter.add((Route) route_list.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Route route = (Route) route_list.get(position);
		Intent intent = new Intent();
		intent.setClass(this, Directions.class);
		Bundle bundle = new Bundle();

		bundle.putString("tag", route.getTag());

		intent.putExtras(bundle);

		startActivity(intent);

	}

	private class MyAdapter extends ArrayAdapter<Route> {

		private ArrayList<Route> items;

		public MyAdapter(Context context, int textViewResourceId,
				ArrayList<Route> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			Route o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.text);
				if (tt != null) {
					tt.setText(o.getTitle());
				}

			}
			return v;
		}

	}
}
