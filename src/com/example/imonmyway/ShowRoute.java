package com.example.imonmyway;

import java.util.ArrayList;
import java.util.List;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowRoute extends FragmentActivity implements LocationListener,
		OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
		android.location.LocationListener, OnMapClickListener,
		OnMapLongClickListener, OnMarkerClickListener {

	GoogleMap myMap;
	protected GoogleApiClient mGoogleApiClient;
	LocationManager mLocationManager;
	Marker myMarker;
	Button addMapLocation;
	AutoCompleteTextView searchEdit;
	LocationsStruct location = new LocationsStruct();
	LocationsStruct new_location = new LocationsStruct();
	ArrayList<ActionsStruct> actionsRunningList = new ArrayList<ActionsStruct>();
	List<Location> location_list = new ArrayList<Location>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_showlocation);

		if (!isGooglePlayServicesAvailable()) {
			Toast.makeText(ShowRoute.this,
					"No connection available to the Google Play Services :/",
					Toast.LENGTH_LONG).show();
			return;
		}

		TinyDB tinydb = new TinyDB(this);
		actionsRunningList = tinydb.getListObjectActions("RunningActions",
				ActionsStruct.class);

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(GoogleMap map) {

		myMap = map;

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(bestProvider);

		locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

		setUpMap();

		map.setMyLocationEnabled(true);

		if (actionsRunningList.size() <= 0)
			return;

		ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();

		TinyDB tinydb = new TinyDB(this);
		locationsList = tinydb.getListObject("MyLocations",
				LocationsStruct.class);

		LatLngBounds.Builder builder = new LatLngBounds.Builder();

		for (int i = 0; i < actionsRunningList.size(); i++) {

			for (int j = 0; j < locationsList.size(); j++) {
				if (locationsList.get(j).getName()
						.equals(actionsRunningList.get(i).getLocation())) {

					String contactName = actionsRunningList.get(i)
							.getContactList().get(0).getName();
					String text = actionsRunningList.get(i).getText();
					String message = "";

					if (actionsRunningList.get(i).getType() == 0) {
						message = "Send a message to: " + contactName
								+ ". Message: " + text;
					} else if (actionsRunningList.get(i).getType() == 1) {
						message = "Make a call to: " + contactName;
					}

					LatLng loc = new LatLng(locationsList.get(j)
							.getCoordinates().getLatitude(), locationsList
							.get(j).getCoordinates().getLongitude());
					builder.include(loc);

					myMap.addMarker(new MarkerOptions()
							.title(actionsRunningList.get(i).getLocation())
							.snippet(message).position(loc));

					break;
				}
			}
		}

		LatLngBounds bounds = builder.build();

		int padding = 5; // offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

		myMap.moveCamera(cu);
		myMap.animateCamera(cu);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	private void setUpMap() // If the setUpMapIfNeeded(); is needed then...
	{
		myMap.setOnMapClickListener(this);
		myMap.setOnMapLongClickListener(this);
		myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	}

	@Override
	public void onMapClick(LatLng point) {
		// Toast.makeText(Map.this, "Clicked on the point:" + point,
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMapLongClick(LatLng point) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(myMarker)) {
			// handle click here
		}
		return false;
	}

}
