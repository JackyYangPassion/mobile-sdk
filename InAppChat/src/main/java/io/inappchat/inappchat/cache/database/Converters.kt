package io.inappchat.inappchat.cache.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.inappchat.inappchat.cache.database.entity.EmailContact
import io.inappchat.inappchat.cache.database.entity.PhoneContact

/**
 * Created by DK on 2019-08-25.
 */
class Converters {

  @TypeConverter
  fun phoneContactListToJson(value: List<PhoneContact>?): String {
    return Gson().toJson(value)
  }

  @TypeConverter
  fun jsonToPhoneContactList(value: String): List<PhoneContact>? {
    val objects = Gson().fromJson(value, Array<PhoneContact>::class.java)
    return objects?.toList()
  }

  @TypeConverter
  fun emailContactListToJson(value: List<EmailContact>?): String {
    return Gson().toJson(value)
  }

  @TypeConverter
  fun jsonToEmailContactList(value: String): List<EmailContact>? {
    val objects = Gson().fromJson(value, Array<EmailContact>::class.java)
    return objects?.toList()
  }
}