package com.exam.timetable.model.data


import com.google.gson.annotations.SerializedName


/**
 * api data class
 */


data class LecureDataParent(@SerializedName("Items") val lectureDataArr:ArrayList<LectureData>,
                            val Count: Int, val ScannedCount:Int
)
data class LectureData(val code:String, val lecture:String,  val professor:String,
                          val location:String, val start_time:String,
                          val end_time: String, val dayofweek:ArrayList<String>
)

data class MemoDataParent(@SerializedName("Items") val memoDataArr:ArrayList<MemoData>,
                          val Count: Int, val ScannedCount:Int
)
data class MemoData(val user_key:String, val lecture_code:String,  val type:String,
                       val title:String, val description:String,
                       val date: String, val keyLecture:String
)

data class TimeTableDataParent(@SerializedName("Items") val timeTableDataArr:ArrayList<TimeTableData>,
                               val Count: Int, val ScannedCount:Int
)
data class TimeTableData(val lecture_code: String)

/**
 * Request BodyParameter Data
 */
data class BodyParamAddLecture (val user_key: String, val code: String)
data class BodyParamDeleteLecture (val user_key: String, val code: String)
data class BodyParamAddMemo (val user_key: String, val code: String, val type:String,
                             val title:String, val description: String, val date:String)
data class BodyParamDeleteMemo (val code: String, val user_key: String, val type:String)
