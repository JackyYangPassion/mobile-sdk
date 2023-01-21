package io.inappchat.inappchat;

import android.content.Context;


import androidx.annotation.NonNull;

/** Created by DK on 15/11/18. */

public class Configuration {

  private final String apiKey;
  private final String namespace;
  private final Context context;

  private Configuration(Builder builder) {
    this.apiKey = builder.apiKey;
    this.namespace = builder.namespace;
    this.context = builder.context;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getNamespace() {
    return namespace;
  }

  public Context getContext() {
    return context;
  }

  public static class Builder {
    private String apiKey;
    private Context context;
    private String namespace;

    public Builder() {}

    public Builder apiKey(@NonNull String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder namespace(@NonNull String namespace) {
      this.namespace = namespace;
      return this;
    }


    public Builder context(@NonNull Context context) {
      this.context = context;
      return this;
    }

    public Configuration build() {
      return new Configuration(this);
    }
  }
}
