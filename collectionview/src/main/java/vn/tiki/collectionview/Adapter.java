package vn.tiki.collectionview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public interface Adapter<T> {

  DataProvider<T> onCreateDataProvider();

  RecyclerView.LayoutManager onCreateLayoutManager();

  RecyclerView.Adapter<?> onCreateRecyclerViewAdapter();

  void onBindItems(List<T> items);

  @NonNull View onCreateErrorView(ViewGroup parent, Throwable throwable);
}
