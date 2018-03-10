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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.abooji.R;
import com.android.abooji.adapter.DialogItemAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.ItemModel;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class PersonalFragment extends Fragment implements OnClickListener
{
	Fragment fr;
	private View mRootView;
	TextViewPlus mChildrenAge;
	Spinner mSexSpinner, mAgeRangeSpinner, mMaritalStatusSpinner;
	ListView mChildrenAgeList;
	RadioGroup mChildernGroup;
	RadioButton mChildrenButton;
	Dialog dialog;
	DialogItemAdapter adapter;
	ArrayList<ItemModel> list = new ArrayList<ItemModel>();
	SharedPreferences sharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		mRootView = inflater.inflate(R.layout.activity_personal, container, false);
		initview();
		return mRootView;
	}
	private void initview() 
	{
		sharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		mRootView.findViewById(R.id.activity_personal_next_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_personal_save_btn).setVisibility(View.GONE);;

		mSexSpinner = (Spinner) mRootView.findViewById(R.id.activity_personal_sex_spinner);
		mAgeRangeSpinner = (Spinner) mRootView.findViewById(R.id.activity_personal_age_range_spinner);
		mMaritalStatusSpinner = (Spinner) mRootView.findViewById(R.id.activity_personal_marital_status_spinner);
		mChildernGroup = (RadioGroup) mRootView.findViewById(R.id.activity_personal_children_group);

		mChildrenAge = (TextViewPlus) mRootView.findViewById(R.id.activity_personal_children_age_edittxt);
		mChildrenAge.setOnClickListener(this);

		getModel();

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_personal_next_btn:
//			if(fieldValidation()){
				int selectedId = mChildernGroup.getCheckedRadioButtonId();
				mChildrenButton = (RadioButton) mRootView.findViewById(selectedId);

				Utils.storeString(sharedPreferences, Utils.GENDER, mSexSpinner.getSelectedItem().toString());
				Utils.storeInt(sharedPreferences, Utils.GENDER_POS, mSexSpinner.getSelectedItemPosition());
				
				Utils.storeString(sharedPreferences, Utils.AGE_GROUP, mAgeRangeSpinner.getSelectedItem().toString());
				Utils.storeInt(sharedPreferences, Utils.AGE_GROUP_POS, mAgeRangeSpinner.getSelectedItemPosition());
				
				Utils.storeString(sharedPreferences, Utils.MARITAL_STATUS, mMaritalStatusSpinner.getSelectedItem().toString());
				Utils.storeInt(sharedPreferences, Utils.MARITAL_STATUS_POS, mMaritalStatusSpinner.getSelectedItemPosition());
				
				Utils.storeString(sharedPreferences, Utils.CHILDREN, mChildrenButton.getText().toString());
				Utils.storeInt(sharedPreferences, Utils.CHILDREN_ID, selectedId);
				
				Utils.storeString(sharedPreferences, Utils.CHILDREN_AGE_GROUP, mChildrenAge.getText().toString());

				Logger.e("Personal Fragment", "Sex :: " + mSexSpinner.getSelectedItem().toString() + " Age Group :: " + mAgeRangeSpinner.getSelectedItem().toString() + " Marital_status :: " + mMaritalStatusSpinner.getSelectedItem().toString() + "Children :: " + mChildrenButton.getText().toString() + " Children_Age :: " + mChildrenAge.getText().toString());
				fr  = new CardFragment();
				FragmentReplaceFunction();
//			}
			break;

		case R.id.activity_personal_save_btn:
			break;

		case R.id.activity_personal_children_age_edittxt:
			openChildrenAgeDialog();
			break;

		case R.id.dialog_remove_btn:
			dialog.dismiss();
			break;

		}
	}

	private void openChildrenAgeDialog() {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_spinner);
		dialog.findViewById(R.id.dialog_remove_btn).setOnClickListener(this);
		mChildrenAgeList = (ListView) dialog.findViewById(R.id.categoryList);
		adapter = new DialogItemAdapter(getActivity(), list, mChildrenAge);
		mChildrenAgeList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		dialog.show();

	}

	private ArrayList<ItemModel> getModel() {
		list.add(new ItemModel("Infant"));
		list.add(new ItemModel("Toddler"));
		list.add(new ItemModel("Child"));
		list.add(new ItemModel("Teens"));
		list.add(new ItemModel("Young Adults"));
		list.add(new ItemModel("Adults"));
		return list;
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
		if (!Utils.validateString(mSexSpinner.getSelectedItem().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.personal_fill_gender));

		} else if (!Utils.validateString(mAgeRangeSpinner.getSelectedItem().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.personal_fill_age_range));

		} else if (!Utils.validateString(mMaritalStatusSpinner.getSelectedItem().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.personal_fill_marital_status));

		} else if (!Utils.validateString(mChildrenAge.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.personal_select_children));

		}
		return flag;
	}
	

}