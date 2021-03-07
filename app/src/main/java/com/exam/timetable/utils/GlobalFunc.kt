package com.exam.timetable.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.view.View
import android.widget.Toast
import com.exam.timetable.App
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun getCurrentTitleDate() : String {
    val cal = Calendar.getInstance()
    println(cal)

    val year = cal[Calendar.YEAR]
    val month = cal[Calendar.MONTH] + 1
    val day = cal[Calendar.DAY_OF_MONTH]
    val hour = cal[Calendar.HOUR_OF_DAY]
    val min = cal[Calendar.MINUTE]
    val sec = cal[Calendar.SECOND]
//    println("현재 시각은 " + year + "년도 " + month + "월 " + day + "일 " + hour + "시 " + min + "분 " + sec + "초입니다.")
    return "$year 년 $month 월"

}
fun weekCalendar(yyyymmdd: String): Array<String?> {
    var dateString = "20190719"
    dateString = yyyymmdd
    val sdf = SimpleDateFormat("yyyyMMdd")
    var date = Date()
    try {
        date = sdf.parse(dateString)
        println("date=$date")
    } catch (e: ParseException) {
    }
    val cal = Calendar.getInstance(Locale.KOREA)
    cal.time = date
    println("qq 입력한 날짜 : " + sdf.format(cal.time))
    cal.add(Calendar.DATE, 2 - cal[Calendar.DAY_OF_WEEK])
    //        System.out.println("qq 첫번째 요일(월요일)날짜:"+sdf.format(cal.getTime()));
    cal.time = date
    cal.add(Calendar.DATE, 8 - cal[Calendar.DAY_OF_WEEK])
    //        System.out.println("qq 마지막 요일(일요일)날짜:"+sdf.format(cal.getTime()));
    val arrYMD = arrayOfNulls<String>(7)
    val inYear = cal[Calendar.YEAR]
    val inMonth = cal[Calendar.MONTH]
    var inDay = cal[Calendar.DAY_OF_MONTH]
    var yoil = cal[Calendar.DAY_OF_WEEK] //요일나오게하기(숫자로)
    yoil = if (yoil != 1) {   //해당요일이 일요일이 아닌경우
        yoil - 2
    } else {           //해당요일이 일요일인경우
        7
    }
    inDay = inDay - yoil
    for (i in 0..6) {
        cal[inYear, inMonth] = inDay + i //
        val y = Integer.toString(cal[Calendar.YEAR])
        var m = Integer.toString(cal[Calendar.MONTH] + 1)
        var d = Integer.toString(cal[Calendar.DAY_OF_MONTH])
        if (m.length == 1) m = "0$m"
        if (d.length == 1) d = "0$d"
        arrYMD[i] = y + m + d // 0 일요일 1 월요일
//        if (y + m + d == yyyymmdd) println("qq 이거슨오늘!!! =$y$m$d  $i")

    }
    return arrYMD
}

fun getToday():String {
    val sdf = SimpleDateFormat("yyyyMMdd");
    val c1 = Calendar.getInstance();
    return sdf.format(c1.getTime());
}

fun getTodayWithDash():String {
    val sdf = SimpleDateFormat("yyyy-MM-dd");
    val c1 = Calendar.getInstance();
    return sdf.format(c1.getTime());
}


fun getDayOfTheWeekConvert(getHangul: String) : String {
    var returnName = ""
    when (getHangul) {
        "월" -> returnName = Const.MON.toString()
        "화" -> returnName = Const.TUE.toString()
        "수" -> returnName = Const.WED.toString()
        "목" -> returnName = Const.THU.toString()
        "금" -> returnName = Const.FRI.toString()
        "토" -> returnName = Const.SAT.toString()
        "일" -> returnName = Const.SUN.toString()

    }
    return returnName
}


fun getDayOfTheWeekReverseConvert(num: String) : String {
    var returnName = ""
    when (num) {
        Const.MON.toString() -> returnName = "월"
        Const.TUE.toString() -> returnName = "화"
        Const.WED.toString() -> returnName = "수"
        Const.THU.toString() -> returnName = "목"
        Const.FRI.toString() -> returnName = "금"
        Const.SAT.toString() -> returnName = "토"
        Const.SUN.toString() -> returnName = "일"

    }
    return returnName
}

fun toastMsg(resId: Int) {
    toastMsg(App.getApplication().getString(resId))
}

@SuppressLint("CheckResult")
fun toastMsg(msg: String) {
    if (msg.isEmpty())
        return

    Maybe.just(0).observeOn(AndroidSchedulers.mainThread()).subscribe {
        Toast.makeText(App.getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}

fun isNetworkConnected(): Boolean{
    val cm = App.getApplication().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)  as ConnectivityManager
    val activeNetwork : NetworkInfo? = cm.activeNetworkInfo
    val isConnected = activeNetwork != null && activeNetwork.isConnected
    return isConnected
}

