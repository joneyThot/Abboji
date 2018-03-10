package com.android.abooji.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.model.AdvertsModel;
import com.android.abooji.model.PremiumoffersModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

public class TodaysItemDetailsFragment extends BaseFragment{
	View mRootView;
	Fragment fm;
	private SharedPreferences mSharedPreferences;
	private TodaysSpecialModel mTodaysSpecialList = new TodaysSpecialModel();
	private int mDeviceWidth = 480;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.advert_items_details, container, false);
		initview();
		return mRootView;
	}

	@SuppressLint("NewApi")
	private void initview() {
		mSharedPreferences = mActivity.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) mActivity).setTitleText(getString(R.string.title_todaystext), View.VISIBLE, View.VISIBLE, View.VISIBLE);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		WindowManager w = ((Activity) mActivity).getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			w.getDefaultDisplay().getSize(size);
			mDeviceWidth = size.x;
		} else {
			Display d = w.getDefaultDisplay();
			mDeviceWidth = d.getWidth();
		}

		if(getArguments() != null) {
			mTodaysSpecialList =  (TodaysSpecialModel) getArguments().get(Utils.TODAYS_ITEM_DETAIL);
		}

		if(Utils.validateString(mTodaysSpecialList.default_image_path)) {
			Picasso.with(mActivity).load(mTodaysSpecialList.default_image_path).resize(mDeviceWidth, (int) (mDeviceWidth * 0.55)).centerCrop().placeholder(R.drawable.image_not_found).error(R.drawable.image_not_found).into(((ImageView) mRootView.findViewById(R.id.advert_imageview)));
		}
		
		if(Utils.validateString(mTodaysSpecialList.title)){
			((TextView) mRootView.findViewById(R.id.title_advert)).setText(mTodaysSpecialList.title);
		}

		if(Utils.validateString(mTodaysSpecialList.card)){
			((TextView) mRootView.findViewById(R.id.card_advert)).setText(mTodaysSpecialList.card);
		}
		if(Utils.validateString(mTodaysSpecialList.location)){
			((TextView) mRootView.findViewById(R.id.location_advert)).setText(mTodaysSpecialList.location);
		}
		if(Utils.validateString(mTodaysSpecialList.hours)){
			((TextView) mRootView.findViewById(R.id.hours_advert)).setText(mTodaysSpecialList.hours);
		}
		if(Utils.validateString(mTodaysSpecialList.discount_amt)){
			((TextView) mRootView.findViewById(R.id.discount_advert)).setText(mTodaysSpecialList.discount_amt);
		}
		if(Utils.validateString(mTodaysSpecialList.description)){
			((TextView) mRootView.findViewById(R.id.discription_advert)).setText(Html.fromHtml(mTodaysSpecialList.description));
		}


	}

}