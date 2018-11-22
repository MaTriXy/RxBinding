@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.MotionEvent
import android.view.View
import android.view.View.OnHoverListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.AlwaysTrue
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of hover events for `view`.
 *
 * *Warning:* Values emitted by this observable are **mutable** and part of a shared
 * object pool and thus are **not safe** to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls [MotionEvent.obtain] or
 * [MotionEvent.obtainNoHistory] to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnHoverListener] to observe
 * touches. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked with each value to determine the return value of the
 * underlying [View.OnHoverListener].
 */
@CheckResult
@JvmOverloads
fun View.hovers(
  handled: (MotionEvent) -> Boolean = AlwaysTrue
): Observable<MotionEvent> {
  return ViewHoverObservable(this, handled)
}

private class ViewHoverObservable(
  private val view: View,
  private val handled: (MotionEvent) -> Boolean
) : Observable<MotionEvent>() {

  override fun subscribeActual(observer: Observer<in MotionEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, handled, observer)
    observer.onSubscribe(listener)
    view.setOnHoverListener(listener)
  }

  private class Listener(
    private val view: View, private val handled: (MotionEvent) -> Boolean,
    private val observer: Observer<in MotionEvent>
  ) : MainThreadDisposable(), OnHoverListener {

    override fun onHover(v: View, event: MotionEvent): Boolean {
      if (!isDisposed) {
        try {
          if (handled(event)) {
            observer.onNext(event)
            return true
          }
        } catch (e: Exception) {
          observer.onError(e)
          dispose()
        }

      }
      return false
    }

    override fun onDispose() {
      view.setOnHoverListener(null)
    }
  }
}
