package vn.tiki.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import intents.Intents;
import vn.tale.viewholdersdemo.ViewHoldersDemoActivity;
import vn.tiki.sample.base.BaseActivity;
import vn.tiki.sample.collectionview.CollectionViewActivity;
import vn.tiki.sample.login.LoginActivity;
import vn.tiki.sample.productlist.ProductListingActivity;
import vn.tiki.sample.redux.ReduxDemoActivity;

public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_cart, menu);
    return true;
  }

  @OnClick(R.id.btOpenCollectionView)
  public void openCollectionView(View view) {
    startActivity(CollectionViewActivity.intent(this));
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

  @OnClick(R.id.btOpenReduxDemo)
  public void openReduxDemo(View view) {
    startActivity(new Intent(this, ReduxDemoActivity.class));
  }

  @OnClick(R.id.btOpenViewHoldersDemo)
  public void openViewHoldersDemo(View view) {
    startActivity(new Intent(this, ViewHoldersDemoActivity.class));
  }
}
