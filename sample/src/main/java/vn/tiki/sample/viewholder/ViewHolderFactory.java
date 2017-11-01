package vn.tiki.sample.viewholder;

import android.view.View;
import android.view.ViewGroup;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.tikiapp.product.R;

public class ViewHolderFactory {

  public static AbsViewHolder makeViewHolder(ViewGroup parent, int type) {
    if (type == R.layout.product_item_suggestion1) {
      return SuggestionViewHolder.create(parent);
    }
    return null;
  }

  public static int type(Object object) {
    if (object instanceof Suggestion) {
      return R.layout.product_item_suggestion1;
    }
    return View.NO_ID;
  }
}
