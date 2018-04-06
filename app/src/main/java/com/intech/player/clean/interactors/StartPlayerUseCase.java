package com.intech.player.clean.interactors;

import com.intech.player.clean.interactors.boundaries.PlayerController;

import io.reactivex.CompletableSource;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
public class StartPlayerUseCase {
    private final PlayerController playerController;

    public StartPlayerUseCase(PlayerController playerController) {
        this.playerController = playerController;
    }

    public CompletableSource execute() {
        return playerController.start();
    }
}
