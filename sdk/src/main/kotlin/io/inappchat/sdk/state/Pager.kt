/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op

interface Identifiable {
    val id: String
}

@Stable
abstract class Pager<T : Identifiable>(
    val items: SnapshotStateList<T> = mutableStateListOf<T>(),
    val pageSize: Int = 20
) {
    var loading by mutableStateOf(false)
    var refreshing by mutableStateOf(false)
    var hasMore by mutableStateOf(true)

    init {
        if (isSinglePage) {
            hasMore = false
        }
    }

    open val isSinglePage: Boolean get() = false

    fun loadMoreIfEmpty() {
        if (items.isEmpty()) {
            loadMore()
        }
    }

    fun loadMoreIfNeeded(item: T) {
        if (isSinglePage || hasMore) return
        if (items.size < pageSize * 2) {
            loadMore()
            return
        }
        val thresholdIndex = items.size - pageSize
        if (items.indexOf(item) < thresholdIndex) {
            loadMore()
        }
    }

    fun skip(isRefresh: Boolean) = if (isRefresh) 0 else items.size

    fun refresh() {
        if (refreshing) {
            return
        }
        refreshing = true
        loading = true
        val pager = this
        op({
            val items = bg { load(0, pageSize) }
            pager.items.removeAll { true }
            pager.items.addAll(items)
            hasMore = items.size >= pageSize
            loading = false
            refreshing = false
        })
    }

    fun loadMore() {
        if (isSinglePage || !hasMore || refreshing || loading) return
        loading = true
        val pager = this
        op({
            val items = bg { load(items.size, pageSize) }
            pager.items.addAll(items)
            hasMore = items.size >= pageSize
            loading = false
        })
    }

    abstract suspend fun load(skip: Int, limit: Int): List<T>

}