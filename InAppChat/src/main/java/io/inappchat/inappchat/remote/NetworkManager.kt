package io.inappchat.inappchat.remote

interface NetworkManager {
  fun api(): ApiHandler
  fun api(networkConfig: NetworkConfig)
}