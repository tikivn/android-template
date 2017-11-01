package vn.tiki.sample.viewholder;

import android.view.View;
import android.widget.TextView;
import vn.tiki.viewholders.ViewHolder;
import vn.tiki.sample.R;

@ViewHolder(
    layout = R.layout.clv_item_text
)
public class TextViewHolder {

  TextView textView;

  void bind(String text) {
    textView.setText(text);
  }

  void bindView(View view) {
    textView = (TextView) view;
  }
}
