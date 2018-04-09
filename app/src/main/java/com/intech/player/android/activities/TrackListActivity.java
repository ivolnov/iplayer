package com.intech.player.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.intech.player.R;
import com.intech.player.android.fragments.TrackListFragment;
import com.intech.player.di.DaggerActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackListActivity extends AppCompatActivity {

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
