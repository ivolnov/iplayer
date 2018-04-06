package com.intech.player.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.intech.player.R;
import com.intech.player.android.adapters.TrackListRecyclerViewAdapter;
import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Track;
import com.intech.player.mvp.presenters.TrackListPresenter;
import com.intech.player.mvp.views.TrackListView;

/**
 * A {@link TrackListView} implementation.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class TrackListFragment extends MvpAppCompatFragment
        implements TrackListView, TrackListRecyclerViewAdapter.ViewHolder.IViewHolderListener {

    @InjectPresenter
    TrackListPresenter mTrackListPresenter;

    public TrackListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater
                .inflate(R.layout.track_item_list, container, false);

        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new TrackListRecyclerViewAdapter(this));
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_track_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.actions_search); //TODO: butterknife

        if (searchItem != null) {
            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(getSearchTextListener());
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void addTrack(@NonNull Track track) {
        //TODO: add track to view holder and notify it
    }

    @Override
    public void addArtwork(@NonNull Artwork artwork) {
        //TODO: add track to view holder and notify it
    }

    @Override
    public void showError(@NonNull String message) {
        //TODO: show a toast?
    }

    @Override
    public void onItemClicked(Track track) {
        //TODO: start player activity
    }

    @Override
    public void onLoadArtworks(String url) {
        mTrackListPresenter.loadArtworks(url);
    }

    private SearchView.OnQueryTextListener getSearchTextListener() {
        return  new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mTrackListPresenter.onEnterQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //mTrackListPresenter.onEnterQuery(query);
                return false;
            }
        };
    }
}
