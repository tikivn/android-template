package vn.tiki.sample.extra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import intents.Intents;
import vn.tiki.intents.BindExtra;
import vn.tiki.sample.R;

public class ExtraInjectionFragment extends Fragment {

  @BindView(R.id.tvHeight) TextView tvHeight;
  @BindView(R.id.tvWeight) TextView tvWeight;

  @BindExtra int height;
  @BindExtra int weight;

  @Nullable @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_extra_injection, container, false);

    ButterKnife.bind(this, view);
    Intents.bind(this);

    tvHeight.setText(height + " cm");
    tvWeight.setText(weight + " kg");

    return view;
  }
}
