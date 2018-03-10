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
import com.android.abooji.model.SaveLocationModel;
import com.google.gson.Gson;

public class SaveLocationRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public SaveLocationRequestTask(Context mContext)
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
		return saveLocation(params[0], params[1], params[2], params[3]);
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

	private Object saveLocation(String user_id,String location_name, String latitude, String longitude) {
		SaveLocationModel saveLocationModel = new SaveLocationModel();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "save_user_location.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
			nameValuePairs.add(new BasicNameValuePair("location_name", location_name));
			nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
			nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
						
			result = Utils.postRequest(getServerPath, nameValuePairs);
			
			JSONObject jObj = new JSONObject(result.toString());
			Gson gson = new Gson();
			saveLocationModel = gson.fromJson(jObj.toString(), SaveLocationModel.class);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return saveLocationModel;
	}
}
