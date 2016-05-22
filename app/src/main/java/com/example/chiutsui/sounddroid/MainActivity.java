package com.example.chiutsui.sounddroid;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chiutsui.sounddroid.soundcloud.SoundCloud;
import com.example.chiutsui.sounddroid.soundcloud.SoundcloudService;
import com.example.chiutsui.sounddroid.soundcloud.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String TAG = "MainActivity";

    private List<Track> mTracksRetrieved = null;

    private List<Track> mPreviousTracks = null;

    private TracksAdapter mAdapter;
    private ImageView mPlayerThumbnail;
    private TextView mPlayerTitle;

    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerState;
    private SearchView mSearchView;

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
                toggleSongState();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlayerState.setImageResource(R.drawable.ic_play);
            }
        });

        mPlayerThumbnail = (ImageView) findViewById(R.id.playerTrackThumbnail);
        mPlayerTitle = (TextView) findViewById(R.id.playerTrackName);

        mPlayerState = (ImageView) findViewById(R.id.playerState);

        mPlayerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSongState();
            }
        });

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

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer.reset();

                try {
                    mMediaPlayer.setDataSource(selectedTrack.getmStreamURL()+"?client_id="+SoundcloudService.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        SoundCloud.getService().mostRecentSongs(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();

        SoundCloud.getService().searchSongs(query, new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        return true;
    }

    private void updateTracks(List<Track> tracks) {
        mTracksRetrieved.clear();
        mTracksRetrieved.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void toggleSongState() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerState.setImageResource(R.drawable.ic_play);
        } else
        {
            mMediaPlayer.start();
            mPlayerState.setImageResource(R.drawable.ic_pause);;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        mSearchView = (SearchView) menu.findItem(R.id.search_view).getActionView();

        mSearchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search_view), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mPreviousTracks=new ArrayList<Track>(mTracksRetrieved);

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                updateTracks(mPreviousTracks);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_view) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
