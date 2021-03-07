package com.exam.timetable.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.exam.timetable.common.BaseViewModel
import com.exam.timetable.livedata.Event
import com.exam.timetable.model.data.*
import com.exam.timetable.model.datasource.remote.memo.MemoDataRemoteDataSource
import com.exam.timetable.model.repository.detail.DetailDataRepository
import com.exam.timetable.model.repository.timetable.MemotableInfoRepository
import com.exam.timetable.model.repository.timetable.TimetableInfoRepository
import com.exam.timetable.ui.timetableview.Sticker
import com.exam.timetable.utils.Const
import com.exam.timetable.utils.Const.Companion.STICKER_COLOR

import com.exam.timetable.utils.Resource
import com.exam.timetable.utils.getDayOfTheWeekConvert
import com.exam.timetable.utils.isNetworkConnected
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.*


class DetailViewModel(private val detailDataRepository: DetailDataRepository,
                      private val timetableInfoRepository: TimetableInfoRepository,
                      private val memotableInfoRepository: MemotableInfoRepository,
                      private val memoDataRemoteDataSource: MemoDataRemoteDataSource
) : BaseViewModel()  {

    private val _itemLiveData = MutableLiveData<Event<Resource<ResponseBody>>>()
    val itemLiveData: LiveData<Event<Resource<ResponseBody>>> get() = _itemLiveData
    private val _detailItemLiveData = MutableLiveData<Event<Resource<TimeTableDBInfo>>>()
    val detailItemLiveData: LiveData<Event<Resource<TimeTableDBInfo>>> get() = _detailItemLiveData
    private val _myMemoTableLiveData = MutableLiveData<Event<Resource<List<MemoDBInfo>>>>()
    val myMemoTableLiveData: LiveData<Event<Resource<List<MemoDBInfo>>>> get() = _myMemoTableLiveData

    private val _removeLectureLiveData = MutableLiveData<Event<Resource<ResponseBody>>>()
    val removeLectureLiveData: LiveData<Event<Resource<ResponseBody>>> get() = _removeLectureLiveData
    private val _removeLectureDBLiveData = MutableLiveData<Event<Resource<List<TimeTableDataParent>>>>()
    val removeLectureDBLiveData: LiveData<Event<Resource<List<TimeTableDataParent>>>> get() = _removeLectureDBLiveData
    private val _removeMemoLiveData = MutableLiveData<Event<Resource<ResponseBody>>>()
    val removeMemoLiveData: LiveData<Event<Resource<ResponseBody>>> get() = _removeMemoLiveData
    private val _removeMemoDBLiveData = MutableLiveData<Event<Resource<MemoDataParent>>>()
    val removeMemoDBLiveData: LiveData<Event<Resource<MemoDataParent>>> get() = _removeMemoDBLiveData


    fun addLectureToTimeTable(data:InteractionData) {
        if (!isNetworkConnected())
            return

        compositeDisposable.add(
            detailDataRepository.addLectureToTimeTable(data.code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    _itemLiveData.postValue(Event(Resource.success(it)))
                    insertTimeTableInfoToDB(data)
                }, {
                    _itemLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                })
        )
    }

    fun removeLectureToTimeTable(info:TimeTableDBInfo, lectureKey:String) {
        compositeDisposable.add(
            detailDataRepository.removeLectureToTimeTable(info.lectureCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    _removeLectureLiveData.postValue(Event(Resource.success(it)))
                    removeLectureToDBTimeTable(info)
                }, {
                    _removeLectureLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                })
        )
    }

    fun removeLectureToDBTimeTable(info:TimeTableDBInfo) {
        compositeDisposable.add(
            timetableInfoRepository.removeTimeTableInfoDB(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({
                    _removeLectureDBLiveData.postValue(Event(Resource.success(null)))
                },
                    {
                    _removeLectureDBLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                })
        )
    }

    fun insertTimeTableInfoToDB(data:InteractionData) {
        val num = Random().nextInt(Const.STICKER_COLOR.size)
        for (weekData in data.dayofweek) {
            val key = "${data.code}_${getDayOfTheWeekConvert(weekData)}"
            val myInfo = TimeTableDBInfo(key, data.code, data.lecture, data.professor, data.location
                , data.start_time, data.end_time, getDayOfTheWeekConvert(weekData), STICKER_COLOR[num])

            compositeDisposable.add(
                 timetableInfoRepository.insertTimeTableInfoDB(myInfo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({

                },
                {

                }))
        }
    }

    fun getTimeTableData(key:String) {
        compositeDisposable.add(
            timetableInfoRepository.getTimeTableInfoDB(key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    _detailItemLiveData.postValue(Event(Resource.success(it)))
                },
                {
                    _detailItemLiveData.postValue(Event(Resource.error(it.message.toString(), null)))

                })
        )
    }



    fun getMemoTableData(key:String) {
        compositeDisposable.add(
            memotableInfoRepository.getMemoTableInfoDB(key).subscribeOn(Schedulers.io())

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
                }))
    }


    fun removeMemoAPI(info:MemoDBInfo) {
        compositeDisposable.add(
            memoDataRemoteDataSource.deleteMemo(info.lectureCode, info.type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    _removeMemoLiveData.postValue(Event(Resource.success(it)))
                    removeMemoTableData(info)
                },
                {
                    _removeMemoLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                }))
    }

    fun removeMemoTableData(info:MemoDBInfo) {
        compositeDisposable.add(
            memotableInfoRepository.removeMemoTableInfoDB(info).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({
                    _removeMemoDBLiveData.postValue(Event(Resource.success(null)))
                },
                {
                    _removeMemoDBLiveData.postValue(Event(Resource.error("로컬 데이터 삭제 오류", null)))
                }))
    }

    override fun onCleared() {
        super.onCleared()
    }

}