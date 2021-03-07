package com.exam.timetable.model.repository.timetable


import com.exam.timetable.model.datasource.local.timetable.TimeTableDBInfoLocalDataSource
import com.exam.timetable.model.data.TimeTableDBInfo
import com.exam.timetable.model.repository.timetable.MemotableInfoRepository
import com.exam.timetable.model.repository.timetable.TimetableInfoRepository
import io.reactivex.Completable
import io.reactivex.Single

class TimeTableInfoRepositoryImpl(
    private val timeTableDBInfoLocalDataSource: TimeTableDBInfoLocalDataSource
) : TimetableInfoRepository
{
    override fun getTimeTableInfoAllDB(): Single<List<TimeTableDBInfo>> {
        return timeTableDBInfoLocalDataSource.getInfoAll()
    }

    override fun getTimeTableInfoDB(lecureKey: String): Single<TimeTableDBInfo> {
        return timeTableDBInfoLocalDataSource.getInfo(lecureKey)
    }

    override fun insertTimeTableInfoDB(info: TimeTableDBInfo): Single<Long> {
        return timeTableDBInfoLocalDataSource.insertInfo(info)
    }

    override fun insertTimeTableInfoDBNoRx(info: TimeTableDBInfo): Long {
        return timeTableDBInfoLocalDataSource.insertInfoNoRx(info)
    }

    override fun removeTimeTableInfoDB(info: TimeTableDBInfo): Completable {
        return timeTableDBInfoLocalDataSource.removeInfo(info)
    }

    override fun removeAllTimeTableInfoDB(): Int {
        return timeTableDBInfoLocalDataSource.removeAll()
    }

    override fun updateTimeTableInfoDB(info: TimeTableDBInfo): Completable {
        return timeTableDBInfoLocalDataSource.updateInfo(info)
    }
}