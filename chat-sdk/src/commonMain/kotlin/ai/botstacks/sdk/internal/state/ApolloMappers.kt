package ai.botstacks.sdk.internal.state

import ai.botstacks.sdk.fragment.FMessage
import ai.botstacks.sdk.state.MessageAttachment
import ai.botstacks.sdk.type.AttachmentType
import ai.botstacks.sdk.type.ChatType
import ai.botstacks.sdk.type.MemberRole
import ai.botstacks.sdk.type.NotificationSetting
import ai.botstacks.sdk.type.OnlineStatus

internal fun ChatType.toType(): ai.botstacks.sdk.state.ChatType {
    return when(this) {
        ChatType.DirectMessage -> ai.botstacks.sdk.state.ChatType.DirectMessage
        ChatType.Group -> ai.botstacks.sdk.state.ChatType.Group
        ChatType.Conversation -> ai.botstacks.sdk.state.ChatType.Conversation
        ChatType.Thread -> ai.botstacks.sdk.state.ChatType.Thread
        ChatType.UNKNOWN__ -> ai.botstacks.sdk.state.ChatType.Unknown
    }
}

internal fun OnlineStatus.toStatus(): ai.botstacks.sdk.state.OnlineStatus {
    return when (this) {
        OnlineStatus.Online -> ai.botstacks.sdk.state.OnlineStatus.Online
        OnlineStatus.Offline -> ai.botstacks.sdk.state.OnlineStatus.Offline
        OnlineStatus.Away -> ai.botstacks.sdk.state.OnlineStatus.Away
        OnlineStatus.DND -> ai.botstacks.sdk.state.OnlineStatus.DND
        OnlineStatus.UNKNOWN__ -> ai.botstacks.sdk.state.OnlineStatus.Unknown
    }
}

internal fun NotificationSetting.toSetting(): ai.botstacks.sdk.state.NotificationSetting {
    return when (this) {
        NotificationSetting.all -> ai.botstacks.sdk.state.NotificationSetting.All
        NotificationSetting.mentions -> ai.botstacks.sdk.state.NotificationSetting.Mentions
        NotificationSetting.none -> ai.botstacks.sdk.state.NotificationSetting.None
        NotificationSetting.UNKNOWN__ -> ai.botstacks.sdk.state.NotificationSetting.Unknown
    }
}

internal fun ai.botstacks.sdk.state.NotificationSetting.toApolloType(): NotificationSetting {
    return when (this) {
        ai.botstacks.sdk.state.NotificationSetting.All -> NotificationSetting.all
        ai.botstacks.sdk.state.NotificationSetting.Mentions -> NotificationSetting.mentions
        ai.botstacks.sdk.state.NotificationSetting.None -> NotificationSetting.none
        ai.botstacks.sdk.state.NotificationSetting.Unknown -> NotificationSetting.UNKNOWN__
    }
}

internal fun AttachmentType.toType(): ai.botstacks.sdk.state.AttachmentType? {
    return when (this) {
        AttachmentType.image -> ai.botstacks.sdk.state.AttachmentType.Image
        AttachmentType.video -> null
        AttachmentType.vcard -> null
        AttachmentType.location -> ai.botstacks.sdk.state.AttachmentType.Location
        AttachmentType.audio -> null
        AttachmentType.file -> null
        AttachmentType.UNKNOWN__ -> ai.botstacks.sdk.state.AttachmentType.Unknown
    }
}

internal fun ai.botstacks.sdk.state.AttachmentType.toApolloType(): AttachmentType {
    return when (this) {
        ai.botstacks.sdk.state.AttachmentType.Image -> AttachmentType.image
        ai.botstacks.sdk.state.AttachmentType.Location -> AttachmentType.location
        ai.botstacks.sdk.state.AttachmentType.Unknown -> AttachmentType.UNKNOWN__
    }
}

internal fun FMessage.Attachment.toAttachment(): MessageAttachment {
    return MessageAttachment(
        id = id,
        type = type.toType() ?: ai.botstacks.sdk.state.AttachmentType.Unknown,
        url = url,
        data = data,
        mime = mime,
        width = width,
        height = height,
        duration = duration,
        address = address,
        latitude = latitude,
        longitude = longitude
    )
}

internal fun MemberRole.toRole(): ai.botstacks.sdk.state.MemberRole {
    return when (this) {
        MemberRole.Owner -> ai.botstacks.sdk.state.MemberRole.Owner
        MemberRole.Admin -> ai.botstacks.sdk.state.MemberRole.Admin
        MemberRole.Member -> ai.botstacks.sdk.state.MemberRole.Member
        MemberRole.Invited -> ai.botstacks.sdk.state.MemberRole.Invited
        MemberRole.RejectedInvite -> ai.botstacks.sdk.state.MemberRole.RejectedInvite
        MemberRole.Kicked -> ai.botstacks.sdk.state.MemberRole.Kicked
        MemberRole.UNKNOWN__ -> ai.botstacks.sdk.state.MemberRole.Unknown
    }
}