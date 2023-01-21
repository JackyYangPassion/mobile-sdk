package io.inappchat.inappchat.chat.repository

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import io.inappchat.inappchat.core.type.RestoreType
import io.inappchat.inappchat.data.common.Result
import io.reactivex.rxjava3.core.Single

/** @author meeth
 */
@RestrictTo(LIBRARY_GROUP)
interface ChatRestoreRepository {

  fun chatRestore(): Single<Result>

  fun restore(restoreType : RestoreType): Single<Result>

  fun chatSkipRestore(): Single<Result>
}