package vn.tiki.sample.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import vn.tiki.sample.entity.CartItem

@Database(entities = arrayOf(CartItem::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun cartDao(): CartDao
}