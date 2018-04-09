package com.intech.player.clean.interactors;

import com.intech.player.clean.boundaries.PlayerController;

import io.reactivex.Completable;

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

    public Completable execute() {
        return playerController.start();
    }
}
