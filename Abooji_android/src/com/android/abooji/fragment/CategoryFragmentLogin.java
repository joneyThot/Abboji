package com.android.abooji.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.adapter.CategoryListAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.model.SaveUserModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.GetCategoryRequestTask;
import com.android.abooji.service.GetSaveUserRequestTask;
import com.android.abooji.service.SaveUserRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CategoryFragmentLogin extends BaseFragment implements OnClickListener
{
	Fragment fr;
	private View mRootView;
	TextViewPlus mCategory_pref_edittext;
	private ArrayList<CategoryTypeModel> mCategoryTypeModel = new ArrayList<CategoryTypeModel>();
	private CategoryListAdapter mCategoryListAdapter;
	ListView mCategoryList;
	Dialog dialog;
	SharedPreferences sharedPreferences;
//	private SharedPreferences mSharedPreferences;
	String category_pref;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		mRootView = inflater.inflate(R.layout.activity_category, container, false);
		initview();
		return mRootView;
	}
	private void initview() 
	{
		sharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.category), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mRootView.findViewById(R.id.activity_category_prev_btn).setVisibility(View.GONE);
		mRootView.findViewById(R.id.activity_category_next_btn).setVisibility(View.GONE);
		mRootView.findViewById(R.id.activity_category_save_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_category_preference_edittxt).setOnClickListener(this);
		mCategory_pref_edittext = (TextViewPlus) mRootView.findViewById(R.id.activity_category_preference_edittxt);

		getCouponList();
		
		category_pref = sharedPreferences.getString(Utils.CATEGORY_PREFERENCE, "");
		mCategory_pref_edittext.setText(category_pref);

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_category_prev_btn:

//			fr  = new CardFragment();
//			FragmentReplaceFunction();
			break;

		case R.id.activity_category_next_btn:
//			if(fieldValidation()){ 
			//			}
			break;

		case R.id.activity_category_preference_edittxt:
			openCategoryDialog();
			break;

		case R.id.dialog_remove_btn:
			dialog.dismiss();
			break;
			
		case R.id.activity_category_save_btn:

			saveUserData();
			

//			fr = new LocationFragment();
//			FragmentReplaceFunction();

			break;
		}
	}

	private void openCategoryDialog() {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_spinner);
		dialog.findViewById(R.id.dialog_remove_btn).setOnClickListener(this);
		mCategoryList = (ListView) dialog.findViewById(R.id.categoryList);
		mCategoryListAdapter = new CategoryListAdapter(getActivity(), mCategoryTypeModel, mCategory_pref_edittext);
		mCategoryList.setAdapter(mCategoryListAdapter);
		dialog.show();
	}

	private void getCouponList() {
		if (Utils.checkInternetConnection(getActivity())) {
			GetCategoryRequestTask getCategoryRequestTask = new GetCategoryRequestTask(getActivity());
			getCategoryRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onResponseReceived(Object response) {
					mCategoryTypeModel = (ArrayList<CategoryTypeModel>) response;

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

	private void FragmentReplaceFunction()
	{
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.commit();
	}

	private boolean fieldValidation() 
	{
		boolean flag = true;
		if (!Utils.validateString(mCategory_pref_edittext.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.category_fill_cat_preference));

		} 
		return flag;
	}

	private void saveUserData() {

		if(Utils.checkInternetConnection(getActivity())){
			SaveUserRequestTask saveUserRequestTask = new SaveUserRequestTask(getActivity());
			saveUserRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					SaveUserModel saveUserModel = (SaveUserModel) response;
					if(saveUserModel != null)
					{
						if(saveUserModel.status.equalsIgnoreCase("1")) {

							Toast.makeText(getActivity(), "data updated", Toast.LENGTH_SHORT).show();
							getUserData(sharedPreferences.getString(Utils.USER_ID, ""));

						} else if(saveUserModel.status.equalsIgnoreCase("0")){
							Toast.makeText(getActivity(), "User does not exist nothing to update", Toast.LENGTH_SHORT).show();
						} 
						else if(saveUserModel.status.equalsIgnoreCase("2")){
							Toast.makeText(getActivity(), "data was not inserted", Toast.LENGTH_SHORT).show();
						}

					}

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			saveUserRequestTask.execute(sharedPreferences.getString(Utils.USER_ID, ""),
					sharedPreferences.getString(Utils.USER_EMAIL, ""),
					sharedPreferences.getString(Utils.GENDER, ""),
					sharedPreferences.getString(Utils.AGE_GROUP, ""),
					sharedPreferences.getString(Utils.MARITAL_STATUS, ""),
					sharedPreferences.getString(Utils.CHILDREN, ""),
					sharedPreferences.getString(Utils.CHILDREN_AGE_GROUP, ""),
					sharedPreferences.getString(Utils.CARD_CAT, ""),
					sharedPreferences.getString(Utils.CARD_TYPE, ""),
					"",
					sharedPreferences.getString(Utils.CARDS, ""),
					mCategory_pref_edittext.getText().toString(),
					sharedPreferences.getString(Utils.ALERT, ""),
					sharedPreferences.getString(Utils.ALERT_TIME, ""));
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}
	
	private void getUserData(String user_id) {

		if(Utils.checkInternetConnection(getActivity())){
			GetSaveUserRequestTask getSaveUserRequestTask = new GetSaveUserRequestTask(getActivity());
			getSaveUserRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					SaveUserModel saveUserModel = (SaveUserModel) response;
					if(saveUserModel != null)
					{
						if(saveUserModel.status.equalsIgnoreCase("1")) {

							Logger.e("Category Fragment Get ", "Category Pref :: " + saveUserModel.category_preference);
							Utils.storeString(sharedPreferences, Utils.CATEGORY_PREFERENCE, saveUserModel.category_preference);
							Logger.e("Category Fragment Save ", "Category Pref :: " + mCategory_pref_edittext.getText().toString());
						

						} else if(saveUserModel.status.equalsIgnoreCase("0")){
							Toast.makeText(getActivity(), "User does not exist nothing to update", Toast.LENGTH_SHORT).show();
						} 
						else if(saveUserModel.status.equalsIgnoreCase("2")){
							Toast.makeText(getActivity(), "data was not inserted", Toast.LENGTH_SHORT).show();
						}
					}
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getSaveUserRequestTask.execute(user_id);


		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}

}