package com.sfmuni;
import java.util.*;
public class GoogleSearchResult {
	private String city;
	private double lat;
	private double lng;
	private String country;
	private String region;
	private String streetAddress;
	private String title;
	private ArrayList<String> phonenumber;
	public GoogleSearchResult()
	{
		phonenumber=new ArrayList<String>();
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLng() {
		return lng;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRegion() {
		return region;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void addPhonenumber(String number) {
		phonenumber.add(number);
	}
	public ArrayList<String> getPhonenumbers() {
		return phonenumber;
	}
}
