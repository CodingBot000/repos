package com.exam.timetable.ui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import com.exam.timetable.R
import com.exam.timetable.model.data.MemoDBInfo
import kotlinx.android.synthetic.main.view_memo.view.*

open class MemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)
    : LinearLayout(context, attrs, defStyleAttr){

    interface SetClickListener {
        fun setTrashClickListener(memo: MemoDBInfo)
        fun setShowMemoClickListener(memo: MemoDBInfo)
    }

    init{
        inflate(context, R.layout.view_memo, this)
    }

    fun setDataListener(mode: Int, memo: MemoDBInfo, mSetClickListener: SetClickListener?){
        tvMemoTitle.text = memo.title
        if (mode == 0) {
            ivTrash.setOnClickListener {
                mSetClickListener?.setTrashClickListener(memo)
            }
            iv.setOnClickListener {
                mSetClickListener?.setShowMemoClickListener(memo)
            }
        } else {
            tvMemoTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 5f);
            tvMemoTitle.setPadding(0, 0, 0, 0)
            ivTrash.visibility = View.GONE
            val layoutParams = LayoutParams(20, 20)
            iv.setPadding(0, 0, 0, 0)
            iv.layoutParams = layoutParams
        }
    }

}
