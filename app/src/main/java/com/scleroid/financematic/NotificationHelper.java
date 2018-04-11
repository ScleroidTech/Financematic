package com.scleroid.financematic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Random;

;

/**
 * Helper class to manage notification channels, and create notifications.
 */
class NotificationHelper extends ContextWrapper {
    private NotificationManager mNotificationManager;
    public static final String FOLLOWERS_CHANNEL = "follower";

    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param context The application context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context context) {
        super(context);

        // Create the channel object with the unique ID FOLLOWERS_CHANNEL
        NotificationChannel followersChannel =
                new NotificationChannel(
                        FOLLOWERS_CHANNEL,
                        getString(R.string.notification_channel_followers),
                        NotificationManager.IMPORTANCE_DEFAULT);

        // Configure the channel's initial settings
        followersChannel.setLightColor(Color.GREEN);
        followersChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 500, 200, 500});

        // Submit the notification channel object to the notification manager
        getNotificationManager().createNotificationChannel(followersChannel);

    }

    /**
     * Get a follow/un-follow notification
     * <p>
     * <p>Provide the builder rather than the notification it's self as useful for making
     * notification changes.
     *
     * @param title the title of the notification
     * @param body  the body text for the notification
     * @return A Notification.Builder configured with the selected channel and details
     */
    public Notification.Builder getNotificationFollower(String title, String body) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent());
    }

    /**
     * Get a direct message notification
     * <p>
     * <p>Provide the builder rather than the notification it's self as useful for making
     * notification changes.
     *
     * @param title Title for notification.
     * @param body  Message for notification.
     * @return A Notification.Builder configured with the selected channel and details
     */
    public Notification.Builder getNotificationDM(String title, String body) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent());
    }

    /**
     * Create a PendingIntent for opening up the MainActivity when the notification is pressed
     *
     * @return A PendingIntent that opens the MainActivity
     */
    private PendingIntent getPendingIntent() {
        Intent openMainIntent = new Intent(this, com.scleroid.financematic.Notification.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(openMainIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
    }

    /**
     * Send a notification.
     *
     * @param id           The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, Notification.Builder notification) {
        getNotificationManager().notify(id, notification.build());
    }

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * <p>Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * Get a random name string from resources to add personalization to the notification
     *
     * @return A random name
     */
    public String getRandomName() {
        String[] names = getApplicationContext().getResources().getStringArray(R.array.names_array);
        return names[new Random().nextInt(names.length)];
    }
}
