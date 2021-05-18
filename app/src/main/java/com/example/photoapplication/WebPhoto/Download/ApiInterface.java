package com.example.photoapplication.WebPhoto.Download;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    String BASE_URL="http://www.splashbase.co/api/v1/images/";

    @GET("random?image_only=true")
    Call<DATA_MODEL> getImageJson();
}
