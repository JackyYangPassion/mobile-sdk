package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.resources.Res
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Stable
class EditProfileState(private val user: User) {
    var selectedImage by mutableStateOf<KmpFile?>(null)
    @OptIn(ExperimentalFoundationApi::class)
    val textState : TextFieldState = TextFieldState(
        initialText = user.username
    )


    val userImage: Any?
        get() = selectedImage ?: user.avatar
}

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun EditProfileView(
    state: EditProfileState,
) {
    val pickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            state.selectedImage = files.firstOrNull()
        }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(BotStacks.dimens.inset),
    ) {
        item {
            Avatar(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { pickerLauncher.launch() },
                type = AvatarType.User(
                    url = state.userImage,
                    empty = painterResource(Res.Drawables.Outlined.Camera)
                ),
                size = AvatarSize.Large
            )
        }
        item {
            TextInput(
                Modifier
                    .fillMaxWidth()
                    .padding(top = BotStacks.dimens.grid.x6),
                state = state.textState,
                placeholder = "Username",
                maxLines = 1,
                fontStyle = BotStacks.fonts.body2,
                indicatorColor = BotStacks.colorScheme.caption,
            )
        }
    }
}