package com.ripbull.coresdk.user.repository;

import androidx.annotation.RestrictTo;
import com.ripbull.coresdk.user.UserModule;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface UserModuleHook {
    UserModule provideModule();
}
