package vn.tiki.sample.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndReachDetector extends RecyclerView.OnScrollListener {

  private final LinearLayoutManager layoutManager;
  private final Runnable onReachCallback;

  public EndReachDetector(LinearLayoutManager layoutManager, Runnable onReachCallback) {
    this.layoutManager = layoutManager;
    this.onReachCallback = onReachCallback;
  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
    if (onReachCallback == null) {
      return;
    }
    if (newState == RecyclerView.SCROLL_STATE_IDLE
        && layoutManager.findLastCompletelyVisibleItemPosition()
        == recyclerView.getAdapter().getItemCount() - 1) {
      onReachCallback.run();
    }
  }
}
