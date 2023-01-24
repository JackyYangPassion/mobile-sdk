package io.inappchat.inappchat.utils

import io.reactivex.rxjava3.disposables.Disposable
import java.util.ArrayList

class DisposableList {
    private val disposables = ArrayList<Disposable>()
    fun add(d: Disposable) {
        disposables.add(d)
    }

    fun remove(d: Disposable) {
        disposables.remove(d)
    }

    fun dispose() {
        for (d in disposables) {
            d.dispose()
        }
        disposables.clear()
    }
}