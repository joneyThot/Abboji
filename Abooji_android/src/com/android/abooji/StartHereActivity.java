package com.android.abooji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class StartHereActivity extends Activity implements OnClickListener 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starthere);
		initview();
	}

	private void initview() 
	{
		findViewById(R.id.start_here).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.start_here:
			startActivity(new Intent(StartHereActivity.this,DashboardRegisterActivity.class));
			finish();
			break;
		}
	}
}
