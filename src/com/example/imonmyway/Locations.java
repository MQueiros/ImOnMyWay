package com.example.imonmyway;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Locations extends ActionBarActivity {

	TextView helpText;
	Button add_location_button;
	AlertDialog.Builder builder;
	LocationsStruct new_location = new LocationsStruct();
	ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);

		TinyDB tinydb = new TinyDB(this);
		locationsList = tinydb.getListObject("MyLocations",
				LocationsStruct.class);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.locations_main_label));

		add_location_button = (Button) findViewById(R.id.buttonAddLocation);

		add_location_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Locations.this, Map.class);
				startActivityForResult(intent, 1);
			}
		});

		builder = new AlertDialog.Builder(this);

		builder.setMessage(getResources().getString(
				R.string.delete_all_locations_question));

		builder.setPositiveButton(
				getResources().getString(R.string.positive_button),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog

						locationsList.removeAll(locationsList);

						// Removes All Locations...
						TinyDB tinydb = new TinyDB(Locations.this);
						tinydb.putListObject("MyLocations", locationsList);

						RefreshLocationsList();

						dialog.dismiss();
					}

				});

		builder.setNegativeButton(
				getResources().getString(R.string.negative_button),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
						dialog.dismiss();
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();

		RefreshLocationsList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle b = data.getExtras();
				if (b != null) {

					String name = (String) b.getSerializable("NewName");
					String info = (String) b.getSerializable("NewInfo");
					final byte[] locationBytes = (byte[]) b
							.getSerializable("NewCoordinates");

					Parcel parcel = Parcel.obtain();
					parcel.unmarshall(locationBytes, 0, locationBytes.length);
					parcel.setDataPosition(0);

					Location coordinates = Location.CREATOR
							.createFromParcel(parcel);

					new_location = new LocationsStruct(name, coordinates, info);
					locationsList.add(new_location);
				}
			} else if (resultCode == 0) {
				// Do Nothing...
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.locations_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void RefreshLocationsList() {

		helpText = (TextView) findViewById(R.id.textHelp);
		helpText.setVisibility(View.GONE);

		if (locationsList.size() > 0) {

			ListView listView = (ListView) findViewById(R.id.listView1);
			ListAdapter mAdapter = new LocationItemAdapter(this, locationsList);
			listView.setAdapter(mAdapter);
		} else {
			helpText.setVisibility(View.VISIBLE);
		}

		TinyDB tinydb = new TinyDB(this);
		tinydb.putListObject("MyLocations", locationsList);
	}

}