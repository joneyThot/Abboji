package com.android.abooji.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.CardTypeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetCardTypeRequestTask extends AsyncTask<String ,Integer, Object>{

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public GetCardTypeRequestTask(Context mContext)
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
		return card_type();
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

	private Object card_type() {
		ArrayList<CardTypeModel> cardTypeModelArrayList = new ArrayList<CardTypeModel>();
		String result = "";
		String getServerPath = Utils.URL_SERVER_ADDRESS + "card_type.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);

			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());
			Gson gson = new Gson();
			Type listType = new TypeToken<List<CardTypeModel>>() {}.getType();
			cardTypeModelArrayList = gson.fromJson(jObj.get("card_type").toString(), listType);			
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cardTypeModelArrayList;
	}
}
