package vn.tiki.sample.productlist

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.tale.viewholdersdemo.viewholder.ProductModel
import vn.tiki.architecture.redux.Epic
import vn.tiki.architecture.redux.Store
import vn.tiki.architecture.redux.createStore
import vn.tiki.sample.entity.ListData
import vn.tiki.sample.entity.Paging
import vn.tiki.sample.entity.Product
import vn.tiki.sample.mvp.rx.RxBasePresenter
import vn.tiki.sample.productlist.State.Idle
import vn.tiki.sample.productlist.State.LoadMoreError
import vn.tiki.sample.productlist.State.LoadMoreLoading
import vn.tiki.sample.productlist.State.RefreshError
import vn.tiki.sample.productlist.State.RefreshLoading
import vn.tiki.sample.productlist.State.StartError
import vn.tiki.sample.productlist.State.StartLoading
import vn.tiki.sample.productlist.State.Success
import vn.tiki.sample.repository.ProductRepository
import javax.inject.Inject

val page0: Paging = Paging.builder()
    .currentPage(0)
    .lastPage(0)
    .total(0)
    .make()

sealed class State {
  object Idle : State()
  object StartLoading : State()
  object RefreshLoading : State()
  object LoadMoreLoading : State()
  object StartError : State()
  object RefreshError : State()
  object LoadMoreError : State()
  object Success : State()
}

data class Model(val state: State, val paging: Paging = page0, val products: List<ProductModel> = emptyList())

object LoadAction
object LoadMoreAction
object RefreshAction

class ProductListingPresenter @Inject constructor(productRepository: ProductRepository) : RxBasePresenter<ProductListingView>() {

  private val store: Store<Model>

  init {
    val map: (ListData<Product>) -> Pair<Paging, List<ProductModel>> = {
      val paging = Paging.builder()
          .currentPage(it.currentPage())
          .lastPage(it.lastPage())
          .total(it.total())
          .make()

      val productModels = it.items()
          .map { ProductModel(it.title(), it.price(), it.image()) }

      Pair(paging, productModels)
    }

    val load = Epic<Model> { actions, getModel ->
      actions
          .filter { it is LoadAction }
          .filter {
            val model = getModel()
            val state = model.state
            state !is StartLoading
                && state !is RefreshLoading
                && state !is LoadMoreLoading
          }
          .switchMap {
            productRepository.getProducts(1)
                .toObservable()
                .doOnError(Throwable::printStackTrace)
                .map { map(it) }
                .map { Model(Success, it.first, it.second) }
                .onErrorReturnItem(Model(StartError))
                .subscribeOn(Schedulers.io())
                .startWith(Model(StartLoading))
          }
    }

    val refresh = Epic<Model> { actions, getModel ->
      actions
          .filter { it is RefreshAction }
          .filter {
            val state = getModel().state
            state !is StartLoading
                && state !is RefreshLoading
          }
          .switchMap {
            val model = getModel()
            productRepository.fetchProducts(model.paging.currentPage() + 1)
                .toObservable()
                .map { map(it) }
                .map { Model(Success, it.first, it.second) }
                .onErrorReturnItem(model.copy(state = RefreshError))
                .subscribeOn(Schedulers.io())
                .startWith(model.copy(state = RefreshLoading))
          }
    }

    val loadMore = Epic<Model> { actions, getModel ->
      val model = getModel()
      actions
          .filter { it is LoadMoreAction }
          .filter {
            model.state !is RefreshLoading
          }
          .switchMap {
            val refreshAction: Observable<Any> = actions.filter { it is RefreshAction }
            productRepository.getProducts(1)
                .toObservable()
                .takeUntil(refreshAction)
                .map { map(it) }
                .map { Model(Success, it.first, model.products + it.second) }
                .onErrorReturnItem(model.copy(state = LoadMoreError))
                .subscribeOn(Schedulers.io())
                .startWith(model.copy(state = LoadMoreLoading))
          }
    }

    store = createStore(
        initialState = Model(Idle),
        epics = listOf(load, refresh, loadMore),
        debug = true)

    disposeOnDestroy(store.getState()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { model ->
          sendToView { view ->
            when (model.state) {
              StartLoading -> view.showStartLoading()
              RefreshLoading -> view.showRefreshLoading()
              LoadMoreLoading -> view.showLoadMoreLoading()

              StartError -> view.showStartError()
              RefreshError -> view.showRefreshError()
              LoadMoreError -> view.showLoadMoreError()

              is Success -> view.showContent(model.products)
            }
          }
        })
  }

  fun onLoad() {
    store.dispatch(LoadAction)
  }

  fun onRefresh() {
    store.dispatch(RefreshAction)
  }

  fun onLoadMore() {
    store.dispatch(LoadMoreAction)
  }

  override fun destroy() {
    super.destroy()
    store.destroy()
  }
}