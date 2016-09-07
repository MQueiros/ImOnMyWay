package com.example.imonmyway;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		setTitle(getResources().getString(R.string.app_name));

		TinyDB tinydb = new TinyDB(this);
		actionsRunningList = tinydb.getListObjectActions("RunningActions",
				ActionsStruct.class);
		isRunning = tinydb.getBoolean("IsRunning");
		endTime = tinydb.getString("RunningEndTime");

		if (!isRunning) {
			Toast.makeText(
					this,
					getResources().getString(R.string.running_no_actions_toast),
					Toast.LENGTH_LONG).show();
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

				alert_cancel.setMessage(getResources().getString(
						R.string.cancel_dialog_question));
				alert_cancel.setPositiveButton(
						getResources().getString(R.string.positive_button),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// Do nothing but close the dialog
								dialog.dismiss();
								stopService(new Intent(getBaseContext(),
										BackgroundActionsService.class));
								finish();
							}
						});

				alert_cancel.setNegativeButton(
						getResources().getString(R.string.negative_button),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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

				Intent openShowRouteActivity = new Intent(
						"com.examples.imonmyway.MAPSHOWROUTE");
				startActivity(openShowRouteActivity);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		if (actionsRunningList.size() > 0) {

			ListView listView = (ListView) findViewById(R.id.listView1);
			ListAdapter mAdapter = new ActionItemAdapter(this,
					actionsRunningList, false, false, false);
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
