package com.sfmuni;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.helpers.DefaultHandler;


import android.util.Log;


public class DomParser {
	private String routeTag;
	private String routeTitle;
	private double m_lat;
	private double m_lng;
	private boolean startNearest=false;
	private Stop nearestStop;
	private ArrayList stoplist;
	
	public DomParser(double lat,double lng)
	{
		this.m_lat=lat;
		this.m_lng=lng;
	}
	public ArrayList getData()
	{
		return stoplist;
	}
	public ArrayList parse(URL url)
	{
		Log.v("start parse", "start parse");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         stoplist = new ArrayList<Stop>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(url.openStream());
            Element root = (Element) dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("route");
            for (int i=0;i<items.getLength();i++){
            	
              
                Node item = items.item(i);
                NamedNodeMap atts=item.getAttributes();
                routeTag=atts.getNamedItem("tag").getNodeValue();
				routeTitle=atts.getNamedItem("title").getNodeValue();
               double maxLat=Double.parseDouble(atts.getNamedItem("latMax").getNodeValue());
               double minLat=Double.parseDouble(atts.getNamedItem("latMin").getNodeValue());
               double maxLng=Double.parseDouble(atts.getNamedItem("lonMax").getNodeValue());
               
               double minLng=Double.parseDouble(atts.getNamedItem("lonMin").getNodeValue());
               
				if((m_lat<=maxLat)&&(m_lat>=minLat)&&(m_lng<=maxLng)&&(m_lng>=minLng))
				{
					startNearest=true;
					Log.v("startNearest", "startNearest"+(startNearest==true));
				}
				else 
				{
					startNearest=false;
				}
				if(startNearest)
				{
					   NodeList stops = item.getChildNodes();
		                for (int j=0;j<stops.getLength();j++){
		                    Node stopnode = stops.item(j);
		                    String name = stopnode.getNodeName();
		                    if (name.equals("stop")){
		                    	NamedNodeMap stopatts=stopnode.getAttributes();
		                    	double lat2=Double.parseDouble(atts.getNamedItem("lat").getNodeValue());
		    					double lng2=Double.parseDouble(atts.getNamedItem("lon").getNodeValue());
		    					double dis=BasicFunctions.calculateDis(m_lat, m_lng, lat2, lng2);
		    					if(dis<500)
		    					{
		    						Log.v("distance", "distance: "+dis);
		    						nearestStop=new Stop();
		    						startNearest=true;
		    						nearestStop.setLAT(Double.parseDouble(atts.getNamedItem("lat").getNodeValue()));
		    						nearestStop.setLON(Double.parseDouble(atts.getNamedItem("lon").getNodeValue()));
		    						nearestStop.setStopId(atts.getNamedItem("stopId").getNodeValue());
		    						nearestStop.setTag(atts.getNamedItem("tag").getNodeValue());
		    						nearestStop.setTitle(atts.getNamedItem("title").getNodeValue());
		    						nearestStop.setRouteTag(routeTag);
		    						nearestStop.setRouteTitle(routeTitle);
		    						stoplist.add(nearestStop);
		    						
		    					}
		                    } 
		                }
		          
				}
             
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
      return stoplist;

	}
}
