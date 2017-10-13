package vn.tiki.sample.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public final class CartItem {
  @PrimaryKey
  @NonNull private final String id;
  @NonNull private final String productId;
  private int quantity;
  @NonNull private final String size;

  public CartItem(
      @NonNull String id,
      @NonNull String productId,
      @NonNull String size,
      int quantity) {
    this.id = id;
    this.productId = productId;
    this.size = size;
    this.quantity = quantity;
  }

  @NonNull public String getId() {
    return id;
  }

  @NonNull public String getProductId() {
    return productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @NonNull
  public String getSize() {
    return size;
  }
}
