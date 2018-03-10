package com.android.abooji.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class SaveShoppingListModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("user_id")
	public String user_id = "";

	@SerializedName("list_name")
	public String list_name;

	@SerializedName("list")
	public String list;

	@SerializedName("list_id")
	public String list_id;

	@SerializedName("id_of_user")
	public String id_of_user;
	
	@SerializedName("share_ids")
	public String share_ids;
	
//	public String getUser_id() {
//		return user_id;
//	}
//
//	public void setUser_id(String user_id) {
//		this.user_id = user_id;
//	}

	public String getList_name() {
		return list_name;
	}

	public void setList_name(String list_name) {
		this.list_name = list_name;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getList_id() {
		return list_id;
	}

	public void setList_id(String list_id) {
		this.list_id = list_id;
	}

	public String getId_of_user() {
		return id_of_user;
	}

	public void setId_of_user(String id_of_user) {
		this.id_of_user = id_of_user;
	}

	public String getShare_ids() {
		return share_ids;
	}

	public void setShare_ids(String share_ids) {
		this.share_ids = share_ids;
	}

	@SerializedName("status")
	public String status;

}
