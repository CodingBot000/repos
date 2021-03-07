package com.exam.timetable.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.exam.sample.model.datasource.local.favorite.MemoTableDBInfoLocalDataSource
import com.exam.timetable.common.BaseViewModel
import com.exam.timetable.livedata.Event
import com.exam.timetable.model.data.*
import com.exam.timetable.model.datasource.remote.detail.DetailDataRemoteDataSource
import com.exam.timetable.model.repository.detail.DetailDataRepository
import com.exam.timetable.model.repository.memo.MemoRepository

import com.exam.timetable.utils.Resource
import com.exam.timetable.utils.getDayOfTheWeekConvert
import com.exam.timetable.utils.isNetworkConnected
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody


class MemoViewModel(private val memoRepository: MemoRepository,
                    private val memoTableDBInfoLocalDataSource: MemoTableDBInfoLocalDataSource,
                    private val detailDataRemoteDataSource: DetailDataRemoteDataSource) : BaseViewModel()  {

    private val _itemLiveData = MutableLiveData<Event<Resource<ResponseBody>>>()
    val itemLiveData: LiveData<Event<Resource<ResponseBody>>> get() = _itemLiveData
    private val _itemMemoLiveData = MutableLiveData<Event<Resource<MemoDBInfo>>>()
    val itemMemoLiveData: LiveData<Event<Resource<MemoDBInfo>>> get() = _itemMemoLiveData


    fun addMemo(memo: MemoData) {
        if (!isNetworkConnected())
            return

        compositeDisposable.add(
            memoRepository.addMemo(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    _itemLiveData.postValue(Event(Resource.success(it)))
                    insertMemoToDB(memo!!)
                }, {
                    _itemLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                })
        )
    }



    fun insertMemoToDB(memo: MemoData) {

        val myMemo = MemoDBInfo(memo.keyLecture, memo.lecture_code, memo.type, memo.title, memo.description, memo.date)

        compositeDisposable.add(
            memoTableDBInfoLocalDataSource.insertInfo(myMemo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({
                    _itemMemoLiveData.postValue(Event(Resource.success(null)))
                },
                {
                    _itemMemoLiveData.postValue(Event(Resource.success(null)))
                })
        )
    }


    override fun onCleared() {
        super.onCleared()
    }

}