package io.inappchat.inappchat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.rollbar.android.Rollbar
import io.inappchat.inappchat.announcement.repository.AnnouncementModuleHook
import io.inappchat.inappchat.chat.repository.ChatModuleHook
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.DataManagerImpl
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.downloader.FileDownloader
import io.inappchat.inappchat.fcm.FcmModule
import io.inappchat.inappchat.group.GroupModule
import io.inappchat.inappchat.module.ModuleAdapter
import io.inappchat.inappchat.module.ModuleAdapterImpl
import io.inappchat.inappchat.notification.repository.NotificationModuleHook
import io.inappchat.inappchat.offline.OfflineManagerImpl
import io.inappchat.inappchat.remote.util.HeaderUtils.getHeaderSignature
import io.inappchat.inappchat.tenant.AuthenticationModule
import io.inappchat.inappchat.typing.TypingModule
import io.inappchat.inappchat.user.UserModule
import io.inappchat.inappchat.utils.Monitoring
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.lang.ref.WeakReference

class InAppChat {
    private var apiKey: String? = null
    private var namespace: String? = null
    private var context: WeakReference<Context>? = null
    private var moduleAdapter: ModuleAdapter? = null

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }

    fun context(): Context {
        return context?.get()!!
    }

    companion object {
        private val INSTANCE = InAppChat()
        var fcmIntent: Intent? = null
            private set

        /** Initialize all core elements  */
        fun init(
            context: Context,
            namespace: String,
            apiKey: String,
            userConfiguration: UserConfiguration? = null
        ) {
            shared().context = WeakReference(context)
            shared().apiKey = apiKey
            shared().namespace = namespace
            Monitoring.setup(context)
            shared().moduleAdapter = ModuleAdapterImpl.newInstance(data())

            // save config data to preference
            data().preference().apiKey = apiKey
            data().preference().packageName = context.packageName

            // set network config
            data().network().api(data().networkConfig())
            OfflineManagerImpl.shared(shared().context())
            FileDownloader.initialize(context)
            if (BuildConfig.DEBUG) {
                Timber.plant(DebugTree())
            } else {
                Timber.plant(CrashReportingTree())
            }
            if (userConfiguration != null) {
                registerFCMToServer()
                setDeviceId()
                tenant().configureUser(userConfiguration)
            }
        }

        fun connect() {
            val tenantId = data().preference().tenantId
            val chatUserId = data().preference().chatUserId
            val mqttServer = data().preference().mqttServer
            val deviceId = data().preference().deviceId

            // username : <tenantId>/<X-Request-Signature>/<X-Nonce>
            val time = System.currentTimeMillis()
            val requestSignature = getHeaderSignature(
                data().preference().mqttApiKey!!,
                data().preference().packageName!!, time
            )
            val separator = ":"
            val password =
                requestSignature + separator + time + separator + data().preference().chatToken
            if (!TextUtils.isEmpty(tenantId) && !TextUtils.isEmpty(chatUserId)) {
                data().mqtt().createConnection(
                    tenantId,
                    chatUserId,
                    mqttServer,
                    tenantId,
                    password,
                    deviceId
                )
            }
        }

        fun registerFCMToServer() {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task: Task<String?> ->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    // Get new Instance ID token
                    data().preference().fcmToken = task.result
                }
            FirebaseMessaging.getInstance().isAutoInitEnabled = true
            setDeviceId()
        }

        fun tenant(): AuthenticationModule {
            return shared().moduleAdapter!!.tenant()
        }

        fun chat(): ChatModuleHook {
            return shared().moduleAdapter!!.chat()
        }

        @JvmStatic
        fun user(): UserModule {
            return shared().moduleAdapter!!.user()
        }

        fun typing(): TypingModule {
            return shared().moduleAdapter!!.typing()
        }

        fun group(): GroupModule {
            return shared().moduleAdapter!!.group()
        }

        @JvmStatic
        fun fcm(): FcmModule {
            return shared().moduleAdapter!!.fcm()
        }

        @JvmStatic
        fun event(): EventHandler {
            return shared().moduleAdapter!!.event()
        }

        fun notification(): NotificationModuleHook {
            return shared().moduleAdapter!!.notification()
        }

        fun announcement(): AnnouncementModuleHook {
            return shared().moduleAdapter!!.announcement()
        }

        private fun shared(): InAppChat {
            return INSTANCE
        }

        private fun data(): DataManager {
            val context = shared().context()
            return DataManagerImpl.shared(context)
        }

        fun getIntentForFCM(intent: Intent) {
            fcmIntent = intent
        }

        @JvmStatic
        fun saveFCMToken(token: String?) {
            data().preference().fcmToken = token
        }

        private fun setDeviceId() {
            val context = shared().context()
            @SuppressLint("HardwareIds") val androidId = Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
            data().preference().deviceId = androidId
        }

        @JvmStatic
        val appContext: Context
            get() = shared().context()
    }
}