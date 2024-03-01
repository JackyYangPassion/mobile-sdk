package ai.botstacks.sdk.state

/**
 * Notification setting for a user, can be set per chat
 */
enum class NotificationSetting {
    /**
     * Allow all notifications, mentions and dms
     */
    All,
    /**
     * Allow only mention notifictions
     */
    Mentions,
    /**
     * No notifications
     */
    None,
    /**
     * Auto generated constant for unknown enum values
     */
    Unknown,
}