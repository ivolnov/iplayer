package com.intech.player.clean.interactors;

import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.controller.ITunesPlayerController;

import io.reactivex.Observable;


/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class GetPlayerEventsUseCase {

    private ITunesPlayerController controller;

    public GetPlayerEventsUseCase(ITunesPlayerController controller) {
        this.controller = controller;
    }

    public Observable<EventRequestModel> execute() {
        return controller.getPlayerEvents();
    }
}
