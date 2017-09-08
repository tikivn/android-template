package vn.tiki.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.tiki.sample.extra.ExtraInjectionActivity_;
import vn.tiki.sample.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btOpenExtraInjection)
  public void openExtraInjection(View view) {
    final Intent intent = ExtraInjectionActivity_.intentBuilder(this)
        .name("Giang")
        .age(29)
        .make();
    startActivity(intent);
  }

  @OnClick(R.id.btOpenLogin)
  public void openLogin(View view) {
    startActivity(LoginActivity.intent(this));
  }
}
