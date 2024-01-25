package ai.botstacks.sdk.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
private fun resourceForName(name: String, extension: String = "xml") = painterResource("drawable/$name.$extension")

object Drawables {
    val AddressBook: Painter
        @Composable get() = resourceForName("address_book")
    val AddressBookFilled: Painter
        @Composable get() = resourceForName("address_book_fill")
    val ArchiveBoxFilled: Painter
        @Composable get() = resourceForName("archive_box_fill")
    val BellSimpleFilled: Painter
        @Composable get() = resourceForName("bell_simple_fill")
    val Camera: Painter
        @Composable get() = resourceForName("camera")
    val CaretLeft: Painter
        @Composable get() = resourceForName("caret_left")
    val ChatDots: Painter
        @Composable get() = resourceForName("chat_dots")
    val ChatTextFilled: Painter
        @Composable get() = resourceForName("chat_text_fill")
    val Check: Painter
        @Composable get() = resourceForName("check")
    val CheckCircleFilled: Painter
        @Composable get() = resourceForName("check_circle_fill")
    val Copy: Painter
        @Composable get() = resourceForName("copy")
    val DoorFilled: Painter
        @Composable get() = resourceForName("door_fill")
    val EmojiAdd: Painter
        @Composable get() = resourceForName("emoji_add")
    val Empty: Painter
        @Composable get() = resourceForName(name = "empty")
    val EmptyAllChannels: Painter
        @Composable get() = resourceForName("empty_all_channels", extension = "png")
    val EmptyChannels: Painter
        @Composable get() = resourceForName("empty_channels", extension = "png")
    val EmptyChats: Painter
        @Composable get() = resourceForName("empty_chats", extension = "png")
    val EmptyThreads: Painter
        @Composable get() = resourceForName("empty_threads", extension = "png")
    val FileArrowDownFilled: Painter
        @Composable get() = resourceForName("file_arrow_down_fill")
    val FileVideo: Painter
        @Composable get() = resourceForName("file_video")
    val GearFilled: Painter
        @Composable get() = resourceForName("gear_fill")
    val Gif: Painter
        @Composable get() = resourceForName("gif")
    val ImageSquare: Painter
        @Composable get() = resourceForName("image_square")
    val LockFilled: Painter
        @Composable get() = resourceForName("lock_fill")
    val LockSimpleOpenFilled: Painter
        @Composable get() = resourceForName("lock_simple_open_fill")
    val MagnifyingGlass: Painter
        @Composable get() = resourceForName("magnifiying_glass")
    val MapPin: Painter
        @Composable get() = resourceForName("map_pin")
    val MenuOverflow: Painter
        @Composable get() = resourceForName("dots_three_vertical_fill")
    val MicrophoneFilled: Painter
        @Composable get() = resourceForName("microphone_fill")
    val MicrophoneSlashFilled: Painter
        @Composable get() = resourceForName("microphone_slash_fill")
    val PaperclipFilled: Painter
        @Composable get() = resourceForName("paperclip_fill")
    val PaperPlaneTiltFilled: Painter
        @Composable get() = resourceForName("paper_plane_tilt_fill")
    val PauseFilled: Painter
        @Composable get() = resourceForName("pause_fill")
    val PlayFilled: Painter
        @Composable get() = resourceForName("play_fill")
    val Plus: Painter
        @Composable get() = resourceForName("plus")
    val PlusCircleFilled: Painter
        @Composable get() = resourceForName("plus_circle_fill")
    val SmallNotification: Painter
        @Composable get() = resourceForName("small_notification")
    val Star: Painter
        @Composable get() = resourceForName("star")
    val StarFilled: Painter
        @Composable get() = resourceForName("star_fill")
    val TelevisionFilled: Painter
        @Composable get() = resourceForName("television_fill")
    val TrashFilled: Painter
        @Composable get() = resourceForName("trash_fill")
    val UserCircleFilled: Painter
        @Composable get() = resourceForName("user_circle_fill")
    val UserFilled: Painter
        @Composable get() = resourceForName("user_fill")
    val UsersFilled: Painter
        @Composable get() = resourceForName("users_fill")
    val UsersThreeFilled: Painter
        @Composable get() = resourceForName("users_three_fill")
    val UserOutlined: Painter
        @Composable get() = resourceForName("user_outlined")
    val UsersOutlined: Painter
        @Composable get() = resourceForName("users_outlined")
    val VideoCamera: Painter
        @Composable get() = resourceForName("video_camera")
    val X: Painter
        @Composable get() = resourceForName("x")

}