package com.connectutb.callshield;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BlockLogAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] logitems;

	public BlockLogAdapter(Activity context, String[] numbers) {
		super(context, R.layout.blocklist_row, numbers);
		this.context = context;
		this.logitems = numbers;
	}
	
	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
		static class ViewHolder {
			public TextView textViewName;
			public TextView textViewNumber;
			public TextView textViewTimestamp;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ViewHolder will buffer the assess to the individual fields of the row
			// layout

			ViewHolder holder;
			// Recycle existing view if passed as parameter
			// This will save memory and time on Android
			// This only works if the base layout for all classes are the same
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(R.layout.blocklog_row, null, true);
				rowView.setBackgroundResource(R.drawable.bg_white_row);
				holder = new ViewHolder();
				holder.textViewName = (TextView) rowView.findViewById(R.id.textViewLogName);
				holder.textViewNumber = (TextView) rowView.findViewById(R.id.textViewLogNumber);
				holder.textViewTimestamp = (TextView) rowView.findViewById(R.id.textViewLogTimestamp);
				rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
			}
			String[] arrayString = logitems[position].split(";");
			String blockName = arrayString[1];
			String blockNumber = "("+arrayString[0]+")";
			holder.textViewName.setText(blockName);
			holder.textViewNumber.setText(blockNumber);
			holder.textViewTimestamp.setText(arrayString[2]);
			return rowView;
		}
}