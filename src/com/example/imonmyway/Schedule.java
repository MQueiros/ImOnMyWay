package com.example.imonmyway;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class Schedule extends ActionBarActivity {

	AlertDialog.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedules);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Schedule");

		builder = new AlertDialog.Builder(this);

		builder.setMessage("Delete All Locations?");

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Do nothing but close the dialog

				dialog.dismiss();
			}

		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
				dialog.dismiss();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// API 5+ solution
			onBackPressed();
			return true;

		case R.id.action_reset:

			AlertDialog alert = builder.create();
			alert.show();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.schedule_menu, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
}
