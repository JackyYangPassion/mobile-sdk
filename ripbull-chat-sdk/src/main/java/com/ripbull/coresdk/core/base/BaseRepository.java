package com.ripbull.coresdk.core.base;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.ripbull.ertc.cache.database.DataSource;
import com.ripbull.ertc.cache.preference.PreferenceManager;
import com.ripbull.ertc.remote.NetworkManager;
import com.ripbull.coresdk.data.DataManager;

/** Created by DK on 2019-05-04. */
public class BaseRepository {

  private final DataManager dataManager;

  protected BaseRepository(DataManager dataManager) {
    this.dataManager = dataManager;
  }

  protected DataManager data() {
    return dataManager;
  }

  protected DataSource db() {
    return dataManager.db();
  }

  protected NetworkManager network() {
    return dataManager.network();
  }

  protected PreferenceManager preference() {
    return dataManager.preference();
  }

  protected String getTenantId() {
    return data().preference().getTenantId();
  }

  protected String getUserId() {
    return data().preference().getUserId();
  }

  protected String getAppUserId() {
    return data().preference().getAppUserId();
  }

  protected String getApiKey() {
    return data().preference().getApiKey();
  }

  protected String getChatUserId() {
    return data().preference().getChatUserId();
  }

  protected String getName() {
    return data().preference().getName();
  }

  protected String getFCMToken() {
    return data().preference().getFcmToken();
  }

  protected String getDeviceId() {
    return data().preference().getDeviceId();
  }

  @NonNull
  protected <R> R withTenant(Function<String, R> function, Supplier<R> errorSupplier) {
    final String tenantId = data().preference().getTenantId();
    if (tenantId != null && !TextUtils.isEmpty(tenantId)) {
      return function.apply(tenantId);
    }
    return errorSupplier.get();
  }
}
