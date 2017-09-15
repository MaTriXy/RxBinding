@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v17.leanback.widget

import android.support.v17.leanback.widget.SearchEditText
import com.jakewharton.rxbinding2.internal.VoidToUnit
import io.reactivex.Observable
import kotlin.Suppress
import kotlin.Unit

/**
 * Create an observable which emits the keyboard dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun SearchEditText.keyboardDismisses(): Observable<Unit> = RxSearchEditText.keyboardDismisses(this).map(VoidToUnit)
