package com.yasserelgammal.notemv.ui

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yasserelgammal.notemv.R
import com.yasserelgammal.notemv.persistence.Note
import com.yasserelgammal.notemv.ui.adapter.NoteAdapter
import com.yasserelgammal.notemv.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : DaggerFragment(), NoteAdapter.Interaction {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    lateinit var noteAdapter : NoteAdapter

    lateinit var allNotes : List<Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        allNotes = mutableListOf()
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        initRecyclerView()
        observerLiveData()

        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddFragment()
            Navigation.findNavController((it)).navigate(action)
        }

    }

    private fun setUpViewModel() {
    noteViewModel = ViewModelProvider(this,viewModelProviderFactory).get(NoteViewModel::class.java)
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter(allNotes,this@ListFragment)
    recyclerView.apply {
        layoutManager = LinearLayoutManager(this@ListFragment.context)
        adapter= noteAdapter
        val swipe = ItemTouchHelper(iniSwipeItems())
        swipe.attachToRecyclerView(recyclerView)
    }
    }

    private fun observerLiveData() {
    noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
        allNotes = it
        noteAdapter.swap(it)
    })
    }

    override fun onItemSelected(position: Int, item: Note) {
    val navDirection = ListFragmentDirections.actionListFragmentToEditFragment(item)
//        Log.d("data",item.title)
    findNavController().navigate(navDirection)
    }

    private fun iniSwipeItems():ItemTouchHelper.SimpleCallback{
        return object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = allNotes[position]
                when(direction){
                    ItemTouchHelper.RIGHT -> {
                        noteAdapter.remove(position)
                        var snack = Snackbar.make(listRoot,note.title + " Deleted", Snackbar.LENGTH_LONG)
                        snack.setAction("Undo") {
                            noteAdapter.add(note,position)
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
                        noteAdapter.remove(position)
                        var snack = Snackbar.make(listRoot,note.title + " Archived", Snackbar.LENGTH_LONG)
                        snack.setAction("Undo") {
                            noteAdapter.add(note,position)
                        }
                        snack.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    noteViewModel.archiveNote(1,note.id)
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
                    .addSwipeLeftActionIcon(R.drawable.ic_archive_white_24dp)
//                    .addSwipeRightLabel(getString(R.string.action_delete))

//                    )
//                    .addActionIcon(R.drawable.my_icon)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

}
