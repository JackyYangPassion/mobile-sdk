package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.Auth
import io.inappchat.sdk.models.Auth0LoginInput
import io.inappchat.sdk.models.LoginPasswordInput
import io.inappchat.sdk.models.NFTLoginInput
import io.inappchat.sdk.models.ResetPasswordInput
import io.inappchat.sdk.models.UserInfo

interface AuthApi {
    /**
     * Verify User information
     * verify user information, device information
     * Responses:
     *  - 200: The User info
     *
     * @param auth0LoginInput 
     * @return [UserInfo]
     */
    @POST("auth/auth0/login")
    suspend fun auth0Login(@Body auth0LoginInput: Auth0LoginInput): Response<UserInfo>

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
