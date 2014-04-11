package com.eimmer.webservicemanager.RequestObjects;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

public class ServiceCall {

	private WebRequest webRequest;
	
	public ServiceCall(WebRequest webRequest){
		this.webRequest = webRequest;
	}
	
	public ServiceResponse callService()
			throws ClientProtocolException, IOException {
		final ServiceResponse webResponse = new ServiceResponse();

		final URL url = new URL(webRequest.getUrl());
		final HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		try {
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			webResponse.setReponse(convertStreamToString(in));
		} catch(IOException e){
			webResponse.setErrorCode(1);
		}finally {
			urlConnection.disconnect();
		}

		return webResponse;
	}

	private static String convertStreamToString(InputStream is) throws IOException {
		final Scanner s = new Scanner(is).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : "";
		s.close();
		is.close();

		return response;
	}
}
