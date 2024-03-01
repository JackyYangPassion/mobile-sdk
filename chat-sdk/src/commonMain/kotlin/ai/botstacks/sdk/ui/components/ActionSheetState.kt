package ai.botstacks.sdk.ui.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState

@OptIn(ExperimentalMaterialApi::class)
abstract class ActionSheetState(internal val sheetState: ModalBottomSheetState) {
    suspend fun show() {
        sheetState.show()
    }

    suspend fun hide() {
        sheetState.hide()
    }
}