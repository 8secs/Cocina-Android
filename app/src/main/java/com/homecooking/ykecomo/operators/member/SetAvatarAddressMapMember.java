package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import rx.functions.Func1;

/**
 * Created by andres on 25/3/15.
 */
public class SetAvatarAddressMapMember implements Func1<ApiResponse, Member> {

    @Override
    public Member call(ApiResponse apiResponse) {
        Member member = apiResponse.getMembers().get(0);
        Image image = apiResponse.getImages().get(0);
        if(apiResponse.getAddresses().size() > 0){
            Address address = apiResponse.getAddresses().get(0);
            member.setAddress(address);
            member.setAddressStr(App.getMemberAddressStr(member));
        }
        member.setAvatarFilename(image.getFilename());
        return member;
    }
}
