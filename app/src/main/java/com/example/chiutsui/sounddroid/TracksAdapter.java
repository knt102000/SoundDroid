package com.example.chiutsui.sounddroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chiutsui.sounddroid.soundcloud.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chiutsui on 5/15/16.
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView titleText;
        private final ImageView trackThumbnail;

        ViewHolder(View v) {
            super(v);
            titleText = (TextView) v.findViewById(R.id.trackTitle);
            trackThumbnail = (ImageView) v.findViewById(R.id.track_thumbnail);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(null,v,getAdapterPosition(),0);
            }
        }
    }

    private List<Track> mTracks;
    private Context mContext;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    TracksAdapter(Context context, List<Track> tracks) {
        mTracks=tracks;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track track = mTracks.get(position);

        holder.titleText.setText(track.getmTitle());
        Picasso.with(mContext).load(track.getAvatarURL()).into(holder.trackThumbnail);
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }
}
