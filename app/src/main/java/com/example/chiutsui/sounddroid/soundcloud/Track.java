package com.example.chiutsui.sounddroid.soundcloud;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chiutsui on 5/11/16.
 */
public class Track {
    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    //Getters and Setters
    public int getmId() {return mId;}

    public String getmTitle() {
        return mTitle;
    }

    public String getmStreamURL() {
        return mStreamURL;
    }

    public String getmArtworkURL() {return mArtworkURL;}

    public String getAvatarURL() {
        String avatarUrl = mArtworkURL;
        if (avatarUrl != null) {
            avatarUrl=avatarUrl.replace("large", "tiny");
        }
        return avatarUrl;
    }
}
