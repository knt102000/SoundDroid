package com.example.chiutsui.sounddroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.chiutsui.sounddroid.soundcloud.SoundCloud;
import com.example.chiutsui.sounddroid.soundcloud.SoundcloudService;
import com.example.chiutsui.sounddroid.soundcloud.Track;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<Track> mTracksRetrieved = null;

    private TracksAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.songsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTracksRetrieved = new ArrayList<Track>();
        mAdapter = new TracksAdapter(this, mTracksRetrieved);
        recyclerView.setAdapter(mAdapter);


        SoundCloud.getService().searchSongs("dark horse", new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                Log.d(TAG,"Length is "+tracks.size());
                mTracksRetrieved.clear();
                mTracksRetrieved.addAll(tracks);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }
}
