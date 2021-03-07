package com.exam.timetable.adapter



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exam.timetable.R
import com.exam.timetable.databinding.ListItemBinding
import com.exam.timetable.model.data.LectureData
import com.exam.timetable.model.data.MemoDBInfo

import java.util.ArrayList


class LectureAdapter (private val itemListClick: (LectureData) -> Unit)
    : RecyclerView.Adapter<LectureAdapter.ItemViewHolder>() {

    private var recyclerItemList: ArrayList<LectureData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {

        var binding:ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item,
            parent,
            false
        )

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {
        recyclerItemList[position].let {
            holderItem.bind(it)
        }
    }


    override fun getItemCount(): Int {
        return recyclerItemList.size
    }

    fun initItem(list: ArrayList<LectureData>) {
        recyclerItemList = list
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LectureData) {
            binding.item = item
            binding.root.setOnClickListener {
                itemListClick(item)
            }
            binding.tvTime.text = "${item.start_time} - ${item.end_time} | ${item.dayofweek}"
            binding.executePendingBindings()
        }
    }
}
