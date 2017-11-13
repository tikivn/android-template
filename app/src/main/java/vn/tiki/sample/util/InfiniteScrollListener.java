package vn.tiki.sample.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class InfiniteScrollListener extends RecyclerView.OnScrollListener {

  private final Runnable callbacks;
  private final LinearLayoutManager linearLayoutManager;
  private boolean loading = true; // True if we are still waiting for the last set of data to load.
  private int previousTotal = 0; // The total number of items in the dataset after the last load
  private final int visibleThreshold;

  public InfiniteScrollListener(LinearLayoutManager linearLayoutManager, int visibleThreshold, Runnable callbacks) {
    this.linearLayoutManager = linearLayoutManager;
    this.visibleThreshold = visibleThreshold;
    this.callbacks = callbacks;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    final int visibleItemCount = recyclerView.getChildCount();
    final int totalItemCount = linearLayoutManager.getItemCount();
    final int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

    if (loading) {
      if (totalItemCount > previousTotal || totalItemCount == 0) {
        loading = false;
        previousTotal = totalItemCount;
      }
    }

    // End has been reached
    if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
      recyclerView.post(callbacks);
      loading = true;
    }
  }

  public void reset() {
    previousTotal = 0;
    loading = true;
  }
}