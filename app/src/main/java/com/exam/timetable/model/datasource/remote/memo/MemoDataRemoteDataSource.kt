package com.exam.timetable.model.datasource.remote.memo

import com.exam.timetable.model.data.MemoData
import com.exam.timetable.model.data.MemoDataParent
import com.exam.timetable.model.data.TimeTableDataParent
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface MemoDataRemoteDataSource {
    fun requestMemoData(code: String): Single<MemoDataParent>
    fun addMemo(memo: MemoData):Single<ResponseBody>
    fun deleteMemo(code:String, type:String): Single<ResponseBody>
}


