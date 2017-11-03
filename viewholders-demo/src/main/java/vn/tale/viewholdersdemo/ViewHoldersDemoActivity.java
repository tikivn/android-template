package vn.tale.viewholdersdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import viewholders.NoAdapterFactory;
import vn.tale.viewholdersdemo.viewholder.ButtonViewHolder;
import vn.tale.viewholdersdemo.viewholder.ColorViewHolder;
import vn.tale.viewholdersdemo.viewholder.TextViewHolder;
import vn.tiki.noadapter2.OnItemClickListener;
import vn.tiki.noadapter2.OnlyAdapter;

public class ViewHoldersDemoActivity extends AppCompatActivity {

  private static final Random RANDOM = new Random();
  private OnlyAdapter adapter;
  private List<Object> items = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_holders_demo);

    RecyclerView rvGrid = findViewById(R.id.rvGrid);

    final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
    gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
      @Override
      public int getSpanSize(final int position) {
        if (items.get(position) instanceof ButtonViewHolder.Model) {
          return 5;
        } else {
          return 1;
        }
      }
    });
    rvGrid.setLayoutManager(gridLayoutManager);
    rvGrid.setHasFixedSize(true);
    adapter = NoAdapterFactory.builder()
        .onItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(final View view, final Object item, final int position) {
            if (item instanceof ButtonViewHolder.Model) {
              int id = view.getId();
              if (id == R.id.btAdd) {
                final Object randomItem = randomItem(items.size());
                items.add(randomItem);
                updateList();
              } else if (id == R.id.btRemove) {
                items.remove(items.size() - 1);
                updateList();
              } else if (id == R.id.btShuffle) {
                Collections.shuffle(items);
                updateList();
              }
            }
          }
        })
        .build();
    rvGrid.setAdapter(adapter);

    // init with controller buttons
    init();
  }

  private void init() {
    items.add(new ButtonViewHolder.Model());
    updateList();
  }

  private int randomColor() {
    int r = RANDOM.nextInt(256);
    int g = RANDOM.nextInt(256);
    int b = RANDOM.nextInt(256);

    return android.graphics.Color.rgb(r, g, b);
  }

  @NonNull
  private Object randomItem(int i) {
    Object item;
    if (i % 3 == 0) {
      item = new TextViewHolder.Model("Text #" + i);
    } else {
      item = new ColorViewHolder.Model(randomColor());
    }
    return item;
  }

  private void updateList() {
    adapter.setItems(new ArrayList<>(items));
  }
}
