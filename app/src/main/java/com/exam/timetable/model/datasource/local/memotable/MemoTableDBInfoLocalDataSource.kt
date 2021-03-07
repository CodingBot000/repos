package com.exam.sample.model.datasource.local.favorite

import androidx.lifecycle.LiveData

import com.exam.timetable.model.data.MemoDBInfo
import io.reactivex.Completable
import io.reactivex.Single


interface MemoTableDBInfoLocalDataSource {
    fun getInfo(userId : String) : Single<List<MemoDBInfo>>
    fun getInfoAll() : Single<List<MemoDBInfo>>
    fun insertInfo(favoriteInfo: MemoDBInfo) : Completable
    fun removeInfo(favoriteInfo: MemoDBInfo) : Completable
    fun updateInfo(favoriteInfo: MemoDBInfo) : Completable
}