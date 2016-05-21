package com.example.chiutsui.sounddroid.soundcloud;

import retrofit.RestAdapter;

/**
 * Created by chiutsui on 5/12/16.
 */
public class SoundCloud {
    private static final String API_URL = "http://api.soundcloud.com";

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .build();

    private static final SoundcloudService service = restAdapter.create(SoundcloudService.class);

    public static SoundcloudService getService() {
        return service;
    }
}
