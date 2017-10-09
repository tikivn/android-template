package vn.tiki.sample.collectionview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import vn.tiki.collectionview.Adapter;
import vn.tiki.collectionview.CollectionView;
import vn.tiki.collectionview.DataProvider;
import vn.tiki.noadapter2.OnlyAdapter;
import vn.tiki.sample.R;

public class CollectionViewActivity extends AppCompatActivity {

  @BindView(R.id.vCollectionView) CollectionView vCollectionView;
  private OnlyAdapter adapter;

  public static Intent intent(Context context) {
    return new Intent(context, CollectionViewActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_collection_view);
    ButterKnife.bind(this);

    adapter = OnlyAdapter.builder()
        .viewHolderFactory((parent, type) -> TextViewHolder.create(parent))
        .build();

    vCollectionView.setAdapter(new Adapter<String>() {
      @Override public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(
            CollectionViewActivity.this,
            LinearLayoutManager.VERTICAL,
            false);
      }

      @Override public RecyclerView.Adapter<?> getRecyclerViewAdapter() {
        return adapter;
      }

      @Override public DataProvider<String> getDataProvider() {
        return new TodoDataProvider();
      }

      @Override public void setItems(List<String> items) {
        adapter.setItems(items);
      }

      @NonNull @Override public View onCreateErrorView(Throwable throwable) {
        return getLayoutInflater().inflate(R.layout.view_error, null);
      }
    });
  }
}
