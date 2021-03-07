package com.exam.sample.model.datasource.local.favorite


import com.exam.timetable.database.MemoDao
import com.exam.timetable.model.data.MemoDBInfo
import io.reactivex.Completable
import io.reactivex.Single

class MemoTableDBInfoLocalDataSourceImpl(private val memoDao: MemoDao) :
    MemoTableDBInfoLocalDataSource {

    override fun getInfo(userId : String) : Single<List<MemoDBInfo>> {
        return memoDao.getData(userId)
    }

    override fun getInfoAll() : Single<List<MemoDBInfo>> {
        return memoDao.getAll()
    }

    override fun insertInfo(info: MemoDBInfo) : Completable {
        return memoDao.insert(info)
    }

    override fun insertInfoNoRx(info: MemoDBInfo): Long {
        return memoDao.insertNoRx(info)
    }

    override fun removeInfo(info: MemoDBInfo) : Completable {
        return memoDao.delete(info)
    }

    override fun updateInfo(info: MemoDBInfo) : Completable {
        return memoDao.update(info)
    }

    override fun removeAll(): Int {
        return memoDao.deleteAll()
    }
}