package org.anderes.app.trejnado.gui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_program.*
import org.anderes.app.trejnado.Constants
import org.anderes.app.trejnado.R
import org.anderes.app.trejnado.TrainingMachine

class ProgramActivity : AppCompatActivity() {

    lateinit var adapter: MachinelistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)
        setSupportActionBar(program_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val programId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
        val machinelistRecyclerView = findViewById<View>(R.id.machine_list_id) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)

        val machinesRef = FirebaseDatabase.getInstance().reference
            .child(Constants.TRAINING_PROGRAM_CHILD)
            .child(programId)
            .child(Constants.TRAINING_PROGRAM_MACHINE_CHILD)

        val options = FirebaseRecyclerOptions.Builder<TrainingMachine>().setQuery(machinesRef, TrainingMachine::class.java).build()
        adapter = MachinelistAdapter(options)

        machinelistRecyclerView.layoutManager = mLinearLayoutManager
        machinelistRecyclerView.adapter = adapter
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
