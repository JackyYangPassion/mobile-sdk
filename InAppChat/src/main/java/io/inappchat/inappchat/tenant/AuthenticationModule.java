package io.inappchat.inappchat.tenant;

import io.inappchat.inappchat.UserConfiguration;
import io.inappchat.inappchat.core.type.AccountDetails;
import io.inappchat.inappchat.data.common.Result;
import io.reactivex.rxjava3.core.Single;

/** Created by DK on 24/11/18. */
public interface AuthenticationModule {
  Single<Result> validate(String namespace);

  Single<Result> login(AccountDetails details);

  Single<Result> connect(AccountDetails details);

  Single<Result> changePassword(AccountDetails details);

  Single<Result> forgotPassword(AccountDetails details);

  Boolean isTenantValidated();

  Boolean isUserAuthenticated();

  Single<Result> updateFeatureFlag();

  Single<Result> logoutOtherDevices();

  String projectType();

  Single<Result> configureUser(UserConfiguration userConfiguration);
}
