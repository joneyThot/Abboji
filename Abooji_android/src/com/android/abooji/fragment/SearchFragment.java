package com.android.abooji.fragment;

import java.util.ArrayList;
import java.util.TreeMap;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.android.abooji.model.SearchModel;
import com.android.abooji.model.TodaysSpecialModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.GetCardCategoryRequestTask;
import com.android.abooji.service.GetCategoryRequestTask;
import com.android.abooji.service.GetUserLocationRequestTask;
import com.android.abooji.service.SearchRequestTask;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SearchFragment extends BaseFragment implements OnClickListener
{
	Fragment fr;
	private View mRootView;
	EditText mKeyword_edittxt;
	Spinner mCategorySpinner,mCardSpinner,mLocationSpinner;
	private ArrayList<CardCategoryModel> mCardCategoryModel = new ArrayList<CardCategoryModel>();
	private ArrayList<CategoryTypeModel> mCategoryTypeModel = new ArrayList<CategoryTypeModel>();
	TreeMap<Integer, SaveLocationModel> mSaveLocationModel = new TreeMap<Integer, SaveLocationModel>();
	private ArrayList<SearchModel> mSearchModelList = new ArrayList<SearchModel>();
	private ArrayList<SearchModel> mFilterResult = new ArrayList<SearchModel>();
	SearchCategoryAdapter mSearchCategoryAdapter;
	CardCategoryAdapter mCardCategoryAdapter;
	SearchLocationAdapter mSearchLocationAdapter;
	private SharedPreferences mSharedPreferences;
	Dialog searchErrorDialog;
	int hwidth,hHeight;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		mRootView = inflater.inflate(R.layout.fragment_search, container, false);
		initview();
		CheckFocus();
		return mRootView;
	}
	private void initview() 
	{
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_searchtxt), View.VISIBLE, View.INVISIBLE, View.VISIBLE);

		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		mKeyword_edittxt = (EditText)mRootView.findViewById(R.id.search_keyword_edttxt);
		mCategorySpinner = (Spinner) mRootView.findViewById(R.id.search_category_spinner);
		mCardSpinner = (Spinner) mRootView.findViewById(R.id.search_card_spinner);
		mLocationSpinner = (Spinner) mRootView.findViewById(R.id.search_location_spinner);

		mRootView.findViewById(R.id.search_ResetBtn).setOnClickListener(this);
		mRootView.findViewById(R.id.search_searchBtn).setOnClickListener(this);
		mRootView.findViewById(R.id.search_keywordRemove).setOnClickListener(this);

		getCategory();
		getCard();
		getUserLocation(mSharedPreferences.getString(Utils.USER_ID, ""));

		Display display = getActivity().getWindowManager().getDefaultDisplay(); 

		hwidth = display.getWidth();  // deprecated
		hHeight = display.getHeight(); 

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.search_ResetBtn:

			mKeyword_edittxt.setText("");
			mCategorySpinner.setSelection(0);
			mCardSpinner.setSelection(0);
			mLocationSpinner.setSelection(0);

			break;

		case R.id.search_searchBtn:
			Search(mKeyword_edittxt.getText().toString().trim(),((CategoryTypeModel)mCategorySpinner.getSelectedItem()).id,((CardCategoryModel)mCardSpinner.getSelectedItem()).id,((SaveLocationModel)mLocationSpinner.getSelectedItem()).id);
			break;

		case R.id.search_keywordRemove:
			Clearingdata(mKeyword_edittxt);
			break;

		}
	}

	public void Clearingdata(EditText edt)
	{
		Utils.clearData(edt);
	}
	private void CheckFocus()
	{
		mKeyword_edittxt.setOnFocusChangeListener(new OnFocusChangeListener() 
		{          
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				hideShowFunction(mKeyword_edittxt,"SEARCH_KEYWORD");
			}
		});
	}

	private void hideShowFunction(final EditText objEdit, final String status) {
		objEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)	{
				int i = objEdit.getText().toString().length();
				if(i<=0) {
					if(status.equals("SEARCH_KEYWORD"))	{
						mRootView.findViewById(R.id.search_keywordRemove).setVisibility(View.GONE);
					}
				} else {
					if(status.equals("SEARCH_KEYWORD"))	{
						mRootView.findViewById(R.id.search_keywordRemove).setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void getCategory() {
		if (Utils.checkInternetConnection(getActivity())) {
			GetCategoryRequestTask getCategoryRequestTask = new GetCategoryRequestTask(getActivity());
			getCategoryRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {

					mCategoryTypeModel.add(new CategoryTypeModel("","All","","","",false));

					mCategoryTypeModel.addAll((ArrayList<CategoryTypeModel>) response);
					if(mCategoryTypeModel != null){
						mSearchCategoryAdapter = new SearchCategoryAdapter(getActivity(), mCategoryTypeModel);
						mCategorySpinner.setAdapter(mSearchCategoryAdapter);
						mSearchCategoryAdapter.notifyDataSetChanged();	
					}
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
					mCardCategoryModel.add(new CardCategoryModel("", "All"));
					mCardCategoryModel.addAll((ArrayList<CardCategoryModel>) response);
					if(mCardCategoryModel != null){
						mCardCategoryAdapter = new CardCategoryAdapter(getActivity(), mCardCategoryModel);
						mCardSpinner.setAdapter(mCardCategoryAdapter);
						mCardCategoryAdapter.notifyDataSetChanged();
					}
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

	private void getUserLocation(String user_id) {
		if(Utils.checkInternetConnection(getActivity())){
			GetUserLocationRequestTask getUserLocationRequestTask = new GetUserLocationRequestTask(getActivity());
			getUserLocationRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mSaveLocationModel.put(0, new SaveLocationModel("", "", "All", "", "", ""));
					mSaveLocationModel.putAll((TreeMap<Integer, SaveLocationModel>) response);
					if(mSaveLocationModel != null){
						mSearchLocationAdapter = new SearchLocationAdapter(getActivity(), mSaveLocationModel);
						mLocationSpinner.setAdapter(mSearchLocationAdapter);
						mSearchLocationAdapter.notifyDataSetChanged();
					}
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

	private void Search(String keyword,String category, String card, String location) {
		if(Utils.checkInternetConnection(getActivity())){
			SearchRequestTask searchRequestTask = new SearchRequestTask(getActivity());
			searchRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {

					//result str -- {"search_card":[],"search_keyword_cat":[],"loaction":null}
					String result = "{"+'"'+"search_card"+'"'+":"+"[]"+","+'"'+"search_keyword_cat"+'"'+":"+"[]"+'"'+"loaction"+'"'+":"+"null"+"}";
					//					Logger.e("SEARHRESULT", "" + result);
					if(response.equals(result)){
						openSearchErrorDialog();
					} else {

						mSearchModelList = (ArrayList<SearchModel>) response;

						if(mSearchModelList != null){		

							FilterData();

							SearchDetailsFragment searchDetailsFragment = new SearchDetailsFragment();
							Bundle bundle = new Bundle();
							bundle.putSerializable(Utils.SEARCH_DATA, mFilterResult);
							searchDetailsFragment.setArguments(bundle);
							mActivity.pushFragments(AppConstants.TAB_HOME, searchDetailsFragment, true, false);
						}
					}
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			searchRequestTask.execute(keyword,category,card,location);
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}

	private void FilterData() {
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
		for (int i = 0; i < mSearchModelList.size(); i++) {
			if(Utils.validateString(mSearchModelList.get(i).category_id)){
				t_category = mSearchModelList.get(i).category_id;
			}
			//			t_card = mSearchModelList.get(i).card;
			//			t_location = mSearchModelList.get(i).location;
			//			t_lat = mSearchModelList.get(i).latitude;
			//			t_lang = mSearchModelList.get(i).longitude;

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

			if(selectedCard != null){
				category_temp = true;
			} 

			if(category_temp){
				mFilterResult.add(mSearchModelList.get(i));
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

	private void openSearchErrorDialog() {

		searchErrorDialog = new Dialog(getActivity());
		searchErrorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		searchErrorDialog.setContentView(R.layout.fragment_search_fail);
		searchErrorDialog.getWindow().setLayout((hwidth/2)+100, (hHeight/3)+50);
		searchErrorDialog.show();
	}

}