package com.exam.timetable.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isEmpty
import com.exam.timetable.R
import com.exam.timetable.common.BaseActivity
import com.exam.timetable.databinding.ActivityDetailBinding
import com.exam.timetable.livedata.EventObserver
import com.exam.timetable.model.data.InteractionData
import com.exam.timetable.model.data.MemoDBInfo
import com.exam.timetable.model.data.TimeTableDBInfo
import com.exam.timetable.utils.*
import com.exam.timetable.utils.Const.Companion.RXEVENT_REFRESH
import com.exam.timetable.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel



class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>()
{

    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.activity_detail

    override val viewModel : DetailViewModel by viewModel()

    private var timeTableInfo:TimeTableDBInfo? = null

    private lateinit var mode:String
    var lectureKey:String? = null
    var interactionData: InteractionData? = null
    var isNeedRefresh = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initData()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun getIntentData() {
        interactionData = intent.getParcelableExtra<InteractionData>(Const.EXTRA_KEY_INTERACTION)
        lectureKey = intent.getStringExtra(Const.EXTRA_KEY_KEYUNIQUE_CODE)
        mode = intent.getStringExtra(Const.EXTRA_KEY_SERVICE)

    }

    override fun init() {
        getIntentData()

        if (mode == Const.EXTRA_KEY_MEMO_ADD) {
            viewModel.getTimeTableData(lectureKey!!)
            btnAddLecture.visibility = View.GONE
            llMemoBtnContainer.visibility = View.VISIBLE
        } else {
            btnAddLecture.visibility = View.VISIBLE
            llMemoBtnContainer.visibility = View.GONE
            interactionData?.let{
                tvLectureTitle.text = it.lecture
                tvTime.text = "강의시간 : ${it.start_time} + ${it.end_time} | ${it.dayofweek}"
                tvCode.text = "교과목코드 : ${it.code}"
                tvProfessor.text = "담당교수 : ${it.professor}"
                tvLecureRoom.text = "강의실 : ${it.location}"
            }
        }

        btnAddLecture.setOnClickListener {
            viewModel.addLectureToTimeTable(interactionData!!)
        }

        btnAddMemo.setOnClickListener {
            if (timeTableInfo?.lectureCode.isNullOrEmpty()) {
                toastMsg("메모 등록을 위한 코드에 오류가 있습니다.")
                return@setOnClickListener
            }
            val intent = Intent(this@DetailActivity, MemoActivity::class.java)
            intent.putExtra(Const.EXTRA_KEY_LECTURE_CODE, timeTableInfo!!.lectureCode)
            intent.putExtra(Const.EXTRA_KEY_KEYUNIQUE_CODE, timeTableInfo!!.keyLecture)
            startActivity(intent)
        }

        btnRemoveLecture.setOnClickListener {
            if (timeTableInfo != null && lectureKey != null)
                viewModel.removeLectureToTimeTable(timeTableInfo!!, lectureKey!!)
        }

    }


    override fun initObserver() {
        viewModel.apply {
            isLoading.observe(this@DetailActivity, EventObserver {
                if (it) binding.progress.visibility =
                    View.VISIBLE else binding.progress.visibility =
                    View.GONE
            })

            detailItemLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        timeTableInfo = it.data
                        timeTableInfo!!.apply {
                            tvLectureTitle.text = lecture
                            tvTime.text = "강의시간 : ${start_time} - ${end_time} | ${getDayOfTheWeekReverseConvert(dayofweek)}"
                            tvCode.text = "교과목코드 : ${lectureCode}"
                            tvProfessor.text = "담당교수 : ${professor}"
                            tvLecureRoom.text = "강의실 : ${location}"
                        }
                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")

                    }
                }
            })

            myMemoTableLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {

                        containerMemo.removeAllViews()

                        if (it.data!!.isNotEmpty())
                            memoTitle.visibility = View.VISIBLE
                        for (memo in it.data!!) {

                            val memoView = MemoView(this@DetailActivity)

                            memoView.setDataListener(0, memo, object : MemoView.SetClickListener {
                                override fun setTrashClickListener(memo: MemoDBInfo) {
                                    removeMemoAPI(memo)
                                }

                                override fun setShowMemoClickListener(memo: MemoDBInfo) {
                                    val dlg = MemoShowDialog(this@DetailActivity)
                                    dlg.show(memo)
                                }
                            })
                            containerMemo.addView(memoView)

                        }
                        if (containerMemo.isEmpty())
                            memoTitle.visibility = View.GONE
                        else
                            memoTitle.visibility = View.VISIBLE

                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")
                        if (containerMemo.isEmpty())
                            memoTitle.visibility = View.GONE
                        else
                            memoTitle.visibility = View.VISIBLE
                    }
                }
            })


            itemLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        val intent = Intent(this@DetailActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        isNeedRefresh = true
                    }

                    Status.ERROR -> {
//                        toastMsg(it.message ?: "")
                        RequestErrorMsgHandler.RequestErrorMsg(it.message ?: "", Const.REQUEST_TYPE_TIMETABLE_ADD)

                    }
                }
            })

            removeLectureLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {

                    }

                    Status.ERROR -> {
                        RequestErrorMsgHandler.RequestErrorMsg(it.message ?: "", Const.REQUEST_TYPE_TIMETABLE_REMOVE)
                        if (it.message!!.contains("422")) {
                            //이미 삭제된 데이터인데 오류로 DB에 남아있는것이므로 LocalDB도 지워준다
                            if (timeTableInfo != null)
                                removeLectureToDBTimeTable(timeTableInfo!!)
                        }

                    }
                }
            })

            removeLectureDBLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        val intent = Intent(this@DetailActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        isNeedRefresh = true
                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")

                    }
                }
            })

            removeMemoLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {

                    }

                    Status.ERROR -> {
                        RequestErrorMsgHandler.RequestErrorMsg(it.message ?: "", Const.REQUEST_TYPE_MEMO_REMOVE)

                    }
                }
            })

            removeMemoDBLiveData.observe(this@DetailActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (mode == Const.EXTRA_KEY_MEMO_ADD) {
                            isNeedRefresh = true
                            getMemoTableData(lectureKey!!)
                        }

                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")

                    }
                }
            })

        }

    }
    private fun initData() {
        viewModel.apply {
            if (mode == Const.EXTRA_KEY_MEMO_ADD)
                getMemoTableData(lectureKey!!)
        }

    }


    override fun onDestroy() {
        if (isNeedRefresh)
            RxEvent.sendEvent(RXEVENT_REFRESH)
        super.onDestroy()

    }
}
