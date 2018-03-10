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
import android.widget.Toast;

import com.android.abooji.R;
import com.android.abooji.adapter.DialogItemAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.ItemModel;
import com.android.abooji.model.SaveUserModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.GetSaveUserRequestTask;
import com.android.abooji.service.SaveUserRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class PersonalFragmentLogin extends BaseFragment implements OnClickListener
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
	String mSex,mAge,mMarital,mChildren,mChildren_Age_Group;
	int mSex_pos,mAge_pos,mMarital_pos,mChildren_id;

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

		mRootView.findViewById(R.id.activity_personal_next_btn).setVisibility(View.GONE);;
		mRootView.findViewById(R.id.activity_personal_save_btn).setOnClickListener(this);

		mSexSpinner = (Spinner) mRootView.findViewById(R.id.activity_personal_sex_spinner);
		mAgeRangeSpinner = (Spinner) mRootView.findViewById(R.id.activity_personal_age_range_spinner);
		mMaritalStatusSpinner = (Spinner) mRootView.findViewById(R.id.activity_personal_marital_status_spinner);
		mChildernGroup = (RadioGroup) mRootView.findViewById(R.id.activity_personal_children_group);

		mChildrenAge = (TextViewPlus) mRootView.findViewById(R.id.activity_personal_children_age_edittxt);
		mChildrenAge.setOnClickListener(this);

		getModel();

		//		mSex = sharedPreferences.getString(Utils.GENDER, "");
		mSex_pos = sharedPreferences.getInt(Utils.GENDER_POS, 0);

		//		mAge = sharedPreferences.getString(Utils.AGE_GROUP, "");
		mAge_pos = sharedPreferences.getInt(Utils.AGE_GROUP_POS, 0);

		//		mMarital = sharedPreferences.getString(Utils.MARITAL_STATUS, "");
		mMarital_pos = sharedPreferences.getInt(Utils.MARITAL_STATUS_POS, 0);

		//		mChildren = sharedPreferences.getString(Utils.CHILDREN, "");
		mChildren_id = sharedPreferences.getInt(Utils.CHILDREN_ID, 0);

		mChildren_Age_Group = sharedPreferences.getString(Utils.CHILDREN_AGE_GROUP, "");
		mChildrenAge.setText(mChildren_Age_Group);

		mSexSpinner.setSelection(mSex_pos);
		mAgeRangeSpinner.setSelection(mAge_pos);
		mMaritalStatusSpinner.setSelection(mMarital_pos);
		mChildrenButton = (RadioButton) mRootView.findViewById(mChildren_id);

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_personal_next_btn:

			break;

		case R.id.activity_personal_save_btn:

			saveUserData();
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
					mSexSpinner.getSelectedItem().toString(),
					mAgeRangeSpinner.getSelectedItem().toString(),
					mMaritalStatusSpinner.getSelectedItem().toString(),
					mChildrenButton.getText().toString(),
					mChildrenAge.getText().toString(),
					sharedPreferences.getString(Utils.CARD_CAT, ""),
					sharedPreferences.getString(Utils.CARD_TYPE, ""),
					"",
					sharedPreferences.getString(Utils.CARDS, ""),
					sharedPreferences.getString(Utils.CATEGORY_PREFERENCE, ""),
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

							Logger.e("Personal Fragment Get", "Sex :: " + saveUserModel.gender +" Age Group :: " + saveUserModel.age_range	+ " Marital_status :: " + saveUserModel.marrital_status	+ "Children :: " + saveUserModel.children+ " Children_Age :: " + saveUserModel.childAgeGroup);

							Utils.storeString(sharedPreferences, Utils.GENDER, saveUserModel.gender);
							Utils.storeInt(sharedPreferences, Utils.GENDER_POS, mSexSpinner.getSelectedItemPosition());
							
							Utils.storeString(sharedPreferences, Utils.AGE_GROUP, saveUserModel.age_range);
							Utils.storeInt(sharedPreferences, Utils.AGE_GROUP_POS, mAgeRangeSpinner.getSelectedItemPosition());
							
							Utils.storeString(sharedPreferences, Utils.MARITAL_STATUS, saveUserModel.marrital_status);
							Utils.storeInt(sharedPreferences, Utils.MARITAL_STATUS_POS, mMaritalStatusSpinner.getSelectedItemPosition());
							
							Utils.storeString(sharedPreferences, Utils.CHILDREN, saveUserModel.children);
							Utils.storeInt(sharedPreferences, Utils.CHILDREN_ID, mChildren_id);
							
							Utils.storeString(sharedPreferences, Utils.CHILDREN_AGE_GROUP, saveUserModel.childAgeGroup);


							Logger.e("Personal Fragment Save", "Sex :: " + mSexSpinner.getSelectedItem().toString() + " Age Group :: " + mAgeRangeSpinner.getSelectedItem().toString() + " Marital_status :: " + mMaritalStatusSpinner.getSelectedItem().toString() + "Children :: " + mChildrenButton.getText().toString() + " Children_Age :: " + mChildrenAge.getText().toString());


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