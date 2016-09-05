package com.example.imonmyway;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ActionLocationsActivity extends Activity{

	ArrayList<String> location_names = new ArrayList<String>();
	ArrayList<String> location_images = new ArrayList<String>();
	ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Location");
		
		TinyDB tinydb = new TinyDB(this);
        locationsList = tinydb.getListObject("MyLocations", LocationsStruct.class);
        
        for(int i=0;i<locationsList.size();i++){
        	
        	location_names.add(locationsList.get(i).getName());
        	location_images.add("ic_action_place");
        }
        
		ListView listView = new ListView(this);
        ListAdapter mAdapter = new ActLists(this, location_names, location_images, true);
        listView.setAdapter(mAdapter);
        
        builder.setView(listView);
        final Dialog dialog = builder.create();

        dialog.show();
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                  long arg3) 
            {
            	Intent intent = new Intent();
                intent.putExtra("Place", location_names.get(position));
  	      	 	setResult(Activity.RESULT_OK, intent);
  	      	 	finish();
            }
         });
	}	
	
}
