package com.exam.timetable.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.exam.timetable.R
import com.exam.timetable.model.data.MemoDBInfo
import kotlinx.android.synthetic.main.dialog_memoshow.*


class MemoShowDialog(context: Context) : Dialog(context) {

    fun show(memo: MemoDBInfo) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_memoshow)
        setCancelable(true)

        val params:WindowManager.LayoutParams = getWindow().attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        getWindow().attributes = params


        tvTitle.text = memo.title
        tvDesc.text = memo.description
        tvType.text = memo.type

        btnCancelDialog.setOnClickListener {
            dismiss()
        }

        show()
    }
}