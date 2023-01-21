package io.inappchat.inappchat.remote.core

/**
 * @author meeth
 */
data class ErtcException @JvmOverloads constructor(
  val error: Error,
  override val message: String = "",
  val exception: Throwable? = null
) :
  Throwable(message, exception) {
  sealed class Error(val code: Int) {
    class InvalidModel : Error(111)
    class Failure : Error(222)
    class NoNetwork(code : Int) : Error(code)
    class AuthError(code: Int) : Error(code)
    class UnknownError(code: Int) : Error(code)
    class SocketTimeout(code: Int) : Error(code)
    class IOError(code: Int) : Error(code)
    class ServerDown(code: Int) : Error(code)
  }


}