package ai.botstacks.sdk.internal.ui.components.permissions

internal sealed interface PermissionType {
    data object Camera : PermissionType
    data object Location: PermissionType
}