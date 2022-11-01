package com.ripbull.ertc.remote

interface NetworkManager {
  fun api(): ApiHandler
  fun api(networkConfig: NetworkConfig)
}