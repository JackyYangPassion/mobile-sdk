package com.ripbull.ertc.remote;

import com.ripbull.ertc.remote.service.ChatApi;
import com.ripbull.ertc.remote.service.UserApi;
import io.reactivex.annotations.NonNull;

/** Created by DK on 15/11/18. */
public class NetworkConfig {

  private final String authApiKey;
  private final String authUrl;
  private final String chatApiKey;
  private final String chatUrl;
  private final String userApiKey;
  private final String userUrl;
  private final String packageName;
  private final String chatToken;
  private final String chatRefreshToken;
  private final String userToken;
  private final String userRefreshToken;
  private final String userId;
  private final String chatUserId;
  private final String tenantId;
  private final String deviceId;
  private ChatApi chatApi;
  private UserApi userApi;
  private final TokenChangeListener tokenChangeListener;

  private NetworkConfig(Builder builder) {
    this.authApiKey = builder.authApiKey;
    this.authUrl = builder.authUrl;
    this.chatApiKey = builder.chatApiKey;
    this.chatUrl = builder.chatUrl;
    this.userApiKey = builder.userApiKey;
    this.userUrl = builder.userUrl;
    this.packageName = builder.packageName;
    this.userToken = builder.userToken;
    this.userRefreshToken = builder.userRefreshToken;
    this.chatToken = builder.chatToken;
    this.chatRefreshToken = builder.chatRefreshToken;
    this.userId = builder.userId;
    this.chatUserId = builder.chatUserId;
    this.tenantId = builder.tenantId;
    this.deviceId = builder.deviceId;
    this.tokenChangeListener = builder.tokenChangeListener;
  }

  public String getAuthApiKey() {
    return authApiKey;
  }

  public String getAuthUrl() {
    return authUrl;
  }

  public String getChatApiKey() {
    return chatApiKey;
  }

  public String getChatUrl() {
    return chatUrl;
  }

  public String getUserApiKey() {
    return userApiKey;
  }

  public String getUserUrl() {
    return userUrl;
  }

  public String getChatToken() {
    return chatToken;
  }

  public String getChatRefreshToken() {
    return chatRefreshToken;
  }

  public String getUserToken() {
    return userToken;
  }

  public String getUserRefreshToken() {
    return userRefreshToken;
  }

  public String getUserId() {
    return userId;
  }

  public String getChatUserId() {
    return chatUserId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public ChatApi getChatApi() {
    return chatApi;
  }

  public void setChatApi(ChatApi chatApi) {
    this.chatApi = chatApi;
  }

  public UserApi getUserApi() {
    return userApi;
  }

  public void setUserApi(UserApi userApi) {
    this.userApi = userApi;
  }

  public TokenChangeListener getTokenChangeListener() {
    return tokenChangeListener;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public static class Builder {
    private String authApiKey;
    private String authUrl;
    private String chatApiKey;
    private String chatUrl;
    private String userApiKey;
    private String userUrl;
    private String packageName;
    private String chatToken;
    private String chatRefreshToken;
    private String userToken;
    private String userRefreshToken;
    private String userId;
    private String chatUserId;
    private String tenantId;
    private String deviceId;
    private TokenChangeListener tokenChangeListener;

    public Builder() {}

    public Builder authApiKey(@NonNull String apiKey) {
      this.authApiKey = apiKey;
      return this;
    }

    public Builder authUrl(@NonNull String url) {
      this.authUrl = url;
      return this;
    }

    public Builder chatApiKey(@NonNull String apiKey) {
      this.chatApiKey = apiKey;
      return this;
    }

    public Builder chatUrl(@NonNull String url) {
      if (url != null){
        this.chatUrl = url.substring(0, url.length() - 3);
      }
      return this;
    }

    public Builder userApiKey(@NonNull String apiKey) {
      this.userApiKey = apiKey;
      return this;
    }

    public Builder userUrl(@NonNull String url) {
      if (url != null){
        this.userUrl = url.substring(0, url.length() - 3);
      }
      return this;
    }

    public Builder userToken(@NonNull String token) {
      this.userToken = token;
      return this;
    }

    public Builder userRefreshToken(@NonNull String token) {
      this.userRefreshToken = token;
      return this;
    }

    public Builder chatToken(@NonNull String token) {
      this.chatToken = token;
      return this;
    }

    public Builder chatRefreshToken(@NonNull String token) {
      this.chatRefreshToken = token;
      return this;
    }

    public Builder packageName(@NonNull String packageName) {
      this.packageName = packageName;
      return this;
    }

    public Builder userId(@NonNull String userId) {
      this.userId = userId;
      return this;
    }

    public Builder chatUserId(@NonNull String chatUserId) {
      this.chatUserId = chatUserId;
      return this;
    }

    public Builder tenantId(@NonNull String tenantId) {
      this.tenantId = tenantId;
      return this;
    }

    public Builder deviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    public Builder tokenChangeListener(@NonNull TokenChangeListener listener) {
      this.tokenChangeListener = listener;
      return this;
    }

    public NetworkConfig build() {
      return new NetworkConfig(this);
    }
  }
}
