package vn.tiki.sample.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import vn.tiki.sample.entity.CartItem

@Dao
interface CartDao {

  @Query("SELECT * FROM CartItem")
  fun getAll(): List<CartItem>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(cartItem: CartItem): Long

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(cartItem: CartItem): Int

  @Query("SELECT * FROM CartItem WHERE id = :id LIMIT 1")
  fun getById(id: String): CartItem
}
