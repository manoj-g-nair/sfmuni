package com.sfmuni;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;
import android.content.res.*;
import android.content.*;
import com.google.android.maps.*;
import java.util.*;

public class StopOverlay extends Overlay {
	private ArrayList m_list;
	
	private int mode = 0;
	private int m_radius = 3;
	private Resources m_res = null;
	private double m_lat;
	private double m_lng;
	private MyLocationOverlay m_location;
	private MapView m_map;
	public StopOverlay(ArrayList list, int mode, Resources res) {
		this.m_list = list;
		this.mode = mode;
		this.m_res = res;
		
	}
	public StopOverlay(MyLocationOverlay location,MapView map,int mode)
	{
		this.m_location=location;
		this.m_map=map;
		this.mode=mode;
	}
	
	public StopOverlay(double m_lat,double m_lng,int mode,Resources res)
	{
		this.m_lat=m_lat;
		this.m_lng=m_lng;
		this.mode=mode;
		this.m_res=res;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		//Log.v("draw", "draw once");
		Projection projection = mapView.getProjection();
		BitmapFactory bfactory = new BitmapFactory();

		Bitmap bitmapOrg = null;
		
		if (shadow == false) {
			Paint paint = new Paint();
			if (mode == 1) {

				paint.setAntiAlias(true);
				paint.setColor(Color.RED);
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				paint.setStrokeWidth(1);
				/*for (int i = 0; i < m_list.size(); i++) {
					Point point = new Point();
					Stop stop = (Stop) m_list.get(i);
					GeoPoint gp = new GeoPoint((int) (stop.getLAT() * 1000000),
							(int) ((stop.getLON() * 1000000)));
					projection.toPixels(gp, point);
					RectF oval = new RectF(point.x - m_radius, point.y
							- m_radius, point.x + m_radius, point.y + m_radius);
					canvas.drawOval(oval, paint);

				}
*/
				bitmapOrg = bfactory.decodeResource(m_res, R.drawable.busstop);
				for (int i = 0; i < m_list.size(); i++) {
					Point point = new Point();
					Stop stop = (Stop) m_list.get(i);
					GeoPoint gp = new GeoPoint(
							(int) (stop.getLAT() * 1000000), (int) ((stop
									.getLON() * 1000000)));
					projection.toPixels(gp, point);
					
					int width = bitmapOrg.getWidth();
					int height = bitmapOrg.getHeight();
					int newWidth = 12;
					int newHeight = 12;
				
					point.x=point.x-6;
					point.y=point.y-6;
					
					float scaleWidth = ((float) newWidth) / width;
					float scaleHeight = ((float) newHeight) / height;

				
					Matrix matrix = new Matrix();
				
					matrix.postScale(scaleWidth, scaleHeight);
					
					// rotate the Bitmap
					

					// recreate the new Bitmap

					Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
							width, height, matrix, true);

					// make a Drawable from Bitmap to allow to set the BitMap
					// to the ImageView, ImageButton or what ever
					// BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
					// canvas.drawBitmap(resizedBitmap, matrix, paint);
					canvas.drawBitmap(resizedBitmap, point.x, point.y, paint);
				}
			} else if (mode == 2) {
				bitmapOrg = bfactory.decodeResource(m_res, R.drawable.bus1);
				for (int i = 0; i < m_list.size(); i++) {
					Point point = new Point();
					Vehicle vehicle = (Vehicle) m_list.get(i);
					GeoPoint gp = new GeoPoint(
							(int) (vehicle.getLat() * 1000000), (int) ((vehicle
									.getLon() * 1000000)));
					projection.toPixels(gp, point);
					int width = bitmapOrg.getWidth();
					int height = bitmapOrg.getHeight();
					int newWidth = 16;
					int newHeight = 12;
					point.x=point.x-8;
					point.y=point.y-6;
					// calculate the scale - in this case = 0.4f
					float scaleWidth = ((float) newWidth) / width;
					float scaleHeight = ((float) newHeight) / height;

					// createa matrix for the manipulation
					Matrix matrix = new Matrix();
					// resize the bit map
					matrix.postScale(scaleWidth, scaleHeight);
					// rotate the Bitmap
					//matrix.postRotate(((-1)*vehicle.getHeading()));
					//matrix.setRotate(vehicle.getHeading());
					//matrix.preRotate(vehicle.getHeading());
					// recreate the new Bitmap
					Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
							width, height, matrix, true);

					// make a Drawable from Bitmap to allow to set the BitMap
					// to the ImageView, ImageButton or what ever
					// BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
					// canvas.drawBitmap(resizedBitmap, matrix, paint);
					canvas.drawBitmap(resizedBitmap, point.x, point.y, paint);
				}

			} else if (mode == 3) {
				paint.setAntiAlias(true);
				paint.setStyle(Style.STROKE);
				paint.setColor(Color.GREEN);
				paint.setStrokeWidth(3);
				for (int i = 0; i < m_list.size(); i++) {

					Path path = (Path) m_list.get(i);
					ArrayList locs = path.getLocations();
					Point startPoint = new Point();
					Location loc = (Location) locs.get(0);
					GeoPoint gp = new GeoPoint((int) (loc.getLat() * 1000000),
							(int) ((loc.getLon() * 1000000)));
					projection.toPixels(gp, startPoint);
					for (int j = 1; j < locs.size(); j++) {
						Point endPoint = new Point();
						loc = (Location) locs.get(j);
						gp = new GeoPoint((int) (loc.getLat() * 1000000),
								(int) ((loc.getLon() * 1000000)));
						projection.toPixels(gp, endPoint);
						canvas.drawLine(startPoint.x, startPoint.y, endPoint.x,
								endPoint.y, paint);
						startPoint = endPoint;

					}
				}
			}
			else if(mode==4)
			{
				paint.setAntiAlias(true);
				paint.setColor(Color.GREEN);
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				paint.setStrokeWidth(1);
				
					Point point = new Point();
					
					GeoPoint gp = new GeoPoint((int) (m_lat * 1000000),
							(int) ((m_lng * 1000000)));
					projection.toPixels(gp, point);
					RectF oval = new RectF(point.x - m_radius, point.y
							- m_radius, point.x + m_radius, point.y + m_radius);
					canvas.drawOval(oval, paint);

				
			}
			else if(mode==5)
			{
				m_location.draw(canvas, m_map, true);
				
			}

		}
		return super.draw(canvas, mapView, shadow, when);
	}

}
