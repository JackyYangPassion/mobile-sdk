package io.inappchat.inappchat.cache.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import java.util.List;

/** @author meeth */
public interface BaseDao<T> {

  @Insert
  void insert(T t);

  @Insert(onConflict = OnConflictStrategy.FAIL)
  void insertWithFail(T t);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insertWithIgnore(T t);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertWithReplace(T t);

  @Insert(onConflict = OnConflictStrategy.ROLLBACK)
  void insertWithRollback(T t);

  @Insert(onConflict = OnConflictStrategy.ABORT)
  void insertWithAbort(T t);

  @Update
  void update(T t);

  @Insert
  void insert(List<T> list);

  @Insert(onConflict = OnConflictStrategy.FAIL)
  void insertWithFail(List<T> list);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insertWithIgnore(List<T> list);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertWithReplace(List<T> list);

  @Insert(onConflict = OnConflictStrategy.ROLLBACK)
  void insertWithRollback(List<T> list);

  @Delete
  void delete(T t);

  @Delete
  void delete(List<T> list);
}
