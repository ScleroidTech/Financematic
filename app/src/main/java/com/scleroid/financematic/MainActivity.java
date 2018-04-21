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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.scleroid.financematic.base.BaseActivity;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.ExpenseRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.fragments.RegisterCustomerFragment;
import com.scleroid.financematic.fragments.customer.CustomerFragment;
import com.scleroid.financematic.fragments.dashboard.DashboardFragment;
import com.scleroid.financematic.fragments.dialogs.DelayDialogFragment;
import com.scleroid.financematic.fragments.dialogs.RegisterReceivedDialogFragment;
import com.scleroid.financematic.fragments.expense.ExpenseFragment;
import com.scleroid.financematic.fragments.loanDetails.LoanDetailsFragment;
import com.scleroid.financematic.fragments.people.PeopleFragment;
import com.scleroid.financematic.fragments.report.ReportFragment;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.BottomNavigationViewHelper;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.bloco.faker.Faker;
import timber.log.Timber;

import static com.scleroid.financematic.AlarmReceiver.NOTIFY;
import static com.scleroid.financematic.utils.CommonUtils.makeToast;

public class MainActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener, GarlandApp.FakerReadyListener,
		           HasSupportFragmentInjector {
	//TODO Refactor repeating code, look at  android-mvvm-architecture for ideas, its by mind-dorks
	// tags used to attach the fragments
	private static final String TAG_DASHBOARD = "dashboard";
	private static final String TAG_NEW_CUSTOMER = "new_customer";
	private static final String TAG_REPORT = "report";
	private static final String TAG_EXPENSES = "expenses";
	private static final String TAG_SUMMERY = "Customer_Summery";
	private static final String TAG_NOTIFICATION = "notification";
	private static final String DIALOG_DELAY = "Delay Payment";
	private static final String DIALOG_MONEY_RECEIVED = "Received Payment";
	private static final String TAG_PEOPLE = "People";
	// index to identify current nav menu item
	public static int navItemIndex = 0;
	public static String CURRENT_TAG = TAG_DASHBOARD;

	@Inject
	CustomerRepo customerRepo;


	@Inject
	ActivityUtils activityUtils;
	@Inject
	AppExecutors appExecutors;
	@Inject
	LoanRepo loanRepo;
	@Inject
	TransactionsRepo transactionsRepo;
	@Inject
	InstallmentRepo installmentRepo;
	@Inject
	ExpenseRepo expenseRepo;
	@Inject
	DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
	@Inject
	Context context;
	private ActionBarDrawerToggle toggle;



	private DrawerLayout drawer;
	private NavigationView navigationView;
	private String[] activityTitles;
	private BottomNavigationView bottomNavigationView;

	@NonNull
	public static Intent newIntent(Context activity) {
		return new Intent(activity, MainActivity.class);
	}

	public CustomerRepo getCustomerRepo() {
		return customerRepo;
	}

	public LoanRepo getLoanRepo() {
		return loanRepo;
	}

	public TransactionsRepo getTransactionsRepo() {
		return transactionsRepo;
	}

	public InstallmentRepo getInstallmentRepo() {
		return installmentRepo;
	}

	public ExpenseRepo getExpenseRepo() {
		return expenseRepo;
	}

	public ActionBarDrawerToggle getToggle() {
		return toggle;
	}

	public void setToggle(final ActionBarDrawerToggle toggle) {
		this.toggle = toggle;
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


		final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		drawer = findViewById(R.id.drawer_layout);


		navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// load toolbar titles from string resources
		activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

		/*bottom navigation*/


		setToggle(new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close) {

			@Override
			public void onDrawerOpened(View drawerView) {
				// Code here will be triggered once the drawer open as we dont want anything to
				// happen so we leave this blank
				super.onDrawerOpened(drawerView);

				//Used to change the z index of a custom drawer,
				//Hack when navigation drawer doesn't listen to click events
				drawerView.bringToFront();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// Code here will be triggered once the drawer closes as we dont want anything to
				// happen so we leave this blank
				super.onDrawerClosed(drawerView);
			}
		});

		drawer.addDrawerListener(toggle);
		toggle.syncState();
		bottomNavigationView = findViewById(R.id.navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener
				(item -> {
					handleUiClick(item);
					return true;
				});
		BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

		// attaching bottom sheet behaviour - hide / show on scroll
		/*  CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
		navigation.getLayoutParams();*/
		/* layoutParams.setBehavior(new BottomNavigationBehavior());*/

		// load the store fragment by default
		/* toolbar.setCustomerName("Finance Matic");*/
		// loadFragment(new DashboardFragment());
		//     appExecutors = new InstantAppExecutors();
		if (savedInstanceState == null) {
			navItemIndex = 0;
			CURRENT_TAG = TAG_DASHBOARD;
			loadFragmentFromNavigationDrawers();
		}
		boolean frag = getIntent().getBooleanExtra(NOTIFY, false);
		if (frag) {
			CURRENT_TAG = TAG_NOTIFICATION;
			loadFragment(NotificationActivity.newInstance());
		}


		((GarlandApp) getApplication()).addListener(this);

	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

	/**
	 * @return actionBar
	 */
	@Override
	public ActionBar getActionBarBase() {
		return getSupportActionBar();
	}

	/***
	 * Returns respected fragment that user
	 * selected from navigation menu
	 */
	private void loadFragmentFromNavigationDrawers() {
		// selecting appropriate nav menu item
		selectNavMenu();

		//selecting appropriate bottom navigation menu item
		if (navItemIndex < 3) { selectBottomNavMenu(); }
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

	private void selectNavMenu() {
		navigationView.getMenu().getItem(navItemIndex).setChecked(true);
	}

	private void selectBottomNavMenu() {
		MenuItem item = bottomNavigationView.getMenu().getItem(navItemIndex);
		if (item != null) { item.setChecked(true); }
	}
	private void setToolbarTitle() {
		getSupportActionBar().setTitle(activityTitles[navItemIndex]);
	}



/*
    https://stackoverflow.com/questions/23273275/toggle-button-not-working-for-navigation-drawer
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }*/




	/*bottom navigation*/

	private Fragment getCurrentFragment() {
		switch (navItemIndex) {
			//TODO
			case 0:
				// dashboard
				return new DashboardFragment();
			case 1:
				return new PeopleFragment();

			case 2:
				// Report fragment
				return new ReportFragment();
			case 3:
				// new Customers fragment
				return new RegisterCustomerFragment();
			case 4:
				// Expenses fragment
				return new ExpenseFragment();

          /*
           TODO
           case 4:
                // Notifications fragment
                return new Fragment();

            case 5:
                //setting fragment
                return new SettingsFragment();*/
			default:
				return new DashboardFragment();// HomeFragment.newInstance(HomeFragment
			// .parcelCount);
		}

	}

	private void loadFragment(Fragment fragment) {
		activityUtils.loadFragmentWithoutBackStack(fragment, getSupportFragmentManager());
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
			Notification fragment = new Notification();
			/*CustomerFragment fragment = new CustomerFragment();*/
			loadFragment(fragment);
		}

		// user is in notifications fragment
		// and selected 'Clear All'
		if (id == R.id.action_settings) {
			Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG)
					.show();
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
		//	Toasty.error(getBaseContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT)
		// .show();
		handleUiClick(item);


		return true;
	}

	private void handleUiClick(final @NonNull MenuItem item) {
		//Check to see which item was being clicked and perform appropriate action
		switch (item.getItemId()) {
			//Replacing the main content with ContentFragment Which is our Inbox View;
			case R.id.navigation_home:
			case R.id.nav_dashboard:
				navItemIndex = 0;
				CURRENT_TAG = TAG_DASHBOARD;
				break;
			case R.id.nav_new_customer:
				navItemIndex = 3;
				CURRENT_TAG = TAG_NEW_CUSTOMER;
				break;
			case R.id.navigation_report:
			case R.id.nav_report:
				navItemIndex = 2;
				CURRENT_TAG = TAG_REPORT;
				break;
			case R.id.nav_expenses:
				navItemIndex = 4;
				CURRENT_TAG = TAG_EXPENSES;
				break;

			case R.id.navigation_list_people:
				navItemIndex = 1;
				CURRENT_TAG = TAG_PEOPLE;
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
	}


	@Override
	public void onFakerReady(Faker faker) {
		Timber.wtf("is this called?");

		addFakeData(faker);

	}

	private void addFakeData(final Faker faker) {
		final TempDataFaker tempDataFaker = new TempDataFaker();
		for (int i = 0; i < 5; i++)
			tempDataFaker.populateData(faker);
		tempDataFaker.saveInDatabase(this);
	}


	/**
	 * Returns an {@link AndroidInjector} of {@link Fragment}s.
	 */
	@Override
	public AndroidInjector<Fragment> supportFragmentInjector() {
		return fragmentDispatchingAndroidInjector;
	}

	@Subscribe
	public void onCallClick(Events.placeCall phoneNumberCarrier) {

		String mobileNumber = phoneNumberCarrier.getNumber();

		activityUtils.callIntent(this, mobileNumber);


	}

	@Subscribe
	public void onAddressClick(Events.goToAddress addressCarrier) {

		String address = addressCarrier.getAddress();

		activityUtils.addressIntent(this, address);


	}


	@Subscribe
	public void onCustomerFragmentOpen(Events.openCustomerFragment customerBundle) {
		int customerId = customerBundle.getCustomerId();
		CustomerFragment fragment = CustomerFragment.newInstance(customerId);
		activityUtils.loadFragment(fragment, getSupportFragmentManager());
	}

	@Subscribe
	public void onDelayFragmentOpen(Events.openDelayFragment customerBundle) {
		int delayId = customerBundle.getInstallmentId();
		int acNo = customerBundle.getLoanAccountNo();
		DelayDialogFragment fragment = DelayDialogFragment.newInstance(delayId, acNo);
		activityUtils.loadDialogFragment(fragment, getSupportFragmentManager(), DIALOG_DELAY);


	}

	@Subscribe
	public void onReceiveMoneyFragmentOpen(Events.openReceiveMoneyFragment customerBundle) {
		int delayId = customerBundle.getInstallmentId();
		int acNo = customerBundle.getAccountNo();
		RegisterReceivedDialogFragment fragment =
				RegisterReceivedDialogFragment.newInstance(acNo, delayId);
		activityUtils.loadDialogFragment(fragment, getSupportFragmentManager(),
				DIALOG_MONEY_RECEIVED);


	}


	@Subscribe
	public void onLoanFragmentOpen(Events.openLoanDetailsFragment loanBundle) {
		int accountNo = loanBundle.getAccountNo();
		LoanDetailsFragment fragment = LoanDetailsFragment.newInstance(accountNo);

		activityUtils.loadFragment(fragment, getSupportFragmentManager());


	}

	@Subscribe
	public void onShowToast(Events.showToast toastData) {

		String message = toastData.getMessage();
		String type = toastData.getType();
		makeToast(message, type, this);


	}


}
