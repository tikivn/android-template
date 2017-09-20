package vn.tiki.sample.productlist;

import java.util.List;
import vn.tiki.architecture.mvp.Mvp;
import vn.tiki.sample.entity.Product;

public interface ProductListingView extends Mvp.View {

  void showLoading();

  void showContent(List<Product> items);

  void showLoadError();
}
