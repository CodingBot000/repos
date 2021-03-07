package com.exam.timetable.utils

import com.exam.timetable.R

class Const {
    companion object  {
        @kotlin.jvm.JvmField
        var TODAY:String = "20210305"

        const val LOG_TAG = "TIMETABLE_TASK"
        const val DB_NAME = "TimeTableDB.db"
        const val HTTP_LOG = true

        const val API_KEY = "QJuHAX8evMY24jvpHfHQ4pHGetlk5vn8FJbk70O6"
        const val USER_KEY = "7b09cd103763feec74065ca0a9419f07"
        const val BASE_URL = "https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/"

        const val EXTRA_KEY_INTERACTION = "INTERACTION"
        const val EXTRA_KEY_LECTURE_CODE = "LECTURECODE"
        const val EXTRA_KEY_KEYUNIQUE_CODE = "KEY_UNIQUE"
        const val EXTRA_KEY_SERVICE = "SERVICE"
        const val EXTRA_KEY_MEMO_ADD = "MEMOADD"
        const val EXTRA_KEY_LECTURE_ADD = "LECTUREADD"
        const val RXEVENT_REFRESH = "REFRESH"

        const val EMIT_INTERVAL = 1000L

        const val REQUEST_TYPE_TIMETABLE_GET = "TIMETABLE_GET"
        const val REQUEST_TYPE_TIMETABLE_ADD = "TIMETABLE_ADD"
        const val REQUEST_TYPE_TIMETABLE_REMOVE = "TIMETABLE_REMOVE"
        const val REQUEST_TYPE_MEMO_GET = "MEMO_GET"
        const val REQUEST_TYPE_MEMO_ADD = "MEMO_ADD"
        const val REQUEST_TYPE_MEMO_REMOVE = "MEMO_REMOVE"
        val STICKER_COLOR = arrayListOf<String>("#f08676", "#ecc369", "#a7ca70", "#7dd1c1", "#7aa5e9", "#fbaa68", "#9f86e1", "#78cb87", "#d397ed")

        const val MON = 0
        const val TUE = 1
        const val WED = 2
        const val THU = 3
        const val FRI = 4
        const val SAT = 5
        const val SUN = 6



    }
}