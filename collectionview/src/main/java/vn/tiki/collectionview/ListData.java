package vn.tiki.collectionview;

import java.util.List;

public interface ListData<T> {

  List<T> items();

  Paging paging();
}
