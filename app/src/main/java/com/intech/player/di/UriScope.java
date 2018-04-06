package com.intech.player.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 06.04.18
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UriScope {
}
