package com.intech.player.android.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.intech.player.App;
import com.intech.player.R;
import com.intech.player.clean.boundaries.model.TrackRequestModel;
import com.intech.player.mvp.models.TrackViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link RecyclerView.Adapter} for {@link TrackRequestModel} entities.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class TrackListRecyclerViewAdapter extends RecyclerView.Adapter<TrackListRecyclerViewAdapter.ViewHolder> {

    @Inject
    Picasso picasso;

    private final List<TrackViewModel> mTracks;
    private final ViewHolder.IViewHolderListener mListener;

    public TrackListRecyclerViewAdapter(ViewHolder.IViewHolderListener listener) {
        mListener = listener;
        mTracks = new ArrayList<>();
        App.getAppComponent().inject(this);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TrackViewModel track = mTracks.get(position);
        holder.view.setOnClickListener(view -> mListener.onItemClicked(track));
        holder.artist.setText(track.getArtist());
        holder.trackName.setText(track.getTrackName());
        picasso
                .load(track.getArtworkUrl())
                .placeholder(R.drawable.ic_picasso_placeholder)
                .error(R.drawable.ic_picasso_error)
                .fit()
                .centerCrop(Gravity.FILL)
                .tag(this)
                .into(holder.artworks);
    }

    public void addTrack(TrackViewModel track) {
        mTracks.add(track);
        notifyDataSetChanged();
    }

    public void clear() {
        mTracks.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.artist)
        TextView artist;
        @BindView(R.id.track_name)
        TextView trackName;
        @BindView(R.id.artworks_img)
        ImageView artworks;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        public interface IViewHolderListener {
            void onItemClicked(TrackViewModel track);
        }
    }
}
