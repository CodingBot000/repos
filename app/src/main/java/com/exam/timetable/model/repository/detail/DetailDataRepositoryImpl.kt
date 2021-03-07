package com.exam.timetable.model.repository.detail



import com.exam.timetable.model.datasource.remote.detail.DetailDataRemoteDataSource
import com.exam.timetable.model.data.TimeTableDataParent
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody

class DetailDataRepositoryImpl(
    private val detailDataRemoteDataSource: DetailDataRemoteDataSource
) : DetailDataRepository
{
    override fun addLectureToTimeTable(code: String): Single<ResponseBody> {
        return detailDataRemoteDataSource.addLectureToTimeTable(code)
    }

    override fun removeLectureToTimeTable(code: String): Single<ResponseBody> {
        return detailDataRemoteDataSource.removeLectureToTimeTable(code)
    }

    override fun getLectureToTimeTable(): Single<TimeTableDataParent> {
        return detailDataRemoteDataSource.getLectureToTimeTable()
    }

}