package io.inappchat.inappchat.user.repository;

import androidx.annotation.RestrictTo;
import io.inappchat.inappchat.user.UserModule;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface UserModuleHook {
    UserModule provideModule();
}
