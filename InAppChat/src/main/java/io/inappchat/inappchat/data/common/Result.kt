package io.inappchat.inappchat.data.common



/** Created by DK on 27/12/18.  */

public data class Result(
  val isSuccess: Boolean? = false,
  val message: String? = null,
  val errorCode: String? = null
)