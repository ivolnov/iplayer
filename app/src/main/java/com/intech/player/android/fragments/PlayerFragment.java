package com.intech.player.android.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.intech.player.App;
import com.intech.player.R;
import com.intech.player.android.services.PlayerBoundForegroundService;
import com.intech.player.mvp.models.TrackViewModel;
import com.intech.player.mvp.presenters.PlayerPresenter;
import com.intech.player.mvp.views.PlayerView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intech.player.android.fragments.TrackListFragment.EXTRA_SELECTED_TRACK;

/**
 * A {@link PlayerView} implementation.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerFragment extends MvpAppCompatFragment implements PlayerView {

    public static final String EXTRA_TRACK = "com.intech.player.EXTRA_TRACK";

    @InjectPresenter
    PlayerPresenter playerPresenter;

    @BindView(R.id.player_button)
    FloatingActionButton button;
    @BindView(R.id.player_surface)
    SurfaceView surface;
    @BindView(R.id.player_artwork)
    ImageView artwork;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    Picasso picasso;

    private CoordinatorLayout mCoordinatorLayout;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            final PlayerBoundForegroundService.SurfaceConsumer surfaceConsumer
                    = (PlayerBoundForegroundService.SurfaceConsumer) iBinder;
            if (playerPresenter.isVideo()) {
                surface.setVisibility(View.VISIBLE);
                artwork.setVisibility(View.GONE);
                surfaceConsumer.setSurfaceView(surface);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public PlayerFragment() {
        App.getAppComponent().inject(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            final TrackViewModel track = getActivity()
                    .getIntent()
                    .getParcelableExtra(EXTRA_SELECTED_TRACK);
            playerPresenter.setTrack(track);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mCoordinatorLayout = (CoordinatorLayout) inflater
                .inflate(R.layout.fragment_player, container, false);

        ButterKnife.bind(this, mCoordinatorLayout);

        if (playerPresenter.hasTrack()) {
            picasso
                    .load(playerPresenter.getTrack().getArtworkUrl())
                    .error(R.drawable.ic_picasso_error)
                    .tag(this)
                    .fit()
                    .centerCrop()
                    .into(artwork);
        }

        return mCoordinatorLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() == null) {
            return;
        }

        if (playerPresenter.hasTrack()) {
            final Intent intent = new Intent(getActivity(), PlayerBoundForegroundService.class)
                    .putExtra(EXTRA_TRACK, playerPresenter.getTrack());
            getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        button.setOnClickListener(view -> playerPresenter.buttonCLicked());
        playerPresenter.listenToPlayer(true);
    }


    @Override
    public void onPause() {
        super.onPause();
        button.setOnClickListener(null);
        playerPresenter.listenToPlayer(false);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            getActivity().unbindService(mConnection);
        }
    }

    @Override
    public void showError(String error) {
        Snackbar
                .make(mCoordinatorLayout, error, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void setButtonPlay() {
        if (getContext() != null) {
            button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
        }

    }

    @Override
    public void setButtonPause() {
        if (getContext() != null) {
            button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause));
        }
    }

    @Override
    public void setProgress(double progress) {
        progressBar.setProgress((int)(progress * progressBar.getMax()));
    }
}
