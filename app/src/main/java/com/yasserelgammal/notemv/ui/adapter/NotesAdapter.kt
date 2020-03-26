package com.yasserelgammal.notemv.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yasserelgammal.notemv.R
import com.yasserelgammal.notemv.persistence.Note
import kotlinx.android.synthetic.main.note_item.view.*


class NoteAdapter(
    noteList: List<Note>,
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val notes = mutableListOf<Note>()

    init {
        notes.addAll(noteList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_item, parent, false)
        return ViewHolder(view, interaction)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = notes[position])
    }

    fun swap(notes: List<Note>) {
        val diffCallback = DiffCallback(this.notes, notes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.notes.clear()
        this.notes.addAll(notes)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Note) {
            itemView.txtTitle.text = item.title
            itemView.txtDescription.text = item.description
            itemView.noteColorLayout.setBackgroundColor(item.color)

            //Handle item click
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }

        }

    }

    fun add(item: Note, position: Int) {
        notes.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Note)
    }
}