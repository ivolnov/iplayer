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
import android.support.v7.app.AlertDialog;
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
import com.intech.player.android.activities.PlayerActivity;
import com.intech.player.android.services.PlayerBoundForegroundService;
import com.intech.player.mvp.models.TrackViewModel;
import com.intech.player.mvp.presenters.PlayerPresenter;
import com.intech.player.mvp.views.PlayerView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intech.player.android.fragments.TrackListFragment.EXTRA_SELECTED_TRACK;
import static com.intech.player.android.utils.AndroidUtils.okIcon;
import static com.intech.player.android.utils.AndroidUtils.oreo;
import static com.intech.player.mvp.models.utils.ModelConverter.isVideo;

/**
 * A {@link PlayerView} implementation.
 * Starts a player foreground service during creation and binds to it which will
 * effectively result in rebinding on consequent recreations.
 * Listens to the back button press to stop the service as nobody else will.
 * Implements {@link com.intech.player.android.services.PlayerBoundForegroundService.UiComponent}
 * to provide a surface for the player, current track and to find out when presenter should
 * listen to player events as the fragment may be ready much earlier than the service itself.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerFragment
        extends MvpAppCompatFragment
        implements PlayerView,
        PlayerBoundForegroundService.UiComponent,
        PlayerActivity.OnBackPressedListener {

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

    private boolean mBound;
    private CoordinatorLayout mCoordinatorLayout;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            final PlayerBoundForegroundService.UiConsumer dependenciesConsumer
                    = (PlayerBoundForegroundService.UiConsumer) iBinder;
            dependenciesConsumer.plugIn(PlayerFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public PlayerFragment() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void onBackPressed() {
        if (getActivity() == null) {
            return;
        }
        if (playerPresenter.isPaused()) {
            getActivity().stopService(serviceIntent());
        }
    }

    @Override
    public TrackViewModel getTrack() {
        return playerPresenter.getTrack();
    }

    @Override
    public SurfaceView getSurface() {
        return surface;
    }

    @Override
    public void startListening() {
        playerPresenter.listenToPlayer(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startListeningToBackButton();
        setTrack();
        startService();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mCoordinatorLayout = (CoordinatorLayout) inflater
                .inflate(R.layout.fragment_player, container, false);

        ButterKnife.bind(this, mCoordinatorLayout);

        if (playerPresenter.hasTrack()) {
            if (isVideo(playerPresenter.getTrack())) {
                artwork.setVisibility(View.GONE);
                surface.setVisibility(View.VISIBLE);
            } else {
                picasso
                        .load(playerPresenter.getTrack().getArtworkUrl())
                        .error(R.drawable.error_placeholder)
                        .tag(this)
                        .fit()
                        .centerCrop()
                        .into(artwork);
            }
        }

        return mCoordinatorLayout;
    }

    @Override
    public void onResume() {
        button.setOnClickListener(view -> playerPresenter.buttonCLicked());
        super.onResume();
    }


    @Override
    public void onPause() {
        button.setOnClickListener(null);
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        unBindService();
    }

    @Override
    public void showError(String error) {
        Snackbar
                .make(mCoordinatorLayout, error, Snackbar.LENGTH_LONG)
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
                    .create()
                    .show();
        }
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

    private void startListeningToBackButton() {
        if (getActivity() instanceof PlayerActivity) {
            ((PlayerActivity) getActivity()).setListener(this);
        }
    }

    private void setTrack() {
        if (getActivity() != null) {
            final TrackViewModel track = getActivity()
                    .getIntent()
                    .getParcelableExtra(EXTRA_SELECTED_TRACK);
            playerPresenter.setTrack(track);
        }
    }

    private void startService() {
        if (getActivity() == null) {
            return;
        }
        if (playerPresenter.hasTrack()) {
            if (oreo()) {
                getActivity()
                        .startForegroundService(serviceIntent());
            } else {
                getActivity()
                        .startService(serviceIntent());
            }
            getActivity().bindService(serviceIntent(), mConnection, Context.BIND_IMPORTANT);
            mBound = true;
        }
    }

    private void unBindService() {
        playerPresenter.listenToPlayer(false);
        if (mBound && getActivity() != null) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    private Intent serviceIntent() {
        return new Intent(getContext(), PlayerBoundForegroundService.class)
                .putExtra(EXTRA_TRACK, playerPresenter.getTrack());
    }
}
