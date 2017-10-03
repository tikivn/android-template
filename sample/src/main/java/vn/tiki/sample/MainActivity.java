package vn.tiki.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import intents.Intents;
import vn.tiki.sample.login.LoginActivity;
import vn.tiki.sample.productlist.ProductListingActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btOpenExtraInjection)
  public void openExtraInjection(View view) {
    final Intent intent = Intents.extraInjectionActivity(this)
        .name("Giang")
        .age(29)
        .make();
    startActivity(intent);
  }

  @OnClick(R.id.btOpenLogin)
  public void openLogin(View view) {
    startActivity(LoginActivity.intent(this));
  }

  @OnClick(R.id.btOpenProductListing)
  public void openProductListing(View view) {
    startActivity(ProductListingActivity.intent(this));
  }
}
