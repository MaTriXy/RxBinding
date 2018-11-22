package com.jakewharton.rxbinding3.material;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import io.reactivex.functions.Consumer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RxFloatingActionButtonTest {
  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final FloatingActionButton fab = new FloatingActionButton(context);

  @Test
  @UiThreadTest
  public void visibility() throws Exception {
    fab.show();
    Consumer<? super Boolean> action = RxFloatingActionButton.visibility(fab);
    action.accept(false);
    assertEquals(View.GONE, fab.getVisibility());
    action.accept(true);
    assertEquals(View.VISIBLE, fab.getVisibility());
  }
}
