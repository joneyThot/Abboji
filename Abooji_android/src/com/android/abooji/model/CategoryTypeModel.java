package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CategoryTypeModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public String id = "";

	@SerializedName("category_name")
	public String category_name = "";

	@SerializedName("description")
	public String description = "";
	
	@SerializedName("category_icon")
	public String category_icon = "";
	
	@SerializedName("status")
	public String status = "";
	
	public boolean isChecked = false;

	public CategoryTypeModel(String id, String category_name,
			String description, String category_icon, String status,
			boolean isChecked) {
		super();
		this.id = id;
		this.category_name = category_name;
		this.description = description;
		this.category_icon = category_icon;
		this.status = status;
		this.isChecked = isChecked;
	}
	
	

}
