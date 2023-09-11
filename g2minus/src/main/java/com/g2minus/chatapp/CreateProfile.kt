package com.g2minus.chatapp

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.android.material.math.MathUtils.lerp
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.GrowSpacer
import io.inappchat.sdk.ui.views.radius
import io.inappchat.sdk.utils.IPreviews

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateProfile(openSignIn: () -> Unit) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        appState.tokens.size
    }
    var username by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val canEnter = (username.isBlank() || username.length > 4)
    val onFinish = {
        val token = appState.tokens[pagerState.currentPage]
        val un = if (username.isBlank()) "${token.account}-${token.id}" else username
        if (un.length > 4) {
            appState.username = un
            appState.token = token
            openSignIn()
        } else {
            Toast.makeText(context, "Username must be five or more characters", Toast.LENGTH_SHORT)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFF1E1E1E))
            .padding(32.dp, 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.g2minus),
            contentDescription = "g2minus",
            modifier = Modifier.size(55.dp)
        )
        GrowSpacer()
        Text(
            text = "Select your profile",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        HorizontalPager(
            state = pagerState,
            pageSpacing = 10.dp
        ) { page ->
            val token = appState.tokens[page]
            val pageOffset =
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            Card(
                colors = CardDefaults.cardColors(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier
                    .graphicsLayer {

                        lerp(
                            0.85f,
                            1f,
                            1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        alpha = lerp(
                            0.5f,
                            1f,
                            1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                // .aspectRatio(0.5f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(token.image)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .offset {
                            // Calculate the offset for the current page from the
                            // scroll position
                            // Then use it as a multiplier to apply an offset
                            IntOffset(
                                x = (70.dp * pageOffset).roundToPx(),
                                y = 0,
                            )
                        }
                )
                Text(
                    token.account,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Token: " + token.id,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        TextField(
            value = username,
            placeholder = { Text(text = "Choose your nickname") },
            onValueChange = {
                username = it
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    onFinish()
                },
            ),
            modifier = Modifier
                .radius(30.dp)
                .fillMaxWidth()
        )
        GrowSpacer()
        Button(
            onClick = { onFinish() },
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(Color(if (canEnter) 0x92C748 else 0x2B2B2B)),
            colors = ButtonDefaults.buttonColors(
                Color(0xFF92C748),
                Color.White,
                Color(0xFF2B2B2B),
                Color.White
            )
        ) {
            Text(
                text = "Select your profile".uppercase(),
                color = Color(0xFF171717),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@IPreviews
@Composable
fun CreateProfilePreview() {
    InAppChatContext {
        CreateProfile {

        }
    }
}