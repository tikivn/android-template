package vn.tale.viewholdersdemo.viewholder;

import android.widget.TextView;
import butterknife.BindView;
import vn.tale.viewholdersdemo.R2;
import vn.tale.viewholdersdemo.viewholder.TextViewHolder.Model;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_item_text,
    bindTo = Model.class
)
public class TextViewHolder extends BaseViewHolder {

  public static class Model {

    public final String value;

    public Model(final String value) {
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

      return value != null ? value.equals(model.value) : model.value == null;
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }
  }

  @BindView(R2.id.text) TextView text;

  void bind(Model model) {
    text.setText(model.value);
  }

}
