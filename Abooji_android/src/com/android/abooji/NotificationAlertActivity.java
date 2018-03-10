package com.android.abooji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class NotificationAlertActivity extends Activity implements
OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_alert);
		initview();

	}

	private void initview() {
		// TODO Auto-generated method stub
		findViewById(R.id.activity_notification_alert_prev_btn).setOnClickListener(this);
		findViewById(R.id.activity_notification_alert_save_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_notification_alert_prev_btn:
				finish();
			break;
		case R.id.activity_notification_alert_save_btn:

			break;
		default:
			break;
		}
	}

}

