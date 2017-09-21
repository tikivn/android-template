package vn.tiki.sample.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ListResponse<T> {

  @SerializedName("total")
  private int total;

  @SerializedName("last_page")
  private int lastPage;

  @SerializedName("current_Page")
  private int currentPage;

  @SerializedName("items")
  private List<T> items;

  public int getTotal() {
    return total;
  }

  public int getLastPage() {
    return lastPage;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public List<T> getItems() {
    return items;
  }
}