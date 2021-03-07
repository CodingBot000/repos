package com.exam.timetable.model.data


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * intent data
 */

@Parcelize
data class InteractionData(val code:String, val lecture:String,  val professor:String,
                           val location:String, val start_time:String,
                           val end_time: String, val dayofweek:ArrayList<String>
) : Parcelable


