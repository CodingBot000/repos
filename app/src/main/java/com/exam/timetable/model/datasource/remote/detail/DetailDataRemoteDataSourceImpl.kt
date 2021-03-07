package com.exam.timetable.model.datasource.remote.detail

import com.exam.timetable.api.ApiService
import com.exam.timetable.model.data.BodyParamAddLecture
import com.exam.timetable.model.data.BodyParamDeleteLecture
import com.exam.timetable.model.data.TimeTableDataParent
import com.exam.timetable.utils.Const
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody


class DetailDataRemoteDataSourceImpl(private val apiService: ApiService) :
    DetailDataRemoteDataSource {


    override fun addLectureToTimeTable(code: String): Single<ResponseBody> {
        return apiService.addLectureToTimeTable(Const.API_KEY, BodyParamAddLecture(Const.USER_KEY, code))
    }

    override fun removeLectureToTimeTable(code: String): Single<ResponseBody> {
        return apiService.deleteLectureToTimeTable(Const.API_KEY, BodyParamDeleteLecture(Const.USER_KEY, code))
    }

    override fun getLectureToTimeTable(): Single<TimeTableDataParent> {
       return apiService.getLectureToTimeTable(Const.API_KEY, Const.USER_KEY)
    }

}