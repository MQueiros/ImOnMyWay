package com.example.imonmyway;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class BackgroundActionsService extends Service {

	private static final String TAG = "BOOMBOOMTESTGPS";
	private LocationManager mLocationManager = null;
	private static final int LOCATION_INTERVAL = 1000;
	private static final float LOCATION_DISTANCE = 10f;
	private Location mLastLocation;
	
	List<Location> location_list = new ArrayList<Location>();
	
    private final int UPDATE_INTERVAL = 1 * 1000;
    private Timer timer = new Timer();
    private static int a = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    ArrayList<ActionsStruct> actionsRunningList = new ArrayList<ActionsStruct>();
    Boolean isRunning = false;
    String endTime = null;
    RemoteViews remoteViews;

    Boolean all_actions_done = false;
    short number_of_actions_done = 0;
    
    private static final String MyOnClick = "myOnClickTag";
    
    private class LocationListener implements android.location.LocationListener{
        
    	public LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation.set(location);
        }
        @Override
        public void onProviderDisabled(String provider)
        {
        	
        }
        @Override
        public void onProviderEnabled(String provider)
        {
        	
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        	
        }
    } 
    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    
    public BackgroundActionsService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

    	initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }

        super.onDestroy();
        
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    
                }
            }
        }
        
        TinyDB tinydb = new TinyDB(BackgroundActionsService.this);
        tinydb.putListObjectActions("RunningActions", new ArrayList<ActionsStruct>());
		tinydb.putBoolean("IsRunning", false);
		tinydb.putString("RunningEndTime", "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

    	TinyDB tinydb = new TinyDB(BackgroundActionsService.this);
    	actionsRunningList = tinydb.getListObjectActions("RunningActions", ActionsStruct.class);
    	isRunning = tinydb.getBoolean("IsRunning");
    	endTime = tinydb.getString("RunningEndTime");
    	
    	ArrayList<LocationsStruct> locationsList = new ArrayList<LocationsStruct>();
    	
    	tinydb = new TinyDB(this);
        locationsList = tinydb.getListObject("MyLocations", LocationsStruct.class);
		
		for(int i = 0; i < actionsRunningList.size(); i++){
			String location_name = actionsRunningList.get(i).getLocation();
			
			Boolean is_location = false;
			
			for(int j = 0; j < locationsList.size(); j++){
				if(locationsList.get(j).getName().equals(location_name)){
					location_list.add(locationsList.get(j).getCoordinates());
					is_location = true;
					break;
				}
			}
			
			if(!is_location){
				Toast.makeText(this, "Invalid Location!!!", Toast.LENGTH_LONG);
				super.onDestroy();
			}
			
		}
    	
    	remoteViews = new RemoteViews(getPackageName(),  
		R.layout.widget_performing_actions);  
		mBuilder = new NotificationCompat.Builder(  
                 getApplicationContext()).setSmallIcon(R.drawable.imonmyway_app_launcher_no_background).setContent(  
                 remoteViews);  
        
		Intent resultIntent = new Intent(getApplicationContext(), RunningActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());  
        
		stackBuilder.addParentStack(IntroActivity.class);  
       
		stackBuilder.addNextIntent(resultIntent);  
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,  
                 PendingIntent.FLAG_UPDATE_CURRENT);  
		remoteViews.setOnClickPendingIntent(R.id.mainText, resultPendingIntent);  
		
		remoteViews.setOnClickPendingIntent(R.id.button1, 
                getPendingSelfIntent(getApplicationContext(), MyOnClick));
		
		mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);  
       
		mBuilder.setOngoing(true);
		mNotificationManager.notify(100, mBuilder.build()); 
		
		startForeground(100, mBuilder.build());

		System.out.println("Started!!!");
		
        Toast.makeText(this, "Started!", Toast.LENGTH_LONG).show();
        
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
            	remoteViews.setTextViewText(R.id.from, "Debug " + Integer.toString(a));
                remoteViews.setTextViewText(R.id.mainText, "Performing Actions...\n" + number_of_actions_done + "/" + actionsRunningList.size() + " actions performed");
                remoteViews.setTextViewText(R.id.to, "Running Until: " + endTime);
            	mNotificationManager.notify(100, mBuilder.build());
            	a++;
            	ProcessSecond();
            }
        }, 0, UPDATE_INTERVAL);
        
        return START_STICKY;
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
    
    public void onReceive(Context context, Intent intent) {

    	System.out.println("Outside!!!");
    	Toast.makeText(this, "Outside!", Toast.LENGTH_LONG).show();
//        if (MyOnClick.equals(intent.getAction())){
        	Toast.makeText(this, "Inside!", Toast.LENGTH_LONG).show();
        	String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
            nMgr.cancel(100); // Closes the notification with this ID
            super.onDestroy(); // Should destroy the service...
//        }
    };
    
    private void ProcessSecond(){
    	// Each Second (duh!)
    	
    	if(all_actions_done)
    		return; // All Actions Done. Waiting for the user to stop the service...
    	
    	for(int i = 0; i < location_list.size(); i++){
    		
    		if(mLastLocation != null){
    
    			if((actionsRunningList.get(i).getUsedState() == 0) && (mLastLocation.distanceTo(location_list.get(i)) < 100)){ // 100 meters
    				
    				Contact phone = actionsRunningList.get(i).getContactList().get(0);
    				if(actionsRunningList.get(i).getType() == 0)
    				{
    					String message = actionsRunningList.get(i).getText();
    					sendSMS(Integer.toString(phone.getNumber()).trim(), message);
    					
    					// TODO: Notificação de mensagem enviada...
    				}
    				else if(actionsRunningList.get(i).getType() == 1)
    				{
    					Intent intent = new Intent(Intent.ACTION_CALL);
    					intent.setData(Uri.parse("tel:" + Integer.toString(phone.getNumber()).trim()));
    					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    					intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
    					startActivity(intent);
    					
    					// TODO: Notificação de chamada efetuada...
    				}

    				actionsRunningList.get(i).setUsedState(1);
                    number_of_actions_done++;
                    remoteViews.setProgressBar(R.id.progress, 100, (int) ((float)number_of_actions_done/(float)actionsRunningList.size())*100, false);
    				
    				all_actions_done = true;
    				for(int j = 0; j < actionsRunningList.size(); j++){
    					if(actionsRunningList.get(i).getUsedState() == 0){
    						all_actions_done = false;
    						break;
    					}
    				}
    				
    				if(all_actions_done);
    					// All Done!
    			}
    		}
    		
    	}
    }
    
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    
    private void stopService() {
        if (timer != null) timer.cancel();
        
    }
    
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}