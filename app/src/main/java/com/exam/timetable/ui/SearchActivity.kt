package com.exam.timetable.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.exam.timetable.R
import com.exam.timetable.adapter.LectureAdapter

import com.exam.timetable.common.BaseActivity
import com.exam.timetable.databinding.ActivitySearchBinding
import com.exam.timetable.livedata.EventObserver
import com.exam.timetable.model.data.InteractionData
import com.exam.timetable.model.data.LecureDataParent
import com.exam.timetable.utils.*
import com.exam.timetable.utils.extention.hideKeyboard
import com.exam.timetable.viewmodel.SearchViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel


class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>()
{
    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.activity_search

    override val viewModel : SearchViewModel by viewModel()

    private val adapter: LectureAdapter by lazy {
        LectureAdapter(itemListClick = { item ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(
                Const.EXTRA_KEY_INTERACTION,
                InteractionData(item.code, item.lecture, item.professor, item.location, item.start_time,
                    item.end_time, item.dayofweek)
            )
            intent.putExtra(Const.EXTRA_KEY_SERVICE, Const.EXTRA_KEY_LECTURE_ADD)
            startActivity(intent)
        })
    }
    private lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initScrollListener()
        initEditTextEvent()
    }

    override fun init() {
        layoutManager =
            LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        adapter.initItem(LectureDataAll)
//        viewModel.getAllLectures()

    }

    override fun initObserver() {
        viewModel.apply {
            isLoading.observe(this@SearchActivity, EventObserver {
                if (it) binding.progress.visibility = View.VISIBLE else binding.progress.visibility =
                    View.GONE
            })

            itemLiveData.observe(this@SearchActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.data?.let { data -> initList(data) }
                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")
                    }
                }
            })

            itemCodeSearchLiveData.observe(this@SearchActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data -> initList(data) }
                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")
                    }
                }
            })


            itemNameSearchLiveData.observe(this@SearchActivity, EventObserver {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data -> initList(data) }
                    }

                    Status.ERROR -> {
                        toastMsg(it.message ?: "")
                    }
                }
            })



        }
    }

    private fun initList(data : LecureDataParent) {
        adapter.initItem(data.lectureDataArr)

    }

    private fun initScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                edtSearch.hideKeyboard()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun initEditTextEvent() {
        btnSearch.setOnClickListener {
            val key = edtSearch.text.toString()
            if (key.isEmpty()) {
//                viewModel.getAllLectures()
                adapter.initItem(LectureDataAll)
            }
            else if (isCode(key))
                viewModel.getLecturesToCode(key)
            else
                viewModel.getLecturesToName(key)
        }


        edtSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val key = edtSearch.text.toString()
                    if (key.isEmpty())
                        viewModel.getAllLectures()
                    else if (isCode(key))
                        viewModel.getLecturesToCode(key)
                    else
                        viewModel.getLecturesToName(key)

                    return true
                }
                return false
            }
        })


        viewModel.getSearchKeyChangeObserver()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isEmpty())
                    viewModel.getAllLectures()
            }

        edtSearch.requestFocus()
    }

    private fun isCode(searchKey:String) : Boolean {
        return searchKey.contains("PG")
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
