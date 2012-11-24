package com.connectutb.callshield;

import android.app.ListActivity;
import android.os.Bundle;

import com.connectutb.callshield.utils.DbHelper;

public class BlockLogActivity extends ListActivity{
	//Define our database manager
    DbHelper db = new DbHelper(this);
    private String[] blocklog_array = new String[0];
    
    @Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		blocklog_array = db.listLog();
		setListAdapter(new BlockLogAdapter(this, blocklog_array));
	}
}
