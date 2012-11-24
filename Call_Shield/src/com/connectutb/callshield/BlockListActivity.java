package com.connectutb.callshield;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.connectutb.callshield.utils.DbHelper;

public class BlockListActivity  extends ListActivity{
	//Define our database manager
    DbHelper db = new DbHelper(this);
    private String[] blocklist_array = new String[0];
    
    @Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		blocklist_array = db.listBlockedNumbers();
		setListAdapter(new BlockListAdapter(this, blocklist_array));
	}
    
	/** When an item is clicked, we delete it from the database */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
    	super.onListItemClick(l, v, position, id);
    
    	//Grab the number
    	Object o = this.getListAdapter().getItem(position);
    	String[] keyword = o.toString().split(";");
    	String contactNumber = keyword[0];
    	db.deleteEntry(contactNumber);
    	//Notify user
    	Toast.makeText(this, contactNumber + " " + this.getString(R.string.notification_blocked_number_removed), Toast.LENGTH_LONG).show();      
    	//Refresh the list
		blocklist_array = db.listBlockedNumbers();
		setListAdapter(new BlockListAdapter(this, blocklist_array));
    }
}
