package com.example.imonmyway;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IntroActivity extends Activity {

	TextView location_text, action_text, start_now_text;
	View locations_layout, actions_layout, schedule_layout, start_layout;
	TextView locations_helptext, actions_helptext, start_now_helptext;

	ArrayList<ActionsStruct> actionsList = new ArrayList<ActionsStruct>();
	ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();

	Boolean isRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		locations_layout = (View) findViewById(R.id.locationsLayout);
		actions_layout = (View) findViewById(R.id.ActionsLayout);
		schedule_layout = (View) findViewById(R.id.ScheduleLayout);
		start_layout = (View) findViewById(R.id.StartNowLayout);

		locations_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent openLocationsActivity = new Intent(
						"com.examples.imonmyway.LOCATIONS");
				startActivity(openLocationsActivity);
			}
		});

		actions_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent openActionsActivity = new Intent(
						"com.examples.imonmyway.ACTIONS");
				startActivity(openActionsActivity);
			}
		});

		schedule_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent openScheduleActivity = new Intent(
						"com.examples.imonmyway.SCHEDULE");
				startActivity(openScheduleActivity);
			}
		});

		start_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isRunning) {
					Intent openRunningActivity = new Intent(
							"com.examples.imonmyway.RUNNING");
					startActivity(openRunningActivity);
				} else {
					Intent openStartActivity = new Intent(
							"com.examples.imonmyway.START");
					startActivity(openStartActivity);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		TinyDB tinydb = new TinyDB(this);

		actionsList = tinydb.getListObjectActions("MyActions",
				ActionsStruct.class);
		locationsList = tinydb.getListObject("MyLocations",
				LocationsStruct.class);
		isRunning = tinydb.getBoolean("IsRunning");

		start_now_text = (TextView) findViewById(R.id.StartNowText);
		start_now_helptext = (TextView) findViewById(R.id.startnowhelpText);

		if (isRunning) {
			start_now_text.setText(getResources().getString(
					R.string.running_main_label));
			start_now_helptext.setText(getResources().getString(
					R.string.running_help));
		}

		locations_helptext = (TextView) findViewById(R.id.locationshelpText);
		if (locationsList.size() > 0) {
			locations_helptext.setText(locationsList.size()
					+ getResources().getString(R.string.locations_help));
		}

		actions_helptext = (TextView) findViewById(R.id.actionshelpText);

		if (actionsList.size() > 0) {
			actions_helptext.setText(actionsList.size()
					+ getResources().getString(R.string.actions_help));
		}
	}
}
