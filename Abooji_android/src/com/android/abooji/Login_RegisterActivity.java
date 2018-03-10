package com.android.abooji;

import com.android.abooji.service.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Login_RegisterActivity extends Activity implements OnClickListener
{
	SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_register);
		initView();
	}

	public void initView()
	{
		sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.login:
			Intent loginintent = new Intent(Login_RegisterActivity.this, LoginActivity.class);
			loginintent.putExtra("FROM", "LOGIN");
//			Utils.storeString(sharedPreferences, Utils.ACTIVITY_STATUS, "LoginStat");
			startActivity(loginintent);
			//finish();
			break;
			
		case R.id.register:
			Intent registerintent = new Intent(Login_RegisterActivity.this, RegisterActivity.class);
			registerintent.putExtra("FROM", "REGISTER");
//			Utils.storeString(sharedPreferences, Utils.ACTIVITY_STATUS, "RegisterStat");
			startActivity(registerintent);
			//finish();
			break;
		}
	}
}
