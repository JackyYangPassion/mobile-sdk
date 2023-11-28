/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.state

import androidx.compose.runtime.*
import ai.botstacks.sdk.API
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.type.NotificationSetting
import ai.botstacks.sdk.type.OnlineStatus
import ai.botstacks.sdk.utils.bg
import ai.botstacks.sdk.utils.launch

@Stable
class Settings {

    var notifications by mutableStateOf(NotificationSetting.all)
    var availabilityStatus by mutableStateOf(OnlineStatus.Online)
    var blocked = mutableStateListOf<String>()
    val lastUsedReactions = mutableStateListOf<String>()

    fun init() {
        notifications = BotStacksChat.shared.prefs.getString("iac-notifications", null)
            ?.let { NotificationSetting.valueOf(it) }
            ?: NotificationSetting.all
        availabilityStatus = BotStacksChat.shared.prefs.getString("iac-availabilityStatus", null)
            ?.let { OnlineStatus.valueOf(it) }
            ?: OnlineStatus.Online
        blocked =
            BotStacksChat.shared.prefs.getStringSet("iac-blocked", mutableSetOf())?.toMutableStateList()
                ?: mutableStateListOf()
        lastUsedReactions.clear()
        lastUsedReactions.addAll(
            BotStacksChat.shared.prefs.getString("iac-reactions", "😀,🤟,❤️,🔥,🤣")!!.split(",")
        )
    }


    fun setNotifications(setting: NotificationSetting, isSync: Boolean = false) {
        this.notifications = setting
        BotStacksChat.shared.prefs.edit().putString("notifications", setting.rawValue).apply()
        if (isSync) return
        launch { bg { API.updateNotifications(setting) } }
    }

    fun setAvailability(setting: OnlineStatus, isSync: Boolean = false) {
        this.availabilityStatus = setting
        BotStacksChat.shared.prefs.edit().putString("availability", setting.rawValue).apply()
        if (isSync) return
        launch { bg { API.updateAvailability(setting) } }
    }

    fun setBlock(uid: String, blocked: Boolean) {
        if (blocked) {
            if (!this.blocked.contains(uid)) {
                this.blocked.add(uid)
                BotStacksChat.shared.prefs.edit().putStringSet("blocked", this.blocked.toSet())
            }
        } else {
            this.blocked.remove(uid)
            BotStacksChat.shared.prefs.edit().putStringSet("blocked", this.blocked.toSet())
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
        BotStacksChat.shared.prefs.edit().putString("reactions", lastUsedReactions.joinToString { "," })
            .apply()
    }
}