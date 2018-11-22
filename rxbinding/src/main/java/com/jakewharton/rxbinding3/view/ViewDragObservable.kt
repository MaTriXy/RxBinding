@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.AlwaysTrue
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of [DragEvent] for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnDragListener] to observe
 * drags. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked with each value to determine the return value of the
 * underlying [View.OnDragListener].
 */
@CheckResult
@JvmOverloads
fun View.drags(handled: (DragEvent) -> Boolean = AlwaysTrue): Observable<DragEvent> {
  return ViewDragObservable(this, handled)
}

private class ViewDragObservable(
  private val view: View,
  private val handled: (DragEvent) -> Boolean
) : Observable<DragEvent>() {

  override fun subscribeActual(observer: Observer<in DragEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, handled, observer)
    observer.onSubscribe(listener)
    view.setOnDragListener(listener)
  }

  private class Listener(
    private val view: View,
    private val handled: (DragEvent) -> Boolean,
    private val observer: Observer<in DragEvent>
  ) : MainThreadDisposable(), OnDragListener {

    override fun onDrag(v: View, event: DragEvent): Boolean {
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
      view.setOnDragListener(null)
    }
  }
}
