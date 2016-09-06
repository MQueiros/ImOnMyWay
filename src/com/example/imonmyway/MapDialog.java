package com.example.imonmyway;

import android.app.Activity;
import android.content.Intent;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

	public class MapDialog extends Activity implements android.view.View.OnClickListener {

  	public Dialog d;
  	public Button yes, no;
	EditText edit_coord, edit_info, edit_name;
  	String info, coordinates;
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {

		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.activity_map_dialog);
		  
		  Bundle b = getIntent().getExtras();
		  
	        if (b != null) {
	            info = (String) getIntent().getExtras()
	                    .getSerializable("Info");
	            coordinates = (String) getIntent().getExtras()
	                    .getSerializable("Coordinates");
            }
		  
		  edit_coord = (EditText) findViewById(R.id.edit_coord);
		  edit_coord.setText(coordinates);
		  
		  edit_info = (EditText) findViewById(R.id.edit_info);
		  edit_info.setText(info);
		  
		  edit_name = (EditText) findViewById(R.id.edit_name);
		  edit_name.setText("Location 1");
		  
		  yes = (Button) findViewById(R.id.ok_button);
		  no = (Button) findViewById(R.id.cancel_button);
		  
		  yes.setOnClickListener(this);
		  no.setOnClickListener(this);
	
	  }
	
	  @Override
	  public void onClick(View v) {
	    
	    Intent intent = new Intent();
		  
		switch (v.getId()) {
	    case R.id.ok_button:
	      
	      if (edit_name.getText().toString().trim().length() > 0 && edit_info.getText().toString().trim().length() > 0){
		      
		      intent.putExtra("Name", edit_name.getText().toString().trim().toUpperCase());
		      intent.putExtra("Info", edit_info.getText().toString().trim().toUpperCase());
		      setResult(Activity.RESULT_OK, intent);
		      finish();
	      }
	      
	      break;
	      
	    case R.id.cancel_button:
	    	
	      setResult(Activity.RESULT_CANCELED, intent);
	      finish();
	      break;
	      
	    default:
	      break;
	    }
	    
	  }
}

