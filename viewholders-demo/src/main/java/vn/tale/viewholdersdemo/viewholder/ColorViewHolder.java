package vn.tale.viewholdersdemo.viewholder;

import android.view.View;
import vn.tale.viewholdersdemo.R2;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_item_color,
    bindTo = ColorModel.class
)
class ColorViewHolder {

  private View view;

  void bind(ColorModel model) {
    view.setBackgroundColor(model.getValue());
  }

  void bindView(final View view) {
    this.view = view;
  }
}
