package vn.tiki.sample.extra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.tiki.intents.BindExtra;
import vn.tiki.sample.R;
import vn.tiki.sample.base.BaseActivity;

/**
 * Created by Giang Nguyen on 9/3/17.
 */
public class ExtraInjectionActivity extends BaseActivity {

  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.tvAge) TextView tvAge;

  @BindExtra String name;
  @BindExtra int age;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_extra_injection);
    ButterKnife.bind(this);

    ExtraInjectionActivity_.bindExtras(this);

    tvName.setText(name);
    tvAge.setText(String.valueOf(age));

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(
              R.id.fragmentContainer,
              ExtraInjectionFragment_.builder()
                  .height(170)
                  .weight(60)
                  .make())
          .commit();
    }
  }
}
