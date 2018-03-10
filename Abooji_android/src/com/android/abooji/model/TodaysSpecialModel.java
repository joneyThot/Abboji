package com.android.abooji.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TodaysSpecialModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public String id;
	
	@SerializedName("user_type")
	public String user_type;
	
	@SerializedName("user_id")
	public String user_id;

	@SerializedName("offer_type")
	public String offer_type;
	
	@SerializedName("offer_ad_width")
	public String offer_ad_width;
	
	@SerializedName("start_date")
	public String start_date;
	
	@SerializedName("end_date")
	public String end_date;
	
	@SerializedName("hours")
	public String hours;
	
	@SerializedName("offer_image")
	public String offer_image;
	
	@SerializedName("default_image")
	public String default_image;
	
	@SerializedName("category_id")
	public String category_id;
	
	@SerializedName("merchant_ids")
	public String merchant_ids;
	
	@SerializedName("cc_ids")
	public String cc_ids;
	
	@SerializedName("outlet_id")
	public String outlet_id;
	
	@SerializedName("title")
	public String title;
	
	@SerializedName("offer_text")
	public String offer_text;
	
	@SerializedName("discount_amt")
	public String discount_amt;
	
	@SerializedName("display_location")
	public String display_location;
	
	@SerializedName("location")
	public String location;
	
	@SerializedName("latitude")
	public String latitude;
	
	@SerializedName("longitude")
	public String longitude;
	
	@SerializedName("description")
	public String description;
	
	@SerializedName("card")
	public String card;
	
	@SerializedName("time_slot")
	public String time_slot;
	
	@SerializedName("offer_start_date")
	public String offer_start_date;
	
	@SerializedName("offer_end_date")
	public String offer_end_date;
	
	@SerializedName("merchant_merchant_name")
	public String merchant_merchant_name;
	
	@SerializedName("merchant_employer_outlet_name")
	public String merchant_employer_outlet_name;
	
	@SerializedName("card_company_company_name")
	public String card_company_company_name;
	
	@SerializedName("cc_card_card_name")
	public String cc_card_card_name;
	
	@SerializedName("mall_mall_name")
	public String mall_mall_name;
	
	@SerializedName("mall_name_name")
	public String mall_name_name;
	
	@SerializedName("outlet_name")
	public String outlet_name;
	
	@SerializedName("promo_name")
	public String promo_name;
	
	@SerializedName("default_image_path")
	public String default_image_path;
	
	public String getDefault_image_path() {
		return default_image_path;
	}

	public void setDefault_image_path(String default_image_path) {
		this.default_image_path = default_image_path;
	}

	@SerializedName("todaysspecial")
	public ArrayList<TodaysSpecialModel> TodaysSpecialArray = new ArrayList<TodaysSpecialModel>();
	
	@SerializedName("status")
	public String status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOffer_type() {
		return offer_type;
	}

	public void setOffer_type(String offer_type) {
		this.offer_type = offer_type;
	}

	public String getOffer_ad_width() {
		return offer_ad_width;
	}

	public void setOffer_ad_width(String offer_ad_width) {
		this.offer_ad_width = offer_ad_width;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getOffer_image() {
		return offer_image;
	}

	public void setOffer_image(String offer_image) {
		this.offer_image = offer_image;
	}

	public String getDefault_image() {
		return default_image;
	}

	public void setDefault_image(String default_image) {
		this.default_image = default_image;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getMerchant_ids() {
		return merchant_ids;
	}

	public void setMerchant_ids(String merchant_ids) {
		this.merchant_ids = merchant_ids;
	}

	public String getCc_ids() {
		return cc_ids;
	}

	public void setCc_ids(String cc_ids) {
		this.cc_ids = cc_ids;
	}

	public String getOutlet_id() {
		return outlet_id;
	}

	public void setOutlet_id(String outlet_id) {
		this.outlet_id = outlet_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOffer_text() {
		return offer_text;
	}

	public void setOffer_text(String offer_text) {
		this.offer_text = offer_text;
	}

	public String getDiscount_amt() {
		return discount_amt;
	}

	public void setDiscount_amt(String discount_amt) {
		this.discount_amt = discount_amt;
	}

	public String getDisplay_location() {
		return display_location;
	}

	public void setDisplay_location(String display_location) {
		this.display_location = display_location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getTime_slot() {
		return time_slot;
	}

	public void setTime_slot(String time_slot) {
		this.time_slot = time_slot;
	}

	public String getOffer_start_date() {
		return offer_start_date;
	}

	public void setOffer_start_date(String offer_start_date) {
		this.offer_start_date = offer_start_date;
	}

	public String getOffer_end_date() {
		return offer_end_date;
	}

	public void setOffer_end_date(String offer_end_date) {
		this.offer_end_date = offer_end_date;
	}

	public String getMerchant_merchant_name() {
		return merchant_merchant_name;
	}

	public void setMerchant_merchant_name(String merchant_merchant_name) {
		this.merchant_merchant_name = merchant_merchant_name;
	}

	public String getMerchant_employer_outlet_name() {
		return merchant_employer_outlet_name;
	}

	public void setMerchant_employer_outlet_name(
			String merchant_employer_outlet_name) {
		this.merchant_employer_outlet_name = merchant_employer_outlet_name;
	}

	public String getCard_company_company_name() {
		return card_company_company_name;
	}

	public void setCard_company_company_name(String card_company_company_name) {
		this.card_company_company_name = card_company_company_name;
	}

	public String getCc_card_card_name() {
		return cc_card_card_name;
	}

	public void setCc_card_card_name(String cc_card_card_name) {
		this.cc_card_card_name = cc_card_card_name;
	}

	public String getMall_mall_name() {
		return mall_mall_name;
	}

	public void setMall_mall_name(String mall_mall_name) {
		this.mall_mall_name = mall_mall_name;
	}

	public String getMall_name_name() {
		return mall_name_name;
	}

	public void setMall_name_name(String mall_name_name) {
		this.mall_name_name = mall_name_name;
	}

	public String getOutlet_name() {
		return outlet_name;
	}

	public void setOutlet_name(String outlet_name) {
		this.outlet_name = outlet_name;
	}

	public String getPromo_name() {
		return promo_name;
	}

	public void setPromo_name(String promo_name) {
		this.promo_name = promo_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
