package io.inappchat.inappchat.tenant;

import android.content.Context;
import io.inappchat.inappchat.R;
import io.inappchat.inappchat.UserConfiguration;
import io.inappchat.inappchat.core.ChatSDKException;
import io.inappchat.inappchat.core.type.AccountDetails;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.eRTCSDK;
import io.inappchat.inappchat.utils.Constants;
import io.reactivex.rxjava3.core.Single;

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