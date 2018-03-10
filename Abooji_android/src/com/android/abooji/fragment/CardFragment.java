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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.abooji.R;
import com.android.abooji.adapter.CardCategoryAdapter;
import com.android.abooji.adapter.CardTypeAdapter;
import com.android.abooji.adapter.CategoryListAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.CardCategoryModel;
import com.android.abooji.model.CardTypeModel;
import com.android.abooji.model.CategoryTypeModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.GetCardCategoryRequestTask;
import com.android.abooji.service.GetCardTypeRequestTask;
import com.android.abooji.service.GetCategoryRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class CardFragment extends Fragment implements OnClickListener
{
	Fragment fr;
	private View mRootView;
	TextViewPlus mCategoryPref;
	Spinner mCardSpinner, mCardTypeSpinner;
	ListView mCardTypeList,mCardCategoryList;
	CardTypeAdapter mCardTypeAdapter;
	CardCategoryAdapter mCardCategoryAdapter;
	private ArrayList<CardTypeModel> mCardTypeModel = new ArrayList<CardTypeModel>();
	private ArrayList<CardCategoryModel> mCardCategoryModel = new ArrayList<CardCategoryModel>();
	Dialog dialog;
	SharedPreferences sharedPreferences;
	ListView mCategoryList;
	private ArrayList<CategoryTypeModel> mCategoryTypeModel = new ArrayList<CategoryTypeModel>();
	private CategoryListAdapter mCategoryListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		mRootView = inflater.inflate(R.layout.activity_cards, container, false);
		initview();
		return mRootView;
	}
	private void initview() 
	{

		sharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		//		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.cards), View.VISIBLE, View.VISIBLE, View.VISIBLE);

		mRootView.findViewById(R.id.activity_cards_prev_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_cards_next_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_card_save_btn).setVisibility(View.GONE);
		mCardSpinner = (Spinner) mRootView.findViewById(R.id.activity_cards_spinner);
		mCardTypeSpinner = (Spinner) mRootView.findViewById(R.id.activity_cards_type_spinner);
		mCategoryPref = (TextViewPlus) mRootView.findViewById(R.id.activity_cards_category_preference_edittext);
		mCategoryPref.setOnClickListener(this);
		openCardCategory();

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_cards_prev_btn:

			fr  = new PersonalFragment();
			FragmentReplaceFunction();
			break;

		case R.id.activity_cards_next_btn:
			//			if(fieldValidation()){
			Utils.storeString(sharedPreferences, Utils.CARDS, ((CardCategoryModel)mCardSpinner.getSelectedItem()).card_category_title);
			Utils.storeInt(sharedPreferences, Utils.CARDS_POS, mCardSpinner.getSelectedItemPosition());

			Utils.storeString(sharedPreferences, Utils.CARD_TYPE, ((CardTypeModel)mCardTypeSpinner.getSelectedItem()).card_type_title);
			Utils.storeInt(sharedPreferences, Utils.CARDS_TYPE_POS, mCardTypeSpinner.getSelectedItemPosition());

			Utils.storeString(sharedPreferences, Utils.CARD_CAT, mCategoryPref.getText().toString());
			Logger.e("Card Fragment ", "Card :: " + mCardSpinner.getSelectedItem().toString() + " Card Type :: " + mCardTypeSpinner.getSelectedItem().toString() + " Card Category :: " + mCategoryPref.getText().toString());

			fr = new CategoryFragment();
			FragmentReplaceFunction();
			//			}
			break;

		case R.id.activity_cards_category_preference_edittext:
			openCategoryDialog();
			break;

		case R.id.dialog_remove_btn:
			dialog.dismiss();
			break;
		}
	}

	private void openCardCategory() {
		if(Utils.checkInternetConnection(getActivity())){
			GetCardCategoryRequestTask getCardCategoryRequestTask = new GetCardCategoryRequestTask(getActivity());
			getCardCategoryRequestTask.setAsyncCallListener(new AsyncCallListener() {
				@Override
				public void onResponseReceived(Object response) {
					mCardCategoryModel.add(new CardCategoryModel("", "All"));
					mCardCategoryModel.addAll((ArrayList<CardCategoryModel>) response);
					if(mCardCategoryModel != null){
						mCardCategoryAdapter = new CardCategoryAdapter(getActivity(), mCardCategoryModel);
						mCardSpinner.setAdapter(mCardCategoryAdapter);
						mCardCategoryAdapter.notifyDataSetChanged();
						openCardType();
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

	private void openCardType() {
		if(Utils.checkInternetConnection(getActivity())){
			GetCardTypeRequestTask getCardTypeRequestTask = new GetCardTypeRequestTask(getActivity());
			getCardTypeRequestTask.setAsyncCallListener(new AsyncCallListener() {
				@Override
				public void onResponseReceived(Object response) {
					mCardTypeModel.add(new CardTypeModel("", "All"));
					mCardTypeModel.addAll((ArrayList<CardTypeModel>) response);
					if(mCardTypeModel != null){
						mCardTypeAdapter = new CardTypeAdapter(getActivity(), mCardTypeModel);
						mCardTypeSpinner.setAdapter(mCardTypeAdapter);
						mCardTypeAdapter.notifyDataSetChanged();
					}
					getCategoryList();
				}
				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			getCardTypeRequestTask.execute();
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), 
					getResources().getString(R.string.connection));
		}

	}

	private void openCategoryDialog() {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_spinner);
		dialog.findViewById(R.id.dialog_remove_btn).setOnClickListener(this);
		mCategoryList = (ListView) dialog.findViewById(R.id.categoryList);
		mCategoryListAdapter = new CategoryListAdapter(getActivity(), mCategoryTypeModel, mCategoryPref);
		mCategoryList.setAdapter(mCategoryListAdapter);
		dialog.show();
	}

	private void getCategoryList() {
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
		if (!Utils.validateString(mCategoryPref.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.cards_fill_category_preference));

		} 
		return flag;
	}

}