package com.android.abooji.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.SaveLocationModel;

public class GetUserLocationRequestTask extends AsyncTask<String ,Integer, Object>{

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public GetUserLocationRequestTask(Context mContext)
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
		return getUserLocation(params[0]);
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

	private Object getUserLocation(String user_id) {
//		ArrayList<SaveLocationModel> saveLocationModelArrayList = new ArrayList<SaveLocationModel>();
		TreeMap<Integer, SaveLocationModel> mapShoppingList = new TreeMap<Integer, SaveLocationModel>();
	
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "get_user_location.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());
			Iterator iterator = jObj.keys();
			while(iterator.hasNext()){
//				saveLocationModelArrayList.clear();
				String key = (String)iterator.next();
				if(Utils.validateString(key) && key.equalsIgnoreCase("status")){

				} else {
					JSONObject issue = jObj.getJSONObject(key);
					SaveLocationModel saveLocationModel = new SaveLocationModel();
					saveLocationModel.setId(issue.optString("id"));		
					saveLocationModel.setUser_id(issue.optString("user_id"));
					saveLocationModel.setLocation_name(issue.optString("location_name"));
					saveLocationModel.setLatitude(issue.optString("latitude"));
					saveLocationModel.setLongitude(issue.optString("longitude"));
					saveLocationModel.setDate_created(issue.optString("date_created"));

					mapShoppingList.put(Integer.parseInt(key) + 1,saveLocationModel);	
				}

			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapShoppingList;
	}

}
