package vn.tiki.sample.noadapterviewholder;

import android.view.View;
import vn.tiki.noadapterviewholder.ViewHolder;

@ViewHolder(
    layout = 10
)
public abstract class LoadingViewHolder {


  protected void bind(final String item) {}

  protected void bindView(final View view) {}
}
