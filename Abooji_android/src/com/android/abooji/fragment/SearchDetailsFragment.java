package com.android.abooji.fragment;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.adapter.SearchDetailsAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.CardCategoryModel;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.model.SearchModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SearchDetailsFragment extends BaseFragment{
	View mRootView;
	Fragment fm;
	private SharedPreferences mSharedPreferences;
	private ArrayList<SearchModel> mSearchModelList = new ArrayList<SearchModel>();
	SearchDetailsAdapter mSearchDetailsAdapter;
	SeekBar mRangeBar;
	ListView mDetailsList;
	TextViewPlus mSeekValue;

	int progress = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_search_details, container, false);
		initview();
		return mRootView;
	}

	@SuppressWarnings("unchecked")
	private void initview() {
		mSharedPreferences = mActivity.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) mActivity).setTitleText(getString(R.string.title_searchdetails), View.VISIBLE, View.INVISIBLE, View.VISIBLE);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mDetailsList = (ListView) mRootView.findViewById(R.id.search_details_list);

		if(getArguments() != null) {
			mSearchModelList =  (ArrayList<SearchModel>) getArguments().get(Utils.SEARCH_DATA);
			Logger.e("SEARCH SIZE", ""+ mSearchModelList.size());
			if(mSearchModelList != null){
				mSearchDetailsAdapter = new SearchDetailsAdapter(getActivity(), mSearchModelList, onListClickListener);
				mDetailsList.setAdapter(mSearchDetailsAdapter);
				mSearchDetailsAdapter.notifyDataSetChanged();
			}
		}
	}

	OnClickListener onListClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getTag() != null){
				int index = (Integer) v.getTag();
				SearchItemDetailsFragment searchItemDetailsFragment = new SearchItemDetailsFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.SEARCH_ITEM_DETAIL, mSearchModelList.get(index));
				searchItemDetailsFragment.setArguments(bundle);
				mActivity.pushFragments(AppConstants.TAB_HOME, searchItemDetailsFragment, true, false);
			}
		}
	};

}