package vn.tiki.collectionview;

import io.reactivex.Observable;

public interface DataProvider<T> {

  /**
   * Which will be used to fetch data when user PullToRefresh
   *
   * @return an {@link Observable} object that will emit list of items
   */
  Observable<ListData<T>> fetchNewest();

  /**
   * Fetch data for the given page
   *
   * @param page the page number
   * @return an {@link Observable} object that will emit list of items
   */
  Observable<ListData<T>> fetch(int page);
}
