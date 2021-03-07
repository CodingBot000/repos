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

    override fun insertInfo(favoriteInfo: MemoDBInfo) : Completable {
        return memoDao.insert(favoriteInfo)
    }

    override fun removeInfo(favoriteInfo: MemoDBInfo) : Completable {
        return memoDao.delete(favoriteInfo)
    }

    override fun updateInfo(favoriteInfo: MemoDBInfo) : Completable {
        return memoDao.update(favoriteInfo)
    }
}