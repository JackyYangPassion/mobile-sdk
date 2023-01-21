package io.inappchat.inappchat.core



/**
 * Created by DK on 2019-09-24.
 */

data class ChatSDKException @JvmOverloads constructor(
  val error: Error,
  override val message: String = "",
  val exception: Throwable? = null
) :
  Throwable(message, exception) {
  sealed class Error(val code: Int) {
    class InvalidModule : Error(111)
    class Failure(code: Int) : Error(code)
    class NoNetwork(code : Int) : Error(code)
    class AuthError(code: Int) : Error(code)
    class UnknownError(code: Int) : Error(code)
    class SocketTimeout(code: Int) : Error(code)
    class IOError(code: Int) : Error(code)
  }

}
