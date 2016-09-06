package com.example.imonmyway;

import java.io.IOException;
import java.text.DecimalFormat;
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
import android.os.Parcel;
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



public class Map extends FragmentActivity implements LocationListener , OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, android.location.LocationListener, OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener{

	GoogleMap myMap;
    protected GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    Marker myMarker;
    Button addMapLocation;
    AutoCompleteTextView searchEdit;
    LocationsStruct location = new LocationsStruct();
    LocationsStruct new_location = new LocationsStruct();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        addMapLocation = (Button) findViewById(R.id.buttonAddMapLocation);
        addMapLocation.setEnabled(false);
        addMapLocation.setAlpha(.5f);
        
        searchEdit = (AutoCompleteTextView) findViewById(R.id.searchEdit);
        
        addMapLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Location _loc = new Location("dummyprovider");
				_loc.setLatitude(myMarker.getPosition().latitude);
				_loc.setLongitude(myMarker.getPosition().longitude);
				location.setName("");
				location.setLocation(_loc);
				location.setInfo(myMarker.getSnippet());
				
				Intent intent = new Intent(Map.this, MapDialog.class);
				
				intent.putExtra("Coordinates", new DecimalFormat("#.########").format(location.getCoordinates().getLatitude()) + ", " + new DecimalFormat("#.########").format(location.getCoordinates().getLongitude()));
		        intent.putExtra("Info", location.getInfo());
		        startActivityForResult(intent, 1);
			}
		});
          
        
        searchEdit.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
            	
            	// Getting user input location
                String location = searchEdit.getText().toString();
 
                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        }); 
        
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(Map.this, "No connection available to the Google Play Services :/",
 				   Toast.LENGTH_LONG).show();
            return;
        }
        
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
     
    }
	
	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 1) {
             if (resultCode == Activity.RESULT_OK) {
                 Bundle b = data.getExtras();
                 if (b != null) {
                     String name = (String) b.getSerializable("Name");
                     location.setName(name);
                     
                     String info = (String) b.getSerializable("Info");
                     location.setInfo(info);
                     
                     Intent intent = new Intent();
                     intent.putExtra("NewName", location.getName());
                     intent.putExtra("NewInfo", location.getInfo());
                     
                     Parcel p = Parcel.obtain();
                     location.getCoordinates().writeToParcel(p, 0);
                     final byte[] locationBytes = p.marshall(); 
                     p.recycle();
                     
                     intent.putExtra("NewCoordinates", locationBytes);
           	      	 setResult(Activity.RESULT_OK, intent);
           	      	 finish();
                 }  
             } else if (resultCode == 0) {
                 // Do Nothing...    
             }
         }
     }
	
    @Override
    public void onMapReady(GoogleMap map) {
    	
//        LatLng sydney = new LatLng(-33.867, 151.206);
    	myMap = map;
    	
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        
        setUpMap();
        
        map.setMyLocationEnabled(true);
        
        ShowAllLocations();
        
        }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//
	}

	@Override
	public void onLocationChanged(Location location) {

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
//    	Toast.makeText(Map.this, "Long clicked the point:" + point,
//				   Toast.LENGTH_LONG).show();
      
    	if(myMarker!=null){
    		myMarker.remove();
    	}
    	
    	if(myMap == null){
    		
    	}
    	
    	myMarker = myMap.addMarker(new MarkerOptions()
		.title("New Location")
		.snippet("")
		.position(point)
		.icon(BitmapDescriptorFactory
    			        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); 
    	
    	myMap.setOnMarkerClickListener(this);
    	addMapLocation.setEnabled(true);
    	addMapLocation.setAlpha(1);
    	
    	Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
    	List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(point.latitude, point.longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (addresses.size() > 0){
    		myMarker.setSnippet("" + addresses.get(0).getCountryName() + "; " + addresses.get(0).getLocality() + "; " + addresses.get(0).getThoroughfare());
    	}
    	    
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
    
	// An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            myMap.clear();
 
            String[] suggestedLocations = new String[addresses.size()];
            
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
            	
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                suggestedLocations[i] = addressText;
                
//                myMarker = myMap.addMarker(new MarkerOptions()
//        		.title(addressText)
//        		.snippet("")
//        		.position(latLng));
// 
//                // Locate the first location
//                if(i==0)
//                    myMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            
            // Create the adapter and set it to the AutoCompleteTextView 
            ArrayAdapter<String> adapter = 
            new ArrayAdapter<String>(Map.this, android.R.layout.simple_list_item_1, suggestedLocations);
            adapter.setNotifyOnChange(true);
            searchEdit.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void ShowAllLocations(){
    	
    	ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();
        
        TinyDB tinydb = new TinyDB(this);
        locationsList = tinydb.getListObject("MyLocations", LocationsStruct.class);
        
        if(locationsList.size() <= 0)
        	return;
        
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        
        for(int i = 0; i < locationsList.size(); i++){
        	
        	LocationsStruct locStruct = locationsList.get(i);
        	LatLng loc = new LatLng(locStruct.getCoordinates().getLatitude(), locStruct.getCoordinates().getLongitude());
        	
        	builder.include(loc);

        	myMap.addMarker(new MarkerOptions()
        		.title(locStruct.getName())
    			.snippet(locStruct.getInfo())
    			.position(loc));
        } 
        
        LatLngBounds bounds = builder.build();
        
        int padding = 5; // offset from edges of the map in pixels
    	CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        
    	myMap.moveCamera(cu);
    	myMap.animateCamera(cu);

    }
	
}
