package com.exam.timetable.model.repository.timetable

import com.exam.timetable.model.data.TimeTableDBInfo
import io.reactivex.Completable

import io.reactivex.Single

interface TimetableInfoRepository {
    fun getTimeTableInfoAllDB() : Single<List<TimeTableDBInfo>>
    fun getTimeTableInfoDB(lecureKey : String) : Single<TimeTableDBInfo>
    fun insertTimeTableInfoDB(info : TimeTableDBInfo) : Single<Long>
    fun insertTimeTableInfoDBNoRx(info : TimeTableDBInfo) : Long
    fun removeTimeTableInfoDB(info : TimeTableDBInfo) : Completable
    fun removeAllTimeTableInfoDB() : Int
    fun updateTimeTableInfoDB(info : TimeTableDBInfo) : Completable
}