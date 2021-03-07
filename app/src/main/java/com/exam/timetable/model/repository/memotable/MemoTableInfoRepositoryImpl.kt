package com.exam.timetable.model.repository.memotable


import com.exam.sample.model.datasource.local.favorite.MemoTableDBInfoLocalDataSource
import com.exam.timetable.model.data.MemoDBInfo
import com.exam.timetable.model.datasource.local.timetable.TimeTableDBInfoLocalDataSource
import com.exam.timetable.model.data.TimeTableDBInfo
import com.exam.timetable.model.repository.timetable.MemotableInfoRepository
import io.reactivex.Completable
import io.reactivex.Single

class MemoTableInfoRepositoryImpl(
    private val memoTableDBInfoLocalDataSource: MemoTableDBInfoLocalDataSource
) : MemotableInfoRepository
{
    override fun getMemoTableInfoAllDB(): Single<List<MemoDBInfo>> {
        return memoTableDBInfoLocalDataSource.getInfoAll()
    }

    override fun getMemoTableInfoDB(lecureKey: String): Single<List<MemoDBInfo>> {
        return memoTableDBInfoLocalDataSource.getInfo(lecureKey)
    }

    override fun insertMemoTableInfoDB(info: MemoDBInfo): Completable {
        return memoTableDBInfoLocalDataSource.insertInfo(info)
    }

    override fun removeMemoTableInfoDB(info: MemoDBInfo): Completable {
        return memoTableDBInfoLocalDataSource.removeInfo(info)
    }

    override fun updateMemoTableInfoDB(info: MemoDBInfo): Completable {
        return memoTableDBInfoLocalDataSource.updateInfo(info)
    }
}