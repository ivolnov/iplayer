package com.intech.player.clean.interactors;

import com.intech.player.clean.boundaries.PlayerController;

import io.reactivex.Completable;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 06.04.18
 */
public class PausePlayerUseCase {
    private final PlayerController playerController;

    public PausePlayerUseCase(PlayerController playerController) {
        this.playerController = playerController;
    }

    public Completable execute() {
        return playerController.pause();
    }
}
