package com.yasserelgammal.notemv.ui

import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.yasserelgammal.notemv.R
import com.yasserelgammal.notemv.persistence.Note
import com.yasserelgammal.notemv.ui.adapter.ArchiveAdapter
import com.yasserelgammal.notemv.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_archive.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ArchiveFragment : DaggerFragment() , ArchiveAdapter.Interaction{

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    lateinit var archiveAdapter : ArchiveAdapter

    lateinit var allArchiveNotes : List<Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allArchiveNotes = mutableListOf()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        initRecyclerView()
        observerLiveData()
    }
    private fun setUpViewModel() {
        noteViewModel = ViewModelProvider(this,viewModelProviderFactory).get(NoteViewModel::class.java)
    }
    private fun initRecyclerView() {
        archiveAdapter = ArchiveAdapter(allArchiveNotes,this@ArchiveFragment)
        ArchiveRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ArchiveFragment.context)
            adapter= archiveAdapter
            val swipe = ItemTouchHelper(iniSwipeItems())
            swipe.attachToRecyclerView(ArchiveRecyclerView)
        }
    }
    private fun observerLiveData() {
        noteViewModel.getAllArchivesNotes().observe(viewLifecycleOwner, Observer {
            allArchiveNotes = it
            archiveAdapter.swap(it)
        })
    }

    override fun onItemSelected(position: Int, item: Note) {
        val navDirection = ArchiveFragmentDirections.actionArchiveFragmentToEditFragment(item)
        findNavController().navigate(navDirection)
    }

    private fun iniSwipeItems():ItemTouchHelper.SimpleCallback{
        return object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = allArchiveNotes[position]
                when(direction){
                    ItemTouchHelper.RIGHT -> {
                        archiveAdapter.remove(position)
                        var snack = Snackbar.make(archiveList,note.title + " Deleted", Snackbar.LENGTH_LONG)
                        snack.setAction("Undo") {
                            archiveAdapter.add(note,position)
                        }
                        snack.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    noteViewModel.delete(note)
                                }
                            }
                        })
                        snack.show()
                    }
                    ItemTouchHelper.LEFT->{
                        archiveAdapter.remove(position)
                        var snack = Snackbar.make(archiveList,note.title + " Un Archived", Snackbar.LENGTH_LONG)
                        snack.setAction("Undo") {
                            archiveAdapter.add(note,position)
                        }
                        snack.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    noteViewModel.archiveNote(0,note.id)
                                }
                            }
                        })
                        snack.show()
                    }
                }

            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
                    .addSwipeRightActionIcon(R.drawable.ic_delete_white_24dp)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
                    .addSwipeLeftActionIcon(R.drawable.ic_unarchive_black_24dp)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

}
