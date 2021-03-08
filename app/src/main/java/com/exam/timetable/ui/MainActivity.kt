package com.exam.timetable.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.exam.timetable.R

import com.exam.timetable.common.BaseActivity
import com.exam.timetable.databinding.ActivityMainBinding
import com.exam.timetable.livedata.EventObserver
import com.exam.timetable.model.data.*
import com.exam.timetable.ui.timetableview.Schedule
import com.exam.timetable.ui.timetableview.TimetableView
import com.exam.timetable.utils.*
import com.exam.timetable.utils.Const.Companion.RXEVENT_REFRESH
import com.exam.timetable.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top_item.*
import kotlinx.android.synthetic.main.top_item_date.*
import kotlinx.android.synthetic.main.view_timetable.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val LectureDataAll = ArrayList<LectureData>()

class MainActivity :  BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.activity_main

    override val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.v(Const.LOG_TAG, "$TAG onNewIntent")

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun init() {

        tvDate.text = getCurrentTitleDate()
        viewModel.getInitData()

        timetable.setOnStickerSelectEventListener(object : TimetableView.OnStickerSelectedListener {
            override fun OnStickerSelected(idx: Int, schedules: ArrayList<Schedule>?) {

                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(Const.EXTRA_KEY_SERVICE, Const.EXTRA_KEY_MEMO_ADD)
                var schedule: Schedule? = null
                for (i in schedules!!.iterator()) {
                    if (i.hashCode() == idx) {
                        schedule = i
                        break
                    }
                }

                if (schedule == null) {
                    toastMsg("오류로 상세내용을 가져올 수 없습니다.")
                    return
                }

                intent.putExtra(Const.EXTRA_KEY_KEYUNIQUE_CODE, schedule.key)

                startActivity(intent)
            }
        })


        var dateIndex = 0
        btnLeft.setOnClickListener {
            var week = Calendar.getInstance();
            dateIndex -= 7;
            week.add(Calendar.DATE, dateIndex);
            var beforeWeek = SimpleDateFormat("yyyyMMdd").format(week.getTime());
            timetable.setWeekDay(beforeWeek)
            titleDate(beforeWeek)
            timetable.removeAll()
            if (isTodayWeek(beforeWeek))
                viewModel.getInitData()

        }
        tvToday.setOnClickListener {

            timetable.setWeekDay(Const.TODAY)
            titleDate(Const.TODAY)
            timetable.removeAll()
            viewModel.getInitData()
        }
        btnRight.setOnClickListener {
            var week = Calendar.getInstance();
            dateIndex += 7;
            week.add(Calendar.DATE, dateIndex);
            var afterWeek = SimpleDateFormat("yyyyMMdd").format(week.getTime());
            timetable.setWeekDay(afterWeek)
            titleDate(afterWeek)
            timetable.removeAll()
            if (isTodayWeek(afterWeek))
                viewModel.getInitData()
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }

        tvSync.setOnClickListener {
            timetable.setWeekDay(Const.TODAY)
            titleDate(Const.TODAY)
            viewModel.syncMyDatas()
        }
        RxEvent.getObservable().subscribe({
            if (it == RXEVENT_REFRESH) {
                Log.v(Const.LOG_TAG, RXEVENT_REFRESH)
                viewModel.getInitData()
            }
        }, {

        })

    }


    override fun initObserver() {
        viewModel.apply {
            isLoading.observe(this@MainActivity, EventObserver {
                if (it) binding.progress.visibility = View.VISIBLE else binding.progress.visibility = View.GONE


            })

            itemLiveData.observe(this@MainActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {

                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")
                    }
                }

            })

            myTimeTableLiveData.observe(this@MainActivity, EventObserver {

                when (it.status) {
                    Status.SUCCESS -> {

                    }

                    Status.ERROR -> {
                        RequestErrorMsgHandler.RequestErrorMsg(it.message ?: "", Const.REQUEST_TYPE_TIMETABLE_GET)
                    }
                }
            })



            myMemoTableLiveData.observe(this@MainActivity, EventObserver {

                when (it.status) {
                    Status.SUCCESS -> {
                        timetable.removeAll()
                        timetable.add(it.data)
                    }

                    Status.ERROR -> {
                        RequestErrorMsgHandler.RequestErrorMsg(it.message ?: "", Const.REQUEST_TYPE_MEMO_GET)
                    }
                }
            })

        }
    }


    private fun isTodayWeek(day:String) : Boolean  {
        val week = weekCalendar(day)
        for (i in week) {
            if (i == Const.TODAY) {
                return true
            }
        }
        return false
    }

    private fun titleDate(date: String) {
        tvDate.text = "${date.substring(0, 4)}년 ${date.substring(4, 6)}월"
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
