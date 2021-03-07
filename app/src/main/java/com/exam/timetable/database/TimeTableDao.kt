package com.exam.timetable.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.exam.timetable.model.data.TimeTableDBInfo
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface TimeTableDao {
    @Query("SELECT * FROM TABLE_TIMETABLE")
    fun getAll(): Single<List<TimeTableDBInfo>>

    @Query("SELECT * FROM TABLE_TIMETABLE where keyLecture = :key")
    fun getData(key : String) : Single<TimeTableDBInfo>

    @Update
    fun update(timeTableDBInfo : TimeTableDBInfo) : Completable

    @Update
    fun update(list :List<TimeTableDBInfo>) : Completable

    @Delete
    fun delete(timeTableDBInfo : TimeTableDBInfo) : Completable

    @Delete
    fun delete(list :List<TimeTableDBInfo>) : Completable

    @Insert(onConflict = REPLACE)
    fun insert(timeTableDBInfo: TimeTableDBInfo) : Single<Long>


}
