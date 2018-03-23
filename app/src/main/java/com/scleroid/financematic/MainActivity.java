package com.scleroid.financematic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.scleroid.financematic.fragments.DashboardFragment;
import com.scleroid.financematic.fragments.Fragment_report;
import com.scleroid.financematic.fragments.LoanDetailsFragment;
import com.scleroid.financematic.fragments.PeopleFragment;
import com.scleroid.financematic.fragments.RegisterCustomerFragment;
import com.scleroid.financematic.fragments.RegisterMoneyFragment;
import com.scleroid.financematic.utils.BottomNavigationViewHelper;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBar toolbar;
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
                fragment = new Fragment_report();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*sidebar navigation*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);







        /*bottom vavigation*/

  /*    toolbar = getSupportActionBar();*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // attaching bottom sheet behaviour - hide / show on scroll
      /*  CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();*/
       /* layoutParams.setBehavior(new BottomNavigationBehavior());*/

        // load the store fragment by default
       /* toolbar.setTitle("Finance Matic");*/
        loadFragment(new DashboardFragment());

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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



/*
    https://stackoverflow.com/questions/23273275/toggle-button-not-working-for-navigation-drawer
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }*/




    /*bottom navigation*/

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), item.getTitle() + " clicked", Snackbar.LENGTH_SHORT);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.navigation_list) {
            fragment = new PeopleFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();

        } else if (id == R.id.nav_slideshow) {
            fragment = new RegisterMoneyFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
