package com.exam.timetable.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TABLE_TIMETABLE")
class TimeTableDBInfo(
          @PrimaryKey @ColumnInfo(name = "keyLecture") var keyLecture: String,
          @ColumnInfo(name = "lectureCode") var lectureCode: String,
          @ColumnInfo(name = "lecture") var lecture: String,
          @ColumnInfo(name = "professor") var professor: String,
          @ColumnInfo(name = "location") var location: String,
          @ColumnInfo(name = "start_time") var start_time: String,
          @ColumnInfo(name = "end_time") var end_time: String,
          @ColumnInfo(name = "dayofweek") var dayofweek: String,
          @ColumnInfo(name = "color") var color: String
)
