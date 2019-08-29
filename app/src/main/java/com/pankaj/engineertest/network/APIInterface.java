package com.pankaj.engineertest.network;

import com.pankaj.engineertest.model.DataModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("search_by_date?tags=story")
    @Headers({"Accept: application/json"})
    Call<DataModel> getDataList(@Query("page") int page);

}
