package vn.tiki.sample.entity;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import vn.tiki.collectionview.Paging;

@com.google.auto.value.AutoValue
public abstract class ListData<T> implements vn.tiki.collectionview.ListData<T> {

  private vn.tiki.sample.entity.Paging paging;

  @SerializedName("current_Page")
  public abstract int currentPage();

  @SerializedName("items")
  public abstract List<T> items();

  @SerializedName("last_page")
  public abstract int lastPage();

  @SerializedName("total")
  public abstract int total();

  @Override
  public Paging paging() {
    if (paging == null) {
      paging = vn.tiki.sample.entity.Paging.builder()
          .currentPage(currentPage())
          .lastPage(lastPage())
          .total(total())
          .make();
    }
    return paging;
  }

  static <T> TypeAdapter<ListData<T>> typeAdapter(Gson gson, TypeToken<? extends ListData<T>> typeToken) {
    return new AutoValue_ListData.GsonTypeAdapter<>(gson, typeToken);
  }
}
