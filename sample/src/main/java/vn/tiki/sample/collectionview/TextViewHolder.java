package vn.tiki.sample.collectionview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.sample.R;

public class TextViewHolder extends AbsViewHolder {

  private TextViewHolder(View view) {
    super(view);
  }

  public static TextViewHolder create(ViewGroup parent) {
    final View view = LayoutInflater.from(parent.getContext()).inflate(
        R.layout.clv_item_text,
        parent,
        false);
    return new TextViewHolder(view);
  }

  @Override public void bind(Object item) {
    super.bind(item);
    ((TextView) itemView).setText(String.valueOf(item));
  }
}
