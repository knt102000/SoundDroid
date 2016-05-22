package com.example.chiutsui.sounddroid;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chiutsui.sounddroid.soundcloud.SoundCloud;
import com.example.chiutsui.sounddroid.soundcloud.SoundcloudService;
import com.example.chiutsui.sounddroid.soundcloud.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
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
    private ImageView mPlayerThumbnail;
    private TextView mPlayerTitle;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.playerToolbar);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        mPlayerThumbnail = (ImageView) findViewById(R.id.playerTrackThumbnail);
        mPlayerTitle = (TextView) findViewById(R.id.playerTrackName);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.songsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTracksRetrieved = new ArrayList<Track>();
        mAdapter = new TracksAdapter(this, mTracksRetrieved);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = mTracksRetrieved.get(position);
                mPlayerTitle.setText(selectedTrack.getmTitle());
                Picasso.with(MainActivity.this).load(selectedTrack.getAvatarURL()).into(mPlayerThumbnail);

                try {
                    mMediaPlayer.setDataSource(selectedTrack.getmStreamURL()+"?client_id="+SoundcloudService.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);


        SoundCloud.getService().searchSongs("space man", new Callback<List<Track>>() {
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
