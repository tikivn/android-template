package vn.tiki.sample.productdetail;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import intents.Intents;
import javax.inject.Inject;
import vn.tiki.daggers.Daggers;
import vn.tiki.intents.BindExtra;
import vn.tiki.sample.R;
import vn.tiki.sample.base.BaseMvpActivity;
import vn.tiki.sample.entity.Product;
import vn.tiki.sample.glide.GlideApp;
import vn.tiki.sample.util.Strings;
import vn.tiki.sample.util.TextViews;
import vn.tiki.sample.util.Urls;

public class ProductDetailActivity
    extends BaseMvpActivity<ProductDetailView, ProductDetailPresenter>
    implements ProductDetailView {

  @BindView(R.id.ivThumb) ImageView ivThumb;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.tvPrice) TextView tvPrice;
  @BindView(R.id.spSize) Spinner spSize;
  @BindView(R.id.tvDescription) TextView tvDescription;

  @Inject ProductDetailPresenter presenter;

  @BindExtra Product product;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Daggers.inject(this);
    Intents.bind(this);

    setContentView(R.layout.activity_product_detail);
    ButterKnife.bind(this);

    configureSizeOptions();
    connect(presenter, this);
    bind(product);
    presenter.setProduct(product);
  }

  @OnClick(R.id.btAddToCart)
  public void onAddToCartClicked() {
    presenter.onAddToCartClick();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_cart, menu);
    return true;
  }

  private void bind(Product product) {
    tvName.setText(product.title());
    TextViews.setPrice(tvPrice, product.price());
    TextViews.setHtml(tvDescription, Strings.toHtml(product.description()).toString());
    GlideApp
        .with(this)
        .load(Urls.resolveImageUrl(product.image()))
        .into(ivThumb);
  }

  private void configureSizeOptions() {
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.size_options, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    spSize.setAdapter(adapter);
    spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        final CharSequence selectedOption = adapter.getItem(position);
        if (selectedOption == null) {
          return;
        }
        presenter.onSizeSelected(selectedOption.toString());
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }
}
