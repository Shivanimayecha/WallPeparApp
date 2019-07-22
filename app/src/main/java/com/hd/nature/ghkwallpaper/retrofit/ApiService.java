package com.hd.nature.ghkwallpaper.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    //for get category
    @GET("wallpaper/category.php?ccall=display")
    Call<String> getAllCategories();

    //for wallpepar category id
    @FormUrlEncoded
    @POST("wallpaper/wallpaper.php?wcall=id")
    Call<String> getAllWallpepars(@Field("category_id") int category_id);

    @GET("android_app/App_list/api/getusers")
    Call<String>getAllApps();
}
