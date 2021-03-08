package com.exam.timetable.viewmodel


import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import com.exam.timetable.common.BaseViewModel
import com.exam.timetable.livedata.Event
import com.exam.timetable.model.data.*
import com.exam.timetable.model.repository.detail.DetailDataRepository
import com.exam.timetable.model.repository.memo.MemoRepository
import com.exam.timetable.model.repository.search.SearchRepository
import com.exam.timetable.model.repository.timetable.MemotableInfoRepository
import com.exam.timetable.model.repository.timetable.TimetableInfoRepository
import com.exam.timetable.ui.LectureDataAll
import com.exam.timetable.ui.timetableview.Schedule
import com.exam.timetable.utils.Const
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
import java.util.*
import kotlin.concurrent.thread


class MainViewModel(private val timetableInfoRepository: TimetableInfoRepository,
                    private val memotableInfoRepository: MemotableInfoRepository,
                    private val detailDataRepository: DetailDataRepository,
                    private val memoRepository: MemoRepository,
                    private val searchRepository:SearchRepository
                    ) : BaseViewModel()  {

    private val _itemLiveData = MutableLiveData<Event<Resource<TimeTableDataParent>>>()
    val itemLiveData: LiveData<Event<Resource<TimeTableDataParent>>> get() = _itemLiveData
    private val _myTimeTableLiveData = MutableLiveData<Event<Resource<List<TimeTableDBInfo>>>>()
    val myTimeTableLiveData: LiveData<Event<Resource<List<TimeTableDBInfo>>>> get() = _myTimeTableLiveData
    private val _myMemoTableLiveData = MutableLiveData<Event<Resource<ArrayList<Schedule>>>>()
    val myMemoTableLiveData: LiveData<Event<Resource<ArrayList<Schedule>>>> get() = _myMemoTableLiveData

    init {
        getAllLectures()
    }

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
                val timeTableDBInfoArray = ArrayList<TimeTableDBInfo>()
                timeTableDBInfoArray.addAll((it))
                getMemoTableData(timeTableDBInfoArray)
            },
            {
                _myTimeTableLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
            }))
    }

    fun getMemoTableData(timeTableDBInfoArray: ArrayList<TimeTableDBInfo>) {
        compositeDisposable.add(
            memotableInfoRepository.getMemoTableInfoAllDB().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    val schdules = setTimeTableData(it, timeTableDBInfoArray)
                    _myMemoTableLiveData.postValue(Event(Resource.success(schdules)))
                },
                {
                    _myMemoTableLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                })
        )
    }

    fun syncMyDatas() {
        if (!isNetworkConnected())
            return

        thread {
            timetableInfoRepository.removeAllTimeTableInfoDB()
            memotableInfoRepository.removeAllMemoTableInfoDB()
        }

        var callIndex = 0
        compositeDisposable.add(
            Single.merge(
                detailDataRepository.getLectureToTimeTable(),
                memoRepository.requestMemoData("")
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    if (it is MemoDataParent) {
                        for (mData in it.memoDataArr) {
                            for (data in LectureDataAll) {
                                if (mData.lecture_code == data.code) {
                                    for (weekData in data.dayofweek) {
                                        val key = "${data.code}_${getDayOfTheWeekConvert(weekData)}"
                                        val info = MemoDBInfo(
                                            key, mData.lecture_code, mData.type,
                                            mData.title, mData.description, mData.date
                                        )
                                        thread {
                                            memotableInfoRepository.insertMemoTableInfoDBNoRx(info)
                                        }
                                    }
                                }
                            }
                        }
                    } else if (it is TimeTableDataParent) {
                        for (tData in it.timeTableDataArr) {
                            for (data in LectureDataAll) {
                                if (data.code == tData.lecture_code) {
                                    val num = Random().nextInt(Const.STICKER_COLOR.size)
                                    for (weekData in data.dayofweek) {
                                        val key = "${data.code}_${getDayOfTheWeekConvert(weekData)}"
                                        val myInfo = TimeTableDBInfo(
                                            key,
                                            data.code,
                                            data.lecture,
                                            data.professor,
                                            data.location,
                                            data.start_time,
                                            data.end_time,
                                            getDayOfTheWeekConvert(weekData),
                                            Const.STICKER_COLOR[num]
                                        )
                                        thread {
                                            val result =
                                                timetableInfoRepository.insertTimeTableInfoDBNoRx(
                                                    myInfo
                                                )
                                        }
                                    }
                                    break
                                }

                            }
                        }
                    }
                    callIndex++
                    if (callIndex > 1) {
                        getInitData()
                    }

                }, {

                })
        )
    }

    fun setTimeTableData(it: List<MemoDBInfo>, timeTableDBInfoArray: ArrayList<TimeTableDBInfo>)
    : ArrayList<Schedule> {
        val schedules = ArrayList<Schedule>()
        val memoDBInfoMap = HashMap<String, ArrayList<MemoDBInfo>>()
        for (info in it) {
            if (memoDBInfoMap.containsKey(info.keyLecture)) {
                val list = memoDBInfoMap.get(info.keyLecture)
                list!!.add(info)
                memoDBInfoMap.put(
                    info.keyLecture,
                    list
                )
            } else {
                val list = ArrayList<MemoDBInfo>()
                list.add(info)
                memoDBInfoMap.put(
                    info.keyLecture,
                    list
                )
            }
        }


        for (tData in timeTableDBInfoArray) {
            val startTimeArr = tData.start_time.split(":")
            val endTimeArr = tData.end_time.split(":")
            val schedule = Schedule()

            schedule.startTime.hour = startTimeArr[0].toInt()
            schedule.startTime.minute = startTimeArr[1].toInt()
            schedule.endTime.hour = endTimeArr[0].toInt()
            schedule.endTime.minute = endTimeArr[1].toInt()
            schedule.classTitle = tData.lecture
            schedule.classPlace = tData.location
            schedule.day = tData.dayofweek.toInt()
            schedule.color = tData.color
            schedule.key = tData.keyLecture
            schedule.memoDatas = memoDBInfoMap[tData.keyLecture]

            schedules.add(schedule)

        }
        return schedules
    }


    fun getAllLectures() {
        if (!isNetworkConnected())
            return

        compositeDisposable.add(
            searchRepository.requestSearchData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showProgress()
                }
                .doAfterTerminate { hideProgress() }
                .subscribe({ it ->
                    LectureDataAll.clear()
                    for (lectureData in it.lectureDataArr)
                        LectureDataAll.add(lectureData)

                }, {
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
    }

}