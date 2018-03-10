package com.android.abooji.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.LoginModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.ChangePassRequestTask;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ChangePasswordFragment extends BaseFragment implements OnClickListener
{
	Fragment fr;
	private View mRootView;
	private EditText oldPassEditText,newPassEditText,confirmPassWditText;
	private String userId,email;
	private SharedPreferences mSharedPreferences;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		mRootView = inflater.inflate(R.layout.fragment_changepassword, container, false);
		initview();
		return mRootView;
	}
	private void initview() 
	{
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_changepasswordtext), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		userId = String.valueOf(mSharedPreferences.getString(Utils.USER_ID, ""));
		email = String.valueOf(mSharedPreferences.getString(Utils.USER_EMAIL, ""));
		Logger.e("TAg USer", "==Username Is : " + userId);
		Logger.e("TAg USer", "==Email Is : " + email);
		oldPassEditText = (EditText) mRootView.findViewById(R.id.changepass_oldpasswordEdt);
		newPassEditText = (EditText) mRootView.findViewById(R.id.changepass_newpasswordEdt);
		confirmPassWditText = (EditText) mRootView.findViewById(R.id.changepass_ConfirmNewpasswordEdt);

		mRootView.findViewById(R.id.changepassword_submitbtn).setOnClickListener(this);
		mRootView.findViewById(R.id.changepass_removeOldPassword).setOnClickListener(this);
		mRootView.findViewById(R.id.changepass_removeNewPassword).setOnClickListener(this);
		mRootView.findViewById(R.id.changepass_removeConfirmNewPassword).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.changepassword_Cancelbtn:
			break;

		case R.id.changepassword_submitbtn:
			if(fieldValidation()){
				onChangePassword(userId,email,newPassEditText.getText().toString());
			}
			break;

		case R.id.changepass_removeOldPassword:
			break;

		case R.id.changepass_removeNewPassword:
			break;

		case R.id.changepass_removeConfirmNewPassword:
			break;

		}
	}

	private void onChangePassword(String user_id, String email, String new_password) {
		if(Utils.checkInternetConnection(getActivity())){
			ChangePassRequestTask changepasswordRequestTask = new ChangePassRequestTask(getActivity());
			changepasswordRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					LoginModel changepasswordModel = (LoginModel) response;
					if(changepasswordModel != null){
						
						if(changepasswordModel.status.equalsIgnoreCase("1")){
							Toast.makeText(getActivity(), "SuccessFully Change", Toast.LENGTH_SHORT).show();
							oldPassEditText.setText("");
							newPassEditText.setText("");
							confirmPassWditText.setText("");
						}else if(changepasswordModel.status.equalsIgnoreCase("0")){
							Toast.makeText(getActivity(), "no record exists", Toast.LENGTH_SHORT).show();
						} else if(changepasswordModel.status.equalsIgnoreCase("2")){
							Toast.makeText(getActivity(), "Provide valid email", Toast.LENGTH_SHORT).show();
						} 
					}
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			changepasswordRequestTask.execute(user_id, email, new_password);
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}
	
	
	private boolean fieldValidation() 
	{
		boolean flag = true;
		if (!Utils.validateString(oldPassEditText.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.register_enter_password));

		}
		else if (!Utils.validateString(newPassEditText.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.register_enter_password));

		}
		else if (!Utils.validateString(confirmPassWditText.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.register_enter_password));

		}		
		else if((!newPassEditText.getText().toString().equals(confirmPassWditText.getText().toString())))
		{
			flag = false;
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.register_password_notmatch));
		}
		return flag;
	}
}