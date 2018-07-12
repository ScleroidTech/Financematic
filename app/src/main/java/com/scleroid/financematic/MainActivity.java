package com.scleroid.financematic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scleroid.financematic.base.BaseActivity;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.ExpenseRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.fragments.AddMoneyFragment;
import com.scleroid.financematic.fragments.RegisterCustomerFragment;
import com.scleroid.financematic.fragments.customer.CustomerFragment;
import com.scleroid.financematic.fragments.dashboard.DashboardFragment;
import com.scleroid.financematic.fragments.dialogs.DelayDialogFragment;
import com.scleroid.financematic.fragments.dialogs.RegisterReceivedDialogFragment;
import com.scleroid.financematic.fragments.expense.ExpenseFragment;
import com.scleroid.financematic.fragments.loandetails.LoanDetailsFragment;
import com.scleroid.financematic.fragments.people.PeopleFragment;
import com.scleroid.financematic.fragments.report.ReportFragment;
import com.scleroid.financematic.utils.eventbus.Events;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.BottomNavigationViewHelper;
import com.scleroid.financematic.utils.ui.SnackBarUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

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
		HasSupportFragmentInjector, FragmentManager.OnBackStackChangedListener {
	//TODO Refactor repeating code, look at  android-mvvm-architecture for ideas, its by mind-dorks
	// tags used to attach the fragments
	private static final String TAG_DASHBOARD = "dashboard";
	private static final String TAG_NEW_CUSTOMER = "new_customer";
	private static final String TAG_REPORT = "report";
	private static final String TAG_EXPENSES = "expenses";
	private static final String TAG_NOTIFICATION = "notification";
	private static final String DIALOG_DELAY = "Delay Payment";
	private static final String DIALOG_MONEY_RECEIVED = "Received Payment";
	private static final String TAG_PEOPLE = "People";
	private static final String TAG_ADD_MONEY = "add_money";
	private static final int RC_SIGN_IN = 123;
	// index to identify current nav menu item
	public static int navItemIndex = 0;

	// ...
	@NonNull
	public static String CURRENT_TAG = TAG_DASHBOARD;
	// Choose authentication providers
	List<AuthUI.IdpConfig> providers = Arrays.asList(
			new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(false)
					.setRequireName(true)
					.build(), new AuthUI.IdpConfig.PhoneBuilder().build(),
			new AuthUI.IdpConfig.GoogleBuilder().build());
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
	@Inject
	SnackBarUtils snackBarUtils;
	private ActionBarDrawerToggle toggle;
	private DrawerLayout drawer;
	private NavigationView navigationView;
	private String[] activityTitles;
	private BottomNavigationView bottomNavigationView;
	private boolean mToolBarNavigationListenerIsRegistered = false;
	private FirebaseAuth mFirebaseAuth;
	private FirebaseUser firebaseUser;

	private String username, photoUrl;
	private String userEmail;
	private String userPhone;


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

	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// Initialize Firebase Auth
		mFirebaseAuth = FirebaseAuth.getInstance();
		firebaseUser = mFirebaseAuth.getCurrentUser();
		//Check login, & if not, prompt the user to login
		//validateLogin();
		super.onCreate(savedInstanceState);


		final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		drawer = findViewById(R.id.drawer_layout);


		navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// load toolbar titles from string resources
		activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

		/*bottom navigation*/

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
		// loadFragmentRunnable(new DashboardFragment());
		//     appExecutors = new InstantAppExecutors();
		//Listen for changes in the back stack
		getSupportFragmentManager().addOnBackStackChangedListener(this);

		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
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
		};


		setToggle(actionBarDrawerToggle);

		if (savedInstanceState == null) {
			navItemIndex = 0;
			CURRENT_TAG = TAG_DASHBOARD;
			loadFragmentFromNavigationDrawers();
		}
		boolean frag = getIntent().getBooleanExtra(NOTIFY, false);
		if (frag) {
			CURRENT_TAG = TAG_NOTIFICATION;
			loadFragmentRunnable(NotificationActivity.newInstance(), true);
		}
//Handle when activity is recreated like on orientation Change
		shouldDisplayHomeUp();

		//	((GarlandApp) getApplication()).addListener(this);

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
	@Nullable
	@Override
	public ActionBar getActionBarBase() {
		return getSupportActionBar();
	}

	/*@SuppressLint("TimberArgCount")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		handleSignInResponse(requestCode, resultCode, data);
	}

	@SuppressLint("TimberArgCount")
	private void handleSignInResponse(int requestCode, int resultCode, Intent data) {
		Timber.d("This is getting called ");
		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);

			if (resultCode == RESULT_OK) {
				// Successfully signed in
				firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
				validateLogin();
				// ...
			} else {
				// Sign in failed, check response for error code
				// Sign in failed
				if (response == null) {
					// User pressed back button
					showSnackbar(R.string.sign_in_cancelled);
					return;
				}
				int errorCode = response.getError().getErrorCode();
				@SuppressLint("RestrictedApi") String s =
						ErrorCodes.toFriendlyMessage(errorCode) + " ERRORCODE" + errorCode;
				showSnackbar(s);
				Timber.e("Firebase Sign-in error: " + errorCode + " ", response.getError());
			}
			Timber.d(resultCode + " firebase Issues " + response.toString());
			// ...
		}
	}

	*/

	/**
	 * Check if user logged in or not, If not, Call the FireBaseUI to issue the login to user
	 */
	private void validateLogin() {
		if (firebaseUser == null) {
			// Not signed in, launch the Sign In activity
			startActivityForResult(
					AuthUI.getInstance()
							.createSignInIntentBuilder()
							.setLogo(R.mipmap.ic_launcher)      // Set logo drawable
							.setTheme(R.style.GreenTheme)  // Set theme
							.setAvailableProviders(providers)
							.setIsSmartLockEnabled(true, true)

							.build(),
					RC_SIGN_IN);
			//startActivity(new Intent(this, LoginActivity.class));
			//  finish();

		} else {
			username = firebaseUser.getDisplayName();

			if (firebaseUser.getEmail() != null) {
				userEmail = firebaseUser.getEmail();

			}
			if (firebaseUser.getPhoneNumber() != null) {
				userPhone = firebaseUser.getPhoneNumber();
			}
			if (firebaseUser.getPhotoUrl() != null) {
				photoUrl = firebaseUser.getPhotoUrl().toString();
			}

		}

	}

	/**
	 * Calls the {@link SnackBarUtils} method showSnackBar Which is used to display {@link
	 * Snackbar}
	 *
	 * @param msg the message string which needs to be shown
	 */
	private void showSnackbar(int msg) {
		View parentLayout = getWindow().getDecorView().findViewById(android.R.id.content);
		snackBarUtils.showSnackbar(parentLayout, msg);
	}

	/**
	 * Calls the {@link SnackBarUtils} method showSnackBar Which is used to display {@link
	 * Snackbar}
	 *
	 * @param msg the message string which needs to be shown
	 */
	private void showSnackbar(String msg) {
		View parentLayout = getWindow().getDecorView().findViewById(android.R.id.content);
		snackBarUtils.showSnackbar(parentLayout, msg);
	}


	@Override
	public boolean onCreateOptionsMenu(@NonNull Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		MenuItem create_new = menu.findItem(R.id.action_create_new_loan);
		MenuItem notification = menu.findItem(R.id.action_notification);
		tintMenuIcon(this, create_new, android.R.color.white);
		tintMenuIcon(this, notification, android.R.color.white);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_create_new_loan) {

			RegisterCustomerFragment fragment = new RegisterCustomerFragment();
			loadFragmentRunnable(fragment, true);
			return true;
		}

		// user is in notifications fragment
		// and selected 'Mark all as Read'
		if (id == R.id.action_notification) {
			NotificationActivity fragment = new NotificationActivity();
			/*CustomerFragment fragment = new CustomerFragment();*/
			loadFragmentRunnable(fragment, true);
		}

		// user is in notifications fragment
		// and selected 'Clear All'
		if (id == R.id.action_settings) {
			Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG)
					.show();
		}

		return super.onOptionsItemSelected(item);
	}

	private void loadFragmentRunnable(final Fragment fragment, final boolean b) {

		Runnable pendingRunnable = () -> {
			// update the main content by replacing fragments


			loadFragment(fragment, b);
		};

		// If pendingRunnable is not null, then add to the message queue
		// boolean post = handler.post(pendingRunnable);
		appExecutors.diskIO().execute(pendingRunnable);
	}

	private void loadFragment(Fragment fragment, final boolean backstack) {
		if (backstack) {
			activityUtils.loadFragment(fragment, getSupportFragmentManager());
			return;
		}

		activityUtils.loadFragmentWithoutBackStack(fragment, getSupportFragmentManager());
	}

	public void tintMenuIcon(@NonNull Context context, @Nullable MenuItem item,
	                         @ColorRes int color) {
		if (item == null) return;
		Drawable normalDrawable = item.getIcon();
		Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
		DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

		item.setIcon(wrapDrawable);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {

		handleUiClick(item);


		return true;
	}



/*
    https://stackoverflow.com/questions/23273275/toggle-button-not-working-for-navigation-drawer
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }*/




	/*bottom navigation*/

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
			case R.id.nav_add_money:
				navItemIndex = 5;
				CURRENT_TAG = TAG_ADD_MONEY;
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

	/***
	 * Returns respected fragment that user
	 * selected from navigation menu
	 */
	private void loadFragmentFromNavigationDrawers() {
		// selecting appropriate nav menu item
		selectNavMenu();

		//selecting appropriate bottom navigation menu item
		selectBottomNavMenu();
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
		Fragment fragment = getCurrentFragment();
		if (navItemIndex < 3) {
			getSupportFragmentManager().popBackStackImmediate(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			loadFragmentRunnable(fragment, false);
		} else {
			loadFragmentRunnable(fragment, true);
		}
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
		MenuItem item;
		if (navItemIndex > 2) {
			item = bottomNavigationView.getMenu().getItem(0);
			for (int i = 1; i < 2; i++) {
				MenuItem item2 = bottomNavigationView.getMenu().getItem(0);
				item2.setChecked(false);
			}
		} else {
			item = bottomNavigationView.getMenu().getItem(navItemIndex);
			if (item != null) {
				item.setChecked(true);
			}
		}


	}

	private void setToolbarTitle() {
		getSupportActionBar().setTitle(activityTitles[navItemIndex]);
	}

/*
//TODO To not let the activity close directly
	*/
	/*sidebar navigation*//*

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
*/

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

			case 5:
				return new AddMoneyFragment();
          /*

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
	public void onCallClick(@NonNull Events.placeCall phoneNumberCarrier) {

		String mobileNumber = phoneNumberCarrier.getNumber();

		activityUtils.callIntent(this, mobileNumber);


	}

	@Subscribe
	public void onAddressClick(@NonNull Events.goToAddress addressCarrier) {

		String address = addressCarrier.getAddress();

		activityUtils.addressIntent(this, address);


	}

	@Subscribe
	public void onCustomerFragmentOpen(@NonNull Events.openCustomerFragment customerBundle) {
		int customerId = customerBundle.getCustomerId();
		CustomerFragment fragment = CustomerFragment.newInstance(customerId);
		activityUtils.loadFragment(fragment, getSupportFragmentManager());
	}

	@Subscribe
	public void onDelayFragmentOpen(@NonNull Events.openDelayFragment customerBundle) {
		int delayId = customerBundle.getInstallmentId();
		int acNo = customerBundle.getLoanAccountNo();
		DelayDialogFragment fragment = DelayDialogFragment.newInstance(delayId, acNo);
		activityUtils.loadDialogFragment(fragment, getSupportFragmentManager(), DIALOG_DELAY);


	}

	@Subscribe
	public void onReceiveMoneyFragmentOpen(
			@NonNull Events.openReceiveMoneyFragment customerBundle) {
		int delayId = customerBundle.getInstallmentId();
		int acNo = customerBundle.getAccountNo();
		RegisterReceivedDialogFragment fragment =
				RegisterReceivedDialogFragment.newInstance(acNo, delayId);
		activityUtils.loadDialogFragment(fragment, getSupportFragmentManager(),
				DIALOG_MONEY_RECEIVED);


	}

	@Subscribe
	public void onLoanFragmentOpen(@NonNull Events.openLoanDetailsFragment loanBundle) {
		int accountNo = loanBundle.getAccountNo();
		LoanDetailsFragment fragment = LoanDetailsFragment.newInstance(accountNo);

		activityUtils.loadFragment(fragment, getSupportFragmentManager());


	}

	@Subscribe
	public void onShowToast(@NonNull Events.showToast toastData) {

		String message = toastData.getMessage();
		String type = toastData.getType();
		makeToast(message, type, this);


	}

	/**
	 * Called whenever the contents of the back stack change.
	 */
	@Override
	public void onBackStackChanged() {

		shouldDisplayHomeUp();
	}

	public void shouldDisplayHomeUp() {
		//Enable Up button only  if there are entries in the back stack
		boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
		enableBackButton(canback);
	}

	/**
	 * To be semantically or contextually correct, maybe change the name and signature of this
	 * function to something like:
	 * <p>
	 * private void showBackButton(boolean show) Just a suggestion.
	 */
	private void enableBackButton(boolean enable) {

		// To keep states of ActionBar and ActionBarDrawerToggle synchronized,
		// when you enable on one, you disable on the other.
		// And as you may notice, the order for this operation is disable first, then enable -
		// VERY VERY IMPORTANT.
		if (enable) {
//You may not want to open the drawer on swipe from the left in this case
			drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
// Remove hamburger
			toggle.setDrawerIndicatorEnabled(false);
			// Show back button
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			// when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
			// clicks are disabled i.e. the UP button will not work.
			// We need to add a listener, as in below, so DrawerToggle will forward
			// click events to this listener.
			if (!mToolBarNavigationListenerIsRegistered) {
				toggle.setToolbarNavigationClickListener(v -> {
					// Doesn't have to be onBackPressed
					onBackPressed();
				});

				mToolBarNavigationListenerIsRegistered = true;
			}

		} else {
//You must regain the power of swipe for the drawer.
			drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

// Remove back button
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			// Show hamburger
			toggle.setDrawerIndicatorEnabled(true);
			// Remove the/any drawer toggle listener
			toggle.setToolbarNavigationClickListener(null);
			drawer.addDrawerListener(toggle);

			mToolBarNavigationListenerIsRegistered = false;
		}
		toggle.syncState();

		// So, one may think "Hmm why not simplify to:
		// .....
		// getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
		// mDrawer.setDrawerIndicatorEnabled(!enable);
		// ......
		// To re-iterate, the order in which you enable and disable views IS important
		// #dontSimplify.
	}

	@Override
	public boolean onSupportNavigateUp() {
		//This method is called when the up button is pressed. Just the pop back stack.
		getSupportFragmentManager().popBackStack();
		return true;
	}
}
