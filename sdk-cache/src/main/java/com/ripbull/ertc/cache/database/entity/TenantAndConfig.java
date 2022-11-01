package com.ripbull.ertc.cache.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

/** Created by DK on 21/12/18. */
public class TenantAndConfig {

  @Embedded private Tenant tenant;

  @Relation(parentColumn = "id", entityColumn = "tenant_id")
  private List<TenantConfig> configs;

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  public List<TenantConfig> getConfigs() {
    return configs;
  }

  public void setConfigs(List<TenantConfig> configs) {
    this.configs = configs;
  }
}
