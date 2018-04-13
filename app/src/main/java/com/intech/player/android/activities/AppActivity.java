package com.intech.player.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.intech.player.R;

/**
 * Created by ivan volnov on 13.04.2018.
 */
class AppActivity extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
	}

	private void initActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setIcon(R.drawable.ic_launcher_intech);
	}
}
