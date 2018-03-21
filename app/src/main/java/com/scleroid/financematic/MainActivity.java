package com.scleroid.financematic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.scleroid.financematic.fragments.DashboardFragment;
import com.scleroid.financematic.fragments.FragmentReport;
import com.scleroid.financematic.fragments.Fragment_Reg_new_customer;
import com.scleroid.financematic.fragments.Fragment_list_all_peoples;
import com.scleroid.financematic.fragments.Fragment_registor_given_money;
import com.scleroid.financematic.fragments.Fragment_reminder;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {

                case R.id.navigation_home:
                   /* toolbar.setTitle("Customer");*/
                    fragment = new DashboardFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_list:
                   /* toolbar.setTitle("Loan Details");*/
                    fragment = new Fragment_list_all_peoples();
                    loadFragment(fragment);
                    return true;
                case R.id.person_details:
                /*    toolbar.setTitle("Report");*/
                    fragment = new FragmentReport();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_person_loan_details:
               /*  toolbar.setTitle("Person Details");*/
                    fragment = new Fragment_Reg_new_customer();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_reminders:
                /*  toolbar.setTitle("Reminder");*/
                    fragment = new Fragment_reminder();
                    loadFragment(fragment);
                    return true;


            }
            return false;
        }
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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
      /*  CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();*/
       /* layoutParams.setBehavior(new BottomNavigationBehavior());*/

        // load the store fragment by default
       /* toolbar.setTitle("Finance Matic");*/
        loadFragment(new DashboardFragment());

    }

  /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
            fragment = new Fragment_list_all_peoples();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();

        } else if (id == R.id.nav_slideshow) {
            fragment = new Fragment_registor_given_money();
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
