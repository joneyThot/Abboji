package com.android.abooji.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.abooji.R;
import com.android.abooji.model.SaveLocationModel;
import com.android.abooji.service.AsyncCallListener;
import com.android.abooji.service.SaveLocationRequestTask;
import com.android.abooji.service.Utils;
import com.android.abooji.view.MapCustomDialog;
import com.android.abooji.view.TextViewPlus;

public class LocationFragment extends Fragment implements OnClickListener
{
	Fragment fr;
	View mRootView;
	int hwidth,hHeight;
//	AutoCompleteTextView atvPlaces;
//	DownloadTask placesDownloadTask;
//	DownloadTask placeDetailsDownloadTask;
//	ParserTask placesParserTask;
//	ParserTask placeDetailsParserTask;
//	GoogleMap googleMap;
//	final int PLACES = 0;
//	final int PLACES_DETAILS = 1;
//	Marker marker;
//	ArrayList<Marker> marraylst_marker = new ArrayList<Marker>();
//	LatLng point;
//	Button ok, cancel;
//	double latitude = 0.0, longitude = 0.0;
//	Dialog mapDialog;
	EditText mLocationName, mLocationLatLong;
	private SharedPreferences mSharedPreferences;
	/*SupportMapFragment myMAPF;*/
	ToggleButton mYes_No,mMap_Gps;
	private String gpsStatus = "disable";
	LocationManager manager;
	TextViewPlus from_gps, from_map;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{

		mRootView = inflater.inflate(R.layout.activity_location, container, false);
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 

		hwidth = display.getWidth();  // deprecated
		hHeight = display.getHeight(); 
		initview();

		return mRootView;
	}
	private void initview() 
	{
		mSharedPreferences = getActivity().getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
		mRootView.findViewById(R.id.activity_location_prev_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_location_next_btn).setOnClickListener(this);
		mRootView.findViewById(R.id.activity_location_save_btn).setVisibility(View.GONE);

		from_map = (TextViewPlus) mRootView.findViewById(R.id.activity_location_open_map_btn);
		from_map.setOnClickListener(this);

		from_gps = (TextViewPlus) mRootView.findViewById(R.id.activity_location_from_gps_btn);
		from_gps.setOnClickListener(this);

		mYes_No = (ToggleButton) mRootView.findViewById(R.id.location_toggle_yes_no);
		mYes_No.setOnClickListener(this);

		mMap_Gps = (ToggleButton) mRootView.findViewById(R.id.location_toggle_gps_map);
		mMap_Gps.setOnClickListener(this);

		mLocationName = (EditText) mRootView.findViewById(R.id.activity_name_of_location_edittxt);
		mLocationLatLong = (EditText) mRootView.findViewById(R.id.activity_location_edittxt);
		
		mRootView.findViewById(R.id.activity_location_add_btn).setOnClickListener(this);

	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_location_prev_btn:
			fr  = new CategoryFragment();
			FragmentReplaceFunction();
			break;

		case R.id.activity_location_next_btn:
			fr = new NotificationFragment();
			FragmentReplaceFunction();
			break;

		case R.id.activity_location_open_map_btn:
			//			openMapDialog();
//			final Dialog mapDialog = new Dialog(getActivity());
//			mapDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			MapCustomDialog fragment1 = new MapCustomDialog(this);     
			fragment1.show(getFragmentManager(), "");  
//						
//			FragmentManager fm = getActivity().getSupportFragmentManager();
//			CustomeDialog mapDialogFragment = new CustomeDialog();
//			mapDialogFragment.show(fm, "jack");
			
//			FragmentManager fm = getFragmentManager();
//			MapCustomDialog mapDialogFragment = new MapCustomDialog();
//			mapDialogFragment.show(fm, "jack");
					
			break;

//		case R.id.add_btn:
//			break;
//
//		case R.id.close_btn:
//			mapDialog.dismiss();

//			break;	

		case R.id.location_toggle_yes_no:
			if(mYes_No.isChecked()){
				turnGPSOn();
				mMap_Gps.setChecked(true);
				//				from_gps.setClickable(true);
				//				from_map.setClickable(false);
			} else {
				turnGPSOff();
				mMap_Gps.setChecked(false);
				//				from_map.setClickable(true);
				//				from_gps.setClickable(false);
			}
			break;

		case R.id.location_toggle_gps_map:
			if(mMap_Gps.isChecked()){
				mYes_No.setChecked(true);
				//				from_gps.setClickable(true);
				//				from_map.setClickable(false);
			} else {
				mYes_No.setChecked(false);
				//				from_map.setClickable(true);
				//				from_gps.setClickable(false);
			}
			break;

		case R.id.activity_location_from_gps_btn:
			break;
			
		case R.id.activity_location_add_btn:
			String location_name = mLocationName.getText().toString();
			String[] location_text = location_name.split(",");
			String location = location_text[0].toString();
			
			String latlang = mLocationLatLong.getText().toString();
			String[] latlang_text = latlang.split(",");
			String latitude = latlang_text[0];
			String longitude = latlang_text[1];
			
			saveLocation(mSharedPreferences.getString(Utils.USER_ID, ""),location,String.valueOf(latitude),String.valueOf(longitude));
			break;
		}
	}
/*	private void openMapDialog() {
		final Dialog mapDialog = new Dialog(getActivity());
		mapDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mapDialog.setContentView(R.layout.fragment_current_location);
		mapDialog.findViewById(R.id.rl1).setVisibility(View.VISIBLE);
		mapDialog.findViewById(R.id.current_location_add_btn).setVisibility(View.GONE);
		mapDialog.findViewById(R.id.add_btn).setOnClickListener(this);
		mapDialog.findViewById(R.id.close_btn).setOnClickListener(this);
		atvPlaces = (AutoCompleteTextView) mapDialog.findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);

		if(googleMap == null){
			FragmentManager myFM = getActivity().getSupportFragmentManager();
			final SupportMapFragment myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);
			googleMap = myMAPF.getMap();
			googleMap.clear();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		} 

		mapinit();
		mapDialog.getWindow().setLayout((hwidth/2)+200, (hHeight/2)+100);
		mapDialog.show();

		//		close.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				mapDialog.dismiss();
		//			}
		//		});

	}*/

	/*private void mapinit() {

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

				HashMap<String, String> hm = (HashMap<String, String>) adapter
						.getItem(index);
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
					PlaceJSONParser placeJsonParser = new PlaceJSONParser();
					list = placeJsonParser.parse(jObject);
					break;
				case PLACES_DETAILS:
					PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
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

				FragmentManager myFM = getActivity().getSupportFragmentManager();
				final SupportMapFragment myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);
				googleMap = myMAPF.getMap();

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
				mLocationName.setText(atvPlaces.getText().toString());
				mLocationLatLong.setText(""+ latitude + "," + longitude);
				break;
			}
		}
	}*/

	private void FragmentReplaceFunction()
	{
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_place, fr);
		fragmentTransaction.commit();
	}

//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//		if (googleMap != null) {
//			getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.map)).commit();
//			googleMap = null;
//		}
//	}

	public void turnGPSOn()
	{
		/*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);*/
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		getActivity().sendBroadcast(intent);

		String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if(!provider.contains("gps"))
		{ //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3")); 
			getActivity().sendBroadcast(poke);
		}
	}

	public void turnGPSOff()
	{
		/*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);*/
		String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if(provider.contains("gps"))
		{ //if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3")); 
			getActivity().sendBroadcast(poke);
		}
	}

	private void CheckStatus()
	{
		manager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE );
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
		{
			gpsStatus = "disable";
			Toast.makeText(getActivity(), "GPS is disabled!", Toast.LENGTH_LONG).show();
		}
		else
		{
			gpsStatus = "enable";
			Toast.makeText(getActivity(), "GPS is enabled!", Toast.LENGTH_LONG).show();
		}
	}

	
	public void setLatLang(String l_name,String latlang){
		mLocationName.setText(l_name);
		mLocationLatLong.setText(latlang);
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
							Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
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

}