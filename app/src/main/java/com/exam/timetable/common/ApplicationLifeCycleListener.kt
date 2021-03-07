package com.exam.timetable.common

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.exam.timetable.utils.Const


class ApplicationLifeCycleListener(val lifeCycleListenerCallback: LifeCycleListenerCallback) : LifecycleObserver {
    interface LifeCycleListenerCallback {
        fun lifeCycleCallback(event: Lifecycle.Event)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToFoground() {
        Log.v(Const.LOG_TAG, "LifeCycleListener onMoveToForeground")
        lifeCycleListenerCallback.lifeCycleCallback(Lifecycle.Event.ON_START)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        Log.v(Const.LOG_TAG, "LifeCycleListener onMoveToBackground")
        lifeCycleListenerCallback.lifeCycleCallback(Lifecycle.Event.ON_STOP)
    }
}
