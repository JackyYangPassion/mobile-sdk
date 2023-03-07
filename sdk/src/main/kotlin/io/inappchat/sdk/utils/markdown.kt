/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import java.util.regex.Pattern


fun String.replace(pattern: Pattern, block: (String) -> String): String {
    var _text = this
    val matcher = phoneRegex.matcher(this)
    var match = matcher.find()
    while (match) {
        val num = matcher.group()
        _text = _text.replace(num, block(num))
        match = matcher.find()
    }
    return _text
}


val phoneRegex = Pattern.compile("(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}")
fun linkPhones(text: String) = text.replace(phoneRegex) { "[$it](tel:$it)" }

val linkRegex =
    Pattern.compile("((http|ftp|https):\\/\\/)?([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])")

fun linkLinks(text: String) = text.replace(linkRegex) { "[$it](it)" }

val mentionRegex = Pattern.compile("(@[0-9a-zA-Z]+)")
fun linkMentions(text: String) =
    text.replace(mentionRegex) { "[$it](${bundleUrl()}://inappchat/user/$it)" }

