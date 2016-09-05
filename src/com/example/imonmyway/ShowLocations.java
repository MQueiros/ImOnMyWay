package com.example.imonmyway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.*;



public class ShowLocations extends FragmentActivity implements LocationListener , OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, android.location.LocationListener, OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener{

	GoogleMap myMap;
    protected GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    Marker myMarker;
    Button addMapLocation;
    AutoCompleteTextView searchEdit;
    LocationsStruct location = new LocationsStruct();
    LocationsStruct new_location = new LocationsStruct();
    String locationName = "";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_showlocation);
        
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(ShowLocations.this, "No connection available to the Google Play Services :/",
 				   Toast.LENGTH_LONG).show();
            return;
        }
        
        Bundle b = getIntent().getExtras();
        locationName = b.getString("LocationName");
        
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
        
        ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();
        
        TinyDB tinydb = new TinyDB(this);
        locationsList = tinydb.getListObject("MyLocations", LocationsStruct.class);
        
        for(int i = 0; i < locationsList.size(); i++){
        	LocationsStruct locStruct = locationsList.get(i);
        	LatLng loc = new LatLng(locStruct.getCoordinates().getLatitude(), locStruct.getCoordinates().getLongitude());
        	
        	map.addMarker(new MarkerOptions()
        		.title(locStruct.getName())
    			.snippet(locStruct.getInfo())
    			.position(loc));
        	
        	if(locStruct.getName().equals(locationName)){
    			map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 17)); 
        	}
        }
        
 
        
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

    private void setUpMap() //If the setUpMapIfNeeded(); is needed then...
    {
        myMap.setOnMapClickListener(this);
        myMap.setOnMapLongClickListener(this);
        myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public void onMapClick(LatLng point) {
//    	Toast.makeText(Map.this, "Clicked on the point:" + point,
//				   Toast.LENGTH_LONG).show();
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
        if (arg0.equals(myMarker)) 
        {
            //handle click here
        }
        return false;
	}

	
}
