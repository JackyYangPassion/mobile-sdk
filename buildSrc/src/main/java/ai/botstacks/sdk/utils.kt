package ai.botstacks.sdk

import org.gradle.api.Project
import kotlin.math.pow

val publicModules = setOf(
    "chat-sdk",
)

val Project.minSdkVersion: Int
    get() = intProperty("ANDROID_MIN_SDK")

val Project.targetSdkVersion: Int
    get() = intProperty("ANDROID_TARGET_SDK")

val Project.compileSdkVersion: Int
    get() = intProperty("ANDROID_COMPILE_SDK")

val Project.iosDeploymentTarget: String
    get() = stringProperty("IOS_DEPLOYMENT_TARGET")

val Project.groupId: String
    get() = stringProperty("POM_GROUP_ID")

val Project.versionName: String
    get() = stringProperty("POM_VERSION")

val Project.versionCode: Int
    get() = versionName
        .takeWhile { it.isDigit() || it == '.' }
        .split('.')
        .map { it.toInt() }
        .reversed()
        .sumByIndexed { index, unit ->
            // 1.2.3 -> 102030
            (unit * 10.0.pow(2 * index + 1)).toInt()
        }

private fun Project.intProperty(
    name: String,
    default: () -> Int = { error("unknown property: $name") },
): Int = (properties[name] as String?)?.toInt() ?: default()

private fun Project.stringProperty(
    name: String,
    default: () -> String = { error("unknown property: $name") },
): String = (properties[name] as String?) ?: default()

private inline fun <T> List<T>.sumByIndexed(selector: (Int, T) -> Int): Int {
    var index = 0
    var sum = 0
    for (element in this) {
        sum += selector(index++, element)
    }
    return sum
}