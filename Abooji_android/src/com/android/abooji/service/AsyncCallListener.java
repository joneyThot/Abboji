package com.android.abooji.service;

public interface AsyncCallListener {

	public void onResponseReceived(Object response);

	public void onErrorReceived(String error);
}
