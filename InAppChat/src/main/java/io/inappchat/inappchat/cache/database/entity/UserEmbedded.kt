package io.inappchat.inappchat.cache.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/** Created by DK on 02/01/19.  */
class UserEmbedded {
  @Embedded
  val tenant: User? = null
  @Relation(parentColumn = "id", entityColumn = "use_id")
  private val configs: List<UserMetadata>? = null

}