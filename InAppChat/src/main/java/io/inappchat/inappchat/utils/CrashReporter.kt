package io.inappchat.inappchat.utils

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.ref.WeakReference

class CrashReporter<T : Any> : Observer<T> {
    private var list: WeakReference<DisposableList>? = null

    internal constructor(list: DisposableList) {
        this.list = WeakReference(list)
    }

    constructor()

    override fun onSubscribe(d: Disposable) {
        if (list != null) {
            list!!.get()!!.add(d)
        }
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onComplete() {}
}