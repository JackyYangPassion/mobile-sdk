package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Created by DK on 21/12/18. */
@Entity(tableName = "tenant")
public class Tenant {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private String id = "";

  @NonNull
  @ColumnInfo(name = "name")
  private String name = "";

  @NonNull
  @ColumnInfo(name = "api_key")
  private String apiKey = "";

  @NonNull
  @ColumnInfo(name = "name_space")
  private String namespace = "";

  public Tenant(
      @NonNull String id, @NonNull String name, @NonNull String apiKey, @NonNull String namespace) {
    this.id = id;
    this.name = name;
    this.apiKey = apiKey;
    this.namespace = namespace;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  @NonNull
  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(@NonNull String apiKey) {
    this.apiKey = apiKey;
  }

  @NonNull
  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(@NonNull String namespace) {
    this.namespace = namespace;
  }
}
