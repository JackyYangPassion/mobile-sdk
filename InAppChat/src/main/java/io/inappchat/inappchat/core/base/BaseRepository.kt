package io.inappchat.inappchat.core.base

import android.text.TextUtils
import io.inappchat.inappchat.cache.database.DataSource
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.remote.NetworkManager

/** Created by DK on 2019-05-04.  */
open class BaseRepository protected constructor(dataManager: DataManager) {
    private val dataManager: DataManager

    init {
        this.dataManager = dataManager
    }

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
    protected val userId: String
        protected get() = data().preference().userId!!
    protected val appUserId: String
        protected get() = data().preference().appUserId!!
    protected val apiKey: String
        protected get() = data().preference().apiKey!!
    protected open val chatUserId: String
        protected get() = data().preference().chatUserId!!
    protected val name: String
        protected get() = data().preference().name!!
    protected val fcmToken: String
        protected get() = data().preference().fcmToken!!
    protected open val deviceId: String?
        protected get() = data().preference().deviceId

    protected fun <R> withTenant(function: (String) -> R, errorSupplier: () -> R): R {
        val tenantId = data().preference().tenantId
        return if (tenantId != null && !TextUtils.isEmpty(tenantId)) {
            function(tenantId)
        } else errorSupplier()
    }
}