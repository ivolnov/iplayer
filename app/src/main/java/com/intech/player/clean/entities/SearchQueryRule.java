package com.intech.player.clean.entities;

/**
 * A business entity.
 * A rule that validates a search query,
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class SearchQueryRule {

    public static boolean validate(String query) {
        return isMoreThatFourCharacters(query);
    }

    private static boolean isMoreThatFourCharacters(String query) {
        return query.length() > 4;
    }
}
