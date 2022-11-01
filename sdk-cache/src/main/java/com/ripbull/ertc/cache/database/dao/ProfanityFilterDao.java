package com.ripbull.ertc.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;

import com.ripbull.ertc.cache.database.entity.ProfanityFilter;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProfanityFilterDao extends BaseDao<ProfanityFilter> {
  String TABLE_NAME = "profanity_filter";

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE tenant_id = (:tenantId)")
  Flowable<List<ProfanityFilter>> getProfanityFilter(@NonNull String tenantId);

  @Query("DELETE FROM " + TABLE_NAME)
  void deleteAll();
}
