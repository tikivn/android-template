package vn.tiki.sample.collectionview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import noadapterviewholder.NoAdapterFactory;
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

    adapter = NoAdapterFactory.makeAdapter((view, item, position) -> {

    });

    vCollectionView.setAdapter(new Adapter<String>() {
      @Override
      public void onBindItems(List<String> items) {
        adapter.setItems(items);
      }

      @Override
      public DataProvider<String> onCreateDataProvider() {
        return new TodoDataProvider();
      }

      @NonNull
      @Override
      public View onCreateErrorView(ViewGroup parent, Throwable throwable) {
        return getLayoutInflater().inflate(R.layout.view_error, parent, false);
      }

      @Override
      public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(
            CollectionViewActivity.this,
            LinearLayoutManager.VERTICAL,
            false);
      }

      @Override
      public RecyclerView.Adapter<?> onCreateRecyclerViewAdapter() {
        return adapter;
      }
    });
  }
}
