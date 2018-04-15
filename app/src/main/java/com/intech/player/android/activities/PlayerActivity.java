package com.intech.player.android.activities;

import android.media.AudioManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.intech.player.R;
import com.intech.player.android.fragments.PlayerFragment;
import com.intech.player.di.DaggerActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity that informs its listeners about back button press.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerActivity extends AppActivity {

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    @BindView(R.id.fragment_container)
    FrameLayout container;

    @Inject
    PlayerFragment fragment;

    private OnBackPressedListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        DaggerActivityComponent
                .builder()
                .build()
                .inject(this);

        ButterKnife.bind(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(container.getId(), fragment)
                    .commit();
        }
    }

    public void setListener(OnBackPressedListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mListener != null) {
            mListener.onBackPressed();
        }
        super.onBackPressed();
    }
}
