package io.inappchat.inappchat.user

import android.content.Context
import androidx.annotation.RestrictTo
import io.inappchat.inappchat.InAppChat.Companion.appContext
import io.inappchat.inappchat.R
import io.inappchat.inappchat.core.ChatSDKException
import io.inappchat.inappchat.core.ChatSDKException.Error.InvalidModule
import io.inappchat.inappchat.core.type.AvailabilityStatus
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.utils.Constants
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.Subject

/** @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class UserModuleStub private constructor(private val appContext: Context?) : UserModule {

    override val chatUsers: Flowable<List<UserRecord>>
        get() = Flowable.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )
    override val mentionedUsers: Single<List<UserRecord>>
        get() = Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )

    override fun getReactionedUsers(
        reactionUnicodes: List<String>,
        msgId: String,
        threadId: String,
        chatType: ChatType
    ): Single<List<UserRecord>> {
        return Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )
    }

    override fun getUserById(id: String): Flowable<UserRecord> {
        return Flowable.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )
    }

    override fun fetchMoreUsers(): Single<Boolean> {
        return Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )
    }

    override fun getNewUsers(addUpdateOrDelete: String): Flowable<List<UserRecord>> {
        return Flowable.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )
    }

    override fun logout(): Single<Result> {
        return Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.LOGOUT)
            )
        )
    }

    override fun updateProfile(
        profileStatus: String, mediaPath: String,
        mediaType: String
    ): Single<Result> {
        return if (mediaPath == null || mediaPath.isEmpty()) {
            Single.error(
                ChatSDKException(
                    InvalidModule(),
                    appContext!!.getString(
                        R.string.alert_message,
                        Constants.Features.USER_PROFILE
                    )
                )
            )
        } else Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(
                    R.string.alert_message,
                    Constants.Features.USER_PROFILE_IMAGE
                )
            )
        )
    }

    override val profile: Single<UserRecord>
        get() = TODO("Not yet implemented")
    override val loggedInUser: Flowable<UserRecord>
        get() = TODO("Not yet implemented")
    override val userAvailabilityStatus: String
        get() = TODO("Not yet implemented")

    override fun setUserAvailability(availabilityStatus: AvailabilityStatus): Single<*> {
        return Single.error<Any>(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(
                    R.string.alert_message,
                    Constants.Features.AVAILABILITY_STATUS
                )
            )
        )
    }

    override fun subscribeToUserMetaData(): Observable<UserRecord> {
        return Observable.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.USER_MODULE)
            )
        )
    }

    override fun removeProfilePic(): Single<Result> {
        return Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(
                    R.string.alert_message,
                    Constants.Features.USER_PROFILE_IMAGE
                )
            )
        )
    }

    override fun deactivate(): Single<Result> {
        return Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(R.string.alert_message, Constants.Features.LOGOUT)
            )
        )
    }

    override fun metaDataOn(appUserId: String): Observable<UserMetaDataRecord> {
        return Observable.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(
                    R.string.alert_message,
                    Constants.Features.MUTE_NOTIFICATIONS
                )
            )
        )
    }

    override fun fetchLatestUserStatus(): Single<Result> {
        return Single.error(
            ChatSDKException(
                InvalidModule(),
                appContext!!.getString(
                    R.string.alert_message,
                    Constants.Features.AVAILABILITY_STATUS
                )
            )
        )
    }

    private val logoutSubject = Subject.empty<Result>()
    override fun subscribeToLogout(): Observable<Result> {
        return logoutSubject
    }

    companion object {
        @JvmStatic
        fun newInstance(): UserModule {
            return UserModuleStub(appContext)
        }
    }
}