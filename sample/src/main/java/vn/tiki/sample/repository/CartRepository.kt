package vn.tiki.sample.repository

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.tiki.sample.database.CartDao
import vn.tiki.sample.entity.CartItem
import vn.tiki.sample.entity.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject
internal constructor(cartDao: CartDao) {

  private val cartItemLists = BehaviorSubject.createDefault(
      emptyList<CartItem>())
  private val inserts = PublishSubject.create<CartItem>()

  val allItems: Flowable<List<CartItem>>
    get() = cartItemLists.toFlowable(BackpressureStrategy.LATEST)

  init {
    val querySource = Observable.fromCallable<List<CartItem>>({ cartDao.getAll() })
        .subscribeOn(Schedulers.io())
    val insertSource = inserts
        .switchMap { (id, _, _, quantity) ->
          Observable.fromCallable {
            val existingCartItem = cartDao.getById(id)
            val updatedQuantity = existingCartItem.quantity + quantity
            existingCartItem.quantity = updatedQuantity
            cartDao.update(existingCartItem)
          }.subscribeOn(Schedulers.io())
        }
        .flatMap { querySource }

    Observable.merge(querySource, insertSource)
        .subscribe(cartItemLists)
  }

  fun addToCart(product: Product, size: String, quantity: Int) {
    val cartItem = CartItem(
        makeCartId(product.id, size),
        product.id,
        size,
        quantity)
    inserts.onNext(cartItem)
  }

  private fun makeCartId(id: String, size: String): String {
    return id + "_" + size
  }
}
