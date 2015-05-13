package com.homecooking.ykecomo.rest.service;


import com.homecooking.ykecomo.model.Auth;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import rx.Observable;


/**
 * Created by andres on 19/2/15.
 */
public interface ApiService {

    @GET("/api/Page")
    Observable<ApiResponse> getMenu(@Query("ParentID") String parentID, @Query("ShowInMenus") String showInMenus);

    @GET("/api/ProductCategory?ClassName=ProductCategory&ParentID__GreaterThan=0")
    Observable<ApiResponse> getCategories();

    @GET("/api/Product")
    Observable<ApiResponse> getProductsCategory(@Query("ParentID") String parentID);

    @GET("/api/Group")
    Observable<ApiResponse> getChefGroup(@Query("ID") int ID);

    @GET("/api/auth/login")
    Observable<Auth> loginUser(@Query("email") String email, @Query("pwd") String pwd);

    @GET("/api/Member")
    Observable<ApiResponse> getMember(@Query("ID") String id);

    @GET("/api/Member")
    Observable<ApiResponse> getFbMember(@Query("FacebookUID") String id);

    @GET("/api/Member")
    Observable<ApiResponse> getMemberByEmail(@Query("Email") String email);

    @POST("/api/Member")
    Observable<ApiResponse> createMember(@Body List<Hashtable<String, String>> params);

    @PUT("/api/Member/{ID}")
    Observable<ApiResponse> updateMember(@Path("ID") int ID, @Body List<Map<String, Object>> params);

    @PUT("/api/Member/{ID}")
    Observable<ApiResponse> updateAvatarMember(@Path("ID") int ID, @Body List<Hashtable<String, ?>> params);

    @PUT("/api/Member/{FacebookUID}")
    Observable<ApiResponse> updateAvatarMemberFb(@Path("FacebookUID") String FacebookUID, @Body List<Hashtable<String, ?>> params);

    @POST("/api/Address")
    Observable<ApiResponse> createAddress(@Body List<Map<String, Object>> params);

    @GET("/api/Address")
    Observable<ApiResponse> getMemberAddress(@Query("MemberID") String memberID);

    @PUT("/api/Address/{ID}")
    Observable<ApiResponse> updateAddress(@Path("ID") int ID, @Body List<Map<String, Object>> params);

    @Multipart
    @POST("/home/Upload")
    Observable<Integer> uploadImage(@Part("Files") TypedFile photo);
    
    @GET("/api/Product")
    Observable<ApiResponse> getProductDetail(@Query("ID") int id);

    @GET("/api/Image")
    Observable<ApiResponse> getImage(@Query("ID") int id);

    @POST("/api/Image")
    Observable<ApiResponse> createImage(@Body List<Hashtable<String, String>> params);
    
}
