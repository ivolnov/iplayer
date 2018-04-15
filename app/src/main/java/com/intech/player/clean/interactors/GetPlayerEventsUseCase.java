package com.intech.player.clean.interactors;

import com.intech.player.clean.boundaries.PlayerController;
import com.intech.player.clean.boundaries.model.EventRequestModel;

import io.reactivex.Observable;


/**
 * An interactor to obtain a stream of player's events request models.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class GetPlayerEventsUseCase {

    private PlayerController controller;

    public GetPlayerEventsUseCase(PlayerController controller) {
        this.controller = controller;
    }

    public Observable<EventRequestModel> execute() {
        return controller.getPlayerEvents();
    }
}
