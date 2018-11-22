package com.jakewharton.rxbinding3.material;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.filters.SdkSuppress;
import com.google.android.material.appbar.AppBarLayout;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RxAppBarLayoutTest {
  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final CoordinatorLayout parent = new CoordinatorLayout(context);
  private final AppBarLayout view = new AppBarLayout(context);

  @Before public void setUp() {
    parent.addView(view);
  }

  @SdkSuppress(minSdkVersion = 17)
  @Test @UiThreadTest public void offsetChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxAppBarLayout.offsetChanges(view).subscribe(o);
    o.assertNoMoreEvents();

    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
    AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
    params.setBehavior(behavior);
    behavior.onLayoutChild(parent, view, View.LAYOUT_DIRECTION_LTR);
    assertEquals(0, o.takeNext().intValue());

    o.dispose();

    behavior.onLayoutChild(parent, view, View.LAYOUT_DIRECTION_LTR);
    o.assertNoMoreEvents();
  }
}
