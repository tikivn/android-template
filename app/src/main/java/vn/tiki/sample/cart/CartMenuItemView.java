package vn.tiki.sample.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ix.Ix;
import javax.inject.Inject;
import vn.tiki.daggers.Daggers;
import vn.tiki.sample.R;
import vn.tiki.sample.entity.CartItem;
import vn.tiki.sample.repository.CartRepository;

public class CartMenuItemView extends FrameLayout {
  @BindView(R.id.tvItemCount) TextView tvItemCount;
  @BindView(R.id.vProgress) ProgressBar vProgress;

  @Inject CartRepository cartRepository;
  private Disposable disposable;

  public CartMenuItemView(
      @NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    inflate(context, R.layout.cart_view_menu_item, this);
    ButterKnife.bind(this);
    Daggers.inject(this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    showLoading();
    disposable = cartRepository.getAllItems()
        .map(cartItems -> Ix.from(cartItems)
            .map(CartItem::getQuantity)
            .reduce((i, i2) -> i + i2)
            .first(0))
        .map(String::valueOf)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(text -> {
          hideLoading();
          tvItemCount.setText(text);
        });
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (disposable != null) {
      disposable.dispose();
    }
  }

  private void showLoading() {
    vProgress.setVisibility(VISIBLE);
    tvItemCount.setVisibility(GONE);
  }

  private void hideLoading() {
    vProgress.setVisibility(GONE);
    tvItemCount.setVisibility(VISIBLE);
  }
}
