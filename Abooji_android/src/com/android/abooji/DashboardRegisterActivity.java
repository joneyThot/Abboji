package com.android.abooji;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.abooji.fragment.CardFragment;
import com.android.abooji.fragment.CategoryFragment;
import com.android.abooji.fragment.LocationFragment;
import com.android.abooji.fragment.NotificationFragment;
import com.android.abooji.fragment.PersonalFragment;
import com.android.abooji.view.TextViewPlus;

public class DashboardRegisterActivity extends FragmentActivity implements OnClickListener
{
	TextViewPlus title;
	Fragment fr = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		findViewById(R.id.title).setVisibility(View.VISIBLE);
		findViewById(R.id.search_bar).setVisibility(View.GONE);
		findViewById(R.id.drawer_icon).setVisibility(View.GONE);
		initview();
		fr = new PersonalFragment();
		FragmentReplaceFunction();
	}

	private void initview() 
	{
		title = (TextViewPlus)findViewById(R.id.title);
		title.setText(R.string.profile_txt);
		findViewById(R.id.profile_userprofilebtn).setOnClickListener(this);
		findViewById(R.id.profile_cardbtn).setOnClickListener(this);
		findViewById(R.id.profile_categorybtn).setOnClickListener(this);
		findViewById(R.id.profile_locationbtn).setOnClickListener(this);
		findViewById(R.id.profile_notificationbtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.profile_userprofilebtn:
			fr = new PersonalFragment(); 
			FragmentReplaceFunction();
			break;

		case R.id.profile_cardbtn:
			fr = new CardFragment(); 
			FragmentReplaceFunction();
			break;

		case R.id.profile_categorybtn:
			fr = new CategoryFragment(); 
			FragmentReplaceFunction();
			break;

		case R.id.profile_locationbtn:
			fr = new LocationFragment(); 
			FragmentReplaceFunction();
			break;

		case R.id.profile_notificationbtn:
			fr = new NotificationFragment(); 
			FragmentReplaceFunction();
			break;
		}
	}
	private void FragmentReplaceFunction()
	{
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.commit();
	}
}
