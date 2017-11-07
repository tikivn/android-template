package vn.tiki.sample.entity;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.util.List;

@com.google.auto.value.AutoValue
public abstract class ListData<T> {

  @SerializedName("current_Page")
  public abstract int currentPage();

  @SerializedName("items")
  public abstract List<T> items();

  @SerializedName("last_page")
  public abstract int lastPage();

  @SerializedName("total")
  public abstract int total();

  static <T> TypeAdapter<ListData<T>> typeAdapter(Gson gson, TypeToken<? extends ListData<T>> typeToken) {
    return new AutoValue_ListData.GsonTypeAdapter<>(gson, typeToken);
  }
}
