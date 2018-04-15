package com.intech.player.android.activities;


import android.os.Bundle;
import android.widget.FrameLayout;

import com.intech.player.R;
import com.intech.player.android.fragments.TrackListFragment;
import com.intech.player.di.DaggerActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class TrackListActivity extends AppActivity {

    @BindView(R.id.fragment_container)
    FrameLayout container;

    @Inject
    TrackListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

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
}
