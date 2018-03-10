package com.android.abooji.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class RefereseFragmentTab extends BaseFragment {
	
	
	View mRootView;
	private SharedPreferences mSharedPreferences;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_search_tab, container, false);
		initview();
		return mRootView;
	}
	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText("", View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		mActivity.pushFragments(AppConstants.TAB_HOME, new HomeFragmentTab(), false, false);
	}
}