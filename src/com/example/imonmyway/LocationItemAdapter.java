package com.example.imonmyway;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.PopupMenu;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationItemAdapter extends BaseAdapter{

    private ArrayList<LocationsStruct> mainList;
    private Context context;
    AlertDialog.Builder alert_del_location;


    public LocationItemAdapter(Context applicationContext,
            ArrayList<LocationsStruct> questionForSliderMenu) {

        super();
        this.context = applicationContext;
        this.mainList = questionForSliderMenu;

    }

    public LocationItemAdapter() {

        super();
//        this.mainList = QuestionForSliderMenu;

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

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.context.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.location_view, null);
        }

        alert_del_location = new AlertDialog.Builder(this.context);

        View mainView = (View) convertView
                .findViewById(R.id.mainText);
        TextView tv1 = (TextView) convertView
                .findViewById(R.id.row_textView1);
        TextView tv2 = (TextView) convertView
                .findViewById(R.id.text2);
        ImageView imageClick = (ImageView) convertView
                .findViewById(R.id.row_click_imageView1);

        try {

        	mainView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                	Intent intent = new Intent(context, ShowLocations.class);
                	
                	Bundle b = new Bundle();
                	b.putString("LocationName", mainList.get(position).getName()); //Your id
                	intent.putExtras(b); //Put your id to your next Intent
                	
    		        context.startActivity(intent);
                }
        	});
                
        	
//            tv1.setText(" Location "+ " : " + position);
            tv1.setText(this.mainList.get(position).getName());
            tv2.setText(String.valueOf(this.mainList.get(position).getCoordinates().getLatitude()).substring(0, 10) + ", " + String.valueOf(this.mainList.get(position).getCoordinates().getLongitude()).substring(0,10) + "\n" + this.mainList.get(position).getInfo());
            
            imageClick.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                	
                    switch (v.getId()) {
                    case R.id.row_click_imageView1:

                        PopupMenu popup = new PopupMenu(context.getApplicationContext(), v);
                        popup.getMenuInflater().inflate(R.menu.location_popup,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                case R.id.install:

                                    //Or Some other code you want to put here.. This is just an example.
                                    Toast.makeText(context.getApplicationContext(), " Test " + " : " + position, Toast.LENGTH_LONG).show();

                                    break;
                                case R.id.addtowishlist:
                    
                                	alert_del_location.setMessage("Delete location " + position + "?");
                                	alert_del_location.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        	        public void onClick(DialogInterface dialog, int which) {
                            	            // Do nothing but close the dialog
                            	        	mainList.remove(position);
                            	        	notifyDataSetChanged();
                            	            dialog.dismiss();
                            	        }

                            	    });

                                    alert_del_location.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        	        @Override
                        	        public void onClick(DialogInterface dialog, int which) {
                            	            // Do nothing
                            	            dialog.dismiss();
                            	        }
                            	    });
                                	
                                	AlertDialog alert = alert_del_location.create();
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
