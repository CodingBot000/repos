package com.exam.timetable.api


import com.exam.timetable.model.data.*
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    /**
     * TimeTable
     */
    @GET("timetable")
    fun getLectureToTimeTable(
        @Header("x-api-key") apiKey: String,
        @Query("user_key") userKey: String
    ): Single<TimeTableDataParent>

    @POST("timetable")
    fun addLectureToTimeTable(
        @Header("x-api-key") apiKey: String,
        @Body bodyParam: BodyParamAddLecture
    ): Single<ResponseBody>


    @HTTP(method = "DELETE", path = "timetable", hasBody = true)
    fun deleteLectureToTimeTable(
        @Header("x-api-key") apiKey: String,
        @Body bodyParam: BodyParamDeleteLecture
    ): Single<ResponseBody>

    /**
     * memo
     */
    @GET("memo")
    fun requestMemo(
        @Header("x-api-key") apiKey: String,
        @Query("user_key") userKey: String,
        @Query("code") code: String
    ): Single<MemoDataParent>

    @GET("memo")
    fun requestMemoAll(
        @Header("x-api-key") apiKey: String,
        @Query("user_key") userKey: String
    ): Single<MemoDataParent>

    @POST("memo")
    fun addMemo(
        @Header("x-api-key") apiKey: String,
        @Body bodyParam: BodyParamAddMemo
    ): Single<ResponseBody>

    @HTTP(method = "DELETE", path = "memo", hasBody = true)
    fun deleteMemo(
        @Header("x-api-key") apiKey: String,
        @Body bodyParam: BodyParamDeleteMemo
    ): Single<ResponseBody>

    /**
     * lectures
     */

    @GET("lectures")
    fun getSearchAll(
        @Header("x-api-key") apiKey: String
    ): Single<LecureDataParent>

    @GET("lectures")
    fun getSearchFromCode(
        @Header("x-api-key") apiKey: String,
        @Query("code") code: String
    ): Single<LecureDataParent>

    @GET("lectures")
    fun getSearchFromName(
        @Header("x-api-key") apiKey: String,
        @Query("lecture") lecture: String
    ): Single<LecureDataParent>
}

