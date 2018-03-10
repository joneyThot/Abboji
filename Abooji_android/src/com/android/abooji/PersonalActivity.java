package com.android.abooji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PersonalActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		findViewById(R.id.activity_personal_next_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_personal_next_btn:
			Intent i = new Intent(PersonalActivity.this, CardActivity.class);
			i.putExtra("FROM", "PERSONAL");
			startActivity(i);
			break;

		default:
			break;
		}
	}

}
