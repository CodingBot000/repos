package com.exam.timetable.common


import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exam.timetable.App
import com.exam.timetable.livedata.Event
import com.exam.timetable.utils.Const
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(): ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun showProgress() {
        _isLoading.value = Event(true)
    }

    protected fun hideProgress() {
        _isLoading.value = Event(false)
    }

}