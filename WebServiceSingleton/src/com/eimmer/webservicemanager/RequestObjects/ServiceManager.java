package com.eimmer.webservicemanager.RequestObjects;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;

public class ServiceManager {
	private static ServiceManager instance;
	
	public static ServiceManager getInstance(){
		if(instance == null){
			instance = new ServiceManager();
		}
		return instance;
	}
	
	private AtomicBoolean isServiceBusy;
	private UUID currentService;
	private HashMap<UUID, WebRequest> requestMap;
	private HashMap<UUID, WeakReference<WebRequestListener>> listenerMap;
	
	private ServiceManager(){
		this.requestMap = new HashMap<UUID, WebRequest>();
		this.isServiceBusy = new AtomicBoolean(false);
	}

	public UUID addRequest(final WebRequest request, final WebRequestListener listener){
		final UUID serviceId = UUID.randomUUID();
		
		requestMap.put(serviceId, request);
		addRequestListener(serviceId, listener);
		makeServiceCall(request, serviceId);
		
		return serviceId;
	}
	
	private void makeServiceCall(final WebRequest webRequest, final UUID serviceId){
		if(isServiceBusy.compareAndSet(false, true)){
			currentService = serviceId;
			final ServiceCatalyst cat = new ServiceCatalyst(webRequest);
			cat.execute();
		}
	}
	
	private void processResults(final ServiceResponse results){
		final WeakReference<WebRequestListener> weakListener = listenerMap.remove(currentService);;
		if(weakListener.get() != null){
			final WebRequestListener listener = weakListener.get();
			listener.handleWebRequestResponse(results);
		}
		
		currentService = null;
		isServiceBusy.set(false);

		//TODO: Check for next request.
	}
	
	private void addRequestListener(final UUID requestId, final WebRequestListener listener){
		final WeakReference<WebRequestListener> weakListener = new WeakReference<WebRequestListener>(listener);
		listenerMap.put(requestId, weakListener);
	}
	
	private class ServiceCatalyst extends AsyncTask<Void, Void, ServiceResponse>{
		private WebRequest webRequest;
		
		public ServiceCatalyst(WebRequest webRequest){
			this.webRequest = webRequest;
		}

		@Override
		protected ServiceResponse doInBackground(Void... params) {
			final ServiceCall call = new ServiceCall(webRequest);
			ServiceResponse response;
			
			try {
				response = call.callService();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				response = new ServiceResponse(1, "");
			} catch (IOException e) {
				e.printStackTrace();
				response = new ServiceResponse(1, "");
			}
			
			return response;
		}
		
		@Override
		protected void onPostExecute(final ServiceResponse response){
			processResults(response);
		}
	}
}
