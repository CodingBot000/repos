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

    override fun insertInfo(favoriteInfo: TimeTableDBInfo) : Single<Long> {
        return timeTableDao.insert(favoriteInfo)
    }

    override fun removeInfo(favoriteInfo: TimeTableDBInfo) : Completable {
        return timeTableDao.delete(favoriteInfo)
    }

    override fun updateInfo(favoriteInfo: TimeTableDBInfo) : Completable {
        return timeTableDao.update(favoriteInfo)
    }
}