package com.intech.player.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.intech.player.R;
import com.intech.player.clean.entities.Track;

import java.util.Collections;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} for {@link Track} entities.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class TrackListRecyclerViewAdapter extends RecyclerView.Adapter<TrackListRecyclerViewAdapter.ViewHolder> {

    private final List<Track> mTracks;
    private final ViewHolder.IViewHolderListener mListener;

    public TrackListRecyclerViewAdapter(ViewHolder.IViewHolderListener listener) {
        mListener = listener;
        mTracks = Collections.emptyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Track track = mTracks.get(position);
        holder.mView.setOnClickListener(view -> mListener.onItemClicked(track));
        holder.mArtist.setText(track.getArtist());
        holder.mTrackName.setText(track.getTrackName());
        if (holder.mArtworks == null) {
            mListener.onLoadArtworks(track.getArtworkUrl());
        } else {
            //TODO:???
            //holder.mArtworks.setImageDrawable(Drawable.createFromStream());
        }
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mArtist;
        final TextView mTrackName;
        final ImageView mArtworks;

        ViewHolder(View view) {
            super(view);
            mView = view;
            //TODO buttterknife?
            mArtist = view.findViewById(R.id.artist);
            mTrackName = view.findViewById(R.id.track_name);
            mArtworks =  view.findViewById(R.id.artworks_img);
        }

        public interface IViewHolderListener {
            void onItemClicked(Track track);
            void onLoadArtworks(String url);
        }
    }
}
