/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacks.theme
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.ift
import kotlinx.coroutines.delay

class AudioUrlProvider(override val values: Sequence<String> = sequenceOf("https://file-examples.com/storage/fe0358100863d05afed02d2/2017/11/file_example_MP3_5MG.mp3")) :
    PreviewParameterProvider<String>

@IPreviews
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun AudioPlayer(
    @PreviewParameter(AudioUrlProvider::class) url: String
) {
    val context = LocalContext.current
    var playing by remember {
        mutableStateOf(false)
    }
    var current by remember {
        mutableStateOf(0L)
    }
    var total by remember {
        mutableStateOf(0L)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(url))

                setMediaSource(source)
                prepare()
                val player = this
                this.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            current = player.currentPosition
                            total = player.contentDuration
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        playing = isPlaying
                        current = player.currentPosition
                        total = player.contentDuration
                    }
                })
            }
    }

    exoPlayer.playWhenReady = false
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable {
                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                }
                .size(44)
                .background(colorScheme.primary, CircleShape)) {
            Icon(
                painter = ift(
                    playing,
                    Drawables.PlayFilled,
                    Drawables.PauseFilled
                ),
                contentDescription = "play audio",
                tint = Color.White,
                modifier = Modifier.size(12)
            )
        }

        Space()
        Text(text = msToString(current), iac = fonts.body, color = colorScheme.bubbleText)
        Space()
        Slider(
            value = current.toFloat() / 1000.0f,
            onValueChange = {
                exoPlayer.pause()
                current = it.toLong() * 1000L
            },
            onValueChangeFinished = {
                exoPlayer.seekTo(current)
                exoPlayer.play()
            },
            valueRange = 0f..(total.toFloat() / 1000.0f),
            steps = 1,
            colors = SliderDefaults.colors(
                thumbColor = colorScheme.primary,
                activeTickColor = colorScheme.primary,
                activeTrackColor = colorScheme.primary
            ),
            modifier = Modifier.width(theme.videoPreviewSize.width.dp)
        )
        Space()
        Text(text = msToString(total), iac = fonts.body, color = colorScheme.bubbleText)
    }

    if (playing) {
        LaunchedEffect(current) {
            delay(1000)
            current += 1000
        }
    }

    DisposableEffect(key1 = "player", effect = {
        onDispose { exoPlayer.release() }
    })

}

fun msToString(_time: Long): String {
    var time = (_time / 1000L)
    if (time == 0L) {
        return ""
    }
    val seconds = time % 60
    time = (time - seconds) / 60
    val minutes = time % 60
    val hours = (time - minutes) / 60
    var ret = ""
    if (hours > 0) {
        ret += "$hours:"
    }
    ret += "$minutes:"
    return ret + ift(seconds < 10, "0$seconds", "$seconds")
}