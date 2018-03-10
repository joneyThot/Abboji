package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class LoginModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@SerializedName("user_id")
	public String user_id = "";

	@SerializedName("email")
	public String email = "";

	@SerializedName("password")
	public String password = "";

	@SerializedName("company_name")
	public String company_name = "";

	@SerializedName("comp_reg_number")
	public String comp_reg_number = "";

	@SerializedName("company_type")
	public String company_type = "";

	@SerializedName("country_comp_reg_in")
	public String country_comp_reg_in = "";
	
	@SerializedName("new_password")
	public String new_password; 

	@SerializedName("status")
	public String status = "";
	

}
