package io.inappchat.inappchat.remote.util


class Utils {

  companion object {
    fun getMetaDataJson(msgId: String): String {
      return "{\"requestId\" : \"$msgId\"}"
    }
  }
}