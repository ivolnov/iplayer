package com.intech.player.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intech.player.R;
import com.intech.player.mvp.views.PlayerView;

/**
 * A {@link PlayerView} implementation.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerActivityFragment extends Fragment  implements PlayerView {

    public PlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }
}
