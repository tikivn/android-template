package vn.tiki.collectionview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CollectionView extends FrameLayout {

  @VisibleForTesting @NonNull final RecyclerView recyclerView;
  @VisibleForTesting @NonNull final SwipeRefreshLayout refreshLayout;
  @VisibleForTesting @NonNull Map<Class<?>, View> errorViewHolder = new Hashtable<>();
  @VisibleForTesting @Nullable CollectionViewPresenter presenter;
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
      @Override
      public void onRefresh() {
        if (presenter != null) {
          presenter.onRefresh();
        }
      }
    });
    addView(refreshLayout, lpMatchMatch());
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    isAttached = true;
    if (presenter != null) {
      presenter.attach(this);
    }
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    isAttached = false;
    if (presenter != null) {
      presenter.detach();
    }
    errorViewHolder.clear();
  }

  public void setAdapter(@NonNull Adapter adapter) {
    this.adapter = adapter;
    final RecyclerView.LayoutManager layoutManager = adapter.onCreateLayoutManager();
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter.onCreateRecyclerViewAdapter());
    if (layoutManager instanceof LinearLayoutManager) {
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
          if (presenter == null || newState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
          }

          final int lastCompletelyVisibleItemPosition =
              ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

          if (lastCompletelyVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
            presenter.onLoadMore();
          }
        }
      });
    }

    presenter = new CollectionViewPresenter(adapter.onCreateDataProvider());
    if (isAttached) {
      presenter.attach(this);
    }
  }

  void hideLoadMore() {

  }

  void hideRefreshing() {
    refreshLayout.setRefreshing(false);
  }

  @SuppressWarnings("unchecked")
  void setItems(List<?> items) {
    if (adapter != null) {
      adapter.onBindItems(items);
    }
  }

  void showContent() {
    refreshLayout.setRefreshing(false);
  }

  void showError(Throwable throwable) {
    if (adapter == null) {
      throw new NullPointerException("adapter is null");
    }
    refreshLayout.setRefreshing(false);
    final View errorView;
    final Class<? extends Throwable> errorClass = throwable.getClass();
    if (errorViewHolder.containsKey(errorClass)) {
      errorView = errorViewHolder.get(errorClass);
    } else {
      errorView = adapter.onCreateErrorView(this, throwable);
      errorView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (presenter != null) {
            presenter.onLoad();
          }
        }
      });
      errorViewHolder.put(errorClass, errorView);
    }
    addView(errorView, lpWrapWrapCenter());
  }

  void showLoading() {
    if (adapter == null) {
      throw new NullPointerException("adapter is null");
    }
    if (getChildCount() == 2) {
      removeViewAt(1);
    }
    refreshLayout.setRefreshing(true);
  }

  @NonNull
  private LayoutParams lpMatchMatch() {
    return new LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
  }

  @NonNull
  private LayoutParams lpWrapWrapCenter() {
    final LayoutParams layoutParams = new LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER;
    return layoutParams;
  }
}
