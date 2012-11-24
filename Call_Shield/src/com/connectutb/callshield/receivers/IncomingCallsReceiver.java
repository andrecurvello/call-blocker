package com.connectutb.callshield.receivers;

import java.io.IOException;

import com.connectutb.callshield.MainActivity;
import com.connectutb.callshield.R;
import com.connectutb.callshield.utils.DbHelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

public class IncomingCallsReceiver extends BroadcastReceiver{
private Context context;
private static int CALLSHIELD_ID = 1982;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		// We check the incoming call, and if it matches anything in our database, we drop the call
		// and add the event to our block log.
		//Grab incoming call number
		Bundle bundle = intent.getExtras();
		String incomingNumber= bundle.getString("incoming_number");
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
	    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
		
		//Define database manager
		DbHelper db = new DbHelper(context);
		
		//Grab all the numbers in our blocklist
		String[] blocklist = db.listBlockedNumbers();
		//Loop through each item and check if it matches
		boolean isBlocked = false;
		String triggered_block_number = "";
		for( int i = 0; i < blocklist.length; i++)
		{
		    String numberString = blocklist[i];
		    String[] bNumberArray = numberString.split(";");
		    String bNumber = bNumberArray[0];
		    String bNumber_o = bNumber;
		    //Number formatting is a bitch..
		    
		    if (settings.getBoolean("exact_match",false)== false){
		    	//mold and format
		    	bNumber = bNumber.replace(" ", "");
		    	bNumber = bNumber.replace("-", "");
		    	incomingNumber = incomingNumber.replace(" ", "");
		    	incomingNumber = incomingNumber.replace("-", "");
		    	if (incomingNumber.contains(bNumber)){
		    		isBlocked=true;
		    		triggered_block_number = bNumber_o;
		    	}
		    	
		    }else{
		    if (incomingNumber.equals(bNumber)){
		    	isBlocked=true;
		    	triggered_block_number = bNumber_o;
		    }  
		}
		if (isBlocked){
			//We need to accept the call before ending it
	    	acceptTheCall();
			//Block the call
			blockTheCall();
			db.addBlockedLogItem(triggered_block_number);
			showNotification(incomingNumber, "name");
		}
	    }
	    }
	}
	
	public void blockTheCall(){
		try {
		    Thread.sleep(800);
		    Runtime.getRuntime().exec(new String[]{"su","-c","input keyevent 6"});

		}catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public void acceptTheCall(){
		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
	    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
	    context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
	}
	
	public void showNotification(String number, String name){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(context.getString(R.string.statusbar_call_blocked_title))
		        .setContentText(context.getString(R.string.statusbar_call_blocked_title));
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(CALLSHIELD_ID, mBuilder.build());
	}
}