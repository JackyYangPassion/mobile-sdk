package io.inappchat.inappchat.tenant.interactor;

import io.inappchat.inappchat.UserConfiguration;
import io.inappchat.inappchat.core.type.AccountDetails;
import io.inappchat.inappchat.data.common.Result;
import io.reactivex.rxjava3.core.Single;

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
