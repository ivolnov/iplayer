package com.intech.player.clean.boundaries;

import com.intech.player.clean.boundaries.model.EventRequestModel;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Something that can start, pause a player and provides
 * a stream of player's events as {@link EventRequestModel} entities.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
public interface PlayerController {
    Completable start();
    Completable pause();
    Completable stop();
    Observable<EventRequestModel> getPlayerEvents();
}
