package vn.tale.viewholdersdemo.viewholder;

import android.widget.TextView;
import butterknife.BindView;
import vn.tale.viewholdersdemo.R2;
import vn.tiki.viewholders.ViewHolder;

@ViewHolder(
    layout = R2.layout.viewholder_item_text,
    bindTo = TextModel.class
)
class TextViewHolder extends BaseViewHolder {

  @BindView(R2.id.text) TextView text;

  void bind(TextModel model) {
    text.setText(model.getValue());
  }
}
