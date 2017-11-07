package vn.tiki.sample.util;

import io.reactivex.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {
  private Lists() {
    throw new InstantiationError();
  }

  @NonNull public static <T> List<T> merged(@NonNull List<T> list1, @NonNull List<T> list2) {
    if (list1.isEmpty()) {
      return list2;
    } else if (list2.isEmpty()) {
      return list1;
    } else {
      final ArrayList<T> mergedList = new ArrayList<>(list1.size() + list2.size());
      mergedList.addAll(list1);
      mergedList.addAll(list2);
      return mergedList;
    }
  }

  @NonNull public static <T> List<T> appended(@NonNull List<T> list, @NonNull T t) {
    if (list.isEmpty()) {
      return Collections.singletonList(t);
    } else {
      final ArrayList<T> finalList = new ArrayList<>(list.size() + 1);
      finalList.addAll(list);
      finalList.add(t);
      return finalList;
    }
  }
}
