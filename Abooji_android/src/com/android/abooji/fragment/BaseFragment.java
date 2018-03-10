package com.android.abooji.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.abooji.DashboardLoginActivity;

public class BaseFragment extends Fragment {
	public DashboardLoginActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			mActivity = (DashboardLoginActivity) this.getActivity();
	}

	public boolean onBackPressed(){
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){

	}
}
