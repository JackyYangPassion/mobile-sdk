package ai.botstacks.sdk.state

/**
 * The type of a Message attachment. Currently limited to images and location.
 */
enum class AttachmentType {
    /**
     * An image
     */
    Image,
    /**
     * A Location consisting of a combination of Latitude, Longitude and/or Address
     */
    Location,

    Unknown,
}

/**
 * An attachments that can be included with a Message.
 */
data class MessageAttachment(
    /**
     * The ID of the Attachment
     */
    val id: String,
    /**
     * The type of the Attachment
     */
    val type: AttachmentType,
    /**
     * The url of the file or 'data' if an arbitrary object
     */
    val url: String,
    /**
     * The raw data of the Attachment if it is a VCard
     */
    val `data`: String?,
    /**
     * The mime type of the attachment if it is a file, image, video or audio object
     */
    val mime: String?,
    /**
     * The width of the image or video in integer pixels
     */
    val width: Int?,
    /**
     * The height of the image or video in integer pixels
     */
    val height: Int?,
    /**
     * The duration of the audio or video in seconds
     */
    val duration: Int?,
    /**
     * The address of the location
     */
    val address: String?,
    /**
     * The latitude of the location
     */
    val latitude: Double?,
    /**
     * The longitude of the location
     */
    val longitude: Double?,
)
