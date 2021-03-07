package com.exam.timetable

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication

import com.exam.timetable.common.ApplicationLifeCycleListener
import com.exam.timetable.di.*
import com.exam.timetable.di.viewModelModule

import com.exam.timetable.utils.Const
import com.exam.timetable.utils.getToday

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit


class App : MultiDexApplication() {
    companion object {
        private lateinit var sApplication: Application
        private lateinit var sApplicationLifecycle: Lifecycle.Event

        fun getApplication():Application
        {
            return sApplication
        }

        fun getLifeCycleApplication() : Lifecycle.Event {
            return sApplicationLifecycle
        }

    }

    override fun onCreate()
    {
        super.onCreate();

        sApplication = this;
        startKoin {
            androidContext(this@App)
            modules(listOf(networkModule, repoModule, remoteDataModule, viewModelModule, localDataModule))
        }


        ProcessLifecycleOwner.get().getLifecycle()
            .addObserver(ApplicationLifeCycleListener(object : ApplicationLifeCycleListener.LifeCycleListenerCallback {
                override fun lifeCycleCallback(event: Lifecycle.Event) {
                    Log.v(Const.LOG_TAG, "LifeCycleListener ProcessLifecycleOwner event=> $event")
                    sApplicationLifecycle = event
                    if (sApplicationLifecycle == Lifecycle.Event.ON_START) {
                        Const.TODAY = getToday()
                    }
                }
            }))
    }
}