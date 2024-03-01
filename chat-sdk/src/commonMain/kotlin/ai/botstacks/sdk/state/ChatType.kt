package ai.botstacks.sdk.state

/**
 * The type of a Chat entity.
 */
enum class ChatType {
    /**
     * A persistent Chat between two users.
     */
    DirectMessage,
    /**
     * A persistent Chat created for a group of people. Can be branded with a name, description and
     * image, as well as members with MemberRoles
     */
    Group,
    /**
     * Represents an ephemeral Chat. A set of messages spawned around an Support Request or something
     * of the like
     */
    Conversation,
    /**
     * Represents a conversation in reply to a particular message. Does not include nested threads.
     */
    Thread,
    Unknown,
}