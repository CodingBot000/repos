package com.exam.timetable.model.datasource.remote.search

import com.exam.timetable.model.data.LecureDataParent
import io.reactivex.Single

interface SearchRemoteDataSource {
    fun requestSearchData(): Single<LecureDataParent>
    fun requestSearchDataCode(code:String): Single<LecureDataParent>
    fun requestSearchDataName(name:String): Single<LecureDataParent>
}