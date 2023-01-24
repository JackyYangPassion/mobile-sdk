package io.inappchat.inappchat.module

import android.text.TextUtils
import io.inappchat.inappchat.cache.database.DataSource
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.remote.NetworkManager

/** @author meeth
 */
abstract class BaseModule protected constructor(dataManager: DataManager) {
    private val dataManager: DataManager
    protected fun data(): DataManager {
        return dataManager
    }

    protected fun db(): DataSource {
        return dataManager.db()
    }

    protected fun network(): NetworkManager {
        return dataManager.network()
    }

    protected fun preference(): PreferenceManager {
        return dataManager.preference()
    }

    protected val tenantId: String
        protected get() = data().preference().tenantId!!
    protected val userId: String?
        protected get() = data().preference().userId
    protected val appUserId: String?
        protected get() = data().preference().appUserId
    protected val apiKey: String?
        protected get() = data().preference().apiKey

    protected fun _getChatUserId(): String? {
        return data().preference().chatUserId
    }

    protected val fCMToken: String?
        protected get() = data().preference().fcmToken
    protected val deviceId: String?
        protected get() = data().preference().deviceId
    private var cachedUser: User? = null

    init {
        this.dataManager = dataManager
    }

    fun loggedInUser(): User? {
        val userId = userId
        val tenantId = tenantId
        if (cachedUser == null || cachedUser!!.id != userId) {
            cachedUser = if (TextUtils.isEmpty(userId) && tenantId != null) {
                data().db().userDao().getUserByIdInSync(tenantId, userId)
            } else {
                null
            }
        }
        return cachedUser
    }

    protected fun <R> withTenant(function: (String) -> R, errorSupplier: () -> R): R {
        val tenantId = data().preference().tenantId
        return if (tenantId != null && !TextUtils.isEmpty(tenantId)) {
            function(tenantId)
        } else errorSupplier()
    }

    protected fun <R> withChatUserId(
        function: (String) -> R,
        errorSupplier: () -> R
    ): R {
        val userId = data().preference().userId
        return if (userId != null && !TextUtils.isEmpty(userId)) {
            return function(userId)
        } else errorSupplier()
    }
}