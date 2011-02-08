package com.sfmuni;

import java.util.*;

public class MuniPrediction {
	private String agency;
	private String routeTitle;
	private String directionTitle;
	private String stopTitle;
	private ArrayList messages;
	private ArrayList trackedInfo;

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public void setRouteTitle(String routeTitle) {
		this.routeTitle = routeTitle;
	}

	public void setDirectionTitle(String directionTitle) {
		this.directionTitle = directionTitle;
	}

	public void setStopTitle(String stopTitle) {
		this.stopTitle=stopTitle;
	}
	public void setMessages(ArrayList messages)
	{
		this.messages=messages;
	}
	public void setTrackedInfo(ArrayList trackedInfo)
	{
		this.trackedInfo=trackedInfo;
	}
	public String getAgency()
	{
		return this.agency;
	}
	public String getStopTitle()
	{
		return this.stopTitle;
	}
	public String getDirectionTitle()
	{
		return this.directionTitle;
	}
	public String getRouteTitle()
	{
		return this.routeTitle;
	}
	public ArrayList getMessages()
	{
		return this.messages;
	}
	public ArrayList getTrackedInfo()
	{
		return this.trackedInfo;
	}
}
