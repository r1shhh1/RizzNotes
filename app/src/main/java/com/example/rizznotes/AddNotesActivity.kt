package com.example.rizznotes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rizznotes.databinding.ActivityAddNotesBinding
import com.example.rizznotes.databinding.ActivityMainBinding
import com.example.rizznotes.util.Note
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class AddNotesActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityAddNotesBinding

    private lateinit var note : Note
    private lateinit var oldNote : Note
    var isUpdate = false
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.editText.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdate = true

        }catch (e: java.lang.Exception){

            e.printStackTrace()

        }

        binding.imgCheck.setOnClickListener {

            val title = binding.editText.text.toString()
            val note_dec = binding.etNote.text.toString()

            if(title.isNotEmpty() || note_dec.isNotEmpty()){

                val formatter = SimpleDateFormat("EEE, d MMM HH: mm a")

                if(isUpdate){
                    note = Note(
                        oldNote.id,title,note_dec,formatter.format(Date())
                    )
                }else{

                    note = Note(
                        null,title,note_dec,formatter.format(Date())
                    )

                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }else{

                Toast.makeText(this@AddNotesActivity, "Please add some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.backImg.setOnClickListener {

            onBackPressed()
        }
    }
}