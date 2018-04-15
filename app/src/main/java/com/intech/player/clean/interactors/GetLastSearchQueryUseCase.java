package com.intech.player.clean.interactors;

import com.intech.player.clean.boundaries.SearchQueryRepository;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 14.04.18
 */
public class GetLastSearchQueryUseCase {

    private static final String EMPTY_STRING = "";

    private final SearchQueryRepository repository;

    public GetLastSearchQueryUseCase(SearchQueryRepository repository) {
        this.repository = repository;
    }

    public Single<String> execute() {
        return repository
                .getLastQuery()
                .switchIfEmpty(repository.getPromoQuery())
                .switchIfEmpty(emptyQuery());
    }

    private Single<String> emptyQuery() {
        return new Single<String>() {
            @Override
            protected void subscribeActual(SingleObserver<? super String> observer) {
                observer.onSuccess(EMPTY_STRING);
            }
        };
    }
}
