package com.exam.timetable.viewmodel


import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.timetable.common.BaseViewModel
import com.exam.timetable.livedata.Event
import com.exam.timetable.model.data.*
import com.exam.timetable.model.repository.detail.DetailDataRepository
import com.exam.timetable.model.repository.memo.MemoRepository
import com.exam.timetable.model.repository.search.SearchRepository
import com.exam.timetable.model.repository.timetable.MemotableInfoRepository
import com.exam.timetable.model.repository.timetable.TimetableInfoRepository
import com.exam.timetable.utils.Resource
import com.exam.timetable.utils.getDayOfTheWeekConvert
import com.exam.timetable.utils.isNetworkConnected
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.newSingleThreadContext
import okhttp3.Dispatcher
import java.util.HashMap


class MainViewModel(private val timetableInfoRepository: TimetableInfoRepository,
                    private val memotableInfoRepository: MemotableInfoRepository
                    ) : BaseViewModel()  {

    private val _itemLiveData = MutableLiveData<Event<Resource<TimeTableDataParent>>>()
    val itemLiveData: LiveData<Event<Resource<TimeTableDataParent>>> get() = _itemLiveData
    private val _myTimeTableLiveData = MutableLiveData<Event<Resource<List<TimeTableDBInfo>>>>()
    val myTimeTableLiveData: LiveData<Event<Resource<List<TimeTableDBInfo>>>> get() = _myTimeTableLiveData
    private val _myMemoTableLiveData = MutableLiveData<Event<Resource<List<MemoDBInfo>>>>()
    val myMemoTableLiveData: LiveData<Event<Resource<List<MemoDBInfo>>>> get() = _myMemoTableLiveData

    fun getInitData() {
        getTimeTableData()
    }

    fun getTimeTableData() {
        compositeDisposable.add(
            timetableInfoRepository.getTimeTableInfoAllDB().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showProgress()
            }
            .doAfterTerminate { hideProgress() }
            .subscribe({ it ->
                _myTimeTableLiveData.postValue(Event(Resource.success(it)))
                getMemoTableData()
            },
            {
                _myTimeTableLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
            }))
    }

    fun getMemoTableData() {
        compositeDisposable.add(
            memotableInfoRepository.getMemoTableInfoAllDB().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    _myMemoTableLiveData.postValue(Event(Resource.success(it)))
                },
                {
                    _myMemoTableLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                })
        )
    }


    override fun onCleared() {
        super.onCleared()
    }

}