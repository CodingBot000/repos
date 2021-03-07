package com.exam.sample.model.datasource.local.favorite


import com.exam.timetable.database.TimeTableDao
import com.exam.timetable.model.data.TimeTableDBInfo
import com.exam.timetable.model.datasource.local.timetable.TimeTableDBInfoLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class TimeTableDBInfoLocalDataSourceImpl(private val timeTableDao: TimeTableDao) :
    TimeTableDBInfoLocalDataSource {

    override fun getInfo(userId : String) : Single<TimeTableDBInfo> {
        return timeTableDao.getData(userId)
    }

    override fun getInfoAll() : Single<List<TimeTableDBInfo>> {
        return timeTableDao.getAll()
    }

    override fun insertInfo(info: TimeTableDBInfo) : Single<Long> {
        return timeTableDao.insert(info)
    }

    override fun insertInfoNoRx(info: TimeTableDBInfo): Long {
        return timeTableDao.insertNoRx(info)
    }

    override fun removeInfo(info: TimeTableDBInfo) : Completable {
        return timeTableDao.delete(info)
    }


    override fun updateInfo(info: TimeTableDBInfo) : Completable {
        return timeTableDao.update(info)
    }

    override fun removeAll(): Int {
        return timeTableDao.deleteAll()
    }
}