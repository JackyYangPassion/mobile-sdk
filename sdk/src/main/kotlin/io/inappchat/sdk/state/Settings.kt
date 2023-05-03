/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import io.inappchat.sdk.API
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.NotificationSettings
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.launch

@Stable
class Settings {

    var notifications by mutableStateOf(NotificationSettings.AllowFrom.all)
    var availabilityStatus by mutableStateOf(AvailabilityStatus.online)
    var blocked = mutableStateListOf<String>()
    val lastUsedReactions = "😀,🤟,❤️,🔥,🤣".split(",").toMutableStateList()

    fun init() {
        notifications = InAppChat.shared.prefs.getString("notifications", null)
            ?.let { NotificationSettings.AllowFrom.valueOf(it) }
            ?: NotificationSettings.AllowFrom.all
        availabilityStatus = InAppChat.shared.prefs.getString("availabilityStatus", null)
            ?.let { AvailabilityStatus.valueOf(it) }
            ?: AvailabilityStatus.online
        blocked =
            InAppChat.shared.prefs.getStringSet("blocked", mutableSetOf())?.toMutableStateList()
                ?: mutableStateListOf()
        lastUsedReactions.addAll(
            InAppChat.shared.prefs.getString("reactions", "😀,🤟,❤️,🔥,🤣")!!.split(",")
        )
    }


    fun setNotifications(setting: NotificationSettings.AllowFrom, isSync: Boolean = false) {
        this.notifications = setting
        InAppChat.shared.prefs.edit().putString("notifications", setting.value).apply()
        if (isSync) return
        launch { bg { API.updateNotifications(setting) } }
    }

    fun setAvailability(setting: AvailabilityStatus, isSync: Boolean = false) {
        this.availabilityStatus = setting
        InAppChat.shared.prefs.edit().putString("availability", setting.value).apply()
        if (isSync) return
        launch { bg { API.updateAvailability(setting) } }
    }

    fun setBlock(uid: String, blocked: Boolean) {
        if (blocked) {
            if (!this.blocked.contains(uid)) {
                this.blocked.add(uid)
                InAppChat.shared.prefs.edit().putStringSet("blocked", this.blocked.toSet())
            }
        } else {
            this.blocked.remove(uid)
            InAppChat.shared.prefs.edit().putStringSet("blocked", this.blocked.toSet())
        }
    }

    fun onReaction(emoji: String) {
        if (lastUsedReactions.contains(emoji)) {
            lastUsedReactions.remove(emoji)
        }
        lastUsedReactions.add(0, emoji)
        if (lastUsedReactions.size > 5) {
            lastUsedReactions.removeRange(5, lastUsedReactions.size - 1)
        }
        InAppChat.shared.prefs.edit().putString("reactions", lastUsedReactions.joinToString { "," })
            .apply()
    }
}