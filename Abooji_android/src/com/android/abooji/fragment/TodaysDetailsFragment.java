package com.android.abooji.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.adapter.CardCategoryAdapter;
import com.android.abooji.adapter.SearchCategoryAdapter;
import com.android.abooji.adapter.SearchLocationAdapter;
import com.android.abooji.adapter.TodaysDetailsAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.CardCategoryModel;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TodaysDetailsFragment extends BaseFragment implements OnItemSelectedListener,OnSeekBarChangeListener{
	View mRootView;
	Fragment fm;
	private SharedPreferences mSharedPreferences;
	private ArrayList<TodaysSpecialModel> mTodaysSpecialList = new ArrayList<TodaysSpecialModel>();
	private ArrayList<TodaysSpecialModel> mFilterResult = new ArrayList<TodaysSpecialModel>();
	TodaysDetailsAdapter mTodaysDetailsAdapter;
	SeekBar mRangeBar;
	ListView mDetailsList;
	TextViewPlus mSeekValue;
	Spinner mCategorySpinner,mCardSpinner,mLocationSpinner;
	private ArrayList<CardCategoryModel> mCardCategoryModel = new ArrayList<CardCategoryModel>();
	private ArrayList<CategoryTypeModel> mCategoryTypeModel = new ArrayList<CategoryTypeModel>();
	TreeMap<Integer, SaveLocationModel> mSaveLocationModel = new TreeMap<Integer, SaveLocationModel>();

	SearchCategoryAdapter mSearchCategoryAdapter;
	CardCategoryAdapter mCardCategoryAdapter;
	SearchLocationAdapter mSearchLocationAdapter;

	int progress = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.todays_details, container, false);
		initview();
		return mRootView;
	}

	@SuppressWarnings("unchecked")
	private void initview() {
		mSharedPreferences = mActivity.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) mActivity).setTitleText(getString(R.string.title_todaystext), View.VISIBLE, View.VISIBLE, View.VISIBLE);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		mCategorySpinner = (Spinner) mRootView.findViewById(R.id.fragment_details_category_spinner);
		mCardSpinner = (Spinner) mRootView.findViewById(R.id.fragment_details_card_spinner);
		mLocationSpinner = (Spinner) mRootView.findViewById(R.id.fragment_details_location_spinner);

		mRangeBar = (SeekBar) mRootView.findViewById(R.id.seekBar1);
		mRangeBar.setOnSeekBarChangeListener(this);

		mSeekValue = (TextViewPlus) mRootView.findViewById(R.id.seekBarValue);
		mSeekValue.setText(""+progress);
		mDetailsList = (ListView) mRootView.findViewById(R.id.details_list);


		if(getArguments() != null) {
			mTodaysSpecialList =  (ArrayList<TodaysSpecialModel>) getArguments().get(Utils.TODAYS_DETAIL);
			mCategoryTypeModel =  (ArrayList<CategoryTypeModel>) getArguments().get(Utils.TODAYS_CATEGORY);
			mCardCategoryModel =  (ArrayList<CardCategoryModel>) getArguments().get(Utils.TODAYS_CARD);
			mSaveLocationModel =  (TreeMap<Integer, SaveLocationModel>) getArguments().get(Utils.TODAYS_LOCATION);

			mTodaysDetailsAdapter = new TodaysDetailsAdapter(mActivity, mTodaysSpecialList, onListClickListener);
			mDetailsList.setAdapter(mTodaysDetailsAdapter);
			mTodaysDetailsAdapter.notifyDataSetChanged();

			if(mCardCategoryModel != null){
				ArrayList<CategoryTypeModel> tempCategory = new ArrayList<CategoryTypeModel>();
				tempCategory.add(new CategoryTypeModel("0","All","","","",false));
				tempCategory.addAll(mCategoryTypeModel);
				mSearchCategoryAdapter = new SearchCategoryAdapter(getActivity(), tempCategory);
				mCategorySpinner.setAdapter(mSearchCategoryAdapter);

			}

			if(mCardCategoryModel != null){
				mCardCategoryModel.remove(0);
				ArrayList<CardCategoryModel> tempCard = new ArrayList<CardCategoryModel>();
				tempCard.add(new CardCategoryModel("", "All"));
				tempCard.addAll(mCardCategoryModel);
				mCardCategoryAdapter = new CardCategoryAdapter(getActivity(), tempCard);
				mCardSpinner.setAdapter(mCardCategoryAdapter);

			}

			if(mSaveLocationModel != null){
				HashMap<Integer, SaveLocationModel> tempLocation = new HashMap<Integer, SaveLocationModel>();
				tempLocation.put(0,new SaveLocationModel("", "", "All", "", "", ""));
				mSaveLocationModel.putAll(tempLocation);
				mSearchLocationAdapter = new SearchLocationAdapter(getActivity(), mSaveLocationModel);
				mLocationSpinner.setAdapter(mSearchLocationAdapter);

			}
		}

		mCategorySpinner.setOnItemSelectedListener(this);
		mCardSpinner.setOnItemSelectedListener(this);
		mLocationSpinner.setOnItemSelectedListener(this);

	}

	OnClickListener onListClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getTag() != null){
				int index = (Integer) v.getTag();
				TodaysItemDetailsFragment todaysItemDetailsFragment = new TodaysItemDetailsFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.TODAYS_ITEM_DETAIL, mTodaysSpecialList.get(index));
				todaysItemDetailsFragment.setArguments(bundle);
				mActivity.pushFragments(AppConstants.TAB_HOME, todaysItemDetailsFragment, true, false);
			}
		}
	};


	@SuppressWarnings("unused")
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

		String selectedCategory = ((CategoryTypeModel)mCategorySpinner.getSelectedItem()).id;
		String selectedCard = ((CardCategoryModel)mCardSpinner.getSelectedItem()).card_category_title.toLowerCase().trim();
		String selectedLocaton = ((SaveLocationModel)mLocationSpinner.getSelectedItem()).location_name.toLowerCase().trim();
		String selectedLocationLat = ((SaveLocationModel)mLocationSpinner.getSelectedItem()).latitude;
		String selectedLocationLang = ((SaveLocationModel)mLocationSpinner.getSelectedItem()).longitude;
		double range = 0;
		if(!selectedLocaton.equals("All")){
			if(selectedLocationLat != "" && selectedLocationLat.equalsIgnoreCase(null) && selectedLocationLang != "" && selectedLocationLang.equalsIgnoreCase(null)){
				double selLat = Double.parseDouble(selectedLocationLat);
				double selLang = Double.parseDouble(selectedLocationLang);
				Logger.e("LATLANG ::", "C_lat=" + mActivity.latitude + " C_lang=" + mActivity.longitude + " S_lat=" + selLat + " S_lang=" + selLang);
				range = distance(mActivity.latitude, mActivity.longitude, selLat , selLang);
				Logger.e("Range", ""+range);
			}
		}

		String t_category = "",t_card = "",t_location = "",t_lat = "",t_lang = "";
		String t_categoryArray[];
		boolean category_temp = false;

		mFilterResult.clear();
		for (int i = 0; i < mTodaysSpecialList.size(); i++) {
			t_category = mTodaysSpecialList.get(i).category_id;
			t_card = mTodaysSpecialList.get(i).card;
			t_location = mTodaysSpecialList.get(i).location;
			t_lat = mTodaysSpecialList.get(i).latitude;
			t_lang = mTodaysSpecialList.get(i).longitude;

			t_categoryArray = t_category.split(",");

			if(selectedCategory.equalsIgnoreCase("0") && selectedCategory != null) {
				category_temp = true;
			} else {
				for(int cat = 0; cat < t_categoryArray.length; cat++)
				{				
					if(selectedCategory.equalsIgnoreCase(t_categoryArray[cat])){
						category_temp = true;
					}
				}
			}

			if(selectedCard.equalsIgnoreCase(t_card) && selectedCard != null){
				category_temp = true;
			} 

			if(category_temp){
				mFilterResult.add(mTodaysSpecialList.get(i));
			} 

		}

		double range1 = 0;
		if(!t_location.equals("All")){
			if(t_lat != "" && t_lang != ""){
				double selLat1 = Double.parseDouble(t_lat);
				double selLang1 = Double.parseDouble(t_lang);
				Logger.e("LATLANG ::", "C_lat=" + mActivity.latitude + " C_lang=" + mActivity.longitude + " S_lat=" + selLat1 + " S_lang=" + selLang1);
				range1 = distance(mActivity.latitude, mActivity.longitude, selLat1 , selLang1);
				Logger.e("Range1", ""+range1);
			}
		}

		Logger.e("FilterArray", "FilterArraySize ::" + mFilterResult.size());
		mTodaysDetailsAdapter = new TodaysDetailsAdapter(mActivity, mFilterResult, onListClickListener);
		mDetailsList.setAdapter(mTodaysDetailsAdapter);
		mTodaysDetailsAdapter.notifyDataSetChanged();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}


	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.60934;		
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
		progress = progresValue;
		mSeekValue.setText(""+progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}