/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import io.inappchat.sdk.R
import io.inappchat.sdk.ui.theme.IAC.colors
import io.inappchat.sdk.utils.ift

class AudioUrlProvider(override val values: Sequence<String> = sequenceOf("https://file-examples.com/storage/fe0358100863d05afed02d2/2017/11/file_example_MP3_5MG.mp3")) :
    PreviewParameterProvider<String>

@Preview
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun AudioPlayer(
    @PreviewParameter(AudioUrlProvider::class) url: String
) {
    val context = LocalContext.current
    var playing by remember {
        mutableStateOf(false)
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
            }
    }

    exoPlayer.playWhenReady = false
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable {
                    if (playing) exoPlayer.pause() else exoPlayer.play()
                    playing = !playing
                }
                .size(44)
                .background(colors.primary, CircleShape)) {
            Icon(
                painter = painterResource(
                    ift(
                        playing,
                        R.drawable.pause_fill,
                        R.drawable.play_fill
                    )
                ),
                contentDescription = "play audio"
            )
        }
    }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                clipToOutline = true
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }

}