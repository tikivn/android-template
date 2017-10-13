package vn.tiki.collectionview;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface DataProvider<T> {

  /**
   * Fetch data for the given page
   *
   * @param page the page number
   * @return an {@link Observable} object that will emit list of items
   */
  Single<? extends ListData<T>> fetch(int page);

  /**
   * Which will be used to fetch data when user PullToRefresh
   *
   * @return an {@link Observable} object that will emit list of items
   */
  Single<? extends ListData<T>> fetchNewest();
}
