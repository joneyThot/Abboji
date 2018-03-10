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
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.ShareShoppingListRequestTask;
import com.android.abooji.service.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ShoppingShareFragment extends BaseFragment implements OnClickListener{
	View mRootView;
	Fragment fm;
	EditText mEmail;
	private SharedPreferences mSharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_email_address, container, false);
		initview();
		return mRootView;
	}

	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_shoppingsharetext), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		mActivity.getSlidingMenu().setMode(SlidingMenu.LEFT);
		mActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		mEmail = (EditText) mRootView.findViewById(R.id.email_address_edit);
		mRootView.findViewById(R.id.email_address_save_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.email_address_save_btn:

			shareShopping("",mEmail.getText().toString().trim());
			
			break;

		default:
			break;
		}

	}

	private void shareShopping(String list_id, String email_ids) {
		if (Utils.checkInternetConnection(getActivity())) {
			ShareShoppingListRequestTask shareShoppingListRequestTask = new ShareShoppingListRequestTask(getActivity());
			shareShoppingListRequestTask.setAsyncCallListener(new AsyncCallListener() {
				@Override
				public void onResponseReceived(Object response) {
					Toast.makeText(getActivity(), "Share Successfully", Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
				}
			});
			shareShoppingListRequestTask.execute(list_id,email_ids);
		} else {
			Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
		}


	}

}
