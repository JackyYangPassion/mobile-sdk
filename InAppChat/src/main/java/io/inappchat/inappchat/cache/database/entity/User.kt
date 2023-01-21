package io.inappchat.inappchat.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "user",
  indices = [Index(value = ["id", "tenant_id"])],
  foreignKeys = [ForeignKey(
    entity = Tenant::class,
    parentColumns = ["id"],
    childColumns = ["tenant_id"],
    onDelete = ForeignKey.CASCADE
  )]
)
data class User @JvmOverloads constructor(
  @PrimaryKey @ColumnInfo(name = "id") var id: String,
  @ColumnInfo(name = "tenant_id") var tenantId: String? = null,
  @ColumnInfo(name = "name") var name: String? = null,
  @ColumnInfo(name = "app_state") var appState: String? = null,
  @ColumnInfo(name = "login_type") var loginType: String? = null,
  @ColumnInfo(name = "profile_pic") var profilePic: String? = null,
  @ColumnInfo(name = "profile_thumb") var profileThumb: String? = null,
  @ColumnInfo(name = "profile_status") var profileStatus: String? = null,
  @ColumnInfo(name = "login_timestamp") var loginTimestamp: Long? = null,
  // UserId generated from server. Not email-id
  @ColumnInfo(name = "user_chat_id") var userChatId: String? = null,
  @ColumnInfo(name = "availability_status") var availabilityStatus: String? = null,
  @ColumnInfo(name = "blocked_status") var blockedStatus: String? = null,
  @ColumnInfo(name = "notification_settings") var notificationSettings: String? = null,
  @ColumnInfo(name = "valid_till") var validTill: Long? = null,
  @ColumnInfo(name = "valid_till_value") var validTillValue: String? = null
){
  @Ignore var ertcId: String? = null
  @Ignore var role: String? = null
}