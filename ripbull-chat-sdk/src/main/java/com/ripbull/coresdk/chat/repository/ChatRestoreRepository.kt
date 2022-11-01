package com.ripbull.coresdk.chat.repository

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.ripbull.coresdk.core.type.RestoreType
import com.ripbull.coresdk.data.common.Result
import io.reactivex.Single

/** @author meeth
 */
@RestrictTo(LIBRARY_GROUP)
interface ChatRestoreRepository {

  fun chatRestore(): Single<Result>

  fun restore(restoreType : RestoreType): Single<Result>

  fun chatSkipRestore(): Single<Result>
}