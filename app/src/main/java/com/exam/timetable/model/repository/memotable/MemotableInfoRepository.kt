package com.exam.timetable.model.repository.timetable


import com.exam.timetable.model.data.MemoDBInfo
import com.exam.timetable.model.data.TimeTableDBInfo
import io.reactivex.Completable

import io.reactivex.Single

interface MemotableInfoRepository {
    fun getMemoTableInfoAllDB() : Single<List<MemoDBInfo>>
    fun getMemoTableInfoDB(lecureKey : String) : Single<List<MemoDBInfo>>
    fun insertMemoTableInfoDB(info : MemoDBInfo) : Completable
    fun insertMemoTableInfoDBNoRx(info : MemoDBInfo) : Long
    fun removeMemoTableInfoDB(info : MemoDBInfo) : Completable
    fun removeAllMemoTableInfoDB() : Int
    fun updateMemoTableInfoDB(info : MemoDBInfo) : Completable

}