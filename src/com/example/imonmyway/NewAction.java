package com.example.imonmyway;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class NewAction extends ActionBarActivity{

	public static final int PICK_TYPE    	 = 1;
	public static final int PICK_LOCATION    = 2;
	public static final int PICK_CONTACT     = 3;
	
	Button add_new_action;
	EditText pick_contacts, text_edit;
	EditText type, place;
	String[] action_types = {"Send a Message", "Make a Call"};
	String[] action_types_icon = {"ic_action_chat", "ic_action_chat"};
	TableRow to_row, place_row, text_row;
	ActionsStruct new_action = new ActionsStruct();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_action);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("New Action");
		
		to_row = (TableRow) findViewById(R.id.to_row);
		to_row.setVisibility(View.GONE);
		place_row = (TableRow) findViewById(R.id.place_row);
		place_row.setVisibility(View.GONE);
		text_row = (TableRow) findViewById(R.id.text_row);
		text_row.setVisibility(View.GONE);
		
		add_new_action = (Button) findViewById(R.id.buttonAddNewAction);
		add_new_action.setEnabled(false);
		add_new_action.setAlpha(.5f);
		
		add_new_action.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new_action.setText(text_edit.getText().toString());
				
                Intent intent = new Intent();
                intent.putExtra("NewAction", new_action);
  	      	 	setResult(Activity.RESULT_OK, intent);
  	      	 	finish();
			}
		});
		
		pick_contacts = (EditText) findViewById(R.id.to_edit);
		
		pick_contacts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
	             startActivityForResult(intent, PICK_CONTACT);
			}
		});
		
		type = (EditText) findViewById(R.id.type_text);
		
		type.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewAction.this, ActionTypesActivity.class);
		        startActivityForResult(intent, PICK_TYPE);
			}
		});
		
		place = (EditText) findViewById(R.id.place_edit);
		
		place.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewAction.this, ActionLocationsActivity.class);
		        startActivityForResult(intent, PICK_LOCATION);
			}
		});
		
		text_edit = (EditText) findViewById(R.id.text_edit);
		
		text_edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_TYPE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                	
                	String result = (String) b.getSerializable("Type");
                	
                	type = (EditText) findViewById(R.id.type_text);
                    type.setText(result);
                    
                    to_row.setVisibility(View.VISIBLE);
                    place_row.setVisibility(View.VISIBLE);
                    
                    if(result.equals(action_types[0])){
                    	text_row.setVisibility(View.VISIBLE);
                    	new_action.setType(0);
                    	text_edit.setText("");
                    }
                    else{
                    	text_row.setVisibility(View.GONE);
                    	new_action.setType(1);
                    	text_edit.setText("None");
                    }
                    
                    new_action.setContactList(null);
                    new_action.setLocation(null);
                    new_action.setText(null);
                    
                    pick_contacts.setText("");
                    place.setText("");
                    
                    
                }  
            } else if (resultCode == 0) {
                // Do Nothing...    
            }
        }
        if (requestCode == PICK_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                	
                    place.setText((String) b.getSerializable("Place"));
                    new_action.setLocation((String) b.getSerializable("Place"));
                }  
            } else if (resultCode == 0) {
                // Do Nothing...    
            }
        }
        if (requestCode == PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
            	
            	// Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
             // Form an array specifying which columns to return, you can add more.
                String[] projection = new String[] {
                                         ContactsContract.Contacts.DISPLAY_NAME,
                                         Phone.NUMBER
                                      };

                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(column);
                number = number.replaceAll("\\s","");
                
                column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(column);

                Contact contact = new Contact(name, Integer.parseInt(number));
                ArrayList<Contact> contact_list = new ArrayList<Contact>();
                contact_list.add(contact);
                
                pick_contacts.setText(name + " (" + number + ")");
                new_action.setContactList(contact_list);
            	
            }
        } else if (resultCode == 0) {
                // Do Nothing...    
        }
        
    	if(new_action.getContactList() != null && new_action.getLocation() != null){
    		add_new_action.setEnabled(true);
    		add_new_action.setAlpha(1);
    	}
    		
    	else{
    		add_new_action.setEnabled(false);
    		add_new_action.setAlpha(.5f);
    	}
        
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home: 
            // API 5+ solution
            onBackPressed();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
}
