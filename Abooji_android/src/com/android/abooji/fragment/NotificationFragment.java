package com.android.abooji.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.android.abooji.LoginActivity;
import com.android.abooji.R;
import com.android.abooji.adapter.DialogItemAdapter;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.ItemModel;
import com.android.abooji.model.SaveUserModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.SaveUserRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;

public class NotificationFragment extends Fragment implements OnClickListener 
{
	Fragment fr;
	private View mRootView;
	TextViewPlus mAlerTiming;
	Spinner mAlert;
	ListView mAlerTimeList;
	Dialog dialog;
	DialogItemAdapter adapter;
	ArrayList<ItemModel> list = new ArrayList<ItemModel>();
	private SharedPreferences mSharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		mRootView = inflater.inflate(R.layout.activity_notification_alert, container, false);
		initview();
		return mRootView;
	}
	private void initview() 
	{
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		mRootView.findViewById(R.id.activity_notification_alert_save_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_notification_alert_prev_btn).setOnClickListener(this);
		mAlert = (Spinner) mRootView.findViewById(R.id.activity_notification_alert_spinner);
		mAlerTiming = (TextViewPlus) mRootView.findViewById(R.id.activity_notification_alert_timings_edittxt);
		mAlerTiming.setOnClickListener(this);

		getModel();
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_notification_alert_save_btn:
//			if(fieldValidation()){
				Utils.storeString(mSharedPreferences, Utils.ALERT, mAlert.getSelectedItem().toString());
				Utils.storeInt(mSharedPreferences, Utils.ALERT_POS, mAlert.getSelectedItemPosition());
				
				Utils.storeString(mSharedPreferences, Utils.ALERT_TIME, mAlerTiming.getText().toString());
				Logger.e("Notification Fragment ", "Alert :: " + mAlert.getSelectedItem().toString() + " Alert Time :: " + mAlerTiming.getText().toString());
				saveUserData();
//			}

			break;

		case R.id.activity_notification_alert_prev_btn:
			fr = new LocationFragment();
			FragmentReplaceFunction();
			break;

		case R.id.activity_notification_alert_timings_edittxt:
			openAlertTimingDialog();
			break;

		case R.id.dialog_remove_btn:
			dialog.dismiss();
			break;
		}
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

							startActivity(new Intent(getActivity(),LoginActivity.class));
							getActivity().finish();

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
			saveUserRequestTask.execute(mSharedPreferences.getString(Utils.USER_ID, ""),
					mSharedPreferences.getString(Utils.USER_EMAIL, ""),
					mSharedPreferences.getString(Utils.GENDER, ""),
					mSharedPreferences.getString(Utils.AGE_GROUP, ""),
					mSharedPreferences.getString(Utils.MARITAL_STATUS, ""),
					mSharedPreferences.getString(Utils.CHILDREN, ""),
					mSharedPreferences.getString(Utils.CHILDREN_AGE_GROUP, ""),
					mSharedPreferences.getString(Utils.CARD_CAT, ""),
					mSharedPreferences.getString(Utils.CARD_TYPE, ""),
					"",
					mSharedPreferences.getString(Utils.CARDS, ""),
					mSharedPreferences.getString(Utils.CATEGORY_PREFERENCE, ""),
					mSharedPreferences.getString(Utils.ALERT, ""),
					mSharedPreferences.getString(Utils.ALERT_TIME, ""));
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}
	private void openAlertTimingDialog() {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_spinner);
		dialog.findViewById(R.id.dialog_remove_btn).setOnClickListener(this);
		mAlerTimeList = (ListView) dialog.findViewById(R.id.categoryList);
		adapter = new DialogItemAdapter(getActivity(), list, mAlerTiming);
		mAlerTimeList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		dialog.show();

	}

	private ArrayList<ItemModel> getModel() {
		list.add(new ItemModel("06:00 - 07:00"));
		list.add(new ItemModel("07:00 - 08:00"));
		list.add(new ItemModel("08:00 - 09:00"));
		list.add(new ItemModel("09:00 - 10:00"));
		list.add(new ItemModel("10:00 - 11:00"));
		list.add(new ItemModel("11:00 - 12:00"));
		list.add(new ItemModel("12:00 - 13:00"));
		list.add(new ItemModel("13:00 - 14:00"));
		list.add(new ItemModel("14:00 - 15:00"));
		list.add(new ItemModel("15:00 - 16:00"));
		list.add(new ItemModel("16:00 - 17:00"));
		list.add(new ItemModel("17:00 - 18:00"));
		list.add(new ItemModel("18:00 - 19:00"));
		list.add(new ItemModel("19:00 - 20:00"));
		list.add(new ItemModel("20:00 - 21:00"));
		list.add(new ItemModel("21:00 - 22:00"));
		list.add(new ItemModel("22:00 - 23:00"));
		list.add(new ItemModel("23:00 - 24:00"));
		list.add(new ItemModel("24:00 - 01:00"));
		list.add(new ItemModel("01:00 - 02:00"));
		list.add(new ItemModel("02:00 - 03:00"));
		list.add(new ItemModel("03:00 - 04:00"));
		list.add(new ItemModel("04:00 - 05:00"));
		list.add(new ItemModel("05:00 - 06:00"));

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
		if (!Utils.validateString(mAlert.getSelectedItem().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.notification_fill_alert));

		} else if (!Utils.validateString(mAlerTiming.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.notification_fill_alert_timing));

		} 
		return flag;
	}
	
	
}