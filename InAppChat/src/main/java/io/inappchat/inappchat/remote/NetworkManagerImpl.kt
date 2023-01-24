package io.inappchat.inappchat.remote

import com.google.gson.GsonBuilder
import io.inappchat.inappchat.remote.core.ItemTypeAdapterFactory.Companion.newInstance
import io.inappchat.inappchat.remote.core.interceptor.RestInterceptor
import io.inappchat.inappchat.remote.service.AuthApi
import io.inappchat.inappchat.remote.service.ChatApi
import io.inappchat.inappchat.remote.service.UserApi
import io.inappchat.inappchat.utils.Monitoring
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.*

class NetworkManagerImpl : NetworkManager {
    private var apiHandler: ApiHandler? = null
    private var interceptor: RestInterceptor? = null
    private var authApi: AuthApi? = null
    private var userApi: UserApi? = null
    private var chatApi: ChatApi? = null
    override fun api(nwConfig: NetworkConfig) {
        val authServerUrl1 =
            if (nwConfig.authUrl.isNotEmpty()) nwConfig.authUrl else "http://google.com"
        val userServerUrl1 =
            if (nwConfig.userUrl.isNotEmpty()) nwConfig.userUrl else "http://google.com"
        val chatServerUrl1 =
            if (nwConfig.chatUrl.isNotEmpty()) nwConfig.chatUrl else "http://google.com"
        interceptor = RestInterceptor(nwConfig)
        authApi = authApi(authServerUrl1)
        userApi = useApi(userServerUrl1)
        chatApi = chatApi(chatServerUrl1)
        nwConfig.chatApi = chatApi
        nwConfig.userApi = userApi
        apiHandler = ApiHandlerImpl(authApi!!, userApi!!, chatApi!!)
    }

    override fun api(): ApiHandler {
        return this.apiHandler!!
    }

    private fun useApi(baseUrl: String): UserApi {
        return build(baseUrl).create(UserApi::class.java)
    }

    private fun chatApi(baseUrl: String): ChatApi {
        return build(baseUrl).create(ChatApi::class.java)
    }

    private fun authApi(authServerUrl: String): AuthApi {
        return build(authServerUrl).create(AuthApi::class.java)
    }

    private fun build(baseUrl: String): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(newInstance())
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    // interceptor for token and header
    // interceptor for logging
    private val okHttpClient: OkHttpClient
        private get() {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(
                CONNECT_TIME_OUT.toLong(),
                TimeUnit.SECONDS
            )
            builder.readTimeout(
                READ_TIME_OUT.toLong(),
                TimeUnit.SECONDS
            )
            // interceptor for token and header
            builder.addInterceptor(interceptor)
            //if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor { message -> Monitoring.log(message) }
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
            //}
            // interceptor for logging
            return builder.build()
        }

    companion object {
        private const val CONNECT_TIME_OUT = 90
        private const val READ_TIME_OUT = 90
    }
}