package com.yasserelgammal.notemv.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.yasserelgammal.notemv.R
import com.yasserelgammal.notemv.persistence.Note
import com.yasserelgammal.notemv.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.color_selector.*
import kotlinx.android.synthetic.main.color_selector.view.*
import kotlinx.android.synthetic.main.color_selector.view.selectedColor
import kotlinx.android.synthetic.main.fragment_add.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    private var noteColor:Int?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()

        colorSelector.addListener {color->
            noteColor = color
        }
        btnAdd.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.container).popBackStack()
        }


    }

    override fun onDestroyView() {
        saveNoteToDatabaseFinalCheck()
//        (activity as MainActivity).showFloatingButton()
        super.onDestroyView()
    }

    private fun saveNoteToDatabaseFinalCheck() {
    if(validations()){
        saveNoteToDatabase()
        Toast.makeText(activity,"Note saved", Toast.LENGTH_SHORT).show()
    }else{
     Toast.makeText(activity,"Note discard", Toast.LENGTH_SHORT).show()
    }
    }

    private fun saveNoteToDatabase() {
    // getting data
    val noteTitle = addTitle.text.toString().trim()
    val noteDesc = addDescription.text.toString().trim()
    val note = Note(0,noteTitle,noteDesc,noteColor!!)

     if (addTitle.text.isNullOrEmpty()){
         note.title = "Empty Title"
         noteViewModel.insert(note)
     }else{
         noteViewModel.insert(note)
     }

    }

    private fun setUpViewModel() {
    noteViewModel = ViewModelProvider(this,viewModelProviderFactory).get(NoteViewModel::class.java)
    }

    private fun validations():Boolean{
        if (addTitle.text.isNullOrEmpty()
            && addDescription.text.isNullOrEmpty()){
            return false
        }else{
            return true
        }
    }

}
