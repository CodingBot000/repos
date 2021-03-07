package com.exam.sample.model.datasource.local.favorite

import androidx.lifecycle.LiveData

import com.exam.timetable.model.data.MemoDBInfo
import io.reactivex.Completable
import io.reactivex.Single


interface MemoTableDBInfoLocalDataSource {
    fun getInfo(userId : String) : Single<List<MemoDBInfo>>
    fun getInfoAll() : Single<List<MemoDBInfo>>
    fun insertInfo(info: MemoDBInfo) : Completable
    fun insertInfoNoRx(info: MemoDBInfo) : Long
    fun removeInfo(info: MemoDBInfo) : Completable
    fun updateInfo(info: MemoDBInfo) : Completable
    fun removeAll() : Int
}