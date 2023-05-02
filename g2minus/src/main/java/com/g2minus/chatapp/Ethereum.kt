package com.g2minus.chatapp

import io.inappchat.sdk.API
import io.inappchat.sdk.await
import okhttp3.Request
import org.json.JSONObject

val etherscanApiKey = "71CPX2FPG7BF5VZYH73PWQ4PZKW79BPFH2"
val poisonPogContract = "0x41112a2e8626330752a8f9353462edd4771a48a2"

data class Token(val id: String, val account: String, val image: String)

object Etherscan {
    val client = API.okHttpBuilder().build()
    suspend fun getTokens(address: String): List<Token> {
        val request = Request.Builder()
            .url(
                "https://api.etherscan.io/api?module=account&acount=addresstokennftinventory&address=$address&contractaddress=$poisonPogContract&apikey=$etherscanApiKey"

            ).build()
        val response = client.newCall(request).await()
        val tokens = response.body?.string()?.let {
            val json = JSONObject(it)
            val tokens = json.getJSONArray("result").let { array ->
                val ids = mutableListOf<String>()
                for (i in 0..array.length()) {
                    array.getJSONObject(i).getString("TokenId")?.let { id -> ids.add(id) }
                }
                ids
            }
            tokens.map { id -> Token(id, address, "https://api.poisonpog.org/ipfs/$id.png") }
        }
        return tokens ?: listOf()
    }
}
