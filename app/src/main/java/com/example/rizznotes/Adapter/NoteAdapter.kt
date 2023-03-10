package com.example.rizznotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.rizznotes.MainActivity
import com.example.rizznotes.R
import com.example.rizznotes.util.Note
import kotlin.random.Random

class NoteAdapter(private val context: Context, val listener: Notesclicklistener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val notes_layout = itemView.findViewById<CardView>(R.id.cardView)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val note  = itemView.findViewById<TextView>(R.id.tvNotes)
        val date = itemView.findViewById<TextView>(R.id.tvDate)

    }

    interface Notesclicklistener{

        fun onItemClicked(note: Note)

        fun onlongitemClicked(note:Note, cardView: CardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(),null))

        holder.notes_layout.setOnClickListener{

            listener.onItemClicked(NotesList[holder.adapterPosition])

        }

        holder.notes_layout.setOnLongClickListener{

            listener.onlongitemClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true

        }

    }

    fun updateList(newList: List<Note>){

        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()

    }

    fun filterList(search: String){

        NotesList.clear()

        for(item in fullList){

            if(item.title?.lowercase()?.contains(search.lowercase())== true  ||
                    item.note?.lowercase()?.contains(search.lowercase())== true){

                NotesList.add(item)

            }
        }

        notifyDataSetChanged()

    }


    fun randomColor(): Int{

        val list = ArrayList<Int>()
        list.add(R.color.pastel_orange)
        list.add(R.color.pasatel_green)
        list.add(R.color.pastel_red)
        list.add(R.color.pastel_yellow)
        list.add(R.color.pastel_purple)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]

    }

}