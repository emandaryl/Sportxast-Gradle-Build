package com.sportxast.SportXast.tasks;

import android.content.ContentValues;
import android.content.Context;

import com.sportxast.SportXast.localdatabase.DatabaseHelper;
import com.sportxast.SportXast.localdatabase.Keys;

import java.util.ArrayList;

public class SynchronizeTask {
	private Context context;
	private DatabaseHelper DB;

	public SynchronizeTask(Context c) {
		this.context = c;
		DB = new DatabaseHelper(context);
	}
	
	public void execute() {
		ArrayList<ContentValues> allQueueItems = DB.getAllItem(Keys.KEY_MEDIA_QUEUE.TABLE_MEDIA_QUEUE);
		for (ContentValues item : allQueueItems) {
			
		}
	}
}