package com.exam.timetable.model.repository.memo


import com.exam.timetable.model.data.LecureDataParent
import com.exam.timetable.model.data.MemoData
import com.exam.timetable.model.data.MemoDataParent
import com.exam.timetable.model.datasource.remote.memo.MemoDataRemoteDataSource
import com.exam.timetable.model.datasource.remote.search.SearchRemoteDataSource
import io.reactivex.Single
import okhttp3.ResponseBody

class MemoRepositoryImpl(
    private val memoDataRemoteDataSource: MemoDataRemoteDataSource
) : MemoRepository
{
    override fun requestMemoData(code: String): Single<MemoDataParent> {
        return memoDataRemoteDataSource.requestMemoData(code)
    }

    override fun addMemo(
        memo: MemoData
    ): Single<ResponseBody> {
        return memoDataRemoteDataSource.addMemo(memo)
    }

    override fun deleteMemo(code: String, type: String): Single<ResponseBody> {
        return memoDataRemoteDataSource.deleteMemo(code, type)
    }

}