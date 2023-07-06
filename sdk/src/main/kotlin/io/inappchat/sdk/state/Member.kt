package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import io.inappchat.sdk.fragment.FMember
import io.inappchat.sdk.type.MemberRole
import kotlinx.datetime.Instant

@Stable
data class Member(
    val user_id: String,
    val chat_id: String,
    val created_at: Instant,
    var role: MemberRole
) {
    val isMember: Boolean
        get() = when (role) {
            MemberRole.Admin, MemberRole.Member, MemberRole.Owner -> true
            else -> false
        }

    val isAdmin: Boolean
        get() = when (role) {
            MemberRole.Admin, MemberRole.Owner -> true
            else -> false
        }

    val user: User
        get() = User.get(user_id)!!

    val chat: Chat
        get() = Chat.get(chat_id)!!

    companion object {
        fun get(props: FMember) =
            Member(props.user.fUser.id, props.chat_id, props.created_at, props.role)
    }

}