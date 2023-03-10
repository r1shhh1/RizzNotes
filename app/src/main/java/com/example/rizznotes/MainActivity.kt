package com.example.rizznotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rizznotes.Adapter.NoteAdapter
import com.example.rizznotes.Database.NoteDatabase
import com.example.rizznotes.databinding.ActivityMainBinding
import com.example.rizznotes.util.Note
import com.example.rizznotes.util.NotesViewModel

class MainActivity : AppCompatActivity(), NoteAdapter.Notesclicklistener, PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NotesViewModel
    lateinit var adapter: NoteAdapter
    lateinit var selectedNote: Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ressult ->

        if(ressult.resultCode == Activity.RESULT_OK){

            val note = ressult.data?.getSerializableExtra("note") as Note
            if(note != null){

                viewModel.updateNote(note)
            }

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)

        viewModel.allnotes.observe(this) { list ->

            list?.let {

                adapter.updateList(list)

            }

        }

        database = NoteDatabase.getDatabase(this)


    }

    private fun initUI() {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(this,this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

            if(result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("note") as? Note
                if(note != null){

                    viewModel.insertNote(note)

                }

            }
        }

        binding.fbAddNote.setOnClickListener{

            val intent = Intent(this, AddNotesActivity::class.java)
            getContent.launch(intent)

        }


        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){

                    adapter.filterList(newText)

                }

                return true
            }

        })

    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this, AddNotesActivity::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onlongitemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {

        val popUp = PopupMenu(this,cardView)
        popUp.setOnMenuItemClickListener(this@MainActivity)
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.delete_note){

            viewModel.deleteNote(selectedNote)

            return true

        }

        return false

    }
}