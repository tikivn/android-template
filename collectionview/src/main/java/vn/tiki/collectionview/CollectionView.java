package vn.tiki.collectionview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.List;

public class CollectionView extends FrameLayout {
  @VisibleForTesting @NonNull final RecyclerView recyclerView;
  @VisibleForTesting @NonNull final SwipeRefreshLayout refreshLayout;
  @VisibleForTesting @Nullable CollectionViewPresenter presenter;
  @VisibleForTesting @Nullable View errorView;
  @Nullable private Adapter adapter;
  private boolean isAttached;

  public CollectionView(
      @NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    recyclerView = new RecyclerView(context);
    refreshLayout = new SwipeRefreshLayout(context);
    refreshLayout.addView(recyclerView, lpMatchMatch());
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        if (presenter != null) {
          presenter.onRefresh();
        }
      }
    });
    addView(refreshLayout, lpMatchMatch());
  }

  @NonNull private LayoutParams lpMatchMatch() {
    return new LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
  }

  public void setAdapter(@NonNull Adapter adapter) {
    this.adapter = adapter;
    recyclerView.setLayoutManager(adapter.getLayoutManager());
    recyclerView.setAdapter(adapter.getRecyclerViewAdapter());
    presenter = new CollectionViewPresenter(adapter.getDataProvider());
    if (isAttached) {
      presenter.attach(this);
    }
  }

  @SuppressWarnings("unchecked") @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    isAttached = true;
    if (presenter != null) {
      presenter.attach(this);
    }
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    isAttached = false;
    if (presenter != null) {
      presenter.detach();
    }
  }

  void showLoading() {
    if (adapter == null) {
      throw new NullPointerException("adapter is null");
    }
    if (errorView != null) {
      errorView.setVisibility(GONE);
    }
    refreshLayout.setRefreshing(true);
  }

  void showContent() {
    refreshLayout.setRefreshing(false);
  }

  @SuppressWarnings("unchecked") void setItems(List<?> items) {
    if (adapter != null) {
      adapter.setItems(items);
    }
  }

  void showError(Throwable throwable) {
    if (adapter == null) {
      throw new NullPointerException("adapter is null");
    }
    refreshLayout.setRefreshing(false);
    if (errorView == null) {
      errorView = adapter.onCreateErrorView(throwable);
      addView(errorView, lpWrapWrapCenter());
    }
    errorView.setVisibility(VISIBLE);
  }

  @NonNull private LayoutParams lpWrapWrapCenter() {
    final LayoutParams layoutParams = new LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER;
    return layoutParams;
  }

  void hideRefreshing() {
    refreshLayout.setRefreshing(false);
  }

  void hideLoadMore() {

  }
}
