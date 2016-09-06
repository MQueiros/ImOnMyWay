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

public class StartActivity extends ActionBarActivity {
	
	TextView helpText;
	View clock, start;
	TextView clock_edit;
	ArrayList<ActionsStruct> actionsList = new ArrayList<ActionsStruct>();
	
	private int pHour;
    private int pMinute;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;
     
    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                pHour = hourOfDay;
                pMinute = minute;
                updateDisplay(clock_edit);
                displayToast();
            }
    	};
	
	/** Updates the time in the TextView */
    private void updateDisplay(TextView edit) {
    	edit.setText(
            new StringBuilder()
                    .append(pad(pHour)).append(":")
                    .append(pad(pMinute)));
    }
     
    /** Displays a notification when the time is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("New end time chosen: ").append(clock_edit.getText()),   Toast.LENGTH_SHORT).show();
             
    }
	
    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("I'm On My Way!");
		
		TinyDB tinydb = new TinyDB(this);
        actionsList = tinydb.getListObjectActions("MyActions", ActionsStruct.class);
		
		start = (View) findViewById(R.id.buttonStart);
//		start.setEnabled(false);
//		start.setAlpha(.5f);
		
		start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ArrayList<ActionsStruct> actionsRunningList = new ArrayList<ActionsStruct>();
				TinyDB tinydb = new TinyDB(StartActivity.this);
				tinydb.putListObjectActions("RunningActions", actionsRunningList); // null
				
				for(int i = 0; i < actionsList.size(); i++){
					if(actionsList.get(i).getUsedState() != 0)
					{
						actionsList.get(i).setUsedState(0);
						actionsRunningList.add(actionsList.get(i));
					}
				}
				
				if(actionsRunningList.size() == 0){
					Toast.makeText(StartActivity.this, "Pick some actions first!", Toast.LENGTH_LONG).show();
					return;
				}
				
				tinydb.putListObjectActions("RunningActions", actionsRunningList);
				tinydb.putBoolean("IsRunning", true);
				tinydb.putString("RunningEndTime", clock_edit.getText().toString());
				
				startService(new Intent(StartActivity.this, BackgroundActionsService.class));
				
				Intent openRunActivity = new Intent("com.examples.imonmyway.RUNNING");
				finish();
				startActivity(openRunActivity); 
			}
		});
		
		clock = (View) findViewById(R.id.buttonClock);
		clock_edit = (TextView) findViewById(R.id.endHourEdit);
		
		clock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialog(TIME_DIALOG_ID);
			}
		});
		
        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);
 
        /** Display the current time in the TextView */
        // updateDisplay(edit_start_hour);
        
        cal.add(Calendar.HOUR, 1);
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);
        
        updateDisplay(clock_edit);
	}
	
	/** Create a new dialog for time picker */
    
	@Override
    public void onResume(){
        super.onResume();
        
        helpText = (TextView) findViewById(R.id.textHelp);
        helpText.setVisibility(View.GONE);
        
        if(actionsList.size() > 0){
        	
        	ListView listView = (ListView) findViewById(R.id.listView1);
            ListAdapter mAdapter = new ActionItemAdapter(this, actionsList, true, false, false);
            listView.setAdapter(mAdapter);    
        }
        else{
        	helpText.setVisibility(View.VISIBLE);
        }
    }
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this,
                    mTimeSetListener, pHour, pMinute, false);
        }
        return null;
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
