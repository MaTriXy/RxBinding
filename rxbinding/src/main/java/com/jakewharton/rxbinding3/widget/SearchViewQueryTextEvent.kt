package com.jakewharton.rxbinding3.widget

import android.widget.SearchView

data class SearchViewQueryTextEvent(
  /** The view from which this event occurred.  */
  val view: SearchView,
  val queryText: CharSequence,
  val isSubmitted: Boolean
)
