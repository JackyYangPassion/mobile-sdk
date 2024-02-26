package ai.botstacks.sdk.internal.ui.components.permissions

import androidx.compose.runtime.Composable

internal expect class PermissionsManager(callback: PermissionCallback) : PermissionHandler

internal interface PermissionCallback {
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus)
}

@Composable
internal expect fun createPermissionsManager(callback: PermissionCallback): PermissionsManager

internal interface PermissionHandler {
    @Composable
    fun askPermission(permission: PermissionType)

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    fun launchSettings()

}