package com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.api;

import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.model.Resultado;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoService {
    /*
    * https://www.googleapis.com/youtube/v3/
    * search
    * ?part=snippet
    * &order=date
    * &maxResult=20
    * &q=alanzoca
    * &key=AIzaSyB4a8iuFR3LOuqVbfm7edH9kBKyNX4eoBQ
    *
    * https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&order=date&q=alanzoka&key=AIzaSyB4a8iuFR3LOuqVbfm7edH9kBKyNX4eoBQ'
    * */
    @GET("search")
    Call<Resultado> getVideos(
            @Query("part") String part,
            @Query("maxResult") String maxResult,
            @Query("order") String order,
            @Query("q") String q,
            @Query("key") String key

    );
}
