package com.scleroid.financematic;

/**
 * Created by scleroid on 16/4/18.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String NOTIFY = "notification_fragment";
	private static final String CHANNEL_ID = "com.scleroid.financematic.channelId";


	@Override
	public void onReceive(@NonNull Context context, Intent intent) {

		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.putExtra(NOTIFY, true);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(notificationIntent);

		PendingIntent pendingIntent =
				stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(context);
		//add
	   /*  notificationIntent.getStringExtra("id");
		String name = notificationIntent.getStringExtra("name");
*/
		Notification notification = builder.setContentTitle("Customer Name")
				.setContentText("RS 2000 Due in 2 day's")
				.setTicker("New Message Alert!")
				.setSmallIcon(R.drawable.ic_calendarclocktime)
				.setContentIntent(pendingIntent).build();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			builder.setChannelId(CHANNEL_ID);
		}

		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
					CHANNEL_ID,
					"Notification",
					IMPORTANCE_DEFAULT
			);
			notificationManager.createNotificationChannel(channel);
		}

		notificationManager.notify(0, notification);
	}
}
