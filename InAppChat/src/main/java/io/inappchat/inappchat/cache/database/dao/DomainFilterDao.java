package io.inappchat.inappchat.cache.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;

import io.inappchat.inappchat.cache.database.entity.DomainFilter;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DomainFilterDao extends BaseDao<DomainFilter> {
  String TABLE_NAME = "domain_filter";

  @Query("SELECT * FROM " + TABLE_NAME + " WHERE tenant_id = (:tenantId)")
  Flowable<List<DomainFilter>> getDomainFilter(@NonNull String tenantId);

  @Query("DELETE FROM " + TABLE_NAME)
  void deleteAll();
}
