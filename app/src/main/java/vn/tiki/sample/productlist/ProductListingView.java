package vn.tiki.sample.productlist;

import java.util.List;
import vn.tale.viewholdersdemo.viewholder.ProductModel;
import vn.tiki.architecture.mvp.Mvp;

public interface ProductListingView extends Mvp.View {

  void showStartLoading();

  void showRefreshLoading();

  void showLoadMoreLoading();

  void showStartError();

  void showRefreshError();

  void showLoadMoreError();

  void showContent(List<ProductModel> products);

}
