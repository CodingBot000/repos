package com.exam.timetable.model.datasource.remote.memo

import com.exam.timetable.api.ApiService
import com.exam.timetable.model.data.*
import com.exam.timetable.utils.Const
import io.reactivex.Single
import okhttp3.ResponseBody


class MemoDataRemoteDataSourceImpl(private val apiService: ApiService) :
    MemoDataRemoteDataSource {

    override fun requestMemoData(code: String): Single<MemoDataParent> {
        if (code.isEmpty()) {
            return apiService.requestMemoAll(Const.API_KEY, Const.USER_KEY)
        } else {
            return apiService.requestMemo(Const.API_KEY, Const.USER_KEY, code)
        }
    }

    override fun addMemo(
        memo: MemoData
    ): Single<ResponseBody> {
        return apiService.addMemo(Const.API_KEY, BodyParamAddMemo(Const.USER_KEY, memo.lecture_code, memo.type, memo.title, memo.description, memo.date))
    }

    override fun deleteMemo(code: String, type: String): Single<ResponseBody> {
        return apiService.deleteMemo(Const.API_KEY, BodyParamDeleteMemo(code, Const.USER_KEY, type))
    }

}