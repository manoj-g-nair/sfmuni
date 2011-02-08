package com.sfmuni;

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

import android.util.Log;

public class MuniHandler extends DefaultHandler {

	private ArrayList<Object> list = new ArrayList<Object>();
	private Route route = null;
	private Vehicle vehicle=null;
	private Path path=null;
	
	private String direction = "";
	
	private int m_mode = 0;
	private MuniPrediction predic = null;
	private boolean startPath=false;
	private ArrayList<String> messages = new ArrayList<String>();
	private double m_lat=0.0;
	private double m_lng=0.0;
	private ArrayList<String> trackedInfo = new ArrayList<String>();
	private boolean startNearest=false;
	private Stop nearestStop;
	private String routeTag="";
	private String routeTitle="";
	public MuniHandler(int mode) {
		this.m_mode = mode;

	}
	public void setM_lat(double lat) {
		this.m_lat = lat;
	}

	public double getM_lat() {
		return m_lat;
	}

	public void setM_lng(double lng) {
		this.m_lng = lng;
	}

	public double getM_lng() {
		return m_lng;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}

	public ArrayList returnData() {
		return this.list;
	}

	public ArrayList parse(URL url) throws Exception {
		// ArrayList data=new ArrayList();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			try {
				Log.v("start to parse", "start to parse");
				XMLReader xr = parser.getXMLReader();
				xr.setContentHandler(this);
				InputStream stream = url.openStream();
				InputSource source = new InputSource(stream);
				// String xml=source.toString();

				xr.parse(source);
				// parser.parse(new InputSource(url.openStream()), handler);
				Log.v("stop to parse", "stop to parse");

			} catch (SAXNotRecognizedException e) {

				System.err.println("Unknown feature specified: "
						+ e.getMessage());

			} catch (SAXNotSupportedException e) {

				System.err.println("Unsupported feature specified: "
						+ e.getMessage());

			} catch (SAXException e) {

				System.err.println("Error in setting feature: "
						+ e.getMessage());

			}

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return list;

	}

	public void startElement(String uri, String name, String qName,

	Attributes atts) {
		// m_mode==1,get route information
		//Log.v("m_mode", "m_mode "+m_mode);
		if (m_mode == 1) {
			if (name == "route") {
				route = new Route();
			}
			if ((atts != null) && (route != null)) {
				if (atts.getValue("tag") != null) {
					route.setTag(atts.getValue("tag"));
				}
				if (atts.getValue("title") != null) {
					route.setTitle(atts.getValue("title"));

				}
			}
		} else if (m_mode == 2) {// m_mode==2 get stop information
			// Log.v("stop", "going to get stop information");
			// Log.v("name", name);
			if (name.equals("stop")) {
				// Log.v("direction", direction);
				if ((atts != null) && (atts.getValue("dirTag") != null)) {
					if (atts.getValue("dirTag").toString().equals(direction)) {
						Stop stop = new Stop(atts.getValue("tag"));
						stop.setLAT(Double.parseDouble(atts.getValue("lat")
								.toString()));
						stop.setLON(Double.parseDouble(atts.getValue("lon")
								.toString()));
						stop.setTitle(atts.getValue("title").toString());
						stop.setStopId(atts.getValue("stopId").toString());
						list.add(stop);
					}
				}

			}

		}

		else if (m_mode == 3) {// m_mode=3 get direction information
			if (name == "direction") {

				if ((atts != null) && (atts.getValue("title") != null)) {
					if (atts.getValue("useForUI").toString().equals("true")) {
						Direction direction = new Direction();
						direction.setTag(atts.getValue("tag"));
						direction.setTitle(atts.getValue("title"));
						list.add(direction);
					}

				}
			}
		} else if (m_mode == 4) {//m_mode==4 get prediction information

			if (name == "predictions") {
				predic = new MuniPrediction();

				if (atts.getValue("agencyTitle") != null) {
					Log.v("agencyTitle", atts.getValue("agencyTitle"));
					predic.setAgency(atts.getValue("agencyTitle").toString());
					predic
							.setRouteTitle(atts.getValue("routeTitle")
									.toString());
					predic.setStopTitle(atts.getValue("stopTitle").toString());
				}
			} else if (name == "direction") {
				if (atts.getValue("title") != null) {
					predic.setDirectionTitle(atts.getValue("title").toString());
				}
			} else if (name == "prediction") {
				// Log.v("prediction", "found one prediction");
				int minutes = (Integer.parseInt(atts.getValue("seconds")
						.toString()) / 60);
				int seconds = (Integer.parseInt(atts.getValue("seconds")
						.toString()))
						- (minutes * 60);
				trackedInfo.add(minutes + " minutes  " + seconds + " seconds");
				// predic.setTrackedInfo(trackedInfo);
			} else if (name == "message") {
				if (atts.getValue("text") != null) {
					messages.add(atts.getValue("text").toString());
					Log.v("predic", "pppp" + (predic == null));

				}

			}

		} else if (m_mode == 5) {//m_mode=5 get vehicle information
			if(name=="vehicle")
			{
				vehicle=new Vehicle();
				vehicle.setId(atts.getValue("id").toString());
				vehicle.setDirTag(atts.getValue("dirTag").toString());
				vehicle.setLat(Double.parseDouble(atts.getValue("lat").toString()));
				vehicle.setLon(Double.parseDouble(atts.getValue("lon").toString()));
				vehicle.setPredictable(Boolean.parseBoolean(atts.getValue("predictable").toString()));
				vehicle.setRouteTag(atts.getValue("routeTag").toString());
				vehicle.setSpeedKmHr(Double.parseDouble(atts.getValue("speedKmHr").toString()));
				vehicle.setHeading(Integer.parseInt(atts.getValue("heading").toString()));
				list.add(vehicle);
			}
		}
		else if(m_mode==6)//m_mode=6 get path information
		{
			if(name=="path")
			{
				startPath=true;
				path=new Path();
			}
			if((name=="point")&&(startPath==true))
			{
				Location loc=new Location();
				loc.setLat(Double.parseDouble(atts.getValue("lat").toString()));
				loc.setLon(Double.parseDouble(atts.getValue("lon").toString()));
				path.addLocation(loc);
			}
		}
		else if(m_mode==7)
		{
			
			//Log.v("nearest", name);
			if(name=="route")
			{
				routeTag=atts.getValue("tag").toString();
				routeTitle=atts.getValue("title").toString();
				double maxLat=Double.parseDouble(atts.getValue("latMax"));
				double minLat=Double.parseDouble(atts.getValue("latMin"));
				double maxLng=Double.parseDouble(atts.getValue("lonMax"));
				double minLng=Double.parseDouble(atts.getValue("lonMin"));
				if((m_lat<=maxLat)&&(m_lat>=minLat)&&(m_lng<=maxLng)&&(m_lng>=minLng))
				{
					startNearest=true;
					//Log.v("startNearest", "startNearest"+(startNearest==true));
				}
				else 
				{
					startNearest=false;
				}
			}
			if((name=="stop")&&(startNearest==true))
			{
				if((atts.getValue("lat")!=null)&&(atts.getValue("lon")!=null))
				{
					double lat2=Double.parseDouble(atts.getValue("lat"));
					double lng2=Double.parseDouble(atts.getValue("lon"));
					double dis=BasicFunctions.calculateDis(m_lat, m_lng, lat2, lng2);
					//
					//Log.v("m_lat"+m_lat, "m_lng"+m_lng);
					if(dis<500)
					{
						Log.v("distance", "distance: "+dis);
						nearestStop=new Stop();
						startNearest=true;
						nearestStop.setLAT(Double.parseDouble(atts.getValue("lat")));
						nearestStop.setLON(Double.parseDouble(atts.getValue("lon")));
						nearestStop.setStopId(atts.getValue("stopId").toString());
						nearestStop.setTag(atts.getValue("tag").toString());
						nearestStop.setTitle(atts.getValue("title").toString());
						nearestStop.setRouteTag(routeTag);
						nearestStop.setRouteTitle(routeTitle);
						list.add(nearestStop);
						
					}
				}
			}
		}

	}

	public void endElement(String uri, String name, String qName) {
		if (m_mode == 1) {

			if (name.equals("route")) {
				list.add(route);
				// Log.v("add a route", route.getTag());
			}
		} else if (m_mode == 4) {
			if (name == "predictions") {
				if (predic != null) {
					predic.setMessages(messages);
					predic.setTrackedInfo(trackedInfo);
					list.add(predic);
				}
			}
		}
		else if(m_mode==6)
		{
			if(name=="path")
			{
				list.add(path);
			}
			
		}
		
		
	}

	

}