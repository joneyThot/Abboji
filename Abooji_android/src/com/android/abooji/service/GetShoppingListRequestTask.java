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
import com.android.abooji.model.SaveShoppingListModel;

public class GetShoppingListRequestTask extends
		AsyncTask<String, Integer, Object> {

	public Context mContext;
	private ProgressDialog mProgressDialog;
	private AsyncCallListener mAsyncCallListener;
	private String mErrorMessage;
	private boolean mIsError = false;

	public GetShoppingListRequestTask(Context mContext) {
		this.mContext = mContext;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage(mContext.getResources().getString(R.string.loading));
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProgressDialog != null && !mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	@Override
	protected Object doInBackground(String... params) {
		return getShoopingList(params[0]);
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		if (mAsyncCallListener != null) {
			if (mIsError) {
				Logger.e("", "In Asnyc call->errorMessage:" + mErrorMessage);
				mAsyncCallListener.onErrorReceived(mErrorMessage);
			} else
				mAsyncCallListener.onResponseReceived(result);
		}
	}

	public void setAsyncCallListener(AsyncCallListener listener) {
		this.mAsyncCallListener = listener;
	}

	private Object getShoopingList(String user_id) {
		// ArrayList<SaveShoppingListModel> saveShoppingListModelArrayList = new
		// ArrayList<SaveShoppingListModel>();
		TreeMap<Integer, SaveShoppingListModel> mapShoppingList = new TreeMap<Integer, SaveShoppingListModel>();
		String result = "";
		String getServerPath = Utils.URL_SERVER_ADDRESS	+ "get_shopping_list.php";
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
			nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

			result = Utils.postRequest(getServerPath, nameValuePairs);

			JSONObject jObj = new JSONObject(result.toString());

			Iterator iterator = jObj.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				if (Utils.validateString(key) && key.equalsIgnoreCase("status")) {

				} else {
					JSONObject issue = jObj.getJSONObject(key);
					SaveShoppingListModel saveShoppingListModel = new SaveShoppingListModel();
					saveShoppingListModel.setList_id(issue.optString("list_id"));
//					Utils.storeString(Utils.LIST_ID, saveShoppingListModel.getList_id());
					saveShoppingListModel.setId_of_user(issue.optString("id_of_user"));
					saveShoppingListModel.setList_name(issue.optString("list_name"));
					saveShoppingListModel.setList(issue.optString("list"));
					saveShoppingListModel.setShare_ids(issue.optString("share_ids"));

					mapShoppingList.put(Integer.parseInt(key),saveShoppingListModel);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return mapShoppingList;
	}

}
