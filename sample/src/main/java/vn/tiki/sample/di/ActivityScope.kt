package vn.tiki.sample.di

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Identifies a type that the injector only instantiates once. Not inherited.
 *
 * @see Scope @Scope
 */
@Scope
@MustBeDocumented
@kotlin.annotation.Retention(RUNTIME)
annotation class ActivityScope
