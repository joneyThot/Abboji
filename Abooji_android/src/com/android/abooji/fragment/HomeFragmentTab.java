package com.android.abooji.fragment;

import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.abooji.AppConstants;
import com.android.abooji.R;
import com.android.abooji.adapter.AdvertsPagerAdapter;
import com.android.abooji.adapter.CardCategoryAdapter;
import com.android.abooji.adapter.PremiumPagerAdapter;
import com.android.abooji.adapter.SearchCategoryAdapter;
import com.android.abooji.adapter.SearchLocationAdapter;
import com.android.abooji.adapter.TodaysLocationAdapter;
import com.android.abooji.adapter.TodaysPagerAdapter;
import com.android.abooji.adapter.TodaysShoppingListAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.AdvertsModel;
import com.android.abooji.model.CardCategoryModel;
import com.android.abooji.model.CardDetailsModel;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.model.PremiumoffersModel;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.AdvertsRequestTask;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.GetCardCategoryRequestTask;
import com.android.abooji.service.GetCardDetailsRequestTask;
import com.android.abooji.service.GetCategoryRequestTask;
import com.android.abooji.service.GetShoppingListRequestTask;
import com.android.abooji.service.GetUserLocationRequestTask;
import com.android.abooji.service.PremiumoffersRequestTask;
import com.android.abooji.service.TodaysSpecialRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.CirclePageIndicator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class HomeFragmentTab extends BaseFragment implements OnPageChangeListener{

	View mRootview;
	private String TAG = "HomeFragmentTab";
	private SharedPreferences mSharedPreferences;
	private ViewPager mAdvertPager, mTodaysPager, mPrimiumPager;
	private CirclePageIndicator mAdvertCirclePageIndicator ,mTodaysCirclePageIndicator ,mPrimiumCirclePageIndicator;
	private ArrayList<AdvertsModel> mAdvertModelList = new ArrayList<AdvertsModel>();
	private ArrayList<PremiumoffersModel> mPremiumoffersList = new ArrayList<PremiumoffersModel>();
	private ArrayList<TodaysSpecialModel> mTodaysSpecialList = new ArrayList<TodaysSpecialModel>();
	private ArrayList<CardCategoryModel> mCardCategoryModel = new ArrayList<CardCategoryModel>();
	private ArrayList<CardDetailsModel> mCardDetailsModel = new ArrayList<CardDetailsModel>();
	private ArrayList<CategoryTypeModel> mCategoryTypeModel = new ArrayList<CategoryTypeModel>();
	TreeMap<Integer, SaveLocationModel> mSaveLocationModel = new TreeMap<Integer, SaveLocationModel>();
	TreeMap<Integer, SaveShoppingListModel> mSaveShoppingListModel = new TreeMap<Integer, SaveShoppingListModel>();
	private int mDeviceWidth = 480;
	AdvertsPagerAdapter mAdvertsPagerAdapter;
	PremiumPagerAdapter mPremiumPagerAdapter;
	TodaysPagerAdapter mTodaysPagerAdapter;	
	SearchCategoryAdapter mSearchCategoryAdapter;
	CardCategoryAdapter mCardCategoryAdapter;
	SearchLocationAdapter mSearchLocationAdapter;
	TodaysShoppingListAdapter mTodaysShoppingListAdapter;
	TodaysLocationAdapter mTodaysLocationAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootview = inflater.inflate(R.layout.fragment_tab_home, container, false);
		initview();
		return mRootview;
	}

	@SuppressLint("NewApi") private void initview() {
		mSharedPreferences = mActivity.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		mActivity.setTitleText(getString(R.string.title_profiletext), View.VISIBLE, View.VISIBLE, View.VISIBLE);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		mAdvertPager = (ViewPager) mRootview.findViewById(R.id.advertsViewPager);
		mAdvertCirclePageIndicator = (CirclePageIndicator) mRootview.findViewById(R.id.advertsViewPagerIndicator);
		mTodaysPager = (ViewPager) mRootview.findViewById(R.id.todaysViewPager);
		mTodaysCirclePageIndicator = (CirclePageIndicator) mRootview.findViewById(R.id.todaysViewPagerIndicator);
		mPrimiumPager = (ViewPager) mRootview.findViewById(R.id.primiumViewPager);
		mPrimiumCirclePageIndicator = (CirclePageIndicator) mRootview.findViewById(R.id.primuumViewPagerIndicator);

		WindowManager w = getActivity().getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			w.getDefaultDisplay().getSize(size);
			mDeviceWidth = size.x;
		} else {
			Display d = w.getDefaultDisplay();
			mDeviceWidth = d.getWidth();
		}
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		String provider = LocationManager.GPS_PROVIDER;
		if(provider != null) {
			// Getting Current Location
			//Location currentLocation = locationManager.getLastKnownLocation(provider);
			LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
			Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(currentLocation != null) {
				mActivity.latitude =  currentLocation.getLatitude();
				mActivity.longitude =  currentLocation.getLongitude();

				Logger.e("", "currentLocation -- "+ mActivity.latitude + " & " + mActivity.longitude);
			}

			locationManager.requestLocationUpdates(provider, 100, 0, new LocationListener() {

				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

				@Override
				public void onProviderEnabled(String arg0) {}

				@Override
				public void onProviderDisabled(String arg0) {}

				@Override
				public void onLocationChanged(Location location) {
					//						currentLocation = location;
					mActivity.latitude =  location.getLatitude();
					mActivity.longitude =  location.getLongitude();
					Logger.e("", "onLocationChanged -- "+ mActivity.latitude + " & " + mActivity.longitude);
				}
			});
		}

		getAdverts();
		getCategory();
		getCard();
		getCardDetails();
		getPremium();
		getUserLocation(mSharedPreferences.getString(Utils.USER_ID, ""));
		getShoppingList(mSharedPreferences.getString(Utils.USER_ID, ""));
		getTodaysSpecial();

		mTodaysPager.setOnPageChangeListener(this);

	}

	private void getAdverts() {
		if (Utils.checkInternetConnection(getActivity())) {
			AdvertsRequestTask advertsRequestTask = new AdvertsRequestTask(getActivity());
			advertsRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mAdvertModelList =  (ArrayList<AdvertsModel>) response;
					Logger.e(TAG, "mAdvertModelList SIZE -- " + mAdvertModelList.size());
					if(mAdvertModelList.size() > 0 && mAdvertModelList != null) {
						mAdvertPager.setAdapter(new AdvertsPagerAdapter(mActivity, mAdvertModelList, onListClickListenerAdvert));
						mAdvertCirclePageIndicator.setViewPager(mAdvertPager);
						mAdvertCirclePageIndicator.setCurrentItem(4);

						if (mAdvertModelList.size() < 2) {
							mAdvertCirclePageIndicator.setVisibility(View.GONE);
						}
					} 
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			advertsRequestTask.execute();
		} else {
			Toast.makeText(getActivity(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
		}
	}


	OnClickListener onListClickListenerAdvert = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if(v.getTag() != null){
				int index =  (Integer) v.getTag();
				AdvertDetailsFragment advertDetailsFragment =  new AdvertDetailsFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.ADVERT_DETAIL,mAdvertModelList.get(index));
				advertDetailsFragment.setArguments(bundle);
				mActivity.pushFragments(AppConstants.TAB_HOME, advertDetailsFragment, true, false);
			}

		}
	};

	private void getTodaysSpecial() {
		if (Utils.checkInternetConnection(getActivity())) {
			TodaysSpecialRequestTask todaysSpecialRequestTask = new TodaysSpecialRequestTask(getActivity());
			todaysSpecialRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mTodaysSpecialList = (ArrayList<TodaysSpecialModel>) response;
					Logger.e(TAG, "mTodaysSpecialList SIZE -- " + mTodaysSpecialList.size());
					if(mTodaysSpecialList.size() > 0 && mTodaysSpecialList != null) {

						mTodaysPager.setAdapter(new TodaysPagerAdapter(mActivity, mTodaysSpecialList, mSaveLocationModel,mCategoryTypeModel,mCardDetailsModel, mSaveShoppingListModel, onListClickListenerTodays));
						mTodaysCirclePageIndicator.setViewPager(mTodaysPager);
						mTodaysCirclePageIndicator.setCurrentItem(0);

					} 	
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			todaysSpecialRequestTask.execute();
		} else {
			Toast.makeText(getActivity(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
		}

	}

	OnClickListener onListClickListenerTodays= new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if(v.getTag() != null){
//				int index = (Integer) v.getTag();
				TodaysDetailsFragment todaysDetailsFragment = new TodaysDetailsFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.TODAYS_DETAIL, mTodaysSpecialList);
				bundle.putSerializable(Utils.TODAYS_CATEGORY, mCategoryTypeModel);
				bundle.putSerializable(Utils.TODAYS_CARD, mCardCategoryModel);
				bundle.putSerializable(Utils.TODAYS_LOCATION, mSaveLocationModel);
				todaysDetailsFragment.setArguments(bundle);
				mActivity.pushFragments(AppConstants.TAB_HOME, todaysDetailsFragment, true, false);
			}
		}
	};

	private void getPremium() {
		if (Utils.checkInternetConnection(getActivity())) {
			PremiumoffersRequestTask premiumoffersRequestTask = new PremiumoffersRequestTask(getActivity());
			premiumoffersRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mPremiumoffersList = (ArrayList<PremiumoffersModel>) response;
					Logger.e(TAG, "mPremiumoffersList SIZE -- " + mPremiumoffersList.size());
					if(mPremiumoffersList.size() > 0 && mPremiumoffersList != null) {
						mPrimiumPager.setAdapter(new PremiumPagerAdapter(mActivity, mPremiumoffersList, onListClickListenerPremium));
						mPrimiumCirclePageIndicator.setViewPager(mPrimiumPager);
						mPrimiumCirclePageIndicator.setCurrentItem(23);

						if (mPremiumoffersList.size() < 2) {
							mPrimiumCirclePageIndicator.setVisibility(View.GONE);
						}
					} 	
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			premiumoffersRequestTask.execute();
		} else {
			Toast.makeText(getActivity(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
		}
	}


	OnClickListener onListClickListenerPremium = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getTag() != null){
				int index =  (Integer) v.getTag();
				PrimiumDetailsFragment primiumDetailsFragment =  new PrimiumDetailsFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.PREMIUM_DETAIL,mPremiumoffersList.get(index));
				primiumDetailsFragment.setArguments(bundle);
				mActivity.pushFragments(AppConstants.TAB_HOME, primiumDetailsFragment, true, false);
			}
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int pos, float arg1, int arg2) {
		Logger.e("TodaysPage ", "Position::" + pos);

	}

	@Override
	public void onPageSelected(int arg0) {
		Logger.e("TodaysPage ", "Position::" + arg0);
		
	}
	
	private void getCategory() {
		if (Utils.checkInternetConnection(getActivity())) {
			GetCategoryRequestTask getCategoryRequestTask = new GetCategoryRequestTask(getActivity());
			getCategoryRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					
					mCategoryTypeModel.add(new CategoryTypeModel("","Select Category","","","",false));
					mCategoryTypeModel.addAll((ArrayList<CategoryTypeModel>) response);
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getCategoryRequestTask.execute();
		} else {
			Toast.makeText(getActivity(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
		}
	}

	private void getCard() {
		if(Utils.checkInternetConnection(getActivity())){
			GetCardCategoryRequestTask getCardCategoryRequestTask = new GetCardCategoryRequestTask(getActivity());
			getCardCategoryRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mCardCategoryModel.add(new CardCategoryModel("", "Select Card"));
					mCardCategoryModel.addAll((ArrayList<CardCategoryModel>) response);
				}
				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getCardCategoryRequestTask.execute();
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), 
					getResources().getString(R.string.connection));
		}
	}
	
	private void getCardDetails() {
		if(Utils.checkInternetConnection(getActivity())){
			GetCardDetailsRequestTask getCardDetailsRequestTask = new GetCardDetailsRequestTask(getActivity());
			getCardDetailsRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					mCardDetailsModel = (ArrayList<CardDetailsModel>) response;

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getActivity().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getCardDetailsRequestTask.execute();
		}else{
			Utils.showMessageDialog(getActivity(), getActivity().getResources().getString(R.string.alert), getActivity().getResources().getString(R.string.connection));
		}
		
	}

	private void getUserLocation(String user_id) {
		if(Utils.checkInternetConnection(getActivity())){
			GetUserLocationRequestTask getUserLocationRequestTask = new GetUserLocationRequestTask(getActivity());
			getUserLocationRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mSaveLocationModel.put(0, new SaveLocationModel("", "", "Current Location", String.valueOf(mActivity.latitude), String.valueOf(mActivity.longitude), ""));
					mSaveLocationModel.putAll((TreeMap<Integer, SaveLocationModel>) response);
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getUserLocationRequestTask.execute(user_id);
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}
	
	private void getShoppingList(String user_id) {
		if(Utils.checkInternetConnection(getActivity())){
			GetShoppingListRequestTask getShoppingListRequestTask = new GetShoppingListRequestTask(getActivity());
			getShoppingListRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {

					mSaveShoppingListModel = (TreeMap<Integer, SaveShoppingListModel>) response;

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getActivity().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getShoppingListRequestTask.execute(user_id);
		}else{
			Utils.showMessageDialog(getActivity(), getActivity().getResources().getString(R.string.alert), getActivity().getResources().getString(R.string.connection));
		}
	}	
}