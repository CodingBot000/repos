package com.exam.timetable.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.exam.timetable.model.data.MemoDBInfo
import com.exam.timetable.model.data.TimeTableDBInfo

@Database(
    entities = arrayOf(TimeTableDBInfo::class,
                MemoDBInfo::class),
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun timeTableDao(): TimeTableDao
    abstract fun memoDao(): MemoDao
}