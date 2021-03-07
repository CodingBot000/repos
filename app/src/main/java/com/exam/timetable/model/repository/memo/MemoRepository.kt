package com.exam.timetable.model.repository.memo

import com.exam.timetable.model.data.LecureDataParent
import com.exam.timetable.model.data.MemoData
import com.exam.timetable.model.data.MemoDataParent

import io.reactivex.Single
import okhttp3.ResponseBody

interface MemoRepository {
    fun requestMemoData(code: String): Single<MemoDataParent>
    fun addMemo(memo: MemoData):Single<ResponseBody>
    fun deleteMemo(code:String, type:String): Single<ResponseBody>
}