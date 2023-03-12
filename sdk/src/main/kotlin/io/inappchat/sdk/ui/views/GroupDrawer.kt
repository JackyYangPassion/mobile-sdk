/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.utils.genG

@Composable
fun GroupDrawer(group: Group, dismiss: () -> Unit) {
    Box(contentAlignment = Alignment.BottomCenter) {
        Column(modifier = Modifier.padding()) {

        }

    }
}

@Preview
@Composable
fun GroupDrawerPreview() {
    GroupDrawer(group = genG(), {})
}