package com.android.abooji.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class EditCardFragment extends BaseFragment implements OnClickListener{
	Fragment fr;
	View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_edit_card, container, false);
		initview();
		return mRootView;
	}

	private void initview() {
		mRootView.findViewById(R.id.fragment_edit_card_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.fragment_edit_card_add_btn).setOnClickListener(this);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fragment_edit_card_btn:
			break;

		case R.id.fragment_edit_card_add_btn:
			((DashboardLoginActivity) getActivity()).pushFragments(AppConstants.TAB_HOME, new CardFragmentLogin(), false, true);

			break;
		}
	}
}
