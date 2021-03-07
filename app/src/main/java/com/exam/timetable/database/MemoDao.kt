package com.exam.timetable.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.exam.timetable.model.data.MemoDBInfo
import com.exam.timetable.model.data.TimeTableDBInfo
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface MemoDao {
    @Query("SELECT * FROM TABLE_MEMO")
    fun getAll(): Single<List<MemoDBInfo>>

    @Query("SELECT * FROM TABLE_MEMO where keyLecture = :key")
    fun getData(key : String) : Single<List<MemoDBInfo>>

    @Update
    fun update(memoDBInfo : MemoDBInfo) : Completable

    @Update
    fun update(list :List<MemoDBInfo>) : Completable

    @Delete
    fun delete(memoDBInfo : MemoDBInfo) : Completable

    @Delete
    fun delete(list :List<MemoDBInfo>) : Completable

    @Query("DELETE FROM TABLE_MEMO")
    fun deleteAll() : Int

    @Insert(onConflict = REPLACE)
    fun insert(memoDBInfo: MemoDBInfo) : Completable

    @Insert(onConflict = REPLACE)
    fun insertNoRx(memoDBInfo: MemoDBInfo) : Long

}
