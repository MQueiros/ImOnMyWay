package com.example.imonmyway;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class ActionItemAdapter extends BaseAdapter {

	private ArrayList<ActionsStruct> mainList;
	private Context context;
	boolean hasCheckBoxes = false;
	boolean isRunning = false;
	Boolean hasOptions = false;
	AlertDialog.Builder alert_del_location;

	public ActionItemAdapter(Context applicationContext,
			ArrayList<ActionsStruct> questionForSliderMenu,
			boolean hasCheckBoxes, boolean isRunning, Boolean hasOptions) {

		super();
		this.context = applicationContext;
		this.mainList = questionForSliderMenu;
		this.hasCheckBoxes = hasCheckBoxes;
		this.hasOptions = hasOptions;
		this.isRunning = isRunning;

	}

	public ActionItemAdapter() {

		super();
		// this.mainList = QuestionForSliderMenu;

	}

	@Override
	public int getCount() {

		return mainList.size();
	}

	@Override
	public Object getItem(int position) {

		return mainList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (isRunning)
			if (mainList.get(position).getUsedState() == 5)
				return null;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) this.context
					.getApplicationContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.action_view, null);
		}

		alert_del_location = new AlertDialog.Builder(this.context);

		final CheckBox checkbox = (CheckBox) convertView
				.findViewById(R.id.checkBox1);
		ImageView imageClick = (ImageView) convertView
				.findViewById(R.id.options);

		if (hasCheckBoxes == false) {
			checkbox.setVisibility(View.GONE);
		} else {
			checkbox.setVisibility(View.VISIBLE);
		}

		if (hasOptions == false) {
			imageClick.setVisibility(View.GONE);
		} else {
			imageClick.setVisibility(View.VISIBLE);
		}

		TextView tv2 = (TextView) convertView.findViewById(R.id.text2);

		ImageView mainImage = (ImageView) convertView
				.findViewById(R.id.row_imageView1);

		try {

			int imageResource = 0;

			String firstLine;
			if (this.mainList.get(position).getType() == 0) {
				imageResource = this.context.getResources().getIdentifier(
						"@drawable/" + "ic_action_chat", null,
						this.context.getPackageName());
				firstLine = context.getResources().getString(
						R.string.action_type_message_to);
			} else {
				imageResource = this.context.getResources().getIdentifier(
						"@drawable/" + "ic_action_call", null,
						this.context.getPackageName());
				firstLine = context.getResources().getString(
						R.string.action_type_call_to);
			}

			Drawable res = this.context.getResources().getDrawable(
					imageResource);
			mainImage.setImageDrawable(res);

			List<Contact> contacts = this.mainList.get(position)
					.getContactList();

			for (int i = 0; i < this.mainList.get(position).getContactList()
					.size(); i++) {
				firstLine += contacts.get(i).getName() + " ";
			}

			tv2.setText(firstLine + "\nLocation:"
					+ this.mainList.get(position).getLocation() + "\nText: "
					+ this.mainList.get(position).getText());

			checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked == true)
						mainList.get(position).setUsedState(1);
					else
						mainList.get(position).setUsedState(0);
				}
			});

			imageClick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					switch (v.getId()) {
					case R.id.options:

						PopupMenu popup = new PopupMenu(context
								.getApplicationContext(), v);
						popup.getMenuInflater().inflate(R.menu.location_popup,
								popup.getMenu());
						popup.show();
						popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {

								switch (item.getItemId()) {
								case R.id.install:

									// Or Some other code you want to put here..
									// This is just an example.
									Toast.makeText(
											context.getApplicationContext(),
											" Teste " + " : " + position,
											Toast.LENGTH_LONG).show();

									break;
								case R.id.addtowishlist:

									alert_del_location
											.setMessage(context
													.getResources()
													.getString(
															R.string.delete_this_action_question));
									alert_del_location
											.setPositiveButton(
													context.getResources()
															.getString(
																	R.string.positive_button),
													new DialogInterface.OnClickListener() {

														public void onClick(
																DialogInterface dialog,
																int which) {
															// Do nothing but
															// close the dialog
															mainList.remove(position);
															notifyDataSetChanged();
															dialog.dismiss();
														}

													});

									alert_del_location
											.setNegativeButton(
													context.getResources()
															.getString(
																	R.string.negative_button),
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// Do nothing
															dialog.dismiss();
														}
													});

									AlertDialog alert = alert_del_location
											.create();
									alert.show();

									break;

								default:
									break;
								}

								return true;
							}
						});

						break;

					default:
						break;
					}

				}
			});

		} catch (Exception e) {

			e.printStackTrace();
		}

		return convertView;
	}

}
