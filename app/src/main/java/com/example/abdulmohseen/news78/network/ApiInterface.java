package com.example.abdulmohseen.news78.network;

import com.example.abdulmohseen.news78.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
** Uses the URL Endpoint and other queries to complete the call.
**/
public interface ApiInterface {


    //New Endpoint to fetch headlines.
    @GET("top-headlines")
    Call<NewsResponse> getHeadlines(@Query("sources") String sources,
                                    @Query("pageSize") int pageSize,
                                    @Query("apiKey") String apiKey);


}
