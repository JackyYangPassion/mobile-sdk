package io.inappchat.inappchat.remote.core

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * @author meeth
 */
internal class ItemTypeAdapterFactory private constructor() : TypeAdapterFactory {

  companion object {

    private const val RESPONSE_TAG_STATUS = "success"
    private const val RESPONSE_TAG_MESSAGE = "msg"
    private const val RESPONSE_TAG_RESULT = "result"
    private const val RESPONSE_TAG_ERROR_CODE = "errorCode"

    @JvmStatic
    fun newInstance(): TypeAdapterFactory {
      return ItemTypeAdapterFactory()
    }
  }

  override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
    val delegate: TypeAdapter<T> = gson.getDelegateAdapter(this, type)
    val elementAdapter: TypeAdapter<JsonElement> = gson.getAdapter(JsonElement::class.java)
    return ItemTypeAdapter(
      delegate, elementAdapter
    ).nullSafe()
  }

  class ItemTypeAdapter<T>(
    private val delegate: TypeAdapter<T>, private val elementAdapter: TypeAdapter<JsonElement>
  ) : TypeAdapter<T>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter?, value: T) {
      delegate.write(out, value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader?): T {
      val jsonElement = elementAdapter.read(`in`)
      if (jsonElement.isJsonObject) {
        val jsonObject = jsonElement.asJsonObject
        if (jsonObject.has(RESPONSE_TAG_STATUS) && !jsonObject.get(RESPONSE_TAG_STATUS).asBoolean) {
          val message = jsonObject.get(RESPONSE_TAG_MESSAGE).asString
          val errorCode = jsonObject.get(RESPONSE_TAG_ERROR_CODE).asString
          throw ErtcException(ErtcException.Error.Failure(), message)
        }

        val response: T = getResponse(jsonObject)
        validateResponse(response)
        return response
      }
      return delegate.fromJsonTree(jsonElement)
    }

    private fun validateResponse(response: T) {
      if (response is List<*>) {
        return
      }
      if (response !is ValidItem) {
        //If response doesn't implement ValidItem, throw NetworkException
        val message = "Model must implement ValidItem"
        throw ErtcException(ErtcException.Error.InvalidModel(), message)
      } else if (!response.isValid()) {
        //If response is in not valid, throw NetworkException
        val message = "Invalid Model from Network Layer"
        throw ErtcException(ErtcException.Error.InvalidModel(), message)
      }
    }

    private fun getResponse(jsonObject: JsonObject): T {
      return if (jsonObject.has(RESPONSE_TAG_RESULT)) {
        delegate.fromJsonTree(jsonObject.get(RESPONSE_TAG_RESULT))
      } else {
        delegate.fromJsonTree(jsonObject)
      }
    }
  }

  @Suppress("unused")
  @Keep
  data class Response<T : ValidItem>(
    @Expose @SerializedName(RESPONSE_TAG_STATUS) var success: Boolean = false, @Expose @SerializedName(
      RESPONSE_TAG_MESSAGE
    ) var message: String? = null, @Expose @SerializedName(RESPONSE_TAG_RESULT) var result: T? = null, @Expose @SerializedName(
      RESPONSE_TAG_ERROR_CODE
    ) var errorCode: String? = null
  )
}