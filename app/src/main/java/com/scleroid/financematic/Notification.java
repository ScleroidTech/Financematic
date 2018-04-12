package com.scleroid.financematic;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;

/**
 * Created by scleroid on 11/4/18.
 */
public class Notification extends BaseFragment {

    private static final String TAG = Notification.class.getSimpleName();

    private static final int NOTIFICATION_FOLLOW = 1100;
    private static final int NOTIFICATION_UNFOLLOW = 1101;
    private static final int NOTIFICATION_DM_FRIEND = 1200;
    private static final int NOTIFICATION_DM_COWORKER = 1201;

    /*
     * A view model for interacting with the UI elements.
     */
    private MainUi mUIModel;

    /*
     * A helper class for initializing notification channels and sending notifications.
     */
    private com.scleroid.financematic.NotificationHelper mNotificationHelper;


    public Notification () {
        // Required empty public constructor
    }

    public static Notification  newInstance(String param1, String param2) {
        Notification fragment = new Notification();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return layout resource id
     */
    @Override
    public int getLayoutId() {
        return R.layout.notification_tray;
    }

    @Override
    protected void subscribeToLiveData() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View rootView = getRootView();
        mNotificationHelper = new com.scleroid.financematic.NotificationHelper(this.getActivity());
        mUIModel = new MainUi(rootView.findViewById(R.id.activity_main));


        return rootView;


    }
    private void sendNotification(int id) {




        android.app.Notification.Builder notificationBuilder = null;
        switch (id) {
            case NOTIFICATION_FOLLOW:
                notificationBuilder =
                        mNotificationHelper.getNotificationFollower(
                                getString(R.string.follower_title_notification),
                                getString(R.string.follower_added_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;


            case NOTIFICATION_UNFOLLOW:
                notificationBuilder =
                        mNotificationHelper.getNotificationFollower(
                                getString(R.string.follower_title_notification),
                                getString(R.string.follower_removed_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;

            case NOTIFICATION_DM_FRIEND:
                notificationBuilder =
                        mNotificationHelper.getNotificationDM(
                                getString(R.string.direct_message_title_notification),
                                getString(R.string.dm_friend_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;

            case NOTIFICATION_DM_COWORKER:
                notificationBuilder =
                        mNotificationHelper.getNotificationDM(
                                getString(R.string.direct_message_title_notification),
                                getString(R.string.dm_coworker_notification_body,
                                        mNotificationHelper.getRandomName()));
                break;
        }
        if (notificationBuilder != null) {
            mNotificationHelper.notify(id, notificationBuilder);
        }
    }

    /** Send Intent to load system Notification Settings for this app. */
    private void goToNotificationSettings() {
   /*     Intent i = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE,getActivity().getPackageName());
        startActivity(i);*/
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

//for Android 5-7
        intent.putExtra("app_package", getActivity().getPackageName());
        intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);

// for Android O
        intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
    }

    /**
     * Send intent to load system Notification Settings UI for a particular channel.
     *
     * @param channel Name of channel to configure
     */
    private void goToNotificationChannelSettings(String channel) {
        // Skeleton method to be completed later
    }

    /**
     * View model for interacting with Activity UI elements. (Keeps core logic for sample separate.)
     */
    class MainUi implements View.OnClickListener {

        private MainUi(View root) {

            // Setup the buttons
            (root.findViewById(R.id.follow_button)).setOnClickListener(this);
            (root.findViewById(R.id.un_follow_button)).setOnClickListener(this);
            (root.findViewById(R.id.follower_channel_settings_button)).setOnClickListener(this);
            (root.findViewById(R.id.friend_dm_button)).setOnClickListener(this);
            (root.findViewById(R.id.coworker_dm_button)).setOnClickListener(this);
            (root.findViewById(R.id.dm_channel_settings_button)).setOnClickListener(this);
            (root.findViewById(R.id.go_to_settings_button)).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.follow_button:
                    sendNotification(NOTIFICATION_FOLLOW);
                    break;
                case R.id.un_follow_button:
                    sendNotification(NOTIFICATION_UNFOLLOW);
                    break;
                case R.id.follower_channel_settings_button:
                    goToNotificationChannelSettings("");
                    break;
                case R.id.friend_dm_button:
                    sendNotification(NOTIFICATION_DM_FRIEND);
                    break;
                case R.id.coworker_dm_button:
                    sendNotification(NOTIFICATION_DM_COWORKER);
                    break;
                case R.id.dm_channel_settings_button:
                    goToNotificationChannelSettings("");
                    break;
                case R.id.go_to_settings_button:
                    goToNotificationSettings();
                    break;
                default:
                    Log.e(TAG, getString(R.string.error_click));
                    Toast.makeText(getActivity().getApplicationContext(),"Toast error",Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }



}
