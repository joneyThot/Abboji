package com.android.abooji.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.abooji.DashboardLoginActivity;
import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.SaveLocationRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.TextViewPlus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CurrentLocationTabFragment extends BaseFragment implements OnClickListener{
	int hwidth,hHeight;
	AutoCompleteTextView atvPlaces;
	DownloadTask placesDownloadTask;
	DownloadTask placeDetailsDownloadTask;
	ParserTask placesParserTask;
	ParserTask placeDetailsParserTask;
	GoogleMap googleMap;
	final int PLACES = 0;
	final int PLACES_DETAILS = 1;
	Marker marker;
	ArrayList<Marker> marraylst_marker = new ArrayList<Marker>();
	LatLng point;
	Button ok, cancel;
	double latitude = 0.0, longitude = 0.0;
	View mRootView;
	EditText currentlocation_mapdialogedttxt;
	Dialog mapDialog;
	private SaveLocationModel mSaveLocationDetails = new SaveLocationModel();
//	Bundle myBundle = new Bundle();
	String mSaveLocatonName;
	Double mSaveLatitude,mSaveLongitude;
	private SharedPreferences mSharedPreferences;
	SupportMapFragment myMAPF;
	TextViewPlus add_current_location;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		if (mRootView != null) {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (parent != null)
				parent.removeView(mRootView);
		}
		try {
			mRootView = inflater.inflate(R.layout.fragment_current_location, container, false);
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}

		Display display = getActivity().getWindowManager().getDefaultDisplay(); 

		hwidth = display.getWidth();  // deprecated
		hHeight = display.getHeight(); 
		initview();

		return mRootView;
	}

	private void initview() {
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE); 
		((DashboardLoginActivity) getActivity()).setTitleText(getString(R.string.title_currentlocationtext), View.VISIBLE, View.VISIBLE, View.VISIBLE);
		
		add_current_location = (TextViewPlus) mRootView.findViewById(R.id.current_location_add_btn);
		add_current_location.setOnClickListener(this);
		atvPlaces = (AutoCompleteTextView) mRootView.findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);


		if(googleMap == null){
			//			FragmentManager myFM = getActivity().getSupportFragmentManager();
			//			myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);

			FragmentManager fm = getChildFragmentManager();
			myMAPF = (SupportMapFragment) fm.findFragmentById(R.id.map);
			if (myMAPF == null) {
				myMAPF = SupportMapFragment.newInstance();
				fm.beginTransaction().replace(R.id.map, myMAPF).commit();
			}
			googleMap = myMAPF.getMap();
			googleMap.clear();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		} 

		//		if(googleMap == null)
		//			googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();


		if(getArguments() != null) {
			mSaveLocationDetails =  (SaveLocationModel) getArguments().get(Utils.LOCATIONDETAIL);
			mSaveLocatonName = mSaveLocationDetails.getLocation_name();
			mSaveLatitude = Double.parseDouble(mSaveLocationDetails.getLatitude());
			mSaveLongitude = Double.parseDouble(mSaveLocationDetails.getLongitude());

			point = new LatLng(mSaveLatitude, mSaveLongitude);
			marker = googleMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
			marraylst_marker.add(marker);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10.0f));

			atvPlaces.setText(mSaveLocatonName);
		}

		mapinit();
	}

	private void mapinit() {

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (atvPlaces.length() == 0) {
					if (!marraylst_marker.isEmpty()) {
						googleMap.clear();
						marker.remove();
						marraylst_marker.clear();
					}
				}
				placesDownloadTask = new DownloadTask(PLACES);
				String url = getAutoCompleteUrl(s.toString());
				placesDownloadTask.execute(url);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		atvPlaces.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long id) {

				ListView lv = (ListView) arg0;
				SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

				HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);
				placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);
				String url = getPlaceDetailsUrl(hm.get("reference"));
				placeDetailsDownloadTask.execute(url);

			}
		});
	}

	private String getAutoCompleteUrl(String place) {
		String key = "key=AIzaSyAU93CCu4spbwvIXmAtbcvn-VDUMei4wTo";
		String input = "input=" + place;
		String types = "types=geocode";
		String sensor = "sensor=false";
		String parameters = input + "&" + types + "&" + sensor + "&" + key;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"	+ output + "?" + parameters;
		return url;
	}

	private String getPlaceDetailsUrl(String ref) {
		String key = "key=AIzaSyAU93CCu4spbwvIXmAtbcvn-VDUMei4wTo";
		String reference = "reference=" + ref;
		String sensor = "sensor=false";
		String parameters = reference + "&" + sensor + "&" + key;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;
		Log.i("URL", "url :: " + url);
		return url;
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {
		private int downloadType = 0;

		public DownloadTask(int type) {
			this.downloadType = type;
		}

		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			switch (downloadType) {
			case PLACES:
				placesParserTask = new ParserTask(PLACES);
				placesParserTask.execute(result);
				break;

			case PLACES_DETAILS:
				placeDetailsParserTask = new ParserTask(PLACES_DETAILS);
				placeDetailsParserTask.execute(result);
			}
		}
	}

	private class ParserTask extends
	AsyncTask<String, Integer, List<HashMap<String, String>>> {
		int parserType = 0;

		public ParserTask(int type) {
			this.parserType = type;
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<HashMap<String, String>> list = null;

			try {
				jObject = new JSONObject(jsonData[0]);

				switch (parserType) {
				case PLACES:
					com.android.abooji.service.PlaceJSONParser placeJsonParser = new com.android.abooji.service.PlaceJSONParser();
					list = placeJsonParser.parse(jObject);
					break;
				case PLACES_DETAILS:
					com.android.abooji.service.PlaceDetailsJSONParser placeDetailsJsonParser = new com.android.abooji.service.PlaceDetailsJSONParser();
					list = placeDetailsJsonParser.parse(jObject);
				}

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			switch (parserType) {
			case PLACES:
				String[] from = new String[] { "description" };
				int[] to = new int[] { android.R.id.text1 };
				// Creating a SimpleAdapter for the AutoCompleteTextView
				SimpleAdapter adapter = new SimpleAdapter(getActivity(),result, android.R.layout.simple_list_item_1, from, to);
				// Setting the adapter
				atvPlaces.setAdapter(adapter);
				break;
			case PLACES_DETAILS:
				HashMap<String, String> hm = result.get(0);
				latitude = Double.parseDouble(hm.get("lat"));
				longitude = Double.parseDouble(hm.get("lng"));
				
				if(googleMap == null){

					FragmentManager fm = getChildFragmentManager();
					myMAPF = (SupportMapFragment) fm.findFragmentById(R.id.map);
					if (myMAPF == null) {
						myMAPF = SupportMapFragment.newInstance();
						fm.beginTransaction().replace(R.id.map, myMAPF).commit();
					}
					googleMap = myMAPF.getMap();
				} 
				
				point = new LatLng(latitude, longitude);
				Log.i("LATLANG", "Latitude : " + latitude + " & Longitude : "+ longitude);
				CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				MarkerOptions options = new MarkerOptions();
				options.position(point);
				options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				options.title("Position");
				options.snippet("Latitude:" + latitude + ",Longitude:"+ longitude);
				marker = googleMap.addMarker(options);
				marraylst_marker.add(marker);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.current_location_add_btn:

			Log.v("LATLONG", "String"+ latitude + "," + longitude);
			Log.v("STRING", "TEXT :: " + atvPlaces.getText().toString().trim());
			openSaveDataDialog();
			currentlocation_mapdialogedttxt.setText(atvPlaces.getText().toString());

			break;

		case R.id.location_add_btn:

			String selectedText = currentlocation_mapdialogedttxt.getText().toString();
			String[] text = selectedText.split(",");
			String location_name = text[0].toString();
			saveLocation(mSharedPreferences.getString(Utils.USER_ID, ""),location_name,String.valueOf(latitude),String.valueOf(longitude));

			break;
		}
	}

	private void saveLocation(String user_id,String location_name,String lat,String lang) {
		if(Utils.checkInternetConnection(getActivity())){
			SaveLocationRequestTask saveLocationRequestTask = new SaveLocationRequestTask(getActivity());
			saveLocationRequestTask.setAsyncCallListener(new AsyncCallListener() {

				@Override
				public void onResponseReceived(Object response) {
					SaveLocationModel saveLocationModel = (SaveLocationModel) response;
					if(saveLocationModel != null){

						if(saveLocationModel.status.equalsIgnoreCase("1")){
							mapDialog.dismiss();
						}else if(saveLocationModel.status.equalsIgnoreCase("0")){
							Toast.makeText(getActivity(), "no record exists", Toast.LENGTH_SHORT).show();
						} 
					}
				}

				@Override
				public void onErrorReceived(String error) {
					Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
				}
			});
			saveLocationRequestTask.execute(user_id, location_name, lat, lang);
		}else{
			Utils.showMessageDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.connection));
		}

	}

	private void openSaveDataDialog()
	{
		mapDialog = new Dialog(getActivity());
		mapDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mapDialog.setContentView(R.layout.fragment_mapdialog);
		currentlocation_mapdialogedttxt = (EditText)mapDialog.findViewById(R.id.currentlocation_mapdialogedttxt);

		mapDialog.findViewById(R.id.location_add_btn).setOnClickListener(this);
		mapDialog.getWindow().setLayout((hwidth/2)+100, (hHeight/3)+50);
		mapDialog.show();
	}

	//		@Override
	//		public void onDestroyView() {
	//			super.onDestroyView();
	//			if (googleMap != null) {
	//				getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.map)).commit();
	//				googleMap = null;
	//			}
	//		}
	
//	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//	    @Override
//	    public void onMyLocationChange(Location location) {
//	        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//	        marker = googleMap.addMarker(new MarkerOptions().position(loc));
//	        if(googleMap != null){
//	        	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//	        }
//	    }
//	};
}