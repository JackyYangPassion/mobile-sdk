package io.inappchat.sdk.apis

import io.inappchat.sdk.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.inappchat.sdk.models.NFTLoginPOST200Response

interface G2Api {
    /**
     * Posion POG NFT based Login
     * This api verify auth0 token and add user to dtabase
     * Responses:
     *  - 200: User data
     *
     * @param version API versiion
     * @param tenantId Tenant Id. Example 5f61c2c3fee2af1f303a16d7
     * @param authorization Auth0 id_token
     * @return [NFTLoginPOST200Response]
     */
    @POST("{version}/tenants/{tenantId}/nft-login")
    suspend fun nFTLoginPOST(@Path("version") version: kotlin.String, @Path("tenantId") tenantId: kotlin.String, @Header("Authorization") authorization: kotlin.String): Response<NFTLoginPOST200Response>

}
