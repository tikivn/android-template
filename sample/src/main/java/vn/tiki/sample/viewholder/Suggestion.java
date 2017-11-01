package vn.tiki.sample.viewholder;

import android.widget.TextView;
import com.google.auto.value.AutoValue;
import vn.tiki.tikiapp.product.R;

@ViewHolder(layout = R.layout.product_item_suggestion1, onClick = {R.id.ivFill})
@AutoValue
public abstract class Suggestion implements DiffDelegate {

  TextView tvSuggestion;

  abstract CharSequence suggestion();

  @Override
  public boolean isContentTheSame(final Object other) {
    return equals(other);
  }

  @Override
  public boolean isSame(final Object other) {
    return other instanceof Suggestion;
  }

  void bind() {
    tvSuggestion.setText(suggestion());
  }
}
