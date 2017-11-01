package vn.tiki.sample.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.tiki.noadapter2.AbsViewHolder;
import vn.tiki.sample.R;

public class SuggestionViewHolder extends AbsViewHolder {

  private Suggestion suggestion;
  private final TextView tvSuggestion;

  public static SuggestionViewHolder create(ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View view = inflater.inflate(R.layout.product_item_suggestion1, parent, false);
    return new SuggestionViewHolder(view);
  }

  private SuggestionViewHolder(View itemView) {
    super(itemView);
    registerOnClickOn(itemView.findViewById(R.id.ivFill));
    tvSuggestion = itemView.findViewById(R.id.tvSuggestion);
  }

  @Override
  public void bind(final Object item) {
    super.bind(item);
    if (item instanceof Suggestion) {
      suggestion = (Suggestion) item;
      suggestion.tvSuggestion = tvSuggestion;
      suggestion.bind();
    }
  }

  @Override
  public void unbind() {
    if (suggestion != null) {
      suggestion.tvSuggestion = null;
    }
    super.unbind();
  }
}
