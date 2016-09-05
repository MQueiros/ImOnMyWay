package com.example.imonmyway;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class ActLists extends BaseAdapter{

    private ArrayList<String> items = new ArrayList<String>();
    private ArrayList<String> images = new ArrayList<String>();
    private Context context;
    private Boolean showOption = false;


    public ActLists(Context applicationContext,
    		ArrayList<String> items, ArrayList<String> images, Boolean showOption) {

        super();
        this.context = applicationContext;
        this.items = items;
        this.images = images;
        this.showOption = showOption;

    }

    public ActLists() {
        super();
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int position) {

        return items.get(position);
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
            convertView = inflater.inflate(R.layout.listview_action_types, null);
        }

        ImageView imageClick = (ImageView) convertView
                .findViewById(R.id.row_click_imageView1);
        TextView item_text = (TextView) convertView
                .findViewById(R.id.Itemname);
        ImageView item_icon = (ImageView) convertView
                .findViewById(R.id.icon);

        if(showOption == false){
        	imageClick.setVisibility(View.GONE);
        }
        else{
        	imageClick.setVisibility(View.VISIBLE);
        }
        
        try {

        	item_text.setText(this.items.get(position));
        	item_text.setTextColor(Color.BLACK);
        	
        	int imageResource = this.context.getResources().getIdentifier("@drawable/" + images.get(position), null, this.context.getPackageName());
        	Drawable res = this.context.getResources().getDrawable(imageResource);
        	item_icon.setImageDrawable(res);

        	imageClick.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                	
                    switch (v.getId()) {
                    case R.id.row_click_imageView1:

                        PopupMenu popup = new PopupMenu(context.getApplicationContext(), v);
                        popup.getMenuInflater().inflate(R.menu.locations_action_popup,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                case R.id.showMap:

                                	Intent intent = new Intent(context, ShowLocations.class);
                                	
                                	Bundle b = new Bundle();
                                	b.putString("LocationName", items.get(position));
                                	intent.putExtras(b); //Put your id to your next Intent
                                	
                    		        context.startActivity(intent);

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
