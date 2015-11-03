package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;
import rx.Observable;
import rx.functions.Action1;

public final class RxRadioGroup {
  /**
   * Create an observable of the checked view ID changes in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Integer> checkedChanges(@NonNull RadioGroup view) {
    return Observable.create(new RadioGroupCheckedChangeOnSubscribe(view))
        .distinctUntilChanged(); // Radio group can fire non-changes.
  }

  /**
   * An action which sets the checked child of {@code view} with ID. Passing {@code -1} will clear
   * any checked view.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Integer> checked(@NonNull final RadioGroup view) {
    return new Action1<Integer>() {
      @Override public void call(Integer value) {
        if (value == -1) {
          view.clearCheck();
        } else {
          view.check(value);
        }
      }
    };
  }
}
