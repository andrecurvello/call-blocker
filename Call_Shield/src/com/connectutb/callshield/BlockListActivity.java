package com.connectutb.callshield;

import android.app.ListActivity;
import android.os.Bundle;

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

}
