package com.connectutb.callshield;

import java.io.IOException;

import com.connectutb.callshield.utils.DbHelper;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	static final int PICK_CONTACT=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updateCounters();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		checkRoot();
		return true;
	}
	
	
	public void updateCounters(){
		//Update the blocklist counter and the number of blocked calls 
		DbHelper db = new DbHelper(this);
		TextView blocklistNum = (TextView) findViewById(R.id.textBlocklistCounter);
		TextView blocklogNum= (TextView) findViewById(R.id.textBlockedCounter);
		blocklistNum.setText(db.getBlockListCount());
		blocklogNum.setText(db.getBlockLogCount());
	}
	
	 /** Responding to menu selections */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	//Handle item selection
    	switch (item.getItemId()){
    	case R.id.menu_settings:
    		Intent i = new Intent(this, Preferences.class);
        	startActivity(i);	
    		return true;
    	case R.id.menu_addBlockedNumber:
    		 //Let the user pick a contact number
    		addBlockedNumber();
    		return true;
    	case R.id.menu_showBlockList:
    		//Show list of blocked numbers
    		Intent blockList = new Intent(getBaseContext(), BlockListActivity.class);
    		startActivity(blockList);
    		return true;
    	case R.id.menu_showBlockLog:
    		//Show the block log
    		Intent blockLog = new Intent(getBaseContext(), BlockLogActivity.class);
    		startActivity(blockLog);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    public void checkRoot(){
    	//We need root permissions..
    	final Runtime runtime = Runtime.getRuntime();
    	try {
			runtime.exec("su");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void addBlockedNumber(){
    	 // Declare
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, PICK_CONTACT);	
    }
    
     //Handle result from the contact picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//Define our database manager
        DbHelper db = new DbHelper(this);
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{ 
                                ContactsContract.CommonDataKinds.Phone.NUMBER,  
                                ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME },
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        String number = c.getString(0);
                        String name = c.getString(1);
                        //Add the blocked number to the database
                        db.addBlockedNumber(number, name);
                        //Notify the user
                        showSelectedNumber(name, number);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }
    
    public void showSelectedNumber(String name, String number) {
        Toast.makeText(this, number + " " + this.getString(R.string.notification_blocked_number_added), Toast.LENGTH_LONG).show();      
    }
}
    
