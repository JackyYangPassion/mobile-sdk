package com.ripbull.coresdk.tenant.interactor;

import com.ripbull.coresdk.UserConfiguration;
import com.ripbull.coresdk.core.type.AccountDetails;
import com.ripbull.coresdk.data.common.Result;
import io.reactivex.Single;

/** Created by DK on 05/12/18. */
public interface AuthenticationRepository {

  Single<Result> validateNamespace(String url);

  Single<Result> login(AccountDetails details);

  Single<Result> connect(AccountDetails details);

  Single<Result> forgotPassword(AccountDetails details);

  Single<Result> changePassword(AccountDetails details);

  Boolean isTenantValidated();

  Boolean isUserAuthenticated();

  Single logout();

  String getLoginType();

  Single<Result> logoutOtherDevices();

  Single<Result> updateFeatureFlag(String nameSpace);

  String projectType();

  Single<Result> configureUser(UserConfiguration userConfiguration);
}
