package com.exam.timetable.model.datasource.remote.search

import com.exam.timetable.api.ApiService
import com.exam.timetable.model.data.LecureDataParent
import com.exam.timetable.utils.Const
import io.reactivex.Single


class SearchRemoteDataSourceImpl(private val apiService: ApiService) :
    SearchRemoteDataSource {

    override fun requestSearchData(): Single<LecureDataParent> {
        return apiService.getSearchAll(Const.API_KEY)
    }

    override fun requestSearchDataCode(code: String): Single<LecureDataParent> {
        return apiService.getSearchFromCode(Const.API_KEY, code)
    }

    override fun requestSearchDataName(name: String): Single<LecureDataParent> {
        return apiService.getSearchFromName(Const.API_KEY, name)
    }


}