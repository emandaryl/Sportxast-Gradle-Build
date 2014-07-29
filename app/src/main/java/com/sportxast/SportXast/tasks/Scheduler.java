package com.sportxast.SportXast.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Scheduler extends BroadcastReceiver {
  public static final int REQUEST_CODE = 1234523385;
  public static final String ACTION_ALARM = "com.sportxast.SportXast.tasks.Scheduler";
  // Triggered by the Alarm periodically (starts the service to run task)
  @Override
  public void onReceive(Context context, Intent intent) {
    Intent i = new Intent(context, SyncService.class);
    Bundle bundle = intent.getExtras();
	String action = bundle.getString(ACTION_ALARM);
//	if (action.equals(ACTION_ALARM)) {
		context.startService(i);
//	}
  }

}
