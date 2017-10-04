package vn.tiki.sample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import vn.tiki.sample.entity.CartItem;

@Database(entities = { CartItem.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  public abstract CartDao cartDao();
}