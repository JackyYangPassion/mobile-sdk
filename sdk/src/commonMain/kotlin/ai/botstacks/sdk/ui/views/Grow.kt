/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable()
fun ColumnScope.GrowSpacer() = Spacer(modifier = Modifier.weight(1f))

@Composable
fun RowScope.GrowSpacer() = Spacer(modifier = Modifier.weight(1f))
