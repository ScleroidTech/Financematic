package com.scleroid.financematic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.fragments.DashboardFragment;
import com.scleroid.financematic.fragments.ExpenseFragment;
import com.scleroid.financematic.fragments.LoanDetailsFragment;
import com.scleroid.financematic.fragments.PeopleFragment;
import com.scleroid.financematic.fragments.RegisterCustomerFragment;
import com.scleroid.financematic.fragments.ReportFragment;
import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.BottomNavigationViewHelper;
import com.scleroid.financematic.utils.InstantAppExecutors;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import es.dmoral.toasty.Toasty;
import io.bloco.faker.Faker;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GarlandApp.FakerReadyListener, HasSupportFragmentInjector {
    //TODO Refactor repeating code, look at  android-mvvm-architecture for ideas, its by mind-dorks
    // tags used to attach the fragments
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_NEW_CUSTOMER = "new_customer";
    private static final String TAG_REPORT = "report";
    private static final String TAG_EXPENSES = "expenses";
    private static final String TAG_SUMMERY = "Customer_Summery";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_NOTIFICATION = "notification";
    private static final int THREAD_COUNT = 3;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_DASHBOARD;

    @Inject
    ActivityUtils activityUtils;
    @Inject
    AppExecutors appExecutors;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;

        switch (item.getItemId()) {

            case R.id.navigation_home:
                /* toolbar.setTitle("Customer");*/
                fragment = new DashboardFragment();
                loadFragment(fragment);

                return true;
            case R.id.navigation_list:
                /* toolbar.setTitle("Loan Details");*/
                fragment = new PeopleFragment();
                loadFragment(fragment);
                return true;
            case R.id.person_details:
                /*    toolbar.setTitle("Report");*/
                fragment = new ReportFragment();
                loadFragment(fragment);

                return true;
            /* case R.id.navigation_person_loan_details:
             *//*  toolbar.setTitle("Person Details");*//*
                        fragment = new RegisterCustomerFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_reminders:
                    *//*  toolbar.setTitle("Reminder");*//*
                        fragment = new ReminderFragment();
                        loadFragment(fragment);
                        return true;

    */
        }
        return false;
    };
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private String[] activityTitles;


    @NonNull
    public static Intent newIntent(Context activity) {
        return new Intent(activity, MainActivity.class);
    }

    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);

                //Used to change the z index of a custom drawer,
                //Hack when navigation drawer doesn't listen to click events
                drawerView.bringToFront();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        /*bottom navigation*/

        /*    toolbar = getSupportActionBar();*/

        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // attaching bottom sheet behaviour - hide / show on scroll
        /*  CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();*/
        /* layoutParams.setBehavior(new BottomNavigationBehavior());*/

        // load the store fragment by default
        /* toolbar.setTitle("Finance Matic");*/
        // loadFragment(new DashboardFragment());
        appExecutors = new InstantAppExecutors();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            loadFragmentFromNavigationDrawers();
        }

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadFragmentFromNavigationDrawers() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app

        Runnable pendingRunnable = () -> {
            // update the main content by replacing fragments

            Fragment fragment = getCurrentFragment();
            loadFragment(fragment);
        };

        // If pendingRunnable is not null, then add to the message queue
        // boolean post = handler.post(pendingRunnable);
        appExecutors.diskIO().execute(pendingRunnable);

        // show or hide the fab button


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private void loadFragment(Fragment fragment) {
        activityUtils.loadFragment(fragment, getSupportFragmentManager());
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private Fragment getCurrentFragment() {
        switch (navItemIndex) {
            //TODO
            case 0:
                // dashboard
                return new DashboardFragment();
            case 1:
                // new Customers fragment
                return new RegisterCustomerFragment();
            case 2:
                // Report fragment
                return new ReportFragment();
            case 3:
                // Expenses fragment
                return new ExpenseFragment();
            case 4:
                // Expenses fragment
                return new LoanDetailsFragment();

          /*
           TODO
           case 4:
                // Notifications fragment
                return new Fragment();

            case 5:
                //setting fragment
                return new SettingsFragment();*/
            default:
                return new DashboardFragment();// HomeFragment.newInstance(HomeFragment.parcelCount);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem create_new = menu.findItem(R.id.action_create_new_loan);
        MenuItem notification = menu.findItem(R.id.action_notification);
        tintMenuIcon(this, create_new, android.R.color.white);
        tintMenuIcon(this, notification, android.R.color.white);
        return true;
    }

    public void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        if (item == null) return;
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }



/*
    https://stackoverflow.com/questions/23273275/toggle-button-not-working-for-navigation-drawer
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }*/




    /*bottom navigation*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_new_loan) {

            RegisterCustomerFragment fragment = new RegisterCustomerFragment();
            loadFragment(fragment);
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_notification) {
            LoanDetailsFragment fragment = new LoanDetailsFragment();
            loadFragment(fragment);
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /*sidebar navigation*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        Toasty.error(getBaseContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Check to see which item was being clicked and perform appropriate action
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_dashboard:
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                break;
            case R.id.nav_new_customer:
                navItemIndex = 1;
                CURRENT_TAG = TAG_NEW_CUSTOMER;
                break;
            case R.id.nav_report:
                navItemIndex = 2;
                CURRENT_TAG = TAG_REPORT;
                break;
            case R.id.nav_expenses:
                navItemIndex = 3;
                CURRENT_TAG = TAG_EXPENSES;
                break;
            case R.id.summery_details:
                navItemIndex = 4;
                CURRENT_TAG = TAG_SUMMERY;
                break;

          /* case R.id.nav_settings:
                navItemIndex = 5;
                CURRENT_TAG = TAG_SETTINGS;
                break;
            case R.id.nav_share:
                //TODO launch new intent instead of loading fragment
                //   startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                drawer.closeDrawers();
                return true;
            case R.id.nav_send:
                // launch new intent instead of loading fragment
                //TODO  startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                drawer.closeDrawers();
                return true;*/
            default:
                navItemIndex = 0;
        }
        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        loadFragmentFromNavigationDrawers();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFakerReady(Faker faker) {
        populateData(faker);


    }

    private void populateData(Faker faker) {
        int customerId = faker.number.positive();
        int accountNo = Integer.parseInt(faker.business.creditCardNumber());

    }

    private Customer createCustomerData(Faker faker, int customerId) {

        return new Customer(
                customerId,
                faker.name.name(),
                faker.phoneNumber.phoneNumber(),
                faker.address.streetAddress(),
                faker.address.city(),
                (faker.number.hexadecimal(12)) + "",
                (byte) faker.number.between(0, 6)

        );
    }

    private Loan createLoanData(Faker faker, int customerId, int accountNo) {

        return new Loan(
                faker.commerce.price(5000, 1000000),
                faker.date.backward(),
                faker.date.forward(),
                faker.number.between(1, 100),
                faker.commerce.price(0, 2000),
                faker.number.between(1, 20), faker.number.between(0, 20),
                (byte) faker.number.between(0, 9),
                faker.commerce.price(6000, 1100000),
                accountNo,
                customerId

        );
    }

    private TransactionModel createTransactionData(Faker faker, int accountNo) {

        return new TransactionModel(
                faker.number.positive(),
                faker.date.backward(),
                faker.commerce.price(),
                faker.commerce.price(),
                faker.commerce.price(0, 2000),
                accountNo

        );
    }

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    /**
     * Returns an {@link AndroidInjector} of {@link Fragment}s.
     */
    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
