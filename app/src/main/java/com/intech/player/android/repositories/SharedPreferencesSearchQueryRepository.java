package com.intech.player.android.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.intech.player.R;
import com.intech.player.clean.boundaries.SearchQueryRepository;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;

import static android.content.Context.MODE_PRIVATE;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 14.04.18
 */
public class SharedPreferencesSearchQueryRepository implements SearchQueryRepository {

    private static final String PREFS_NAME = "Search query";
    private final String KEY_LAST_QUERY = "PREF_LAST_QUERY";

    private Context mContext;

    public SharedPreferencesSearchQueryRepository(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Maybe<String> getLastQuery() {
        return new Maybe<String>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super String> observer) {
                final SharedPreferences preferences = mContext
                        .getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                if (preferences.contains(KEY_LAST_QUERY)) {
                    observer.onSuccess(preferences.getString(KEY_LAST_QUERY, null));
                } else {
                    observer.onComplete();
                }
            }
        };
    }

    @Override
    public Maybe<String> putLastQuery(@NonNull String query) {
        return new Maybe<String>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super String> observer) {
                final SharedPreferences preferences = mContext
                        .getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

                final boolean written = preferences
                        .edit()
                        .putString(KEY_LAST_QUERY, query)
                        .commit();

                if (written) {
                    observer.onSuccess(query);
                } else {
                    observer.onError(SharedPreferencesSearchQueryRepository.this.error());
                }
            }
        };
    }

    @Override
    public Maybe<String> getPromoQuery() {
        return new Maybe<String>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super String> observer) {
                final String promo = mContext.getString(R.string.promo_query);
                if (promo.isEmpty()) {
                    observer.onComplete();
                } else {
                    observer.onSuccess(promo);
                }
            }
        };
    }

    private Exception error() {
        return new Exception("Failed to put the last search query to the shared properties.");
    }
}
