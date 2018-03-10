package com.android.abooji.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.abooji.R;
import com.android.abooji.logger.Logger;
import com.android.abooji.model.AdvertsModel;
import com.android.abooji.model.CardDetailsModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class GetCardDetailsRequestTask extends AsyncTask<String,Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public GetCardDetailsRequestTask(Context mContext)
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
		return cardDetails();
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

	private Object cardDetails() {
		ArrayList<CardDetailsModel> cardDetailsModelsList = new ArrayList<CardDetailsModel>();
		String result = null;
		String getServerPath = Utils.URL_SERVER_ADDRESS + "card_detail.php"; 
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			result = Utils.postRequest(getServerPath, nameValuePairs);
			JSONObject jObj = new JSONObject(result.toString());
			
			JSONArray cardDetailsArray = jObj.getJSONArray("card_details");
			for (int i = 0; i < cardDetailsArray.length(); i++) {
				JSONObject issue = cardDetailsArray.getJSONObject(i);
				CardDetailsModel cardDetailsModel = new CardDetailsModel();
				cardDetailsModel.setId(issue.getString("id"));
				cardDetailsModel.setCard_id(issue.getString("card_id"));
				cardDetailsModel.setCard_name(issue.getString("card_name"));
				cardDetailsModel.setCard_type_id(issue.getString("card_type_id"));
				cardDetailsModel.setCard_category_id(issue.getString("card_category_id"));
				cardDetailsModel.setCard_image(issue.getString("card_image"));
				cardDetailsModel.setTarget_market(issue.getString("target_market"));
				cardDetailsModel.setContact_name(issue.getString("contact_name"));
				cardDetailsModel.setContact_email(issue.getString("contact_email"));
				cardDetailsModel.setPassword(issue.getString("password"));
				cardDetailsModel.setFirstname(issue.getString("firstname"));
				cardDetailsModel.setLastname(issue.getString("lastname"));
				cardDetailsModel.setMobile(issue.getString("mobile"));
				cardDetailsModel.setTitle(issue.getString("title"));
				cardDetailsModel.setContact_number(issue.getString("contact_number"));
				cardDetailsModel.setPrivilege1(issue.getString("privilege1"));
				cardDetailsModel.setValue1(issue.getString("value1"));
				cardDetailsModel.setPrivilege2(issue.getString("privilege2"));
				cardDetailsModel.setValue2(issue.getString("value2"));
				cardDetailsModel.setPrivilege3(issue.getString("privilege3"));
				cardDetailsModel.setValue3(issue.getString("value3"));
				cardDetailsModel.setCredit_used(issue.getString("credit_used"));
				cardDetailsModel.setBasic(issue.getString("basic"));
				cardDetailsModel.setPremium(issue.getString("premium"));
				cardDetailsModel.setAvailable_credits(issue.getString("available_credits"));
				cardDetailsModel.setStatus(issue.getString("status"));
				cardDetailsModel.setCard_type_title(issue.getString("card_type_title"));
				cardDetailsModel.setCard_category_title(issue.getString("card_category_title"));
				cardDetailsModel.setOrganisation_name(issue.getString("organisation_name"));
				cardDetailsModel.setDefault_image_path(issue.getString("default_image_path"));
				
				cardDetailsModelsList.add(cardDetailsModel);				
				
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cardDetailsModelsList;
	}
}
