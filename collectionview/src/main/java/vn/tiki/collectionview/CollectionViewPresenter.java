package vn.tiki.collectionview;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

class CollectionViewPresenter {

  private final DataProvider<?> dataProvider;
  CollectionView collectionView;
  private ListData<?> listData;
  private Disposable disposable;

  CollectionViewPresenter(DataProvider<?> dataProvider) {
    this.dataProvider = dataProvider;
  }

  void attach(CollectionView collectionView) {
    this.collectionView = collectionView;
    onLoad();
  }

  private void onLoad() {
    collectionView.showLoading();
    disposable = map(dataProvider.fetch(1))
        .subscribe(
            new Consumer<List<?>>() {
              @Override public void accept(List<?> items) throws Exception {
                collectionView.showContent();
                collectionView.setItems(items);
                checkToHideLoadMore();
              }
            },
            new Consumer<Throwable>() {
              @Override public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                collectionView.showError(throwable);
              }
            });
  }

  private Observable<List<?>> map(Observable<? extends ListData<?>> source) {
    return source
        .doOnNext(new Consumer<ListData<?>>() {
          @Override public void accept(ListData<?> listData) throws Exception {
            CollectionViewPresenter.this.listData = listData;
          }
        })
        .map(new Function<ListData<?>, List<?>>() {
          @Override public List<?> apply(ListData<?> listData) throws Exception {
            return listData.items();
          }
        })
        .doOnError(new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  private void checkToHideLoadMore() {
    final Paging paging = listData.paging();
    if (paging.currentPage() == paging.lastPage()) {
      collectionView.hideLoadMore();
    }
  }

  void detach() {
    disposeDisposable();
    collectionView = null;
  }

  private void disposeDisposable() {
    if (disposable != null) {
      disposable.dispose();
    }
  }

  void onRefresh() {
    disposeDisposable();
    disposable = map(dataProvider.fetchNewest())
        .subscribe(
            new Consumer<List<?>>() {
              @Override public void accept(List<?> items) throws Exception {
                collectionView.hideRefreshing();
                collectionView.setItems(items);
                checkToHideLoadMore();
              }
            },
            new Consumer<Throwable>() {
              @Override public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                collectionView.hideRefreshing();
              }
            });
  }

  void onLoadMore() {
    disposeDisposable();
    if (listData == null) {
      return;
    }
    final List<?> currentItems = listData.items();
    final int nextPage = listData.paging().currentPage() + 1;
    disposable = map(dataProvider.fetch(nextPage))
        .subscribe(
            new Consumer<List<?>>() {
              @Override public void accept(List<?> items) throws Exception {
                final int size = currentItems.size() + items.size();
                final ArrayList<Object> mergedItems = new ArrayList<>(size);
                mergedItems.addAll(currentItems);
                mergedItems.addAll(items);
                collectionView.setItems(mergedItems);
                checkToHideLoadMore();
              }
            },
            new Consumer<Throwable>() {
              @Override public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                collectionView.hideLoadMore();
              }
            });
  }
}
