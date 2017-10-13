package vn.tiki.sample.collectionview;

import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import vn.tiki.collectionview.DataProvider;
import vn.tiki.collectionview.ListData;
import vn.tiki.collectionview.Paging;

public class TodoDataProvider implements DataProvider<String> {

  private static final int PER_PAGE = 5;
  private static final int LAST_PAGE = 10;

  @Override
  public Single<ListData<String>> fetch(int page) {
    return Single.fromCallable(() -> generateItems(page));
  }

  @Override
  public Single<ListData<String>> fetchNewest() {
    return Single.fromCallable(() -> generateItems(1));
  }

  private ListData<String> generateItems(int page) throws Exception {
    Thread.sleep(1000);
    if (System.currentTimeMillis() % 2 == 0) {
      throw new Exception("Error");
    }
    final int startIndex = (page - 1) * PER_PAGE;
    final List<String> result = new ArrayList<>(PER_PAGE);
    for (int i = 0; i < PER_PAGE; i++) {
      final int index = i + startIndex;
      result.add("Item " + index);
    }
    final Paging paging = Paging.builder()
        .currentPage(page)
        .lastPage(LAST_PAGE)
        .total(LAST_PAGE * PER_PAGE)
        .make();
    return new ListData<>(result, paging);
  }
}
