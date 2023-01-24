package io.inappchat.inappchat.utils

/** Created by DK on 31/12/18.  */
interface Constants {
    interface TenantConfig {
        interface SingeChat {
            interface Attachment {
                companion object {
                    const val TEXT = "TEXT"
                    const val LOCATION = "LOCATION"
                    const val CONTACT = "CONTACT"
                    const val GIFY = "GIFY"
                    const val IMAGE = "IMAGE"
                    const val AUDIO = "AUDIO"
                    const val VIDEO = "VIDEO"
                    const val DOCUMENT = "DOCUMENT"
                }
            }

            interface Notification {
                companion object {
                    const val MUTE_SETTINGS = "MUTE_SETTINGS"
                }
            }
        }

        interface ReadReceipts {
            companion object {
                const val SENT = "SENT"
                const val READ = "READ"
                const val DELIVERED = "DELIVERED"
            }
        }

        interface E2E {
            companion object {
                const val TEXT = "TEXT"
                const val LOCATION = "LOCATION"
                const val CONTACT = "CONTACT"
                const val GIFY = "GIFY"
                const val MEDIA = "MEDIA"
            }
        }

        interface GroupChat {
            interface Attachment {
                companion object {
                    const val TEXT = "TEXT"
                    const val LOCATION = "LOCATION"
                    const val CONTACT = "CONTACT"
                    const val GIFY = "GIFY"
                    const val IMAGE = "IMAGE"
                    const val AUDIO = "AUDIO"
                    const val VIDEO = "VIDEO"
                    const val DOCUMENT = "DOCUMENT"
                }
            }

            interface Notification {
                companion object {
                    const val MUTE_SETTINGS = "MUTE_SETTINGS"
                }
            }

            companion object {
                const val ALLOW_ADMIN_CHANGE = "allowAdminChange"
            }
        }

        interface UserProfile {
            interface Image {
                companion object {
                    const val EDITABLE = "EDITABLE"
                }
            }

            interface Name {
                companion object {
                    const val EDITABLE = "EDITABLE"
                }
            }

            interface AvailableStatus {
                companion object {
                    const val ALLOW_OVERRIDING = "ALLOW_OVERRIDING"
                }
            }

            interface Notification {
                companion object {
                    const val MUTE_SETTINGS = "MUTE_SETTINGS"
                }
            }

            companion object {
                const val IMAGE = "IMAGE"
                const val NAME = "NAME"
                const val FAV_CONTACTS = "FAV_CONTACTS"
                const val AVAILABLE_STATUS = "AVAILABLE_STATUS"
            }
        }

        interface DeleteChat {
            companion object {
                const val DELETE_FOR_SELF = "delete_for_self"
                const val DELETE_FOR_EVERYONE = "delete_for_everyone"
            }
        }

        interface ForwardChat {
            companion object {
                const val SINGLE_CHAT_TEXT = "forward_single_chat_text"
                const val SINGLE_CHAT_LOCATION = "forward_single_chat_location"
                const val SINGLE_CHAT_CONTACT = "forward_single_chat_contact"
                const val SINGLE_CHAT_GIPHY = "forward_single_chat_giphy"
                const val SINGLE_CHAT_MEDIA = "forward_single_chat_media"
                const val GROUP_CHAT_TEXT = "forward_group_chat_text"
                const val GROUP_CHAT_LOCATION = "forward_group_chat_location"
                const val GROUP_CHAT_CONTACT = "forward_group_chat_contact"
                const val GROUP_CHAT_GIPHY = "forward_group_chat_giphy"
                const val GROUP_CHAT_MEDIA = "forward_group_chat_media"
            }
        }

        companion object {
            const val CHAT_ENABLED = "chat_enabled"
            const val GROUP_ENABLED = "group_enabled"
            const val TYPING_STATUS = "TYPING_STATUS"
            const val READ_RECEIPTS = "READ_RECEIPTS"
            const val USER_PROFILE = "user_profile"
            const val BLOCK_USER = "block_user"
            const val STAR_MESSAGE = "star_message"
            const val E2E_CHAT = "e2e_chat"
            const val NOTIFICATION = "NOTIFICATION"
            const val SEARCH_FILTER = "SEARCH_FILTER"
            const val ATTACHMENT = "ATTACHMENT"
            const val FORWARD_CHAT = "forward_chat"
            const val EDIT_CHAT = "edit_chat"
            const val DELETE_CHAT = "delete_chat"
            const val CHAT_REACTIONS = "chat_reactions"
            const val REPLY_THREAD = "reply_thread"
            const val FOLLOW_CHAT = "follow_chat"
            const val CHANNEL_SEARCH = "channel_search"
            const val GLOBAL_SEARCH = "global_search"
            const val LOCAL_SEARCH = "local_search"
            const val USER_MENTIONS = "user_mentions"
            const val ANNOUNCEMENT = "announcement"
            const val COPY = "copy"
            const val MODERATION = "moderation"
            const val DOMAIN_FILTER = "domain_filter"
            const val PROFANITY_FILTER = "profanity_filter"

            // Deprecated
            const val SINGLE_CHAT_IMAGE = "single_chat_image"
            const val SINGLE_CHAT_AUDIO = "single_chat_audio"
            const val SINGLE_CHAT_VIDEO = "single_chat_video"
            const val SINGLE_CHAT_LOCATION = "single_chat_location"
            const val SINGLE_CHAT_CONTACT = "single_chat_contact"
            const val SINGLE_CHAT_DOCUMENT = "single_chat_document"
            const val SINGLE_CHAT_GIF = "single_chat_gif"
            const val SINGLE_CHAT_GIPHY = "single_chat_gify"
            const val SINGLE_CHAT_TEXT = "single_chat_text"
            const val SINGLE_CHAT_MEDIA = "single_chat_media"
            const val GROUP_CHAT_IMAGE = "group_chat_image"
            const val GROUP_CHAT_AUDIO = "group_chat_audio"
            const val GROUP_CHAT_VIDEO = "group_chat_video"
            const val GROUP_CHAT_LOCATION = "group_chat_location"
            const val GROUP_CHAT_CONTACT = "group_chat_contact"
            const val GROUP_CHAT_DOCUMENT = "group_chat_document"
            const val GROUP_CHAT_GIF = "group_chat_gif"
            const val GROUP_CHAT_GIPHY = "group_chat_gify"
            const val GROUP_CHAT_TEXT = "group_chat_text"
            const val GROUP_CHAT_MEDIA = "group_chat_media"
            const val E2E_CHAT_TEXT = "e2e_chat_text"
            const val E2E_CHAT_LOCATION = "e2e_chat_location"
            const val E2E_CHAT_CONTACT = "e2e_chat_contact"
            const val E2E_CHAT_GIFY = "e2e_chat_gify"
            const val E2E_CHAT_MEDIA = "e2e_chat_media"
            const val DEVICE_TYPE = "android"
        }
    }

    interface Features {
        companion object {
            // Chat Module
            const val CHAT = "Chat"
            const val SEND_TEXT = "Send text"
            const val SEND = "Send "
            const val SEND_LOCATION = "Location Sharing"
            const val SEND_CONTACT = "Send contact"
            const val SEND_DOCUMENT = "Send document"
            const val SEND_GIF = "Send gif"
            const val ADD_TO_FAVOURITE = "Add to favourite"
            const val READ_RECEIPT = "Read receipt"
            const val BLOCK_USER = "Block user"
            const val DOWNLOAD_MEDIA = "Download media"
            const val CHAT_MUTE_NOTIFICATIONS = "Mute chat notifications"
            const val FORWARD_CHAT = "Forward chat"
            const val EDIT_CHAT = "Edit chat"
            const val DELETE_CHAT = "Delete chat"
            const val CHAT_REACTIONS = "Chat reactions"
            const val REPLY_THREAD = "Reply thread"
            const val FOLLOW_THREAD = "Follow thread"
            const val REPORT_MESSAGE = "Report message"
            const val CLEAR_CHAT = "Clear chat"
            const val USER_MENTIONS = "User/Channel mentions"
            const val SEND_MEDIA = "Send media"
            const val MEDIA = "Media"

            // Group Module
            const val GROUP = "Channel"
            const val CREATE_GROUP = "Create channel"
            const val UPDATE_GROUP = "Update channel"
            const val ADD_PARTICIPANTS = "Add participants to channel"
            const val REMOVE_PARTICIPANTS = "Remove participants from channel"
            const val SET_ADMIN = "Set as admin"
            const val REMOVE_ADMIN = "Remove as admin"
            const val EXIT_GROUP = "Exit from channel"
            const val SEARCH_CHANNEL = "Search channel"

            // Tenant Module
            const val NAMESPACE = "Namespace"
            const val LOGIN = "Login"
            const val CONNECT = "Connection"
            const val CHANGE_PASSWORD = "Change password"
            const val FORGOT_PASSWORD = "Forgot password"

            // Typing Module
            const val TYPING = "Typing"

            //Delete Message
            const val THIS_MESSAGE_WAS_DELETED = "This message was deleted"
            const val THIS_MESSAGE_WAS_DELETED_FOR_YOU = "This message was deleted for you"
            const val DELETE = "delete"
            const val EDIT = "edit"
            const val SELF = "self"
            const val EVERYONE = "everyone"

            // User Module
            const val USER_MODULE = "User module"
            const val USER_PROFILE = "User profile"
            const val AVAILABILITY_STATUS = "User availability status"
            const val LOGOUT = "Logout"
            const val MUTE_NOTIFICATIONS = "Mute notifications"
            const val USER_PROFILE_IMAGE = "Update user profile image"

            //Search messages
            const val LOCAL_SEARCH = "Local search"
            const val GLOBAL_SEARCH = "Global search"
            const val CHAT_RESTORE = "Chat Restore"

            // Announcement
            const val ANNOUNCEMENT = "Announcement"

            // Copy Message
            const val COPY = "Copy message"

            // Moderation
            const val MODERATION = "Moderation"
            const val DOMAIN_FILTER = "Domain filter"
            const val PROFANITY_FILTER = "Profanity filter"
        }
    }

    interface ErrorMessage {
        companion object {
            const val ERROR_403 = "User account is deactivated. Contact admin for re-activation."
        }
    }

    companion object {
        const val BUG_SNAG = "a8b9435c5791f744df3be911c7953a41"
        const val GLOBAL_NOTIFICATION_SETTINGS_CHANGED = "notificationSettingsChangedGlobal"
        const val THREAD_NOTIFICATION_SETTINGS_CHANGED = "notificationSettingsChangedThread"
        const val AVAILABILITY_STATUS_CHANGED = "availabilityStatusChanged"
        const val USER_BLOCKED_STATUS_CHANGED = "userBlockedStatusChanged"
        const val USER_ERROR = 403
        const val USER_NOT_EXIT = "This user is not exist"
    }
}