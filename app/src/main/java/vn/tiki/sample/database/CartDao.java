package vn.tiki.sample.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import vn.tiki.sample.entity.CartItem;

@Dao
public interface CartDao {

  @Query("SELECT * FROM CartItem")
  List<CartItem> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(CartItem cartItem);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(CartItem cartItem);

  @Query("SELECT * FROM CartItem WHERE id = :id LIMIT 1")
  CartItem getById(String id);
}
