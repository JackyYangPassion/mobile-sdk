package com.ripbull.coresdk.tenant;

import android.content.Context;
import com.ripbull.coresdk.R;
import com.ripbull.coresdk.UserConfiguration;
import com.ripbull.coresdk.core.ChatSDKException;
import com.ripbull.coresdk.core.type.AccountDetails;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.eRTCSDK;
import com.ripbull.coresdk.utils.Constants;
import io.reactivex.Single;

/** @author meeth */
public class AuthenticationModuleStub implements AuthenticationModule {

  private Context appContext;

  public static AuthenticationModule newInstance() {
    return new AuthenticationModuleStub(eRTCSDK.getAppContext());
  }

  private AuthenticationModuleStub(Context context) {
    this.appContext = context;
  }

  @Override
  public Single<Result> validate(String namespace) {
    return Single.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.NAMESPACE)));
  }

  @Override
  public Single<Result> login(AccountDetails details) {
    return Single.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.LOGIN)));
  }

  @Override
  public Single<Result> connect(AccountDetails details) {
    return Single.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.CONNECT)));
  }

  @Override
  public Single<Result> changePassword(AccountDetails details) {
    return Single.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.CHANGE_PASSWORD)));
  }

  @Override
  public Single<Result> forgotPassword(AccountDetails details) {
    return Single.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.FORGOT_PASSWORD)));
  }

  @Override
  public Boolean isTenantValidated() {
    return null;
  }

  @Override
  public Boolean isUserAuthenticated() {
    return null;
  }

  @Override
  public Single<Result> updateFeatureFlag() {
    return null;
  }

  @Override
  public Single<Result> logoutOtherDevices() {
    return Single.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.CHANGE_PASSWORD)));
  }

  @Override
  public String projectType() {
    return null;
  }

  @Override
  public Single<Result> configureUser(UserConfiguration userConfiguration) {
    return null;
  }
}