package com.android.abooji.fragment;

import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.adapter.UserLocationListAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.DeleteUserLocationRequestTask;
import com.android.abooji.service.GetUserLocationRequestTask;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class EditLocationFragment extends BaseFragment implements OnClickListener,OnCheckedChangeListener,OnItemClickListener {

	Fragment fr;
	View mRootView;
	CheckBox item_Check;
	ListView mListLocation;
	TextView current_location_txt;
	UserLocationListAdapter mUserLocationListAdapter;
	//	ArrayList<SaveLocationModel> mSaveLocationModel = new ArrayList<SaveLocationModel>();
	TreeMap<Integer, SaveLocationModel> mSaveLocationModel = new TreeMap<Integer, SaveLocationModel>();
	private SharedPreferences mSharedPreferences;
	//	GPSTracker mGPSTracker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_edit_location, container, false);
		initview();
		return mRootView;
	}

	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_editlocationtext), View.VISIBLE, View.VISIBLE, View.VISIBLE);

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

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		//		mGPSTracker = new GPSTracker(mActivity);
		item_Check = (CheckBox) mRootView.findViewById(R.id.item_check);
		item_Check.setOnCheckedChangeListener(this);
		mListLocation = (ListView) mRootView.findViewById(R.id.listview);
		mListLocation.setOnItemClickListener(this);
		current_location_txt = (TextView) mRootView.findViewById(R.id.edit_location_current_location_textview);
		mRootView.findViewById(R.id.edit_location_add_btn).setOnClickListener(this);
		getUserLocation(mSharedPreferences.getString(Utils.USER_ID, ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_location_add_btn:

			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_MAP, new CurrentLocationTabFragment(), false, true);

			//			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.logindash_contenframe, new CurrentLocationTabFragment()).commit();
			//			FragmentManager fm = getFragmentManager();
			//			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			//			fragmentTransaction.replace(R.id.fragment_place, fr);
			//			fragmentTransaction.commit();
			break;

		default:
			break;
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
					mUserLocationListAdapter = new UserLocationListAdapter(getActivity(), mSaveLocationModel,mRemoveLocationName);
					mListLocation.setAdapter(mUserLocationListAdapter);
					mUserLocationListAdapter.notifyDataSetChanged();

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

	private OnClickListener mRemoveLocationName = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getTag() != null) {
				int index =  (Integer) v.getTag();
				deleteLocation(mSaveLocationModel.get(index).getUser_id(),mSaveLocationModel.get(index).getLocation_name());
				mSaveLocationModel.remove(index);
				mUserLocationListAdapter.notifyDataSetChanged();
			}
		}
	};

	private void deleteLocation(String user_id, String location_name) {
		if (Utils.checkInternetConnection(getActivity())) {
			DeleteUserLocationRequestTask deleteUserLocationRequestTask = new DeleteUserLocationRequestTask(getActivity());
			deleteUserLocationRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {

					Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
				}
			});
			deleteUserLocationRequestTask.execute(user_id,location_name);
		} else {
			Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){	
			//			current_location_txt.setVisibility(View.VISIBLE);
			mListLocation.setVisibility(View.VISIBLE);
		} else {
			//			current_location_txt.setVisibility(View.GONE);
			mListLocation.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
		CurrentLocationTabFragment currentLocationFragment =  new CurrentLocationTabFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Utils.LOCATIONDETAIL,mSaveLocationModel.get(pos));
		currentLocationFragment.setArguments(bundle);
		((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_MAP, currentLocationFragment, false, true);

	}
}