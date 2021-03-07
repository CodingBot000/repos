package com.exam.timetable.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton

import com.exam.timetable.R
import com.exam.timetable.common.BaseActivity

import com.exam.timetable.databinding.ActivityMemoBinding
import com.exam.timetable.livedata.EventObserver
import com.exam.timetable.model.data.InteractionData
import com.exam.timetable.model.data.MemoData
import com.exam.timetable.utils.*
import com.exam.timetable.utils.Const.Companion.RXEVENT_REFRESH

import com.exam.timetable.viewmodel.MemoViewModel
import kotlinx.android.synthetic.main.activity_memo.*
import org.koin.android.viewmodel.ext.android.viewModel


class MemoActivity : BaseActivity<ActivityMemoBinding, MemoViewModel>()
{

    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.activity_memo

    override val viewModel : MemoViewModel by viewModel()

    var isNeedRefresh = false
    var code = ""
    var keyLecture = ""
    var typeStr = ""
    var memo:MemoData? = null
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
        code = intent.getStringExtra(Const.EXTRA_KEY_LECTURE_CODE)
        keyLecture = intent.getStringExtra(Const.EXTRA_KEY_KEYUNIQUE_CODE)
    }

    override fun init() {
        getIntentData()

        btnAddMemo.setOnClickListener {
            if (edtTitle.text.isEmpty() || edtDesc.text.isEmpty()) {
                toastMsg("모든 정보를 채워주세요")
                return@setOnClickListener
            }
             memo = MemoData(Const.USER_KEY, code, typeStr, edtTitle.text.toString(),edtDesc.text.toString(), getTodayWithDash(),keyLecture)
            viewModel.addMemo(memo!!)
        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->

            typeStr = radioGroup.findViewById<RadioButton>(i).text.toString()
        }
        typeStr = radioBtn1.text.toString()
    }


    override fun initObserver() {
        viewModel.apply {
            isLoading.observe(this@MemoActivity, EventObserver {
                if (it) binding.progress.visibility =
                    View.VISIBLE else binding.progress.visibility =
                    View.GONE
            })

            itemLiveData.observe(this@MemoActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                    }

                    Status.ERROR -> {
                        RequestErrorMsgHandler.RequestErrorMsg(it.message ?: "", Const.REQUEST_TYPE_MEMO_ADD)
                    }
                }
            })


            itemMemoLiveData.observe(this@MemoActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        toastMsg(it.message ?: "")
                        val intent = Intent(this@MemoActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                        isNeedRefresh = true
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

        }

    }


    override fun onDestroy() {
        if (isNeedRefresh)
            RxEvent.sendEvent(RXEVENT_REFRESH)
        super.onDestroy()


    }
}
