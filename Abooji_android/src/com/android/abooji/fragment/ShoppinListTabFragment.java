package com.android.abooji.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ShoppinListTabFragment extends BaseFragment implements OnClickListener{
	View mRootView;
	Fragment fm;
	private SharedPreferences mSharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
		initview();
		return mRootView;
	}

	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_shoppinglisttext), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		mRootView.findViewById(R.id.shoppinglist_editlist_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.shoppinglist_shareshopping_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.shoppinglist_addnewlist_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shoppinglist_editlist_btn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_SHOPPING, new ShoppingListEditFragment(), true, true);
		
			break;
		case R.id.shoppinglist_shareshopping_btn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_SHOPPING, new ShoppingShareFragment(), true, true);
	
			break;
		case R.id.shoppinglist_addnewlist_btn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_SHOPPING, new ShoppingAddNewListFragment(), true, true);
			
			break;
		}

	}

}
