package com.sfmuni;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class Stop extends DefaultHandler{
	private String title;
	private String tag;
	private double lat;
	private double lon;
	private String stopId;
	private String routeTag;
	private String routeTitle;
	
	public void setStopId(String stopId)
	{
		this.stopId=stopId;
	}
	public String getStopId()
	{
		return this.stopId;
	}
	public Stop()
	{
		
	}
	public Stop(String tag)
	{
		this.tag=tag;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	public String getTag()
	{
		return this.tag;
	}
	public double getLAT()
	{
		return this.lat;
	}
	public double getLON()
	{
		return this.lon;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public void setTag(String tag)
	{
		this.tag=tag;
	}
	public void setLAT(double lat)
	{
		this.lat=lat;
	}
	public void setLON(double lon)
	{
		this.lon=lon;
	}
	public void setRouteTag(String routeTag) {
		this.routeTag = routeTag;
	}
	public String getRouteTag() {
		return routeTag;
	}
	public void setRouteTitle(String routeTitle) {
		this.routeTitle = routeTitle;
	}
	public String getRouteTitle() {
		return routeTitle;
	}
}
