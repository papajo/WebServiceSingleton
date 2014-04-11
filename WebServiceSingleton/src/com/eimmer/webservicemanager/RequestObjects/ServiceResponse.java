package com.eimmer.webservicemanager.RequestObjects;

public class ServiceResponse {
	private String response;
	private int errorCode;
	
	public ServiceResponse(){
		response = "";
		errorCode = 0;
	}
	
	public ServiceResponse(final int errorCode, final String response){
		this.errorCode = errorCode;
		this.response = response;
	}

	public String getReponse() {
		return response;
	}

	public void setReponse(final String reponse) {
		this.response = reponse;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}
}
