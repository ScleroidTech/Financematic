package com.scleroid.financematic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;

/**
 * Created by scleroid on 11/4/18.
 */
public class Notification1 extends BaseFragment {

    Button b1;
    public Notification1 () {
        // Required empty public constructor
    }

    public static Notification1  newInstance(String param1, String param2) {
        Notification1 fragment = new Notification1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return layout resource id
     */
    @Override
    public int getLayoutId() {
        return R.layout.notification_main;
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View rootView = getRootView();

        b1 = rootView .findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
               /* displayNotification();*/
              /*  sendNotification();*/
            }
        });


        return rootView;


    }
    private void addNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.getActivity())
                        .setSmallIcon(R.drawable.ic_calendarclocktime)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");



        Intent notificationIntent = new Intent(this.getActivity(), Notification.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.getActivity(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager)getActivity(). getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }




    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

 /*   public void sendNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getActivity());
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.scleroid.financematic"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getActivity(), 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Notifications Title");
        builder.setContentText("Your notification content here.");
        builder.setSubText("Tap to view the website.");
        builder.setChannelId(channelId);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(1, builder.build());
    }*/
/*    protected void displayNotification() {
        Log.i("Start", "notification");

   *//* Invoking the default notification service *//*
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this.getActivity());

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.ic_calendarclocktime);

   *//* Increase notification number every time a new notification arrives *//*
       mBuilder.setNumber(++numMessages);

   *//* Add Big View Specific Configuration *//*
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   *//* Creates an explicit intent for an Activity in your app *//*
        Intent resultIntent = new Intent(this.getActivity(), NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getActivity());
        stackBuilder.addParentStack(NotificationView.class);

   *//* Adds the Intent that starts the Activity to the top of the stack *//*
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

   *//* notificationID allows you to update the notification later on. *//*
       mNotificationManager.notify(notificationID, mBuilder.build());
    }*/




}
