package vn.tiki.sample.extra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import javax.inject.Inject;
import vn.tiki.daggers.ActivityInjector;
import vn.tiki.daggers.Daggers;
import vn.tiki.intents.Extra;
import vn.tiki.sample.R;
import vn.tiki.sample.model.UserModel;

/**
 * Created by Giang Nguyen on 9/3/17.
 */
public class ExtraInjectionActivity extends AppCompatActivity implements ActivityInjector {

  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.tvAge) TextView tvAge;

  @Extra String name;
  @Extra int age;

  @Inject UserModel userModel;

  @Override public Object activityModule() {
    return new ExtraInjectionModule.Module();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_extra_injection);
    ButterKnife.bind(this);
    ExtraInjectionActivity_.bindExtras(this);
    Daggers.inject(this, this);

    tvName.setText(name);
    tvAge.setText(String.valueOf(age));

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragmentContainer, ExtraInjectionFragment_.builder()
              .height(170)
              .weight(60)
              .make())
          .commit();
    }
  }
}
