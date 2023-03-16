/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Identifiable
import io.inappchat.sdk.state.Pager
import io.inappchat.sdk.utils.Fn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Identifiable> PagerList(
    pager: Pager<T>,
    prefix: List<T> = listOf(),
    invert: Boolean = false,
    header: @Composable Fn? = null,
    content: @Composable LazyItemScope.(T) -> Unit,
    footer: @Composable Fn? = null,
    empty: @Composable () -> Unit = {},
    divider: Boolean = false,
    topInset: Float = 0f,
    bottomInset: Float = 0f
) {
    val array = prefix + pager.items
    val pullRefreshState = rememberPullRefreshState(pager.refreshing, { pager.refresh() })


    if (array.isEmpty() && !pager.hasMore) {
        Column(modifier = Modifier.pullRefresh(pullRefreshState)) {
            Space(topInset)
            PullRefreshIndicator(
                pager.refreshing,
                pullRefreshState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            header?.invoke()
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(16.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                empty()
            }
            Spacer(modifier = Modifier.weight(1f))
            footer?.invoke()
            Space(bottomInset + 12.0f)
        }
    } else {
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = topInset.dp, bottom = bottomInset.dp),
                state = listState,
                reverseLayout = invert
            ) {
                header?.let { item { it() } }
                items(array, key = { item -> item.id }) { item ->
                    content(item)
                    if (divider) Divider()
                }
            }
            PullRefreshIndicator(
                pager.refreshing,
                pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            InfiniteListHandler(listState = listState) {
                pager.loadMore()
            }

            LaunchedEffect(key1 = pager.items.firstOrNull()?.id, block = {
                coroutineScope.launch { listState.animateScrollToItem(0) }
            })
        }
    }
}