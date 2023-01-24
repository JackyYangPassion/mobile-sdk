package io.inappchat.inappchat.user

import androidx.annotation.RestrictTo
import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.user.repository.UserRepository
import java.io.IOException

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class UserPagingSource(val dataManager: DataManager, val userRepository: UserRepository) :
    PagingSource<String, User>() {

    override fun getRefreshKey(state: PagingState<String, User>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, User> {
        val tenantId =
            dataManager.preference().tenantId ?: return LoadResult.Page(emptyList(), null, null)
        try {
            val response =
                userRepository.getUsersInSync(tenantId, params.key)
            val lastId = response.last().id
            return LoadResult.Page(
                data = response,
                nextKey = if (response.size >= params.loadSize) lastId else null,
                prevKey = null
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return LoadResult.Error(e)
        } catch (e: Error) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        return LoadResult.Page(emptyList(), null, null)
    }

}