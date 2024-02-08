/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.state.Identifiable
import ai.botstacks.sdk.state.Pager
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.components.internal.InfiniteListHandler
import ai.botstacks.sdk.utils.Fn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Identifiable> IACList(
    modifier: Modifier = Modifier,
    items: List<T> = listOf(),
    invert: Boolean = false,
    header: @Composable Fn? = null,
    footer: @Composable Fn? = null,
    empty: @Composable () -> Unit = {},
    divider: Boolean = false,
    topInset: Dp = 0.dp,
    bottomInset: Dp = 0.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    scrollToTop: String? = null,
    hasMore: Boolean = false,
    loadMore: (() -> Unit)? = null,
    refresh: (() -> Unit)? = null,
    refreshing: Boolean = false,
    content: @Composable LazyItemScope.(T) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, { refresh?.let { it() } })
    if (items.isEmpty() && !hasMore) {
        Column(
            modifier = modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {
            header?.invoke()
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(16.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                empty()
            }
            Spacer(modifier = Modifier.weight(1f))
            footer?.invoke()
        }
    } else {
        Box(
            modifier = modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(
                contentPadding = PaddingValues(top = topInset, bottom = bottomInset),
                state = listState,
                reverseLayout = invert,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
            ) {
                header?.let { item { it() } }
                items(items, key = { item -> item.id }) { item ->
                    Column(Modifier.padding(0.dp)) {
                        content(item)
                        if (divider) Divider(color = colorScheme.caption)
                    }
                }
            }
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            InfiniteListHandler(listState = listState) {
                loadMore?.invoke()
            }

            LaunchedEffect(key1 = scrollToTop, block = {
                coroutineScope.launch { listState.animateScrollToItem(0) }
            })
        }
    }
}

@Composable
fun <T : Identifiable> PagerList(
    pager: Pager<T>,
    modifier: Modifier = Modifier,
    prefix: List<T> = listOf(),
    invert: Boolean = false,
    header: @Composable Fn? = null,
    footer: @Composable Fn? = null,
    empty: @Composable () -> Unit = {},
    divider: Boolean = false,
    topInset: Dp = 0.dp,
    bottomInset: Dp = 0.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    scrollToTop: String? = null,
    content: @Composable LazyItemScope.(T) -> Unit
) {
    val array = prefix + pager.items
    LaunchedEffect(key1 = pager.id, block = {
        pager.loadMoreIfEmpty()
    })
    IACList(
        modifier = modifier,
        items = array,
        invert = invert,
        header = header,
        footer = footer,
        empty = empty,
        divider = divider,
        topInset = topInset,
        bottomInset = bottomInset,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        scrollToTop = scrollToTop,
        hasMore = pager.hasMore,
        loadMore = pager::loadMore,
        refresh = pager::refresh,
        refreshing = pager.refreshing,
        content = content
    )
}