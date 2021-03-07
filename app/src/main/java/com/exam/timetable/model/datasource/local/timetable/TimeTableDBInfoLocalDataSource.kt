package com.exam.timetable.model.datasource.local.timetable


import com.exam.timetable.model.data.TimeTableDBInfo
import io.reactivex.Completable
import io.reactivex.Single


interface TimeTableDBInfoLocalDataSource {
    fun getInfo(lectureKey : String) : Single<TimeTableDBInfo>
    fun getInfoAll() : Single<List<TimeTableDBInfo>>
    fun insertInfo(info: TimeTableDBInfo) : Single<Long>
    fun removeInfo(info: TimeTableDBInfo) : Completable
    fun updateInfo(info: TimeTableDBInfo) : Completable
}