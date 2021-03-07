package com.exam.timetable.model.repository.search


import com.exam.timetable.model.data.LecureDataParent
import com.exam.timetable.model.datasource.remote.search.SearchRemoteDataSource
import io.reactivex.Single

class SearchRepositoryImpl(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchRepository
{
    override fun requestSearchData(): Single<LecureDataParent> {
        return searchRemoteDataSource.requestSearchData()
    }

    override fun requestSearchDataCode(code: String): Single<LecureDataParent> {
        return searchRemoteDataSource.requestSearchDataCode(code)
    }

    override fun requestSearchDataName(name: String): Single<LecureDataParent> {
        return searchRemoteDataSource.requestSearchDataName(name)
    }

}