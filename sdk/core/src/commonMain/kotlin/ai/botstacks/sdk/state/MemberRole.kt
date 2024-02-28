package ai.botstacks.sdk.state

/**
 * The role of a User Member of a Chat
 */
enum class MemberRole {
    /**
     * An Owner, has total permisisonal control over a Chat
     */
    Owner,
    /**
     * An Admin, has near total permissional control over a Chat
     */
    Admin,
    /**
     * A member can send Messages and read Messages from a Chat
     */
    Member,
    /**
     * An Invite sent to a User for a Chat. The User can join the Chat if invited, even if the Chat is
     * private
     */
    Invited,
    /**
     * A User that rejected an Invite to a Chat. Retained in order to prevent multiple Invites
     */
    RejectedInvite,
    /**
     * A User that was kicked from a group Chat. Retained in order to prevent the User rejoining the
     * Chat.
     */
    Kicked,

    Unknown,
}