package com.yasserelgammal.notemv.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.yasserelgammal.notemv.R
import com.yasserelgammal.notemv.persistence.Note
import com.yasserelgammal.notemv.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    private var noteColor:Int?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareForNoteEdit()
        setUpViewModel()

        btnEdit.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.container)
                .popBackStack()
            saveNoteToDatabase()
        }

        editColorSelector.addListener {color->
            noteColor = color
        }

    }

    private fun prepareForNoteEdit() {
    arguments?.let {
        val safeargs = EditFragmentArgs.fromBundle(it)
        val note = safeargs.note
        noteColor = note?.color
        editTitle.setText(note?.title)
        editDescription.setText(note?.description)
        editColorSelector.selectedColorValue = note!!.color
    }
    }

    private fun setUpViewModel() {
        noteViewModel = ViewModelProvider(this,viewModelProviderFactory).get(NoteViewModel::class.java)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        saveNoteFinalCheck()
    }

    private fun saveNoteFinalCheck() {
        if(validations()){
            saveNoteToDatabase()
            Toast.makeText(activity,"Note saved", Toast.LENGTH_SHORT).show()
        }else{
            //delete
            //get note ID
            val noteId = EditFragmentArgs.fromBundle(arguments!!).note?.id
            noteViewModel.deleteById(noteId!!)
            Toast.makeText(activity,"Note deleted", Toast.LENGTH_SHORT).show()
        }    }

    private fun saveNoteToDatabase() {
        //get note ID
        val noteId = EditFragmentArgs.fromBundle(arguments!!).note?.id
        val noteTitle = editTitle.text.toString().trim()
        val noteDesc = editDescription.text.toString().trim()
        val note = Note(noteId!!,noteTitle,noteDesc, noteColor!!)
        if (editTitle.text.isNullOrEmpty()){
            note.title = "Empty Title"
            noteViewModel.update(note)
        }else{
            noteViewModel.update(note)
        }

    }
    private fun validations():Boolean{
        if (editTitle.text.isNullOrEmpty()
            && editDescription.text.isNullOrEmpty()){
            return false
        }else{
            return true
        }
    }
}
