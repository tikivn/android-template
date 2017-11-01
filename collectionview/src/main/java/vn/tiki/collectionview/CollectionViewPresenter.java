package vn.tiki.collectionview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

class CollectionViewPresenter {

  static final LoadingItem LOADING_ITEM = new LoadingItem();
  CollectionView collectionView;
  @NonNull private final DataProvider<?> dataProvider;
  @Nullable private Disposable disposable;
  @Nullable private ListData<?> listData;

  CollectionViewPresenter(@NonNull DataProvider<?> dataProvider) {
    this.dataProvider = dataProvider;
  }

  void attach(CollectionView collectionView) {
    this.collectionView = collectionView;
    onLoad();
  }

  void detach() {
    disposeDisposable();
    collectionView = null;
  }

  void onLoad() {
    collectionView.showLoading();
    disposable = map(dataProvider.fetch(1))
        .map(includeLoading())
        .subscribe(
            new Consumer<List<?>>() {
              @Override
              public void accept(List<?> items) throws Exception {
                collectionView.showContent();
                collectionView.setItems(items);
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                collectionView.showError(throwable);
              }
            });
  }

  void onLoadMore() {
    disposeDisposable();
    if (listData == null || isCurrentLastPage(listData.paging())) {
      return;
    }
    final int nextPage = listData.paging().currentPage() + 1;
    final List<?> currentItems = listData.items();
    disposable = map(dataProvider.fetch(nextPage))
        .map(new Function<List<?>, List<?>>() {
          @Override
          public List<?> apply(List<?> objects) throws Exception {
            return concat(currentItems, objects);
          }
        })
        .map(includeLoading())
        .subscribe(
            new Consumer<List<?>>() {
              @Override
              public void accept(List<?> items) throws Exception {
                collectionView.setItems(items);
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                collectionView.hideLoadMore();
              }
            });
  }

  void onRefresh() {
    disposeDisposable();
    disposable = map(dataProvider.fetchNewest())
        .map(includeLoading())
        .subscribe(
            new Consumer<List<?>>() {
              @Override
              public void accept(List<?> items) throws Exception {
                collectionView.hideRefreshing();
                collectionView.setItems(items);
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                collectionView.hideRefreshing();
              }
            });
  }

  private void disposeDisposable() {
    if (disposable != null) {
      disposable.dispose();
    }
  }

  @NonNull
  private Function<List<?>, List<?>> includeLoading() {
    return new Function<List<?>, List<?>>() {
      @Override
      public List<?> apply(List<?> objects) throws Exception {
        if (listData == null || isCurrentLastPage(listData.paging())) {
          return objects;
        }
        return append(objects, LOADING_ITEM);
      }
    };
  }

  private boolean isCurrentLastPage(@NonNull Paging paging) {
    return paging.currentPage() == paging.lastPage();
  }

  private Single<List<?>> map(Single<? extends ListData<?>> source) {
    return source
        .doOnEvent(new BiConsumer<ListData<?>, Throwable>() {
          @Override
          public void accept(final ListData<?> listData, final Throwable throwable) throws Exception {
            if (throwable == null) {
              CollectionViewPresenter.this.listData = listData;
            } else {
              throwable.printStackTrace();
            }
          }
        })
        .map(new Function<ListData<?>, List<?>>() {
          @Override
          public List<?> apply(ListData<?> listData) throws Exception {
            return listData.items();
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  private static List<?> append(List<?> list1, Object item) {
    final int size = list1.size() + 1;
    final ArrayList<Object> result = new ArrayList<>(size);
    result.addAll(list1);
    result.add(item);
    return result;
  }

  private static List<?> concat(List<?> list1, List<?> list2) {
    final int size = list1.size() + list2.size();
    final ArrayList<Object> result = new ArrayList<>(size);
    result.addAll(list1);
    result.addAll(list2);
    return result;
  }
}
