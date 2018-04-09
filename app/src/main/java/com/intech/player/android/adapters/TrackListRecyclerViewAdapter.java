package com.intech.player.android.adapters;

import android.support.v7.widget.RecyclerView;
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

import java.util.Collections;
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
        mTracks = Collections.emptyList();
        App.getAppComponent().inject(this);
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
        final TrackViewModel track = mTracks.get(position);
        holder.mView.setOnClickListener(view -> mListener.onItemClicked(track));
        holder.mArtist.setText(track.getArtist());
        holder.mTrackName.setText(track.getTrackName());
        picasso
                .load(track.getArtworkUrl())
                .placeholder(R.drawable.ic_picasso_placeholder)
                .error(R.drawable.ic_picasso_error)
                //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerInside()
                .tag(this)
                .into(holder.mArtworks);
    }

    public void addTrack(TrackViewModel track) {
        mTracks.add(track);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        @BindView(R.id.artist)
        TextView mArtist;
        @BindView(R.id.track_name)
        TextView mTrackName;
        @BindView(R.id.artworks_img)
        ImageView mArtworks;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        public interface IViewHolderListener {
            void onItemClicked(TrackViewModel track);
        }
    }
}
