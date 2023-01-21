package io.inappchat.inappchat.tenant;

import io.inappchat.inappchat.UserConfiguration;
import io.inappchat.inappchat.core.type.AccountDetails;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.module.BaseModule;
import io.inappchat.inappchat.tenant.interactor.AuthenticationRepository;
import io.inappchat.inappchat.tenant.interactor.AuthenticationRepositoryImpl;
import io.reactivex.rxjava3.core.Single;

/** Created by DK on 05/12/18. */
public class AuthenticationModuleImpl extends BaseModule implements AuthenticationModule {

  private final AuthenticationRepository authenticationRepository;

  public static AuthenticationModule newInstance(DataManager dataManager) {
    AuthenticationRepository tenant = new AuthenticationRepositoryImpl(dataManager);
    return new AuthenticationModuleImpl(dataManager, tenant);
  }

  private AuthenticationModuleImpl(DataManager dataManager, AuthenticationRepository authenticationRepository) {
    super(dataManager);
    this.authenticationRepository = authenticationRepository;
  }

  private AuthenticationRepository auth() {
    return this.authenticationRepository;
  }

  @Override
  public Single<Result> validate(String namespace) {
    return auth().validateNamespace(namespace);
  }

  @Override
  public Single<Result> login(AccountDetails details) {
    return auth().login(details);
  }

  @Override
  public Single<Result> connect(AccountDetails details) {
    return auth().connect(details);
  }

  @Override
  public Boolean isTenantValidated() {
    return auth().isTenantValidated();
  }

  @Override
  public Boolean isUserAuthenticated() {
    return auth().isUserAuthenticated();
  }

  @Override
  public Single<Result> forgotPassword(AccountDetails details) {
    return auth().forgotPassword(details);
  }

  @Override
  public Single<Result> changePassword(AccountDetails details) {
    return auth().changePassword(details);
  }

  @Override
  public Single<Result> updateFeatureFlag() {
    return auth().updateFeatureFlag(data().db().tenantDao().getAll().getTenant().getNamespace());
  }

  @Override
  public Single<Result> logoutOtherDevices() {
    return auth().logoutOtherDevices();
  }

  @Override
  public String projectType() {
    return auth().projectType();
  }

  @Override
  public Single<Result> configureUser(UserConfiguration userConfiguration) {
    return auth().configureUser(userConfiguration);
  }
}
