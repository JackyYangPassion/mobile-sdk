package io.inappchat.inappchat.chat.model

/**
 * Created by DK on 01/07/20.
 */
class MessageBuilder private constructor(
  val breed: String?,
  val breed_test: String?
) {
  data class Builder(
    var bread: String? = null,
    var breadTest: String? = null
  ) {
    fun bread(bread: String?) = apply { this.bread = bread }
    fun breadTest(breadTest: String?) = apply { this.breadTest = breadTest }
    fun build() = MessageBuilder(bread, breadTest)
  }
}