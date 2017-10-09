package vn.tiki.sample.collectionview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;

public class InternalAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

  private final RecyclerView.Adapter<VH> adapter;

  public InternalAdapter(RecyclerView.Adapter<VH> adapter) {
    this.adapter = adapter;
  }

  @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return adapter.onCreateViewHolder(parent, viewType);
  }

  @Override public void onBindViewHolder(VH holder, int position) {
    adapter.onBindViewHolder(holder, position);
  }

  @Override public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
    super.onBindViewHolder(holder, position, payloads);
    adapter.onBindViewHolder(holder, position, payloads);
  }

  @Override public int getItemViewType(int position) {
    return adapter.getItemViewType(position);
  }

  @Override public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(hasStableIds);
    adapter.setHasStableIds(hasStableIds);
  }

  @Override public long getItemId(int position) {
    return adapter.getItemId(position);
  }

  @Override public int getItemCount() {
    return adapter.getItemCount();
  }

  @Override public void onViewRecycled(VH holder) {
    super.onViewRecycled(holder);
    adapter.onViewRecycled(holder);
  }

  @Override public boolean onFailedToRecycleView(VH holder) {
    return adapter.onFailedToRecycleView(holder);
  }

  @Override public void onViewAttachedToWindow(VH holder) {
    super.onViewAttachedToWindow(holder);
    adapter.onViewAttachedToWindow(holder);
  }

  @Override public void onViewDetachedFromWindow(VH holder) {
    super.onViewDetachedFromWindow(holder);
    adapter.onViewDetachedFromWindow(holder);
  }

  @Override public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.registerAdapterDataObserver(observer);
    adapter.registerAdapterDataObserver(observer);
  }

  @Override public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.unregisterAdapterDataObserver(observer);
    adapter.unregisterAdapterDataObserver(observer);
  }

  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    adapter.onAttachedToRecyclerView(recyclerView);
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    adapter.onDetachedFromRecyclerView(recyclerView);
  }
}
