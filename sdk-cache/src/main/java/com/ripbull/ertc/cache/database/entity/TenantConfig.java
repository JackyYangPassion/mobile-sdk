package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/** Created by DK on 21/12/18. */
@Entity(
    tableName = "tenant_config",
    primaryKeys = {"tenant_id", "config_key"},
    foreignKeys =
        @ForeignKey(
            entity = Tenant.class,
            parentColumns = "id",
            childColumns = "tenant_id",
            onDelete = ForeignKey.CASCADE))
public class TenantConfig {

  @NonNull
  @ColumnInfo(name = "tenant_id")
  private String tenantId = "";

  @NonNull
  @ColumnInfo(name = "config_key")
  private String key = "";

  @NonNull
  @ColumnInfo(name = "config_value")
  private String value = "";

  public TenantConfig(@NonNull String tenantId, @NonNull String key, @NonNull String value) {
    this.tenantId = tenantId;
    this.key = key;
    this.value = value;
  }

  @NonNull
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(@NonNull String tenantId) {
    this.tenantId = tenantId;
  }

  @NonNull
  public String getKey() {
    return key;
  }

  public void setKey(@NonNull String key) {
    this.key = key;
  }

  @NonNull
  public String getValue() {
    return value;
  }

  public void setValue(@NonNull String value) {
    this.value = value;
  }
}
