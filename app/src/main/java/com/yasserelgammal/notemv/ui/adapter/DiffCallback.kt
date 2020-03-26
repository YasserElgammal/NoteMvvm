package com.yasserelgammal.notemv.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yasserelgammal.notemv.persistence.Note

class DiffCallback(
    private val oldList: List<Note>,
    private val newList: List<Note>
) : DiffUtil.Callback() {

    // Method #1
    override fun getOldListSize() = oldList.size

    // Method #2
    override fun getNewListSize() = newList.size

    // Method #3
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    // Method #4
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].color == newList[newItemPosition].color
                && oldList[oldItemPosition].archive == newList[newItemPosition].archive
    }
}