package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CardDetailsModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public String id;
	
	@SerializedName("card_id")
	public String card_id;
	
	@SerializedName("card_name")
	public String card_name;
	
	@SerializedName("card_type_id")
	public String card_type_id;
	
	@SerializedName("card_category_id")
	public String card_category_id;
	
	@SerializedName("card_image")
	public String card_image;
	
	@SerializedName("target_market")
	public String target_market;
	
	@SerializedName("contact_name")
	public String contact_name;
	
	@SerializedName("contact_email")
	public String contact_email;
	
	@SerializedName("password")
	public String password;
	
	@SerializedName("firstname")
	public String firstname;
	
	@SerializedName("lastname")
	public String lastname;
	
	@SerializedName("mobile")
	public String mobile;
	
	@SerializedName("title")
	public String title;
	
	@SerializedName("contact_number")
	public String contact_number;

	@SerializedName("privilege1")
	public String privilege1;
	
	@SerializedName("value1")
	public String value1;
	
	@SerializedName("privilege2")
	public String privilege2;
	
	@SerializedName("value2")
	public String value2;
	
	@SerializedName("privilege3")
	public String privilege3;
	
	@SerializedName("value3")
	public String value3;
	
	@SerializedName("credit_used")
	public String credit_used;
	
	@SerializedName("basic")
	public String basic;
	
	@SerializedName("premium")
	public String premium;
	
	@SerializedName("available_credits")
	public String available_credits;
	
	@SerializedName("card_type_title")
	public String card_type_title;
	
	@SerializedName("card_category_title")
	public String card_category_title;
	
	@SerializedName("organisation_name")
	public String organisation_name;
	
	@SerializedName("default_image_path")
	public String default_image_path;
	
	
	public String getDefault_image_path() {
		return default_image_path;
	}

	public void setDefault_image_path(String default_image_path) {
		this.default_image_path = default_image_path;
	}

	@SerializedName("status")
	public String status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_type_id() {
		return card_type_id;
	}

	public void setCard_type_id(String card_type_id) {
		this.card_type_id = card_type_id;
	}

	public String getCard_category_id() {
		return card_category_id;
	}

	public void setCard_category_id(String card_category_id) {
		this.card_category_id = card_category_id;
	}

	public String getCard_image() {
		return card_image;
	}

	public void setCard_image(String card_image) {
		this.card_image = card_image;
	}

	public String getTarget_market() {
		return target_market;
	}

	public void setTarget_market(String target_market) {
		this.target_market = target_market;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}
	
	public String getPrivilege1() {
		return privilege1;
	}

	public void setPrivilege1(String privilege1) {
		this.privilege1 = privilege1;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getPrivilege2() {
		return privilege2;
	}

	public void setPrivilege2(String privilege2) {
		this.privilege2 = privilege2;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getPrivilege3() {
		return privilege3;
	}

	public void setPrivilege3(String privilege3) {
		this.privilege3 = privilege3;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getCredit_used() {
		return credit_used;
	}

	public void setCredit_used(String credit_used) {
		this.credit_used = credit_used;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getAvailable_credits() {
		return available_credits;
	}

	public void setAvailable_credits(String available_credits) {
		this.available_credits = available_credits;
	}

	public String getCard_type_title() {
		return card_type_title;
	}

	public void setCard_type_title(String card_type_title) {
		this.card_type_title = card_type_title;
	}

	public String getCard_category_title() {
		return card_category_title;
	}

	public void setCard_category_title(String card_category_title) {
		this.card_category_title = card_category_title;
	}

	public String getOrganisation_name() {
		return organisation_name;
	}

	public void setOrganisation_name(String organisation_name) {
		this.organisation_name = organisation_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
