package ai.botstacks.sample.kmp

import ai.botstacks.sample.kmp.ui.LocalToastManager
import ai.botstacks.sample.kmp.ui.ToastManager
import ai.botstacks.sdk.BotStacksChat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun MainView() {

    BotStacksChat.shared.setup(
        LocalContext.current,
        apiKey = stringResource(id = R.string.botstacks_api_key),
        giphyApiKey = stringResource(id = R.string.giphy_api_key)
    )
    CompositionLocalProvider(
        LocalToastManager provides ToastManager(LocalContext.current)
    ) {
        App()
    }
}