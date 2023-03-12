/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils


fun randomImage() = randomImage(300)

fun randomImage(size: Int) = "https://picsum.photos/seed/${faker.random().nextInt()}/$size"


fun randomImage(width: Int, height: Int) =
    "https://picsum.photos/seed/${faker.random().nextInt()}/$width/$height"


fun chance(num: Int, outOf: Int): Boolean {
    return faker.random().nextInt(1, outOf) <= num
}

fun <T> random(count: Int, item: () -> T): List<T> {
    val num = faker.random().nextInt(0, count)
    if (num > 0) {
        return (0..(num - 1)).map({ item() })
    }
    return listOf()
}

fun <T> randomAmount(from: Collection<T>): List<T> {
    if (from.isEmpty()) {
        return listOf()
    }
    var count = faker.random().nextInt(0, from.size)
    var els = from.toMutableList()
    if (count > 0) {
        var ret = mutableListOf<T>()
        while (count > 0) {
            val i = faker.random().nextInt(0, els.size)
            count = count.minus(1)
            ret.add(els[i])
            els.removeAt(i)
        }
        return ret
    } else {
        return listOf()
    }
}