package ai.botstacks.sdk.data.maps

import ai.botstacks.sdk.state.Location

class CameraPositionBounds(
    val coordinates: List<Location> = listOf(),
    val padding: Int = 0
)

class CameraPosition(
    val target: Location = Location(0.0, 0.0),
    val zoom: Float = 0f
)