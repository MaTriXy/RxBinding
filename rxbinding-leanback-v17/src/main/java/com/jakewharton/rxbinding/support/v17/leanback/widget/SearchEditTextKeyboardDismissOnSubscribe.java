package com.jakewharton.rxbinding.support.v17.leanback.widget;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import android.support.v17.leanback.widget.SearchEditText;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SearchEditTextKeyboardDismissOnSubscribe implements Observable.OnSubscribe<Void> {
  private final SearchEditText view;

  public SearchEditTextKeyboardDismissOnSubscribe(SearchEditText view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    checkUiThread();

    SearchEditText.OnKeyboardDismissListener listener = new SearchEditText.OnKeyboardDismissListener() {
      @Override
      public void onKeyboardDismiss() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };

    view.setOnKeyboardDismissListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override
      protected void onUnsubscribe() {
        // TODO: set to null once http://b.android.com/187101 is released.
        view.setOnKeyboardDismissListener(new SearchEditText.OnKeyboardDismissListener() {
          @Override
          public void onKeyboardDismiss() {
          }
        });
      }
    });
  }
}
