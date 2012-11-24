package com.connectutb.callshield.receivers;

import com.connectutb.callshield.utils.DbHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IncomingCallsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// We check the incoming call, and if it matches anything in our database, we drop the call
		// and add the event to our block log.
		//Grab incoming call number
		//String incomingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		Bundle bundle = intent.getExtras();
		String incomingNumber= bundle.getString("incoming_number");
		
		//Define database manager
		DbHelper db = new DbHelper(context);
		
		//Grab all the numbers in our blocklist
		String[] blocklist = db.listBlockedNumbers();
		boolean gotMatch = false;
		//Loop through each item and check if it matches
		for( int i = 0; i < blocklist.length; i++)
		{
		    String bNumber = blocklist[i];
		    if (incomingNumber.equals(bNumber)){
				//Block the call
				abortBroadcast();
				db.addBlockedLogItem(incomingNumber);
		    }  
		}
	}
}
