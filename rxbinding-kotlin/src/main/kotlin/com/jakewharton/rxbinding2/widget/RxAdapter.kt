@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.widget.Adapter
import com.jakewharton.rxbinding2.InitialValueObservable
import kotlin.Suppress

/**
 * Create an observable of data change events for `adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun <T : Adapter> T.dataChanges(): InitialValueObservable<T> = RxAdapter.dataChanges(this)
