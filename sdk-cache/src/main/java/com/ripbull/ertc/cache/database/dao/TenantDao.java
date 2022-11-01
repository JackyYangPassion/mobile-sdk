package com.ripbull.ertc.cache.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ripbull.ertc.cache.database.entity.Tenant;
import com.ripbull.ertc.cache.database.entity.TenantAndConfig;
import com.ripbull.ertc.cache.database.entity.TenantConfig;

import io.reactivex.Single;
import java.util.List;

/** Created by DK on 22/12/18. */
@Dao
public interface TenantDao extends BaseDao<Tenant> {
  String TABLE_NAME = "tenant";

  @Transaction
  @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :id")
  Single<TenantAndConfig> getTenant(String id);

  @Query(
      "SELECT config_value FROM "
          + "tenant_config"
          + " WHERE tenant_id = :id AND config_key = :key")
  Single<String> getTenantConfigValue(String id, String key);

  @Query(
      "SELECT config_value FROM "
          + "tenant_config"
          + " WHERE tenant_id = :id AND config_key = :key")
  String getFeatureEnabled(String id, String key);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void save(List<TenantConfig> list);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void save(Tenant tenant);

  @Transaction
  @Query("SELECT * FROM Tenant")
  TenantAndConfig getAll();

  @Query("DELETE FROM tenant")
  void deleteAll();
}
