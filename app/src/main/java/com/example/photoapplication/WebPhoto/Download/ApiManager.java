package com.example.photoapplication.WebPhoto.Download;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    Call<DATA_MODEL> call;
    public interface ResponseCallBack {
        void onResponse(Call<DATA_MODEL> call, Response<DATA_MODEL> response);

        void onFailure(Call<DATA_MODEL> call, Throwable t);
    }

    ResponseCallBack responseCallBack;

    public ApiManager(ResponseCallBack responseCallBack) {
        this.responseCallBack = responseCallBack;
    }

    public void start() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiInterface.BASE_URL).build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        call = apiInterface.getImageJson();
        call.enqueue(new Callback<DATA_MODEL>() {
            @Override
            public void onResponse(Call<DATA_MODEL> call, Response<DATA_MODEL> response) {
                if(!response.isSuccessful()){
                    Log.d("002TAG", "onResponse: "+response.message());
                }
                responseCallBack.onResponse(call, response);

            }

            @Override
            public void onFailure(Call<DATA_MODEL> call, Throwable t) {
                responseCallBack.onFailure(call, t);
            }
        });

    }

    public void cancel(){
        if(call!=null){
            call.cancel();
        }
    }
}
