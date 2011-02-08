package com.sfmuni;
import java.util.*;
public class Path {
	private ArrayList locations;

	public Path()
	{
		locations=new ArrayList();
	}

	public ArrayList getLocations() {
		return locations;
	}
	public void addLocation(Location loc)
	{
		locations.add(loc);
	}
}
