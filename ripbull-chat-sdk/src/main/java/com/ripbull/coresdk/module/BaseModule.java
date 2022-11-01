package com.ripbull.coresdk.module;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.ripbull.ertc.cache.database.DataSource;
import com.ripbull.ertc.cache.database.entity.User;
import com.ripbull.ertc.cache.preference.PreferenceManager;
import com.ripbull.ertc.remote.NetworkManager;
import com.ripbull.coresdk.data.DataManager;

/** @author meeth */
public abstract class BaseModule {

  private final DataManager dataManager;

  protected BaseModule(DataManager dataManager) {
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

  protected String _getChatUserId() {
    return data().preference().getChatUserId();
  }

  protected String getFCMToken() {
    return data().preference().getFcmToken();
  }

  protected String getDeviceId() {
    return data().preference().getDeviceId();
  }

  private User cachedUser;

  public User loggedInUser() {
    String userId = getUserId();
    String tenantId = getTenantId();
    if (cachedUser == null || !cachedUser.getId().equals(userId)) {
      if (TextUtils.isEmpty(userId)) {
        cachedUser = data().db().userDao().getUserByIdInSync(tenantId, userId);
      } else {
        cachedUser = null;
      }
    }
    return cachedUser;
  }

  @NonNull
  protected <R> R withTenant(Function<String, R> function, Supplier<R> errorSupplier) {
    final String tenantId = data().preference().getTenantId();
    if (tenantId != null && !TextUtils.isEmpty(tenantId)) {
      return function.apply(tenantId);
    }
    return errorSupplier.get();
  }

  @NonNull
  protected <R> R withChatUserId(Function<String, R> function, Supplier<R> errorSupplier) {
    final String userId = data().preference().getUserId();
    if (userId != null && !TextUtils.isEmpty(userId)) {
      return function.apply(userId);
    }
    return errorSupplier.get();
  }
}
