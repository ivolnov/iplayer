package com.intech.player.clean.boundaries;

import android.support.annotation.NonNull;

import io.reactivex.Maybe;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 14.04.18
 */
public interface SearchQueryRepository {
    Maybe<String> getLastQuery();
    Maybe<String> getPromoQuery();
    Maybe<String> putLastQuery(@NonNull String query);
}