package com.eimmer.webservicemanager.RequestObjects;

public class WebRequest {
	public enum RequestMethod{
		POST,
		GET;
	}
	
	private String url;
	private RequestMethod requestType;

	public WebRequest() {
		this.url = "";
		this.requestType = RequestMethod.GET;
	}

	public WebRequest(final String url) {
		this.url = url;
		this.requestType = RequestMethod.GET;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}
}
