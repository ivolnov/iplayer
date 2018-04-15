package com.intech.player.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.intech.player.R;
import com.intech.player.android.activities.PlayerActivity;
import com.intech.player.android.adapters.TrackListRecyclerViewAdapter;
import com.intech.player.mvp.models.TrackViewModel;
import com.intech.player.mvp.presenters.TrackListPresenter;
import com.intech.player.mvp.views.TrackListView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intech.player.android.utils.AndroidUtils.okIcon;

/**
 * A {@link TrackListView} implementation.
 * Delegates search queries to its presenter and shows an image placeholder instead of a list
 * when told so.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class TrackListFragment extends MvpAppCompatFragment
        implements TrackListView, TrackListRecyclerViewAdapter.ViewHolder.IViewHolderListener {

    public static final String EXTRA_SELECTED_TRACK = "com.intech.player.EXTRA_SELECTED_TRACK";

    @InjectPresenter
    TrackListPresenter trackListPresenter;

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.placeholder)
    ImageView placeholder;

    private SearchView mSearchView;
    private CoordinatorLayout mCoordinatorLayout;
    private TrackListRecyclerViewAdapter mAdapter;

    public TrackListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mCoordinatorLayout = (CoordinatorLayout) inflater
                .inflate(R.layout.track_item_list, container, false);

        ButterKnife.bind(this, mCoordinatorLayout);

        mAdapter = new TrackListRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        return mCoordinatorLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_track_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.actions_search);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
            mSearchView.setOnQueryTextListener(getSearchTextListener());
            trackListPresenter.onGetLastQuery();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void applySearchQuery(String query) {
        if (mSearchView.getQuery().equals(query)) {
            return;
        }
        mSearchView.setQuery(query, true);
    }

    @Override
    public void addTrack(@NonNull TrackViewModel track) {
        mAdapter.addTrack(track);
    }

    @Override
    public void showError(@NonNull String message) {
        Snackbar
                .make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showSevereError(String message) {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.severe_error_title))
                    .setMessage(message)
                    .setPositiveButtonIcon(okIcon(getActivity()))
                    .setPositiveButton(null, null)
                    .show();
        }
    }

    @Override
    public void onItemClicked(TrackViewModel track) {
        final Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(EXTRA_SELECTED_TRACK, track);
        startActivity(intent);
    }

    private SearchView.OnQueryTextListener getSearchTextListener() {
        return  new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.clear();
                trackListPresenter.onEnterQuery(query);
                return false;
            }
        };
    }

    public void showPlaceholder() {
        if (placeholder.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.GONE);
            placeholder.setVisibility(View.VISIBLE);
        }
    }

    public  void showList() {
        if (recyclerView.getVisibility() == View.GONE) {
            placeholder.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
