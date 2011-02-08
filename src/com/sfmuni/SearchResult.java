package com.sfmuni;

import java.util.ArrayList;
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

public class SearchResult extends ListActivity {
	private Runnable viewSearchResult;
	private ArrayList search_result_list = new ArrayList();

	private ProgressDialog m_ProgressDialog = null;
	private MyAdapter m_adapter;
	private String m_keyword = "";
	private double m_lat = 0.0;
	private double m_lng = 0.0;

	// private Routes routes;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result_list);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		m_keyword = bundle.getString("keyword");
		m_lat = bundle.getDouble("lat");
		m_lng = bundle.getDouble("lng");

		// route_list = new Routes().getAllRoutes();
		m_adapter = new MyAdapter(this, R.layout.search_result_item,
				search_result_list);
		setListAdapter(m_adapter);
		viewSearchResult = new Runnable() {
			@Override
			public void run() {
				Log.v("1lat", "11 " +m_lat);
				Log.v("1lng", "11 "+m_lng);
				Log.v("1m_keyword", m_keyword);
				search_result_list = BasicFunctions.googleSearch(m_lat, m_lng,
						m_keyword);
				Log.v("result", "i have" + search_result_list.size());
				GoogleSearchResult re = (GoogleSearchResult) (search_result_list
						.get(0));
				Log.v("result", (re.getTitle()));
				runOnUiThread(returnRes);

			}
		};
		Thread thread = new Thread(null, viewSearchResult, "MagentoBackground");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0, Menu.FIRST, Menu.NONE, R.string.search_result_menu_1);
		// menu.add(0, Menu.FIRST + 1, Menu.NONE + 1,
		// R.string.search_result_menu_2);
		menu.add(0, Menu.FIRST + 2, Menu.NONE + 2,
				R.string.search_result_menu_3);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (item.getItemId()) {
		case (Menu.FIRST):

			break;
		case (Menu.FIRST + 1):
			// intent.setClass(this, RouteMap.class);
			// bundle.putString("lat", value);

			break;
		case (Menu.FIRST + 2):
			intent.setClass(this, SearchResultMap.class);
			//bundle.putStringArrayList("result", search_result_list);
			//.putParcelableArrayList("result", search_result_list);
			bundle.putString("keyword", m_keyword);
			bundle.putDouble("lat", m_lat);
			bundle.putDouble("lng", m_lng);
			
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (search_result_list != null && search_result_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < search_result_list.size(); i++)
					m_adapter.add((GoogleSearchResult) search_result_list
							.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		this.openOptionsMenu();

		// bundle.putString("tag", result.getTag());

		// intent.putExtras(bundle);

		// startActivity(intent);

	}

	private class MyAdapter extends ArrayAdapter<GoogleSearchResult> {

		private ArrayList<GoogleSearchResult> items;

		public MyAdapter(Context context, int textViewResourceId,
				ArrayList<GoogleSearchResult> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.search_result_item, null);
			}
			GoogleSearchResult o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v
						.findViewById(R.id.search_result_text);
				if (tt != null) {
					tt.setText(o.getTitle());
				}

			}
			return v;
		}

	}
}
