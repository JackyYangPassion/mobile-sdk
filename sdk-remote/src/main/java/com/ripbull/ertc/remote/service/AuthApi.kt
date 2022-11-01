package com.ripbull.ertc.remote.service

import com.ripbull.ertc.remote.model.response.TenantDetailResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthApi {

  companion object {

    const val PROV = "/v1"
    const val APP = "/app/v1/"
  }

  @GET("$PROV/tenants/get-tenant-details/{url}")
  fun validateUrl(@Path("url") url: String): Single<TenantDetailResponse>
}
