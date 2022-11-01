package com.ripbull.ertc.remote.service

import com.ripbull.ertc.remote.model.common.Response
import com.ripbull.ertc.remote.model.request.*
import com.ripbull.ertc.remote.model.response.Token
import com.ripbull.ertc.remote.model.response.UserListResponse
import com.ripbull.ertc.remote.model.response.UserResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

  @GET("$PROV{account_id}/users/{userId}/getChatUsers")
  fun getUsers(
    @Path("userId") userId: String,
    @Path("account_id") tenantId: String,
    @Query("lastId") lastId: String?
  ): Single<UserListResponse>

  @GET("$PROV{account_id}/users/getChatUsers")
  fun getUsersInSync(
    @Path("account_id") tenantId: String,
    @Query("lastId") lastId: String?
  ): Call<UserListResponse?>

  @GET("$PROV{account_id}/users/{userId}/getChatUsers")
  fun getUpdatedUsers(
    @Path("userId") userId: String,
    @Path("account_id") tenantId: String,
    @Query("updateType") updateType: String,
    @Query("lastCallTime") lastCallTime: String
  ): Single<UserListResponse>

  @POST("$PROV{account_id}/users/auth0/login")
  fun login(@Path("account_id") tenantId: String, @Body request: Login): Single<UserResponse>

  @POST("$PROV{account_id}/users/auth0/login")
  fun activeAuth0User(@Path("account_id") tenantId: String, @Body appUserIdRequest: AppUserIdRequest): Single<Response>

  @POST("$PROV{account_id}/users/forgotPassword")
  fun forgotPassword(
    @Path("account_id") tenantId: String,
    @Body request: ForgotPassword
  ): Single<Response>

  @POST("$PROV{account_id}/users/{userId}/changePassword")
  fun changePassword(
    @Path("account_id") tenantId: String,
    @Body request: ChangePassword,
    @Path("userId") userId: String
  ): Single<Response>

  @GET("$PROV{account_id}/users/{userId}/auth0/refreshToken")
  fun refreshUserToken(
    @Path("userId") userId: String, 
    @Path("account_id") tenantId: String, 
    @Header("Authorization") auth: String
  ): Call<Token>

  @Multipart
  @POST("$PROV{tenantId}/users/{userId}/updateUser")
  fun updateProfile(
    @Path("tenantId") tenantId: String,
    @Path("userId") userId: String,
    @Part("loginType") loginType: RequestBody,
    @Part("profileStatus") profileStatus: RequestBody,
    @Part mFile: MultipartBody.Part? = null
  ): Single<UserResponse>

  @GET("${PROV}getUser")
  fun getProfile(
    @Path("appUserId") appUserId: String
  ): Single<UserResponse>

  @POST("$PROV{tenantId}/users/{userId}/logout")
  fun userLogout(
    @Path("tenantId") tenantId: String,
    @Path("userId") userId: String,
    @Body request: Logout
  ): Single<Response>

  @DELETE("$PROV{tenantId}/users/{userId}/removeProfilePic")
  fun removeProfilePic(
    @Path("tenantId") tenantId: String,
    @Path("userId") userId: String
  ): Single<UserResponse>

  companion object {

    const val PROV = "/v1/tenants/"
    const val APP = "/app/v1/"
  }
}
