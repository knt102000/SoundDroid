package com.example.chiutsui.sounddroid.soundcloud;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by chiutsui on 5/12/16.
 */
public interface SoundcloudService {

    static final String CLIENT_ID="87012262b10d7bbb14bc0f1251523f82";

    @GET("/tracks?client_id="+CLIENT_ID)
    public void searchSongs(@Query("q") String query, Callback<List<Track>> cb);
}
