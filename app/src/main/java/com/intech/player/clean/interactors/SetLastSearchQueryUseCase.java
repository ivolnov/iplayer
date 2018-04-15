package com.intech.player.clean.interactors;

import android.support.annotation.NonNull;

import com.intech.player.clean.boundaries.SearchQueryRepository;

import io.reactivex.Maybe;

/**
 * An interactor that persists a query as the last one.
 *
 * @author Ivan Volnov
 * @since 14.04.18
 */
public class SetLastSearchQueryUseCase {
    private final SearchQueryRepository repository;

    public SetLastSearchQueryUseCase(SearchQueryRepository repository) {
        this.repository = repository;
    }

    public Maybe<String> execute(@NonNull String query) {
        return repository.putLastQuery(query);
    }
}
