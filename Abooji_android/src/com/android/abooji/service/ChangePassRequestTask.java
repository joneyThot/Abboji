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

public class ChangePassRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public ChangePassRequestTask(Context mContext)
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
		return changePassword(params[0], params[1], params[2]);
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

	private Object changePassword(String user_id,String email, String new_pass) {
		LoginModel changePasswordModel = new LoginModel();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "change_password.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("new_password", new_pass));
						
			result = Utils.postRequest(getServerPath, nameValuePairs);
			
			JSONObject jObj = new JSONObject(result.toString());
			Gson gson = new Gson();
			changePasswordModel = gson.fromJson(jObj.toString(), LoginModel.class);
//			Utils.storeString(Utils.USER_PROFILE, jObj.toString());
//			Utils.storeBoolean(Utils.ORG_MENU, false);
//			Utils.storeBoolean(Utils.ORG_LOGOUT, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return changePasswordModel;
	}
}
