package com.exam.timetable.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "TABLE_MEMO", primaryKeys = ["keyLecture", "title", "description", "date"])
class MemoDBInfo(
    @ColumnInfo(name = "keyLecture") var keyLecture: String,
    @ColumnInfo(name = "lectureCode") var lectureCode: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "date") var date: String
)
