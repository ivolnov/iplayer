package com.intech.player.android.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.intech.player.R;
import com.intech.player.android.services.PlayerBoundForegroundService;
import com.intech.player.mvp.models.TrackViewModel;
import com.intech.player.mvp.presenters.PlayerPresenter;
import com.intech.player.mvp.views.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intech.player.android.fragments.TrackListFragment.EXTRA_SELECTED_TRACK;

/**
 * A {@link PlayerView} implementation.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerFragment extends Fragment  implements PlayerView {

    public static final String EXTRA_TRACK = "com.intech.player.EXTRA_TRACK";

    @InjectPresenter
    PlayerPresenter playerPresenter;

    @BindView(R.id.player_button)
    FloatingActionButton mButton;
    @BindView(R.id.player_surface)
    SurfaceView mSurface;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private CoordinatorLayout mCoordinatorLayout;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            final PlayerBoundForegroundService.SurfaceConsumer surfaceConsumer
                    = (PlayerBoundForegroundService.SurfaceConsumer) iBinder;
            surfaceConsumer.setSurfaceView(mSurface);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public PlayerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mCoordinatorLayout = (CoordinatorLayout) inflater
                .inflate(R.layout.fragment_player, container, false);

        ButterKnife.bind(this, mCoordinatorLayout);

        return mCoordinatorLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

        final TrackViewModel track = getActivity()
                .getIntent()
                .getParcelableExtra(EXTRA_SELECTED_TRACK);

        final Intent intent = new Intent(getActivity(), PlayerBoundForegroundService.class)
                .putExtra(EXTRA_TRACK, track);

        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mButton.setOnClickListener(view -> playerPresenter.buttonCLicked());
        playerPresenter.listenToPlayer(true);
    }


    @Override
    public void onPause() {
        super.onPause();
        mButton.setOnClickListener(null);
        playerPresenter.listenToPlayer(false);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(mConnection);
    }

    @Override
    public void showError(String error) {
        Snackbar
                .make(mCoordinatorLayout, error, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void setButtonPlay() {
        mButton.setImageDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.ic_media_play));
    }

    @Override
    public void setButtonPause() {
        mButton.setImageDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.ic_media_pause));
    }

    @Override
    public void setProgress(double progress) {
        mProgressBar.setProgress((int)(progress * mProgressBar.getMax()));
    }
}
