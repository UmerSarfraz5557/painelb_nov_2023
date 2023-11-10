package br.com.painelb.util

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class ConcurrentLinkedQueueObservable<E> : ConcurrentLinkedQueue<E>() {

    interface OnChangedCallback {
        fun onChanged()
    }

    private val listener = ArrayList<OnChangedCallback>()

    fun addOnChangedCallback(callback: OnChangedCallback) {
        listener.add(callback)
    }

    fun removeOnChangedCallback(callback: OnChangedCallback) {
        listener.remove(callback)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val b = super.addAll(elements)
        if (b) notifyChange()
        return b
    }

    override fun add(element: E): Boolean {
        val add = super.add(element)
        if (add) notifyChange()
        return add
    }

    private fun notifyChange() {
        for (item in listener) {
            item.onChanged()
        }
    }
}
