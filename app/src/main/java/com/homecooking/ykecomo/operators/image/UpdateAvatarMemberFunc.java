package com.homecooking.ykecomo.operators.image;

import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import java.util.ArrayList;
import java.util.Hashtable;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 28/03/15.
 */
public class UpdateAvatarMemberFunc implements Func1<Image, Observable<ApiResponse>> {

    private BaseUserProfileActivity mActivity;

    public UpdateAvatarMemberFunc (BaseUserProfileActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<ApiResponse> call(Image image) {

        Observable<ApiResponse> memberObservable = App.getRestClient()
                .getPageService()
                .updateAvatarMember(App.getMember().getId(), setMemberAvatarParams(image.getId()));
        return memberObservable;
    }

    private ArrayList<Hashtable<String, ?>> setMemberAvatarParams(int avatarID){
        ArrayList<Hashtable<String, ?>> params = new ArrayList<Hashtable<String, ?>>();
        Hashtable<String, Integer> first = new Hashtable<String, Integer>();
        first.put(Constants.AVATAR_ID, avatarID);
        params.add(first);
        return params;
    }
}
