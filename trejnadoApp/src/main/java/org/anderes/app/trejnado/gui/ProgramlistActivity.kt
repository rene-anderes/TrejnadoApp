package org.anderes.app.trejnado.gui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_programlist.*
import org.anderes.app.trejnado.Constants
import org.anderes.app.trejnado.R
import org.anderes.app.trejnado.TrainingProgram

class ProgramlistActivity : AppCompatActivity() {

    lateinit var adapter: ProgramlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programlist)
        setSupportActionBar(programmlist_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val programmRecyclerView = findViewById<View>(R.id.program_list_id) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)

        val mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        val trainingProgramRef = mFirebaseDatabaseReference.child(Constants.TRAINING_PROGRAM_CHILD)
        val options = FirebaseRecyclerOptions.Builder<TrainingProgram>().setQuery(trainingProgramRef, TrainingProgram::class.java).build()
        adapter = ProgramlistAdapter(options)


        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val trainingProgram = adapter.itemCount
                val lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition()
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 || positionStart >= trainingProgram - 1 && lastVisiblePosition == positionStart - 1) {
                    programmRecyclerView.scrollToPosition(positionStart)
                }
            }
        })

        programmRecyclerView.layoutManager = mLinearLayoutManager
        programmRecyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }
}
