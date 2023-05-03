package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.Auth
import io.inappchat.sdk.models.LoginInput
import io.inappchat.sdk.models.LoginPasswordInput
import io.inappchat.sdk.models.NFTLoginInput
import io.inappchat.sdk.models.ResetPasswordInput

interface AuthApi {
    /**
     * Change Password
     * API to change user password
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param loginPasswordInput 
     * @return [Unit]
     */
    @POST("auth/change-password")
    suspend fun changePassword(@Body loginPasswordInput: LoginPasswordInput): Response<Unit>

    /**
     * Login to InAppChat as a user on your Application.
     * Login to InAppChat as a user on your Application. This will updated the User&#39;s profile info in InAppChat. InAppChat will verify the authenticity of the credentials provided by calling the configured backend function of your server.
     * Responses:
     *  - 200: User token and profile
     *
     * @param loginInput 
     * @return [Auth]
     */
    @POST("auth/login")
    suspend fun login(@Body loginInput: LoginInput): Response<Auth>

    /**
     * Logout
     * Logout
     * Responses:
     *  - 204: Logout successful
     *
     * @return [Unit]
     */
    @POST("logout")
    suspend fun logout(): Response<Unit>

    /**
     * Logout
     * logoutOtherDevices
     * Responses:
     *  - 204: logout successful
     *
     * @return [Unit]
     */
    @POST("logoutOtherDevices")
    suspend fun logoutOtherDevices(): Response<Unit>

    /**
     * signup and login with NFT
     * 
     * Responses:
     *  - 200: User token and profile
     *
     * @param nfTLoginInput array of eRTCUserIds of invitees
     * @return [Auth]
     */
    @POST("auth/nft/login")
    suspend fun nftLogin(@Body nfTLoginInput: NFTLoginInput): Response<Auth>

    /**
     * Forgot Password
     * On calling this API, password gets reset and new password gets delivered on email
     * Responses:
     *  - 204: Operation completed successfully
     *
     * @param resetPasswordInput 
     * @return [Unit]
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordInput: ResetPasswordInput): Response<Unit>

}
