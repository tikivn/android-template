package vn.tiki.sample.repository;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import vn.tiki.sample.database.CartDao;
import vn.tiki.sample.entity.CartItem;
import vn.tiki.sample.entity.Product;

@Singleton
public class CartRepository {

  private final BehaviorSubject<List<CartItem>> cartItemLists = BehaviorSubject.createDefault(
      Collections.emptyList());
  private final PublishSubject<CartItem> inserts = PublishSubject.create();

  @Inject
  public CartRepository(CartDao cartDao) {
    final Observable<List<CartItem>> querySource = Observable.fromCallable(cartDao::getAll)
        .subscribeOn(Schedulers.io());
    final Observable<List<CartItem>> insertSource = inserts
        .switchMap(cartItem -> Observable.fromCallable(
            () -> {
              final CartItem existingCartItem = cartDao.getById(cartItem.getId());
              if (existingCartItem == null) {
                return cartDao.insert(cartItem);
              } else {
                final int updatedQuantity = existingCartItem.getQuantity() + cartItem.getQuantity();
                existingCartItem.setQuantity(updatedQuantity);
                return cartDao.update(existingCartItem);
              }
            })
            .subscribeOn(Schedulers.io()))
        .flatMap(__ -> querySource);

    Observable.merge(querySource, insertSource)
        .subscribe(cartItemLists);
  }

  public Flowable<List<CartItem>> getAllItems() {
    return cartItemLists.toFlowable(BackpressureStrategy.LATEST);
  }

  public void addToCart(Product product, String size, int quantity) {
    final CartItem cartItem = new CartItem(
        makeCartId(product.id(), size),
        product.id(),
        size,
        quantity);
    inserts.onNext(cartItem);
  }

  private String makeCartId(String id, String size) {
    return id + "_" + size;
  }
}
