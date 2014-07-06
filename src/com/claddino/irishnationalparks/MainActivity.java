package com.claddino.irishnationalparks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	ProgressDialog mProgressDialog;
	String url = "http://www.npws.ie/nationalparks/mapmarkers/";
	static final LatLng IRELAND = new LatLng(53.030901, -7.300863);
	public static final String TAG = "MainActivity";
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		new MapOfParks().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

	private class MapOfParks extends AsyncTask<Void, Void, Void> {
		
		List<NationalPark> Parks = new ArrayList<NationalPark>();
		
		List<String> parkWebsiteLinks = new ArrayList<String>();

		// using Set to remove duplicates and linkehashset to maintain order
		Set<String> parkEmails = new LinkedHashSet<String>();

		List<String> parkNames = new ArrayList<String>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(MainActivity.this);
			mProgressDialog.setTitle("Irish National Parks");
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				// Connect to the web site
				Document document = Jsoup.connect(url).get();
				// Using Elements to get the Meta data
				Elements description = document.select("div#subhome");

				Elements nationalParkLinks = document
						.select("div#subhome a[href]");

				// all national parks email end with nationalpark.ie
				Pattern nationalParkEmails = Pattern
						.compile("http://(.*?)nationalpark.ie");
				// Pattern patternCoOrdinates =
				// Pattern.compile("\\d{1,2}.\\d{6},-\\d{1,2}.\\d{6}");
				Matcher parkEmailmatcher = nationalParkEmails
						.matcher(nationalParkLinks.toString());

				while (parkEmailmatcher.find()) {

					parkEmails.add(parkEmailmatcher.group());

				}

				Pattern nationalParkNames = Pattern
						.compile(">(.*?)National Park");
				// Pattern patternCoOrdinates =
				// Pattern.compile("\\d{1,2}.\\d{6},-\\d{1,2}.\\d{6}");
				Matcher nationalParkNamesMatcher = nationalParkNames
						.matcher(nationalParkLinks.toString());

				while (nationalParkNamesMatcher.find()) {

					parkNames.add(nationalParkNamesMatcher.group());

				}

				// \\d{1,2}[A-Za-z]\\s*[A-Za-z]{2}\\s*\\d{1,5}\\s*\\d{1,5}
				Pattern patternCoOrdinates = Pattern
						.compile("\\d{1,2}.\\d{6},-\\d{1,2}.\\d{6}");
				// Pattern patternCoOrdinates =
				// Pattern.compile("\\d{1,2}.\\d{6},-\\d{1,2}.\\d{6}");
				Matcher CoOrdinatesmatcher = patternCoOrdinates
						.matcher(description.toString());
				List<String> coordsStrings = new ArrayList<String>();
				while (CoOrdinatesmatcher.find()) {

					coordsStrings.add(CoOrdinatesmatcher.group());
				}

				for (int j = 0; j < parkEmails.size(); j++) {

					// co-ordinates are in format 53.999189,-9.691714
					String latitude = coordsStrings.get(j).substring(0,
							coordsStrings.get(j).lastIndexOf(","));
					String longtitude = coordsStrings.get(j).substring(
							coordsStrings.get(j).lastIndexOf(",") + 1);
					double lati = Double.parseDouble(latitude);
					double longLat = Double.parseDouble(longtitude);
					ParkCoordinates coords = new ParkCoordinates(lati, longLat);

					NationalPark nPark = new NationalPark(coords,
							parkEmails.toArray()[j].toString(), "fdsf",
							parkNames.get(j));

					Parks.add(nPark);
				}
				/*
				 * Log.w(TAG, matcher.group());
				 * 
				 * System.out.println(matcher.group());
				 */

				for (Element element : nationalParkLinks) {
					if (element.toString().contains("nationalpark.ie")) {
						parkWebsiteLinks.add(element.toString());
					} else {

					}
				}

				// Locate the content attribute

				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Set description into TextView

			mProgressDialog.dismiss();

			for (int i = 0; i < Parks.size(); i++) {

				// co-ordinates are in format 53.999189,-9.691714

				map.addMarker(new MarkerOptions()
						.position(
								new LatLng(Parks.get(i).getCoords()
										.getLatitude(), Parks.get(i)
										.getCoords().getLongtitude()))
						.snippet(Parks.get(i).getEmail()).title(Parks.get(i).getInfo())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_launcher))
								);
				map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
	                @Override
	                public void onInfoWindowClick(Marker marker) {
	                    // TODO Auto-generated method stub
	                	String a = marker.getSnippet();
	                    Intent intent = new Intent(MainActivity.this, ParkWebsiteActivity.class);
	                     intent.putExtra("ParkWebsite", a);
	                    startActivity(intent);
	                }
	            });
				

			}

			

			// Move the camera instantly to hamburg with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(IRELAND, 15));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
		}
	}
}

