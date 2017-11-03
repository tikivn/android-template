package vn.tale.viewholdersdemo.viewholder;

import android.view.View;
import vn.tale.viewholdersdemo.R2;
import vn.tale.viewholdersdemo.viewholder.ColorViewHolder.Model;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_item_color,
    bindTo = Model.class
)
public class ColorViewHolder {

  public static class Model {

    private int value;

    public Model(final int value) {
      this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      final Model model = (Model) o;

      return value == model.value;
    }

    @Override
    public int hashCode() {
      return value;
    }
  }

  private View view;

  void bind(Model model) {
    view.setBackgroundColor(model.value);
  }

  void bindView(final View view) {
    this.view = view;
  }
}
