package com.example.imonmyway;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Actions extends ActionBarActivity {

	TextView helpText;
	Button button_add_action;
	AlertDialog.Builder builder;
	ActionsStruct new_action = new ActionsStruct();
	ArrayList<ActionsStruct> actionsList = new ArrayList<ActionsStruct>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actions);

		TinyDB tinydb = new TinyDB(this);
		actionsList = tinydb.getListObjectActions("MyActions",
				ActionsStruct.class);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.actions_bar_title));

		button_add_action = (Button) findViewById(R.id.buttonAddAction);

		button_add_action.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Actions.this, NewAction.class);
				startActivityForResult(intent, 1);
			}
		});

		builder = new AlertDialog.Builder(this);

		builder.setMessage(getResources().getString(
				R.string.delete_all_actions_question));

		builder.setPositiveButton(
				getResources().getString(R.string.positive_button),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog

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

		helpText = (TextView) findViewById(R.id.textHelp);
		helpText.setVisibility(View.GONE);

		if (actionsList.size() > 0) {

			ListView listView = (ListView) findViewById(R.id.listView1);
			ListAdapter mAdapter = new ActionItemAdapter(this, actionsList,
					false, false, true);
			listView.setAdapter(mAdapter);

		} else {
			helpText.setVisibility(View.VISIBLE);
		}

		TinyDB tinydb = new TinyDB(this);
		tinydb.putListObjectActions("MyActions", actionsList);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle b = data.getExtras();
				if (b != null) {

					new_action = (ActionsStruct) b.getSerializable("NewAction");
					actionsList.add(new_action);
					// AddLocation(new_location.getName(),
					// new_location.getLatitude() + ", " +
					// new_location.getLongitude(), new_location.getInfo(), "");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
}
