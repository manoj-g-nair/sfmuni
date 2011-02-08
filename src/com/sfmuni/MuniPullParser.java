package com.sfmuni;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import android.util.Log;


public class MuniPullParser {
	private boolean startStop=false;
	private String routeTag;
	private String routeTitle;
	private double m_lat;
	private double m_lng;
	private boolean startNearest=false;
	private Stop nearestStop;
	public static ArrayList stoplist=new ArrayList();
	private double maxLat;
    private double minLat;
    private double maxLng;
    
    private double minLng;
    private double lat2;
	private double lng2;
	private String stopId;
	private String stopTag;
	private String stopTitle;
	
	public MuniPullParser(double lat,double lng)
	{
		this.m_lat=lat;
		this.m_lng=lng;
		//stoplist=new ArrayList();
	}
	public ArrayList getData()
	{
		return stoplist;
	}
	public ArrayList parse(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		Log.v("start pull parse", "start pull parse");
		
		Stop stop = new Stop();

        while(true) {

           int eventType = parser.getEventType();
           
           if(eventType == XmlPullParser.START_TAG) {

              String tag = parser.getName();
  
              if("route".equals(tag)) {
            	  startStop=true;
            	  for(int i=0;i<parser.getAttributeCount();i++)
            	  {
            		  if(parser.getAttributeName(i).equals("title"))
            		  {
            			  routeTitle=parser.getAttributeValue(i);
            		  }
            		  if(parser.getAttributeName(i).equals("latMax"))
            		  {
            			  maxLat=Double.parseDouble(parser.getAttributeValue(i));
            			 
            		  }
            		  if(parser.getAttributeName(i).equals("latMin"))
            		  {
            			  minLat=Double.parseDouble(parser.getAttributeValue(i));
            			 
            		  }
            		  if(parser.getAttributeName(i).equals("lonMax"))
            		  {
            			  maxLng=Double.parseDouble(parser.getAttributeValue(i));
            			 
            		  }
            		  if(parser.getAttributeName(i).equals("lonMin"))
            		  {
            			  minLng=Double.parseDouble(parser.getAttributeValue(i));
            			 
            		  }
            		 
            		
            	  }
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
              else if(("direction".equals(tag))||("path".equals(tag)))
              {
            	  startStop=false;
              }
              
              else if(("stop".equals(tag))&&(startNearest)&&(startStop)) {

            	  for(int i=0;i<parser.getAttributeCount();i++)
            	  {
            		  if(parser.getAttributeName(i).equals("lat"))
            		  {
            			  lat2=Double.parseDouble(parser.getAttributeValue(i));
            			  
            			  }
            		  else if(parser.getAttributeName(i).equals("lon"))
            		  {
            			  lng2=Double.parseDouble(parser.getAttributeValue(i));
            		  }
            		  else if(parser.getAttributeName(i).equals("stopId"))
            		  {
            			  stopId=parser.getAttributeValue(i);
            		  }
            		  else if(parser.getAttributeName(i).equals("tag"))
            		  {
            			  stopTag=parser.getAttributeValue(i);
            		  }
            		  else if(parser.getAttributeName(i).equals("title"))
            		  {
            			  stopTitle=parser.getAttributeValue(i);
            		  }
            	
            		 
            		  }
            	  double dis=BasicFunctions.calculateDis(m_lat, m_lng, lat2, lng2);
					if(dis<500)
					{
						Log.v("distance", "distance: "+dis);
						nearestStop=new Stop();
						startNearest=true;
						nearestStop.setLAT(lat2);
						nearestStop.setLON(lng2);
						nearestStop.setStopId(stopId);
						nearestStop.setTag(stopTag);
						nearestStop.setTitle(stopTitle);
						nearestStop.setRouteTag(routeTag);
						nearestStop.setRouteTitle(routeTitle);
						stoplist.add(nearestStop);
						
					}
            	  }
               
           }else if(eventType==XmlPullParser.END_DOCUMENT) 
           {
        	   break;
           }
           eventType = parser.next();


        }
		
        
      return stoplist;

	}
}
