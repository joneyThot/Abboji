package com.android.abooji;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.abooji.logger.Logger;
import com.android.abooji.model.LoginModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.RegisterRequestTask;
import com.android.abooji.service.Utils;


public class RegisterActivity extends Activity implements OnClickListener 
{
	private EditText mUserEmail, mPassword, mConfirmPassword;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initview();
		CheckFocus();
	}

	private void initview() 
	{
		sharedPreferences = this.getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);

		findViewById(R.id.register_Cancel).setOnClickListener(this);
		findViewById(R.id.register_Submit).setOnClickListener(this);
		findViewById(R.id.register_removeConfirmPassord).setOnClickListener(this);
		findViewById(R.id.register_removePassword).setOnClickListener(this);
		findViewById(R.id.register_removeUserEmail).setOnClickListener(this);

		mUserEmail = (EditText)findViewById(R.id.register_userEmail);
		mPassword = (EditText)findViewById(R.id.register_password);
		mConfirmPassword = (EditText)findViewById(R.id.register_confirmPassword);
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.register_Cancel:
			super.onBackPressed();
			break;
		case R.id.register_Submit:
			if(fieldValidation())
			{
				onRegister(mUserEmail.getText().toString(), mPassword.getText().toString(),	"", "", "", "");
//								startActivity(new Intent(RegisterActivity.this,StartHereActivity.class));
//								finish();
			}
			break;
		case R.id.register_removeUserEmail:
			Clearingdata(mUserEmail);
			break;

		case R.id.register_removePassword:
			Clearingdata(mPassword);
			break;

		case R.id.register_removeConfirmPassord:
			Clearingdata(mConfirmPassword);
			break;
		}
	}

	private void onRegister(String email, String password, String company_name, String comp_reg_number, String company_type, String country_comp_reg_in) {
		if(Utils.checkInternetConnection(this)){
			RegisterRequestTask registerRequestTask = new RegisterRequestTask(this);
			registerRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					LoginModel registerModel = (LoginModel) response;
					if(registerModel != null)
					{
						if(registerModel.status.equalsIgnoreCase("1")) {
							Utils.storeString(sharedPreferences, Utils.USER_ID, registerModel.user_id);
							Utils.storeString(sharedPreferences, Utils.USER_EMAIL, mUserEmail.getText().toString());
							Utils.storeString(sharedPreferences, Utils.USER_PASSWORD, mPassword.getText().toString());
							Logger.e("Register ", "User ID :: " + registerModel.user_id + " Email :: " +  mUserEmail.getText().toString() + " Password :: " +mPassword.getText().toString());
							Toast.makeText(RegisterActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(RegisterActivity.this, StartHereActivity.class);
							intent.putExtra("FROM", "REGISTER");
							startActivity(intent);
							finish();

						} else if(registerModel.status.equalsIgnoreCase("0")){
							Toast.makeText(RegisterActivity.this, "Some other problem to register user", Toast.LENGTH_SHORT).show();
						} 
						else if(registerModel.status.equalsIgnoreCase("2")){
							Toast.makeText(RegisterActivity.this, "Already exist", Toast.LENGTH_SHORT).show();
						}
						else if(registerModel.status.equalsIgnoreCase("3")){
							Toast.makeText(RegisterActivity.this, "Please provide complete information", Toast.LENGTH_SHORT).show();
						}
					}

				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(RegisterActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			registerRequestTask.execute(email, password, company_name, comp_reg_number, company_type,country_comp_reg_in);
		}else{
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}

	}

	public void Clearingdata(EditText edt)
	{
		Utils.clearData(edt);
	}
	private void CheckFocus()
	{
		mUserEmail.setOnFocusChangeListener(new OnFocusChangeListener() 
		{          
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				hideShowFunction(mUserEmail,"USEREMAIL");
			}
		});
		mPassword.setOnFocusChangeListener(new OnFocusChangeListener() 
		{          
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				hideShowFunction(mPassword,"PASSWORD");
			}
		});
		mConfirmPassword.setOnFocusChangeListener(new OnFocusChangeListener() 
		{          
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				hideShowFunction(mConfirmPassword,"CONFIRMPASSSWORD");
			}
		});
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
						findViewById(R.id.register_removeUserEmail).setVisibility(View.GONE);
					}
					else if(status.equals("PASSWORD"))
					{
						findViewById(R.id.register_removePassword).setVisibility(View.GONE);
					}
					else if(status.equals("CONFIRMPASSSWORD"))
					{
						findViewById(R.id.register_removeConfirmPassord).setVisibility(View.GONE);
					}

				}
				else 
				{
					if(status.equals("USEREMAIL"))
					{
						findViewById(R.id.register_removeUserEmail).setVisibility(View.VISIBLE);
					}
					else if(status.equals("PASSWORD"))
					{
						findViewById(R.id.register_removePassword).setVisibility(View.VISIBLE);
					}
					else if(status.equals("CONFIRMPASSSWORD"))
					{
						findViewById(R.id.register_removeConfirmPassord).setVisibility(View.VISIBLE);
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
	public boolean fieldValidation() 
	{
		boolean flag = true;
		if (!Utils.validateString(mUserEmail.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.register_enter_email));
		}
		else if(mUserEmail.getText().toString().length() > 0 && !Utils.isEmailValid(mUserEmail.getText().toString()))
		{
			flag = false;
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.register_invalid_email));
		}
		else if (!Utils.validateString(mPassword.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.register_enter_password));

		}
		else if (mPassword.getText().toString().trim().length() < 6) 
		{
			flag = false;
			Utils.showMessageDialog(this, "Alert","Please Enter password and must be 6 characters!");
		}
		else if (!Utils.validateString(mConfirmPassword.getText().toString())) 
		{
			flag = false;
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.register_enter_confirmpassword));

		}
		else if((!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())))
		{
			flag = false;
			Utils.showMessageDialog(this, getResources().getString(R.string.alert), getResources().getString(R.string.register_password_notmatch));
		}
		return flag;
	}
}
