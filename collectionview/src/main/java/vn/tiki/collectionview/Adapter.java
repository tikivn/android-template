package vn.tiki.collectionview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public interface Adapter<T> {
  RecyclerView.LayoutManager getLayoutManager();

  RecyclerView.Adapter<?> getRecyclerViewAdapter();

  DataProvider<T> getDataProvider();

  void setItems(List<T> items);

  @NonNull View onCreateErrorView(ViewGroup parent, Throwable throwable);
}
