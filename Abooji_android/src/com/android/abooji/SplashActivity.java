package com.android.abooji;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.abooji.service.Utils;

public class SplashActivity extends Activity 
{
	private Handler mHandler = new Handler();
	SharedPreferences sharedPreferences;

	private Runnable mRunnable = new Runnable() 
	{
		@Override
		public void run() 
		{
			if(Utils.checkInternetConnection(getApplicationContext())){
				if((!sharedPreferences.getString(Utils.USER_EMAIL, "").isEmpty())&&(!sharedPreferences.getString(Utils.USER_PASSWORD, "").isEmpty()))
				{
					startActivity(new Intent(SplashActivity.this,DashboardLoginActivity.class));
					finish();
				}
				else //if((sharedPreferences.getString(Utils.USERNAME, "username")==null)&&(sharedPreferences.getString(Utils.PASSWORD, "password")==null))
				{
					startActivity(new Intent(SplashActivity.this,Login_RegisterActivity.class));
					finish();
				}
			} 
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sharedPreferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
		getActionBar().hide();
		mHandler.postDelayed(mRunnable, 2000);
	}
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		mHandler.removeCallbacks(mRunnable);
	}
}
