package com.android.abooji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LocationActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		findViewById(R.id.activity_location_prev_btn).setOnClickListener(this);
		findViewById(R.id.activity_location_next_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_location_prev_btn:
				finish();
			break;

		case R.id.activity_location_next_btn:
			Intent i = new Intent(LocationActivity.this, NotificationAlertActivity.class);
			i.putExtra("FROM", "LOCATION");
			startActivity(i);
			break;

		default:
			break;
		}
	}

}
