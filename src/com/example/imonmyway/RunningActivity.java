package com.example.imonmyway;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.graphics.PorterDuff;

public class RunningActivity extends ActionBarActivity {
    
	ArrayList<ActionsStruct> actionsRunningList = new ArrayList<ActionsStruct>();
	Boolean isRunning = false;
	String endTime = "";
	TextView actionsInfoText;
	View showMap, cancel;
	AlertDialog.Builder alert_cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_running);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("I'm On My Way!");
		
		TinyDB tinydb = new TinyDB(this);
        actionsRunningList = tinydb.getListObjectActions("RunningActions", ActionsStruct.class);
    	isRunning = tinydb.getBoolean("IsRunning");
    	endTime = tinydb.getString("RunningEndTime");
		
    	if(!isRunning){
    		Toast.makeText(this, "No actions running at this moment...", Toast.LENGTH_LONG).show();
    		finish();
    	}
    	
		actionsInfoText = (TextView) findViewById(R.id.actionsInfoText);
		actionsInfoText.setText("0/" + actionsRunningList.size() + " actions");
		
		showMap = (View) findViewById(R.id.buttonShowMap);
		cancel = (View) findViewById(R.id.buttonCancel);
		
		alert_cancel = new AlertDialog.Builder(this);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				alert_cancel.setMessage("Are you sure you want to cancel?");
				alert_cancel.setPositiveButton("YES", new DialogInterface.OnClickListener() {

    	        public void onClick(DialogInterface dialog, int which) {
        	            // Do nothing but close the dialog
        	            dialog.dismiss();
        	            stopService(new Intent(getBaseContext(), BackgroundActionsService.class));
        	            finish();
        	        }
        	    });

				alert_cancel.setNegativeButton("NO", new DialogInterface.OnClickListener() {

    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
        	            // Do nothing
        	            dialog.dismiss();
        	        }
        	    });
            	
            	AlertDialog alert = alert_cancel.create();
                alert.show();
			}
		});
		
		showMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent openShowRouteActivity = new Intent("com.examples.imonmyway.MAPSHOWROUTE");
				startActivity(openShowRouteActivity); 
			}
		});
	}
    
	@Override
    public void onResume(){
        super.onResume();
      
    	if(actionsRunningList.size() > 0){
        	
        	ListView listView = (ListView) findViewById(R.id.listView1);
            ListAdapter mAdapter = new ActionItemAdapter(this, actionsRunningList, false, false, false);
            listView.setAdapter(mAdapter);  
        }
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home: 
            onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
