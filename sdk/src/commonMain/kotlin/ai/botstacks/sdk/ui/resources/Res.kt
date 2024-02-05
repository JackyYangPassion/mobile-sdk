package ai.botstacks.sdk.ui.resources

import org.jetbrains.compose.resources.DensityQualifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.ThemeQualifier
import org.jetbrains.compose.resources.readResourceBytes

@OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)
@ExperimentalResourceApi
internal object Res {
    /**
     * Reads the content of the resource file at the specified path and returns it as a byte array.
     *
     * Example: `val bytes = Res.readBytes("files/key.bin")`
     *
     * @param path The path of the file to read in the compose resource's directory.
     * @return The content of the file as a byte array.
     */
    suspend fun readBytes(path: String): ByteArray = readResourceBytes(path)

    private fun basicDrawable(name: String, extension: String = ".xml") = DrawableResource(
        "drawable:_$name",
        setOf(
            ResourceItem(
                setOf(),
                "drawable/$name$extension"
            )
        )
    )

    private fun string(name: String) = StringResource(
        "string:$name",
        key = name,
        items = setOf(
            ResourceItem(
                setOf(),
                "values/strings.xml"
            ),
        )
    )

    private val DensityQualifier.path: String
        get() = when (this) {
            DensityQualifier.LDPI -> "drawable-ldpi"
            DensityQualifier.MDPI -> "drawable-mdpi"
            DensityQualifier.HDPI -> "drawable-hdpi"
            DensityQualifier.XHDPI -> "drawable-xhdpi"
            DensityQualifier.XXHDPI -> "drawable-xxhdpi"
            DensityQualifier.XXXHDPI -> "drawable-xxxhdpi"
        }
    private fun resourceBucketsDrawable(name: String, vararg density: DensityQualifier) = DrawableResource(
        "drawable:_$name",
        density.map {
            ResourceItem(
                setOf(it),
                "${it.path}/$name.png"
            )
        }.toSet() + setOf(
            ResourceItem(
                setOf(),
                "drawable/${name}.png"
            )
        )
    )

    object Drawables {
        object Filled {
            val AddressBook = basicDrawable("address_book_fill")
            val ArchiveBox = basicDrawable("archive_box_fill")
            val BellSimple = basicDrawable("bell_simple_fill")
            val Block = basicDrawable("block_fill")
            val ChatText = basicDrawable("chat_text_fill")
            val CheckCircle = basicDrawable("check_circle_fill")
            val Door = basicDrawable("door_fill")
            val FileArrowDown = basicDrawable("file_arrow_down_fill")
            val Gear = basicDrawable("gear_fill")
            val Lock = basicDrawable("lock_fill")
            val LockSimpleOpen = basicDrawable("lock_simple_open_fill")
            val Microphone = basicDrawable("microphone_fill")
            val MicrophoneSlash = basicDrawable("microphone_slash_fill")
            val Paperclip = basicDrawable("paperclip_fill")
            val PaperPlaneTilt = basicDrawable("paper_plane_tilt_fill")
            val Pause = basicDrawable("pause_fill")
            val Play = basicDrawable("play_fill")
            val PlusCircle = basicDrawable("plus_circle_fill")
            val Speaker = basicDrawable("speaker_fill")
            val Star = basicDrawable("star_fill")
            val Television = basicDrawable("television_fill")
            val Trash = basicDrawable("trash_fill")
            val User = basicDrawable("user_fill")
            val UserCircle = basicDrawable("user_circle_fill")
            val Users = basicDrawable("users_fill")
            val UsersThree = basicDrawable("users_three_fill")
        }

        object Outlined {
            val AddressBook = basicDrawable("address_book")
            val AddUser = basicDrawable("add_user")
            val Camera = basicDrawable("camera")
            val CaretLeft = basicDrawable("caret_left")
            val ChatDots = basicDrawable("chat_dots")
            val Check = basicDrawable("check")
            val Close = basicDrawable("x")
            val Copy = basicDrawable("copy")
            val Edit = basicDrawable("edit_outlined")
            val EmojiAdd = basicDrawable("emoji_add")
            val EmptyAllChannels = DrawableResource(
                "drawable:_empty_all_channels",
                setOf(
                    ResourceItem(
                        setOf(DensityQualifier.XHDPI),
                        "drawable-xhdpi/empty_all_channels.png"
                    ),
                    ResourceItem(
                        setOf(DensityQualifier.XXHDPI),
                        "drawable-xxhdpi/empty_all_channels.png"
                    ),
                    ResourceItem(
                        setOf(),
                        "drawable/empty_all_channels.png"
                    )
                )
            )

            val FileVideo = basicDrawable("file_video")
            val Gif = basicDrawable("gif")
            val ImageSquare = basicDrawable("image_square")
            val MagnifyingGlass = basicDrawable("magnifying_glass")
            val MapPin = basicDrawable("map_pin")
            val MenuOverflow = basicDrawable("dots_three_vertical_fill")
            val Plus = basicDrawable("plus")
            val Star = basicDrawable("star")
            val User = basicDrawable("user_outlined")
            val Users = basicDrawable("users_outlined")
            val VideoCamera = basicDrawable("video_camera")
        }

        object Logos {
            val BotStacks = DrawableResource(
                "drawable:_botstacks_logo",
                setOf(
                    ResourceItem(
                        setOf(ThemeQualifier.DARK),
                        "drawable/bostacks_logo.xml"
                    ),
                    ResourceItem(
                        setOf(),
                        "drawable/bostacks_logo.xml"
                    ),
                )
            )
        }

        val Empty = basicDrawable("empty")
        val EmptyChannels = resourceBucketsDrawable("empty_channels", DensityQualifier.XHDPI, DensityQualifier.XXHDPI)
        val EmptyChats = resourceBucketsDrawable("empty_chats", DensityQualifier.XHDPI, DensityQualifier.XXHDPI)
        val EmptyThreads =resourceBucketsDrawable("empty_threads", DensityQualifier.XHDPI, DensityQualifier.XXHDPI)
        val SmallNotification = basicDrawable("small_notification")
    }
}
