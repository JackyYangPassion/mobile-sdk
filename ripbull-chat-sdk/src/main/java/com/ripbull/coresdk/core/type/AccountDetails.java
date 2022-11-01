package com.ripbull.coresdk.core.type;

import com.ripbull.coresdk.tenant.AuthConfig;



/** Created by DK on 25/11/18. */
public class AccountDetails {

  private String username;
  private String password;
  private String newPassword;
  private String mobile;
  private Type type;
  private String userId;
  private AuthConfig authConfig;

  public enum Type {
    EMAIL("email"),
    MOBILE("mobile"),
    REGISTER("register"),
    SSO("sso");

    private String value;

    Type(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public static AccountDetails username(String email, String password) {
    AccountDetails a = new AccountDetails();
    a.type = Type.EMAIL;
    a.username = email;
    a.password = password;
    return a;
  }

  public static AccountDetails forgotPwd(String email) {
    AccountDetails a = new AccountDetails();
    a.type = Type.EMAIL;
    a.username = email;
    return a;
  }

  public static AccountDetails changePwd(String password, String newPassword) {
    AccountDetails a = new AccountDetails();
    a.type = Type.EMAIL;
    a.password = password;
    a.newPassword = newPassword;
    return a;
  }

  private static AccountDetails signUp(String email, String password) {
    AccountDetails a = new AccountDetails();
    a.type = Type.REGISTER;
    a.username = email;
    a.password = password;
    return a;
  }

  private static AccountDetails username(String mobile) {
    AccountDetails a = new AccountDetails();
    a.type = Type.MOBILE;
    a.mobile = mobile;
    return a;
  }

  public static AccountDetails singleSignOn(String userId, AuthConfig config) {
    AccountDetails a = new AccountDetails();
    a.type = Type.SSO;
    a.userId = userId;
    a.authConfig = config;
    return a;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  private String getMobile() {
    return mobile;
  }

  public Type getType() {
    return type;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public String getAppUserId() {
    return userId;
  }


  public AuthConfig getAuthConfig() {
    return authConfig;
  }
}
