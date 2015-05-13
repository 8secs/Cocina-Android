package com.homecooking.ykecomo.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homecooking.ykecomo.rest.service.ApiService;
import com.homecooking.ykecomo.rest.typeAdapterFactory.ItemTypeAdapterFactory;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by andres on 19/2/15.
 */
public class RestClient {

    private static final String BASE_URL = "http://cocina.visitaelaljarafe.com/";
    private ApiService pageService;

    public RestClient(){}

    public ApiService getPageService(){

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(ApiService.class);
    }
}
