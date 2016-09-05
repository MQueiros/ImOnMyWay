package com.example.imonmyway;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.imonmyway.R;

import android.app.Activity;
import android.app.Notification.BigTextStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class IntroActivity extends Activity {
	
	TextView location_text, action_text;
	View locations_layout, actions_layout, schedule_layout, start_layout;
	TextView locations_helptext, actions_helptext;
	
	ArrayList<ActionsStruct> actionsList = new ArrayList<ActionsStruct>();
	ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		TinyDB tinydb = new TinyDB(this);
        actionsList = tinydb.getListObjectActions("MyActions", ActionsStruct.class);
        locationsList = tinydb.getListObject("MyLocations", LocationsStruct.class);
		
        locations_helptext = (TextView) findViewById(R.id.locationshelpText);
        if(locationsList.size() > 0)
        	locations_helptext.setText(locationsList.size() + " locations available");
		
        actions_helptext = (TextView) findViewById(R.id.actionshelpText);
		
        if(actionsList.size() > 0)
        	actions_helptext.setText(actionsList.size() + " actions available");
        
		locations_layout = (View) findViewById(R.id.locationsLayout);
		actions_layout = (View) findViewById(R.id.ActionsLayout);
		schedule_layout = (View) findViewById(R.id.ScheduleLayout);
		start_layout = (View) findViewById(R.id.StartNowLayout);
		
		locations_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent openLocationsActivity = new Intent("com.examples.imonmyway.LOCATIONS");
				startActivity(openLocationsActivity);
			}
		});
		
		actions_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent openActionsActivity = new Intent("com.examples.imonmyway.ACTIONS");
				startActivity(openActionsActivity);
			}
		});
		
		schedule_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent openScheduleActivity = new Intent("com.examples.imonmyway.SCHEDULE");
				startActivity(openScheduleActivity);
			}
		});
		
		start_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent openStartActivity = new Intent("com.examples.imonmyway.START");
				startActivity(openStartActivity); 
				
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		TinyDB tinydb = new TinyDB(this);
        actionsList = tinydb.getListObjectActions("MyActions", ActionsStruct.class);
        locationsList = tinydb.getListObject("MyLocations", LocationsStruct.class);
		
        locations_helptext = (TextView) findViewById(R.id.locationshelpText);
        if(locationsList.size() > 0)
        	locations_helptext.setText(locationsList.size() + " location(s) available");
		actions_helptext = (TextView) findViewById(R.id.actionshelpText);
		if(actionsList.size() > 0)
        	actions_helptext.setText(actionsList.size() + " action(s) available");
	}

	
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
	
	}
