package com.sfmuni;

public class Vehicle {
	private String id;
	private String routeTag;
	private String dirTag;
	private double lat;
	private double lon;
	private boolean predictable;
	private double speedKmHr;
	private int heading;
	public Vehicle()
	{
		
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setRouteTag(String routeTag) {
		this.routeTag = routeTag;
	}
	public String getRouteTag() {
		return routeTag;
	}
	public void setDirTag(String dirTag) {
		this.dirTag = dirTag;
	}
	public String getDirTag() {
		return dirTag;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLon() {
		return lon;
	}
	public void setPredictable(boolean predictable) {
		this.predictable = predictable;
	}
	public boolean isPredictable() {
		return predictable;
	}
	public void setSpeedKmHr(double speedKmHr) {
		this.speedKmHr = speedKmHr;
	}
	public double getSpeedKmHr() {
		return speedKmHr;
	}
	public void setHeading(int heading) {
		this.heading = heading;
	}
	public int getHeading() {
		return heading;
	}
	
}
