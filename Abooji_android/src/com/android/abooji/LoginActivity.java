package com.android.abooji;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.abooji.logger.Logger;
import com.android.abooji.model.LoginModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.ForgotPassRequestTask;
import com.android.abooji.service.LoginRequestTask;
import com.android.abooji.service.Utils;

public class LoginActivity extends Activity implements OnClickListener {

	EditText mEmail_edittxt, mPassword_edittxt, mForgot_emailTxt;
	int hwidth,hHeight;
	Dialog forgotDialog;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginnew);
		Display display = getWindowManager().getDefaultDisplay(); 

		hwidth = display.getWidth();  // deprecated
		hHeight = display.getHeight(); 

		initview();
		CheckFocus();
	}

	private void initview() {

		sharedPreferences = this.getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
		
		findViewById(R.id.activity_login_remove_btn).setOnClickListener(this);
		findViewById(R.id.login_Cancel).setOnClickListener(this);
		findViewById(R.id.activity_login_forgot_password_btn).setOnClickListener(this);
		findViewById(R.id.login_ok).setOnClickListener(this);


		findViewById(R.id.login_removePassword).setOnClickListener(this);
		findViewById(R.id.login_removeEmail).setOnClickListener(this);

		mEmail_edittxt = (EditText) findViewById(R.id.activity_login_email_edittxt);
		mPassword_edittxt = (EditText) findViewById(R.id.activity_login_password_edittxt);

	}

	@Override
	public void onClick(View v) 
	{

		switch (v.getId()) {
		case R.id.activity_login_remove_btn:
			super.onBackPressed();
			break;
		case R.id.login_Cancel:
			super.onBackPressed();
			break;

		case R.id.login_removeEmail:
			Clearingdata(mEmail_edittxt);
			break;

		case R.id.login_removePassword:
			Clearingdata(mPassword_edittxt);
			break;

		case R.id.activity_login_forgot_password_btn:
			openForgotPasswordDialog();
			break;

		case R.id.forgot_sendEmail:
			if(forgotfieldValidation())
			{
				onForgotPassword(mForgot_emailTxt.getText().toString());
			}
			break;

		case R.id.login_ok:
			if(fieldValidation())
			{
				if((!sharedPreferences.getString(Utils.USER_EMAIL, "").isEmpty())&&(!sharedPreferences.getString(Utils.USER_PASSWORD, "").isEmpty()))
				{
					onLogin(sharedPreferences.getString(Utils.USER_EMAIL, ""),sharedPreferences.getString(Utils.USER_PASSWORD, ""),"");
				}
				else 
				{
					onLogin(mEmail_edittxt.getText().toString(),mPassword_edittxt.getText().toString(),"");
				}
				
				//Utils.storeBoolean(Utils.LOGINSTATUS, true);
				/*if(Utils.LOGINSTATUS.equalsIgnoreCase("true"))
				{
					startActivity(new Intent(LoginActivity.this,DashboardLoginActivity.class));
				}
				else
				{

				}
				Utils.storeBoolean(Utils.LOGINSTATUS, true);
				 */
//				startActivity(new Intent(LoginActivity.this,DashboardLoginActivity.class));
//				finish();
			}
			break;

		}
	}

	private void onLogin(String email, String password, String company_type) {
		if(Utils.checkInternetConnection(this)){
			LoginRequestTask loginRequestTask = new LoginRequestTask(this);
			loginRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					LoginModel loginModel = (LoginModel) response;
					if(loginModel != null)
					{
						if(loginModel.status.equalsIgnoreCase("1")) {
							Utils.storeString(sharedPreferences, Utils.USER_ID, loginModel.user_id);
							Utils.storeString(sharedPreferences, Utils.USER_EMAIL, mEmail_edittxt.getText().toString());
							Utils.storeString(sharedPreferences, Utils.USER_PASSWORD, mPassword_edittxt.getText().toString());
							Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(LoginActivity.this, DashboardLoginActivity.class);
							intent.putExtra("FROM", "Login");
							startActivity(intent);
							finish();

						} else if(loginModel.status.equalsIgnoreCase("0")){
							Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
						} else if(loginModel.status.equalsIgnoreCase("2")){
							Toast.makeText(LoginActivity.this, "Please provide complete information", Toast.LENGTH_SHORT).show();
						}
					}

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(LoginActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			loginRequestTask.execute(email, password, company_type);
		}else{
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
	}
	
	private void onForgotPassword(String email) {
		if(Utils.checkInternetConnection(this)){
			ForgotPassRequestTask forgotPassRequestTask = new ForgotPassRequestTask(this);
			forgotPassRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					LoginModel forgotPasswordModel = (LoginModel) response;
					if(forgotPasswordModel != null)
					{
						if(forgotPasswordModel.status.equalsIgnoreCase("1")) {
							forgotDialog.dismiss();
							Toast.makeText(LoginActivity.this, "Forgot successfully", Toast.LENGTH_SHORT).show();

						} else if(forgotPasswordModel.status.equalsIgnoreCase("0")){
							Toast.makeText(LoginActivity.this, "no record exists", Toast.LENGTH_SHORT).show();
						} else if(forgotPasswordModel.status.equalsIgnoreCase("2")){
							Toast.makeText(LoginActivity.this, "Provide valid email", Toast.LENGTH_SHORT).show();
						}
					}

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(LoginActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			forgotPassRequestTask.execute(email);
		}else{
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}
		
	}

	private void openForgotPasswordDialog()
	{
		forgotDialog = new Dialog(LoginActivity.this);
		forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotDialog.setContentView(R.layout.activity_forgotpassword);
		mForgot_emailTxt = (EditText)forgotDialog.findViewById(R.id.forgot_emailEdt);
		forgotDialog.findViewById(R.id.forgot_sendEmail).setOnClickListener(this);
		forgotDialog.getWindow().setLayout((hwidth/2)+100, (hHeight/3)+50);
		forgotDialog.show();
	}
	private void CheckFocus()
	{
		mEmail_edittxt.setOnFocusChangeListener(new OnFocusChangeListener() 
		{          
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				hideShowFunction(mEmail_edittxt,"USEREMAIL");
			}
		});
		mPassword_edittxt.setOnFocusChangeListener(new OnFocusChangeListener() 
		{          
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				hideShowFunction(mPassword_edittxt,"PASSWORD");
			}
		});
	}
	public void Clearingdata(EditText edt)
	{
		Utils.clearData(edt);
	}
	private void hideShowFunction(final EditText objEdit, final String status)
	{
		objEdit.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				int i = objEdit.getText().toString().length();
				if(i<=0)
				{
					if(status.equals("USEREMAIL"))
					{
						findViewById(R.id.login_removeEmail).setVisibility(View.GONE);
					}
					else if(status.equals("PASSWORD"))
					{
						findViewById(R.id.login_removePassword).setVisibility(View.GONE);
					}

				}
				else 
				{
					if(status.equals("USEREMAIL"))
					{
						findViewById(R.id.login_removeEmail).setVisibility(View.VISIBLE);
					}
					else if(status.equals("PASSWORD"))
					{
						findViewById(R.id.login_removePassword).setVisibility(View.VISIBLE);
					}
				}
				Logger.e("", "Count :"+ i );
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) 
			{

			}

			@Override
			public void afterTextChanged(Editable s) 
			{

			}
		});
	}
	private boolean fieldValidation() 
	{
		boolean flag = true;
		if (!Utils.validateString(mEmail_edittxt.getText().toString().trim())) 
		{
			flag = false;
			Utils.showMessageDialog(this, "Alert", "Please enter Email!");
		}
		else if (!Utils.isEmailValid(mEmail_edittxt.getText().toString().trim())) 
		{
			flag = false;
			Utils.showMessageDialog(this, "Alert","Please Enter Valid Email address!");
		}
		else if (mPassword_edittxt.getText().toString().trim().length() < 6) 
		{
			flag = false;
			Utils.showMessageDialog(this, "Alert","Please Enter password and must be 6 characters!");
		}
		return flag;
	}
	
	private boolean forgotfieldValidation() 
	{
		boolean flag = true;
		if (!Utils.validateString(mForgot_emailTxt.getText().toString().trim())) 
		{
			flag = false;
			Utils.showMessageDialog(this, "Alert", "Please enter Email!");
		}
		else if (!Utils.isEmailValid(mForgot_emailTxt.getText().toString().trim())) 
		{
			flag = false;
			Utils.showMessageDialog(this, "Alert","Please Enter Valid Email address!");
		}
		return flag;
	}
}
