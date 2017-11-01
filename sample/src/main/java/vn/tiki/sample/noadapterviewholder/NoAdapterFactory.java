package vn.tiki.sample.noadapterviewholder;

import vn.tiki.noadapter2.OnItemClickListener;
import vn.tiki.noadapter2.OnlyAdapter;
import vn.tiki.noadapterviewholder.TypeDiffCallback;

public final class NoAdapterFactory {

  public static OnlyAdapter makeAdapter(OnItemClickListener onItemClickListener) {
    return new OnlyAdapter.Builder()
        .typeFactory(new TypeFactoryIml())
        .viewHolderFactory(new ViewHolderFactoryImpl())
        .diffCallback(new TypeDiffCallback())
        .onItemClickListener(onItemClickListener)
        .build();
  }

  private NoAdapterFactory() {
    throw new InstantiationError();
  }
}
