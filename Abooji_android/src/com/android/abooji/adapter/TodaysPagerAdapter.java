package com.android.abooji.adapter;

import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.abooji.AppConstants;
import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.fragment.CurrentLocationTabFragment;
import com.android.abooji.model.CardDetailsModel;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.model.SaveShoppingListModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.GPSTracker;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

/**
 * Created by User20 on 6/10/2015.
 */
public class TodaysPagerAdapter extends PagerAdapter implements OnItemClickListener{

	private Context mContext;
	private LayoutInflater mInflater;
	//	private String mUserName = "";
	private OnClickListener mListClickListener;
	private int mDeviceWidth = 480;
	TodaysLocationAdapter mTodaysLocationAdapter;
	TodaysCategoryAdapter mTodaysCategoryAdapter;
	TodaysCardAdapter mTodaysCardAdapter;
	TodaysShoppingListAdapter mTodaysShoppingListAdapter;
	private ArrayList<TodaysSpecialModel> mTodaysSpecialModel;
	private ArrayList<CategoryTypeModel> mCategoryTypeModel;
	private ArrayList<CardDetailsModel> mCardDetailsModel;
	private TreeMap<Integer, SaveLocationModel> mSaveLocationModel;
	private TreeMap<Integer, SaveShoppingListModel> mSaveShoppingListModel;	
	ListView mExpandablelist;
	private SharedPreferences mSharedPreferences;
	TextViewPlus mHeder, mScoreHerder ,mTitle;
	GPSTracker mGPSTracker;


	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public TodaysPagerAdapter(Context context, ArrayList<TodaysSpecialModel> TodaysSpecialModel,TreeMap<Integer, SaveLocationModel> mSaveLocationModel,ArrayList<CategoryTypeModel> mCategoryTypeModel, ArrayList<CardDetailsModel> mCardDetailsModel, TreeMap<Integer, SaveShoppingListModel> mSaveShoppingListModel,OnClickListener onListClickListener) {
		this.mContext = context;
		this.mTodaysSpecialModel = TodaysSpecialModel;
		this.mSaveLocationModel = mSaveLocationModel;
		this.mCategoryTypeModel = mCategoryTypeModel;
		this.mCardDetailsModel = mCardDetailsModel;
		this.mSaveShoppingListModel = mSaveShoppingListModel;
		this.mListClickListener = onListClickListener;

		WindowManager w = ((Activity) mContext).getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			w.getDefaultDisplay().getSize(size);
			mDeviceWidth = size.x;
		} else {
			Display d = w.getDefaultDisplay();
			mDeviceWidth = d.getWidth();
		}
		mSharedPreferences = mContext.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		mGPSTracker = new GPSTracker(context);

	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {

		View vi = new View(mContext.getApplicationContext());
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi = mInflater.inflate(R.layout.fragment_todaysspceial, container, false);
		mHeder = (TextViewPlus) vi.findViewById(R.id.todaystitle);
		mScoreHerder = (TextViewPlus) vi.findViewById(R.id.todaystitlevalue);
		mTitle = (TextViewPlus) vi.findViewById(R.id.titlename);
		LinearLayout mLayout = (LinearLayout) vi.findViewById(R.id.titleLinearLayout);
		mExpandablelist = (ListView) vi.findViewById(R.id.expandablelist);
		mExpandablelist.setOnItemClickListener(this);

		switch (position) {
		case 0:
			if(mSaveLocationModel != null){
				mTodaysLocationAdapter = new TodaysLocationAdapter(mContext, mSaveLocationModel,mLocationClick);
				mExpandablelist.setAdapter(mTodaysLocationAdapter);
				mTodaysLocationAdapter.notifyDataSetChanged();
			}
			mHeder.setText("Today's Special by Location");
			mScoreHerder.setText(""+ mSaveLocationModel.size());
			mTitle.setText("Location");			

			break;

		case 1:
			if(mCategoryTypeModel != null){
				mCategoryTypeModel.remove(0);
				mTodaysCategoryAdapter = new TodaysCategoryAdapter(mContext, mCategoryTypeModel);
				mExpandablelist.setAdapter(mTodaysCategoryAdapter);
				mTodaysCategoryAdapter.notifyDataSetChanged();
			}
			mHeder.setText("Today's Special by Category");
			mScoreHerder.setText(""+mCategoryTypeModel.size());
			mTitle.setText("Category");
			break;

		case 2:
			if(mCardDetailsModel != null){

				mTodaysCardAdapter = new TodaysCardAdapter(mContext, mCardDetailsModel);
				mExpandablelist.setAdapter(mTodaysCardAdapter);
				mTodaysCardAdapter.notifyDataSetChanged();
			}

			mHeder.setText("Today's Special by Card");
			mScoreHerder.setText(""+mCardDetailsModel.size());
			mTitle.setText("Card");
			break;

		case 3:
			if(mSaveShoppingListModel != null){
				mTodaysShoppingListAdapter = new TodaysShoppingListAdapter(mContext, mSaveShoppingListModel);
				mExpandablelist.setAdapter(mTodaysShoppingListAdapter);
				mTodaysShoppingListAdapter.notifyDataSetChanged();
			}
			mHeder.setText("Shopping List");
			mScoreHerder.setText(""+mSaveShoppingListModel.size());
			mLayout.setVisibility(View.GONE);

			break;

		default:
			break;
		}

		vi.setTag(position);
		vi.setOnClickListener(mListClickListener);
		((ViewPager) container).addView(vi);

		return vi;
	}
	
	OnClickListener mLocationClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getTag() != null){
				int index = (Integer) v.getTag();
				CurrentLocationTabFragment currentLocationFragment =  new CurrentLocationTabFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.LOCATIONDETAIL,mSaveLocationModel.get(index));
				currentLocationFragment.setArguments(bundle);
				((DashboardLoginActivity) mContext).pushFragments(AppConstants.TAB_MAP, currentLocationFragment, false, true);
			}
			
		}
	};


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
//		if(mHeder.getText().toString().equalsIgnoreCase("Today's Special by Location")){
//			CurrentLocationTabFragment currentLocationFragment =  new CurrentLocationTabFragment();
//			Bundle bundle = new Bundle();
//			bundle.putSerializable(Utils.LOCATIONDETAIL,mSaveLocationModel.get(pos));
//			currentLocationFragment.setArguments(bundle);
//			((DashboardLoginActivity) mContext).pushFragments(AppConstants.TAB_MAP, currentLocationFragment, false, true);
//		}
//
//	}
}
