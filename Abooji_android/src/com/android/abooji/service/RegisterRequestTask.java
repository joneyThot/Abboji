package com.android.abooji.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.LoginModel;
import com.google.gson.Gson;

public class RegisterRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public RegisterRequestTask(Context mContext)
	{
		this.mContext = mContext;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProgressDialog != null && !mProgressDialog.isShowing())
		{
			mProgressDialog.show();
		}
	}

	@Override
	protected Object doInBackground(String... params) {
		return register(params[0], params[1], params[2], params[3], params[4], params[5]);
	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (mProgressDialog != null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
		if(mAsyncCallListener != null) {
			if(mIsError){
				Logger.e("","In Asnyc call->errorMessage:"+mErrorMessage);
				mAsyncCallListener.onErrorReceived(mErrorMessage);
			}
			else
				mAsyncCallListener.onResponseReceived(result);
		}
	}

	public void setAsyncCallListener(AsyncCallListener listener) {
		this.mAsyncCallListener = listener;
	}

	private Object register(String email, String password, String company_name, String comp_reg_number,String company_type,String country_comp_reg_in) {
		LoginModel registerModel = new LoginModel();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "register.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("company_name", company_name));
			nameValuePairs.add(new BasicNameValuePair("comp_reg_number", comp_reg_number));
			nameValuePairs.add(new BasicNameValuePair("company_type", company_type));
			nameValuePairs.add(new BasicNameValuePair("country_comp_reg_in", country_comp_reg_in));
			
			
			result = Utils.postRequest(getServerPath, nameValuePairs);
			
			JSONObject jObj = new JSONObject(result.toString());
			Gson gson = new Gson();
			registerModel = gson.fromJson(jObj.toString(), LoginModel.class);
//			Utils.storeString(Utils.USER_PROFILE, jObj.toString());
//			Utils.storeBoolean(Utils.ORG_MENU, false);
//			Utils.storeBoolean(Utils.ORG_LOGOUT, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return registerModel;
	}
}
