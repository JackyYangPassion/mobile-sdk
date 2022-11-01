package com.ripbull.coresdk.filter

/**
 * Created by DK on 02/04/20.
 */
class MessageFilter : AbstractFilter() {

  override fun execute(command: Command): Boolean? {
    val result = super.execute(command)
    var authenticated = command.isAuthenticated
    // condition or parsing for respective details
    // isIt a valid Json & is it Authenticated?
    return result
  }
}