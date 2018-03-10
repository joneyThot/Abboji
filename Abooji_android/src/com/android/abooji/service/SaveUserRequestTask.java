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
import com.android.abooji.model.SaveUserModel;
import com.google.gson.Gson;

public class SaveUserRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public SaveUserRequestTask(Context mContext)
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
		return saveUser(params[0], params[1], params[2], params[3], params[4], params[5],
				params[6],params[7],params[8],params[9],params[10],params[11],params[12],
				params[13]);
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

	private Object saveUser(String user_id, String email, String gender, String ageGroup,String maritalStatus,String children, String childAgeGroup,
			String card_cat, String card_type, String friendsEmail,String cards,String categoryPreference,
			String alert,String alertTime) {
		SaveUserModel saveUserModel = new SaveUserModel();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "save_user_profile.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("gender", gender));
			nameValuePairs.add(new BasicNameValuePair("ageGroup", ageGroup));
			nameValuePairs.add(new BasicNameValuePair("maritalStatus", maritalStatus));
			nameValuePairs.add(new BasicNameValuePair("children", children));
			nameValuePairs.add(new BasicNameValuePair("childAgeGroup", childAgeGroup));
			nameValuePairs.add(new BasicNameValuePair("card_cat", card_cat));
			nameValuePairs.add(new BasicNameValuePair("card_type", card_type));			
			nameValuePairs.add(new BasicNameValuePair("friendsEmail", friendsEmail));
			nameValuePairs.add(new BasicNameValuePair("cards", cards));
			nameValuePairs.add(new BasicNameValuePair("categoryPreference", categoryPreference));
			nameValuePairs.add(new BasicNameValuePair("alert", alert));
			nameValuePairs.add(new BasicNameValuePair("alertTime", alertTime));

			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());
			Gson gson = new Gson();
			saveUserModel = gson.fromJson(jObj.toString(), SaveUserModel.class);
			//			Utils.storeString(Utils.USER_PROFILE, jObj.toString());
			//			Utils.storeBoolean(Utils.ORG_MENU, false);
			//			Utils.storeBoolean(Utils.ORG_LOGOUT, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return saveUserModel;
	}
}
