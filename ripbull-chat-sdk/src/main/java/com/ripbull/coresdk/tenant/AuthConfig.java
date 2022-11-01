package com.ripbull.coresdk.tenant;

import android.content.Context;
import androidx.annotation.NonNull;

/** Created by DK on 15/11/18. */

public class AuthConfig {

  private final String fcmToken;
  private final String payload;
  private final Context context;

  private AuthConfig(Builder builder) {
    this.fcmToken = builder.fcmToken;
    this.payload = builder.payload;
    this.context = builder.context;
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
    private String fcmToken;
    private Context context;
    private String payload;

    public Builder() {}

    public Builder fcmToken(@NonNull String fcmToken) {
      this.fcmToken = fcmToken;
      return this;
    }

    public Builder payload(@NonNull String payload) {
      this.payload = payload;
      return this;
    }


    public Builder context(@NonNull Context context) {
      this.context = context;
      return this;
    }

    public AuthConfig build() {
      return new AuthConfig(this);
    }
  }
}
