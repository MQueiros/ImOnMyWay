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

public class ActionTypesActivity extends Activity {

	ArrayList<String> action_types = new ArrayList<String>();
	ArrayList<String> action_types_icons = new ArrayList<String>();
	public int item_position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Action");

		action_types
				.add(getResources().getString(R.string.action_type_message));
		action_types.add(getResources().getString(R.string.action_type_call));

		action_types_icons.add("ic_action_chat");
		action_types_icons.add("ic_action_call");

		ListView listView = new ListView(this);
		ListAdapter mAdapter = new ActLists(this, action_types,
				action_types_icons, false);
		listView.setAdapter(mAdapter);

		builder.setView(listView);
		final Dialog dialog = builder.create();

		dialog.show();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.putExtra("Type", action_types.get(position));
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

}
