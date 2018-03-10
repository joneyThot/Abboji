package com.android.abooji;

import java.util.HashMap;
import java.util.Stack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.abooji.fragment.BarcodeTabFragment;
import com.android.abooji.fragment.BaseFragment;
import com.android.abooji.fragment.CurrentLocationTabFragment;
import com.android.abooji.fragment.HomeFragmentTab;
import com.android.abooji.fragment.RefereseFragmentTab;
import com.android.abooji.fragment.SearchFragment;
import com.android.abooji.fragment.ShoppinListTabFragment;
import com.android.abooji.fragment.SlidingMenuFragment;
import com.android.abooji.logger.Logger;
import com.android.abooji.service.GPSTracker;
import com.android.abooji.service.MyLocation;
import com.android.abooji.service.MyLocation.LocationResult;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class DashboardLoginActivity extends BaseActivity implements OnClickListener
{
	public DashboardLoginActivity() 
	{
		super(R.string.app_name);
	}

	TextViewPlus title;
	Fragment fr = null;
	boolean locPres=false;
	SharedPreferences sharedPreferences;
	

	private TabHost mTabHost;

	/* A HashMap of stacks, where we use tab identifier as keys..*/
	private HashMap<String, Stack<Fragment>> mStacks;

	public double latitude = 0, longitude = 0;
	/*Save current tabs identifier in this..*/
	private String mCurrentTab;										
//	private SharedPreferences mSharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{				
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logindashboard);
		
		GPSTracker gpsTracker = Utils.getCurrentLocation(this);
		if (gpsTracker != null) {
			latitude = gpsTracker.getLatitude();
			longitude = gpsTracker.getLongitude();
			Logger.e("", "GPSTracker -- "+ latitude + " & " +longitude);
		}
		
//		LocationResult locationResult = new LocationResult(){
//			@Override
//			public void gotLocation(Location location){
//				//Got the location!
//				//		    	currentLocation = location;
//				if(location != null) {
//					latitude = location.getLatitude();
//					longitude = location.getLongitude();
//					Logger.e("", "MyLocation -- "+ latitude + " & " +longitude);
//				}
//			}
//		};
//		MyLocation myLocation = new MyLocation();
//		myLocation.getLocation(this, locationResult);
		
		sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
		
		mStacks = new HashMap<String, Stack<Fragment>>();
		mStacks.put(AppConstants.TAB_HOME, new Stack<Fragment>());
		mStacks.put(AppConstants.TAB_SHOPPING, new Stack<Fragment>());
		mStacks.put(AppConstants.TAB_REFERESE, new Stack<Fragment>());
		mStacks.put(AppConstants.TAB_MAP, new Stack<Fragment>());
		mStacks.put(AppConstants.TAB_BARCODE, new Stack<Fragment>());

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setOnTabChangedListener(listener);
		mTabHost.setup();

		initializeTabs();
		initview();
		drawermenuFunction();
	}

	private void initview() 
	{

		findViewById(R.id.title).setVisibility(View.VISIBLE);
		findViewById(R.id.search_bar).setVisibility(View.VISIBLE);
		findViewById(R.id.drawer_icon).setVisibility(View.VISIBLE);
		title = (TextViewPlus)findViewById(R.id.title);
		setTitleText("");
		//title.setText(R.string.profile_txt);

		findViewById(R.id.search_bar).setOnClickListener(this);
		findViewById(R.id.drawer_icon).setOnClickListener(this);

		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	private View createTabView(final int id) {
		View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
		ImageView imageView =   (ImageView) view.findViewById(R.id.tab_icon);
		imageView.setImageDrawable(getResources().getDrawable(id));
		return view;
	}

	public void initializeTabs(){

		TabHost.TabSpec spec = mTabHost.newTabSpec(AppConstants.TAB_HOME);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(createTabView(R.drawable.tab_selector_home));
		mTabHost.addTab(spec);


		spec = mTabHost.newTabSpec(AppConstants.TAB_SHOPPING);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(createTabView(R.drawable.tab_selector_eye));
		mTabHost.addTab(spec);

		spec = mTabHost.newTabSpec(AppConstants.TAB_REFERESE);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(createTabView(R.drawable.tab_selector_referesh));
		mTabHost.addTab(spec);

		spec = mTabHost.newTabSpec(AppConstants.TAB_MAP);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(createTabView(R.drawable.tab_selector_location));
		mTabHost.addTab(spec);

		spec = mTabHost.newTabSpec(AppConstants.TAB_BARCODE);
		spec.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(createTabView(R.drawable.tab_selector_barcode));
		mTabHost.addTab(spec);

		mTabHost.setCurrentTab(0);
		mTabHost.getTabWidget().setStripEnabled(false);
		mTabHost.getTabWidget().setDividerDrawable(null); 
	}

	/*Comes here when user switch tab, or we do programmatically*/
	TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			/* Set current tab.. */
			mCurrentTab = tabId;


	/*		if(mTabHost.getCurrentTabTag().equals("tab_1_identifier"))
			{
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener() 
				{
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							pushFragments(mCurrentTab, new HomeFragmentTab(), false, true);
						}
						return false;
					}
				});

			}
			else if(mTabHost.getCurrentTabTag().equals("tab_2_identifier"))
			{
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener() 
				{
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							pushFragments(mCurrentTab, new ShoppinListTabFragment(), false, true);
						}
						return false;
					}
				});
			}
			else if(mTabHost.getCurrentTabTag().equals("tab_3_identifier"))
			{
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener() 
				{
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							pushFragments(mCurrentTab, new RefereseFragmentTab(), false, true);
						}
						return false;
					}
				});
			}
			else if(mTabHost.getCurrentTabTag().equals("tab_4_identifier"))
			{
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener() 
				{
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							pushFragments(mCurrentTab, new CurrentLocationTabFragment(), false, true);
						}
						return false;
					}
				});
			}
			else if(mTabHost.getCurrentTabTag().equals("tab_5_identifier"))
			{
				mTabHost.getCurrentTabView().setOnTouchListener(new OnTouchListener() 
				{
					@Override
					public boolean onTouch(View v, MotionEvent event) 
					{
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							pushFragments(mCurrentTab, new BarcodeTabFragment(), false, true);
						}
						return false;
					}
				});
			}*/

			if(tabId.equals(AppConstants.TAB_HOME)) {
//				mStacks.remove(AppConstants.TAB_HOME);
//				mStacks.put(AppConstants.TAB_HOME, new Stack<Fragment>());
				pushFragments(tabId, new HomeFragmentTab(), false, true);
			} else if(tabId.equals(AppConstants.TAB_SHOPPING)) {
//				mStacks.remove(AppConstants.TAB_SHOPPING);
//				mStacks.put(AppConstants.TAB_SHOPPING, new Stack<Fragment>());
				pushFragments(tabId, new ShoppinListTabFragment(), false, true);
			} else if(tabId.equals(AppConstants.TAB_REFERESE)) {
//				mStacks.remove(AppConstants.TAB_REFERESE);
//				mStacks.put(AppConstants.TAB_REFERESE, new Stack<Fragment>());
				pushFragments(tabId, new RefereseFragmentTab(), false, true);
			} else if(tabId.equals(AppConstants.TAB_MAP)) {
//				mStacks.remove(AppConstants.TAB_MAP);
//				mStacks.put(AppConstants.TAB_MAP, new Stack<Fragment>());
				pushFragments(tabId, new CurrentLocationTabFragment(), false, true);
			} else if(tabId.equals(AppConstants.TAB_BARCODE)) {
//				mStacks.remove(AppConstants.TAB_BARCODE);
//				mStacks.put(AppConstants.TAB_BARCODE, new Stack<Fragment>());
				pushFragments(tabId, new BarcodeTabFragment(), false, true);
			}  else {
				pushFragments(tabId, mStacks.get(tabId).lastElement(), false, false);
				
			}
		}
	};

	public void pushFragments(String tag, Fragment fragment,boolean shouldAnimate, boolean shouldAdd) {
		hideSoftKeyboard();
		if(shouldAdd)
			mStacks.get(tag).push(fragment);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if(shouldAnimate)
			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		ft.replace(R.id.realtabcontent, fragment);
		ft.commitAllowingStateLoss();
	}

	public void setCurrentTab(int val){
		mTabHost.setCurrentTab(val);
	}

	public void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	public void popFragmentBack() {
		hideSoftKeyboard();
		/*    
		 *    Select the second last fragment in current tab's stack.. 
		 *    which will be shown after the fragment transaction given below 
		 */
		Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

		/*pop current fragment from stack.. */
		mStacks.get(mCurrentTab).pop();

		/* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		ft.replace(R.id.realtabcontent, fragment);
		ft.commit();
	}   


	public int getCurrentTab(){

		if(mTabHost.getCurrentTabTag().equals("tab_1_identifier"))
		{
			Logger.e("", "Current TAb is TAB_HOME");
		}
		else if(mTabHost.getCurrentTabTag().equals("tab_2_identifier"))
		{
			Logger.e("", "Current TAb is TAB_SHOPPING ");
		}
		else if(mTabHost.getCurrentTabTag().equals("tab_3_identifier"))
		{
			Logger.e("", "Current TAb is TAB_REFERESE");
		}
		else if(mTabHost.getCurrentTabTag().equals("tab_4_identifier"))
		{
			Logger.e("", "Current TAb is TAB_MAP");
		}
		else if(mTabHost.getCurrentTabTag().equals("tab_5_identifier"))
		{
			Logger.e("", "Current TAb is TAB_BARCODE");
		}
		return mTabHost.getCurrentTab();

	}

	public void setTitleText(String title, int logo, int search, int menu) {
		((TextView) findViewById(R.id.title)).setText(title);	
		findViewById(R.id.title).setVisibility(View.VISIBLE);
		findViewById(R.id.logo).setVisibility(View.VISIBLE);
		findViewById(R.id.search_bar).setVisibility(search);
		findViewById(R.id.drawer_icon).setVisibility(menu);
	}

	public void hideTitleLayout() {
		findViewById(R.id.title).setVisibility(View.GONE);
		findViewById(R.id.logo).setVisibility(View.GONE);
		findViewById(R.id.search_bar).setVisibility(View.GONE);
		findViewById(R.id.drawer_icon).setVisibility(View.GONE);
	}

	public void popFragments() {
		hideSoftKeyboard();
		/*    
		 *    Select the second last fragment in current tab's stack.. 
		 *    which will be shown after the fragment transaction given below 
		 */
		Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

		/*pop current fragment from stack.. */
		mStacks.get(mCurrentTab).pop();

		/* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		ft.replace(R.id.realtabcontent, fragment);
		ft.commit();
	}  


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(mStacks.get(mCurrentTab).size() == 0){
			return;
		}

		/*Now current fragment on screen gets onActivityResult callback..*/
		mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.search_bar:
			//			locPres=false;
//			setTitleText("SEARCH");
//			this.getFragmentManager().beginTransaction().replace(R.id.realtabcontent, new SearchFragment()).commit();
			pushFragments(AppConstants.TAB_HOME, new SearchFragment(), true, true);
			break;
		case R.id.drawer_icon:
			//			locPres=false;
			showMenu();
			break;
		}
	}
	private void drawermenuFunction()
	{
		getSlidingMenu().setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				SlidingMenuFragment.viewSelectedPosition();
			}
		});
	}

	@Override
	public void onBackPressed() 
	{
		if(((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() == false){

			if (mStacks.get(mCurrentTab).size() > 0) {
				if(mStacks.get(mCurrentTab).equals(AppConstants.TAB_HOME)){
					super.onBackPressed(); // or call finish..	
				} else {
					pushFragments(mCurrentTab, new HomeFragmentTab(), false, true);
				}
			} else {
				popFragments();
			}
		} else {
			//do nothing.. fragment already handled back button press.
		}
		
		/* Stack<Fragment> stack = customBackStack.get(mTabHost.getCurrentTabTag());

		    if (stack.isEmpty()) {
		        super.onBackPressed();
		    } else {

		        Fragment fragment = stack.pop();

		        if (fragment.isVisible()) {
		            if (stack.isEmpty()) {
		                super.onBackPressed();
		            } else {
//		                Fragment frg = stack.pop();
//		                customBackStack.get(mTabHost.getCurrentTabTag()).push(frg);
//
//		                transaction = getSupportFragmentManager().beginTransaction();
//		                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
//		                        R.anim.slide_in_right, R.anim.slide_out_left);
//
//		                transaction.replace(R.id.realtabcontent, frg).commit();
		            }
		        } else {
		            getSupportFragmentManager().beginTransaction().replace(R.id.realtabcontent, fragment).commit();
		        }
		    }*/

//		getSupportFragmentManager().addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) getListener());
//		if (getSlidingMenu().isMenuShowing()) 
//		{
//			getSlidingMenu().toggle();
//		}
//		else 
//		{
//			//		super.onBackPressed();
//		}
	}
	private OnBackStackChangedListener getListener()
	{
		OnBackStackChangedListener result = new OnBackStackChangedListener()
		{
			public void onBackStackChanged() 
			{                   
				FragmentManager fm = getSupportFragmentManager();
				Fragment fragment = null;
				if (fm != null)
				{
					fragment = (Fragment)fm.findFragmentById(R.id.realtabcontent);
					fragment.onResume();
				}                   
			}
		};

		return result;
	}

	private void setTitleText(String titleTextString)
	{
		if(titleTextString.equals("SEARCH"))
		{
			title.setText(R.string.title_searchtxt);
		}
		else if(titleTextString.equals("LOCATION"))
		{
			title.setText(R.string.title_locationtext);
		}
		else if(titleTextString.equals("SHOPPINGLIST"))
		{
			title.setText(R.string.title_shoppinglisttext);
		}
		else if(titleTextString.equals("CURRENTLOCATION"))
		{
			title.setText(R.string.title_currentlocationtext);
		}
		else if(titleTextString.equals(""))
		{
			title.setText(R.string.title_profiletext);
		}
	}
}