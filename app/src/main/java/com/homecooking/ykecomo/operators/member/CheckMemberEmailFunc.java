package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.rest.model.ApiResponse;

import java.util.ArrayList;
import java.util.Hashtable;

import rx.functions.Func1;


public class CheckMemberEmailFunc implements Func1<ApiResponse, ApiResponse> {

    private ArrayList<Hashtable<String, String>> mParams;
    private String mMessage;

    public CheckMemberEmailFunc(ArrayList<Hashtable<String, String>> params, String message){
        this.mParams = params;
        this.mMessage = message;
    }

    @Override
    public ApiResponse call(ApiResponse apiResponse) {
        ApiResponse response = new ApiResponse();
        if (apiResponse.getMembers().size() > 0) {
            response.setMessage(this.mMessage);
        }else{
            response.setCreateMember(this.mParams);
        }
        return response;
    }
}
