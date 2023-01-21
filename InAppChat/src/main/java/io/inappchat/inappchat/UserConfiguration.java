package io.inappchat.inappchat;

import android.content.Context;
import androidx.annotation.NonNull;

/** Created by Sagar on 02/05/22. */
public class UserConfiguration {

  private final String name;
  private final String status;
  private final String appUserId;
  private final String loginType;
  private final String profilePic;
  private final String profilePicThumb;
  private final String profileStatus;
  private final String appState;
  private final String userId;
  private final String accessToken;
  private final String refreshToken;
  private final String fcmToken;
  private final String payload;
  private final Context context;

  private UserConfiguration(UserConfiguration.Builder builder) {
    this.name = builder.name;
    this.status = builder.status;
    this.appUserId = builder.appUserId;
    this.loginType = builder.loginType;
    this.profilePic = builder.profilePic;
    this.profilePicThumb = builder.profilePicThumb;
    this.profileStatus = builder.profileStatus;
    this.appState = builder.appState;
    this.userId = builder.userId;
    this.accessToken = builder.accessToken;
    this.refreshToken = builder.refreshToken;
    this.fcmToken = builder.fcmToken;
    this.payload = builder.payload;
    this.context = builder.context;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public String getAppUserId() {
    return appUserId;
  }

  public String getLoginType() {
    return loginType;
  }

  public String getProfilePic() {
    return profilePic;
  }

  public String getProfilePicThumb() {
    return profilePicThumb;
  }

  public String getProfileStatus() {
    return profileStatus;
  }

  public String getAppState() {
    return appState;
  }

  public String getUserId() {
    return userId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getFcmToken() {
    return fcmToken;
  }

  public String getPayload() {
    return payload;
  }

  public Context getContext() {
    return context;
  }

  public static class Builder {
    private String name;
    private String status;
    private String appUserId;
    private String loginType;
    private String profilePic;
    private String profilePicThumb;
    private String profileStatus;
    private String appState;
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String fcmToken;
    private String payload;
    private Context context;

    public Builder() {}

    public UserConfiguration.Builder name(@NonNull String name) {
      this.name = name;
      return this;
    }

    public UserConfiguration.Builder status(@NonNull String status) {
      this.status = status;
      return this;
    }

    public UserConfiguration.Builder appUserId(@NonNull String appUserId) {
      this.appUserId = appUserId;
      return this;
    }

    public UserConfiguration.Builder loginType(@NonNull String loginType) {
      this.loginType = loginType;
      return this;
    }

    public UserConfiguration.Builder profilePic(@NonNull String profilePic) {
      this.profilePic = profilePic;
      return this;
    }

    public UserConfiguration.Builder profilePicThumb(@NonNull String profilePicThumb) {
      this.profilePicThumb = profilePicThumb;
      return this;
    }

    public UserConfiguration.Builder profileStatus(@NonNull String profileStatus) {
      this.profileStatus = profileStatus;
      return this;
    }

    public UserConfiguration.Builder appState(@NonNull String appState) {
      this.appState = appState;
      return this;
    }

    public UserConfiguration.Builder userId(@NonNull String userId) {
      this.userId = userId;
      return this;
    }
    public UserConfiguration.Builder accessToken(@NonNull String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public UserConfiguration.Builder refreshToken(@NonNull String refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    public UserConfiguration.Builder fcmToken(@NonNull String fcmToken) {
      this.fcmToken = fcmToken;
      return this;
    }

    public UserConfiguration.Builder payload(@NonNull String payload) {
      this.payload = payload;
      return this;
    }

    public UserConfiguration.Builder context(@NonNull Context context) {
      this.context = context;
      return this;
    }

    public UserConfiguration build() {
      return new UserConfiguration(this);
    }
  }
}
