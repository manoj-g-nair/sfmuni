package com.sfmuni;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.json.*;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class BasicFunctions {
	private static MuniPullParser parser;
	public static boolean finishedParse = false;

	public static ArrayList getDirections(String query) {
		ArrayList data = new ArrayList();
		URL url = null;
		try {
			url = new URL(
					"http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=sf-muni&r="
							+ query);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MuniHandler handler = new MuniHandler(3);
		try {
			data = handler.parse(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public static double calculateDis(double lat, double lng, double lat2,
			double lng2) {

		double result = 0.0;

		lat = lat * 3.1415926 / 180;
		lat2 = lat2 * 3.1415926 / 180;
		lng = lng * 3.1415926 / 180;
		lng2 = lng2 * 3.1415926 / 180;

		result = 6378137 * Math.acos(Math.sin(lat) * Math.sin(lat2)
				+ Math.cos(lat) * Math.cos(lat2) * Math.cos(lng2 - lng));

		return result;

	}

	public static ArrayList returnData() {
		return parser.stoplist;
	}

	public static ArrayList getNearestStop(double lat, double lng) {
		ArrayList data = new ArrayList();
		URL url = null;

		try {
			url = new URL(
					"http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=sf-muni");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		parser = new MuniPullParser(lat, lng);

		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);

			XmlPullParser pparser = factory.newPullParser();

			pparser.setInput(url.openStream(), "UTF-8");
			data = parser.parse(pparser);
			finishedParse = true;

			Log.v("stop nearest parsing", "stop nearest parsing");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public static ArrayList getPrediction(String route, String stopId) {
		URL url = null;
		ArrayList data = new ArrayList();
		try {
			url = new URL(
					"http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=sf-muni&r="
							+ route + "&s=" + stopId + "&useShortTitles=true");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MuniHandler handler = new MuniHandler(4);

		try {
			data = handler.parse(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public static ArrayList getAllRoutes() {
		ArrayList routes = new ArrayList();
		URL url;

		try {

			url = new URL(

					"http://webservices.nextbus.com/service/publicXMLFeed?command=routeList&a=sf-muni");
			MuniHandler handler = new MuniHandler(1);
			routes = handler.parse(url);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return routes;
	}

	public static ArrayList getStops(String query, String direction) {
		URL url = null;
		ArrayList stops = new ArrayList();
		try {
			url = new URL(
					"http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=sf-muni&r="
							+ query);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MuniHandler handler = new MuniHandler(2);
		handler.setDirection(direction);
		try {
			stops = handler.parse(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stops;
	}

	public static ArrayList getPaths(String query) {
		ArrayList paths = new ArrayList();
		URL url = null;
		try {
			url = new URL(
					"http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=sf-muni&r="
							+ query);
			// Log.v("url","http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=sf-muni&r"+query
			// );
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MuniHandler handler = new MuniHandler(6);

		try {
			paths = handler.parse(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paths;
	}

	public static ArrayList getVehicles(String query) {
		URL url = null;
		ArrayList vehicles = new ArrayList();
		try {
			long now = System.currentTimeMillis();
			url = new URL(
					"http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a=sf-muni&r="
							+ query + "&t=" + now);
			Log.v(
							"url",
							"http://webservices.nextbus.com/service/publicXMLFeed?command=vehicleLocations&a=sf-muni&r="
									+ query + "&t=" + now);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MuniHandler handler = new MuniHandler(5);

		try {
			vehicles = handler.parse(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vehicles;
	}

	public static ArrayList googleSearch(double lat, double lng, String keyword) {
		ArrayList data = new ArrayList();
		try {
			URL url = new URL(
					"http://ajax.googleapis.com/ajax/services/search/local?hl=en&v=1.0&rsz=large&q="
							+ keyword + "&sll=" + lat + "," + lng + "&start=3");
			URLConnection connection = url.openConnection();

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JSONObject json = new JSONObject(builder.toString());

			System.out.println(json.toString(2));

			Iterator iter = json.keys();

			while (iter.hasNext()) {
				String resp = iter.next().toString();
				// System.out.println(resp);
				if (resp.equals("responseData")) {

					String responsedata = json.getString(resp);
					JSONObject js = new JSONObject(responsedata);

					Iterator it = js.keys();
					while (it.hasNext()) {

						String result = it.next().toString();
						if (result.equals("results")) {

							String results = js.getString(result);
							// System.out.println(results);
							JSONArray j = new JSONArray(results);
							for (int i = 0; i < j.length(); i++) {
								GoogleSearchResult searchResult = new GoogleSearchResult();
								String temp = j.getString(i);
								JSONObject con = new JSONObject(temp);

								Iterator iterator = con.keys();
								while (iterator.hasNext()) {

									String content = iterator.next().toString();
									if (content.equals("city")) {
										// System.out.println(con.getString(content));
										searchResult.setCity(con
												.getString(content));
									} else if (content.equals("country")) {
										// System.out.println(con.getString(content));
										searchResult.setCountry(con
												.getString(content));
									} else if (content.equals("streetAddress")) {
										// System.out.println(con.getString(content));
										searchResult.setStreetAddress(con
												.getString(content));
									} else if (content.equals("phoneNumbers")) {
										String numbers = con
												.getString("phoneNumbers");
										JSONArray k = new JSONArray(numbers);
										for (int m = 0; m < k.length(); m++) {
											String numbertemp = k.getString(m);
											JSONObject connumber = new JSONObject(
													numbertemp);

											Iterator iteratornumber = connumber
													.keys();
											while (iteratornumber.hasNext()) {
												String number = iteratornumber
														.next().toString();
												if (number.equals("number")) {
													Log
															.v(
																	"phonenumber",
																	connumber
																			.getString(number));
													searchResult
															.addPhonenumber(connumber
																	.getString(number));
												}
											}
										}
									} else if (content.equals("lat")) {
										// System.out.println(con.getString(content));
										searchResult.setLat(Double
												.parseDouble(con
														.getString(content)));
									} else if (content.equals("lng")) {
										// System.out.println(con.getString(content));
										searchResult.setLng(Double
												.parseDouble(con
														.getString(content)));
									} else if (content.equals("region")) {
										// System.out.println(con.getString(content));
										searchResult.setRegion(con
												.getString(content));
									} else if (content
											.equals("titleNoFormatting")) {
										// System.out.println(con.getString(content));
										searchResult.setTitle(con
												.getString(content));
									}

								}
								data.add(searchResult);

							}

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
