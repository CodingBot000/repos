package com.exam.timetable.model.datasource.remote.detail

import com.exam.timetable.model.data.TimeTableDataParent
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody

interface DetailDataRemoteDataSource {

    fun addLectureToTimeTable(code:String):Single<ResponseBody>
    fun removeLectureToTimeTable(code:String):Single<ResponseBody>
    fun getLectureToTimeTable(): Single<TimeTableDataParent>
}