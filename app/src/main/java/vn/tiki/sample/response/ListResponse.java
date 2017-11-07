package vn.tiki.sample.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ListResponse<T> {

  @SerializedName("current_Page")
  private int currentPage;
  @SerializedName("items")
  private List<T> items;
  @SerializedName("last_page")
  private int lastPage;
  @SerializedName("total")
  private int total;

  public int getCurrentPage() {
    return currentPage;
  }

  public List<T> getItems() {
    return items;
  }

  public int getLastPage() {
    return lastPage;
  }

  public int getTotal() {
    return total;
  }

}