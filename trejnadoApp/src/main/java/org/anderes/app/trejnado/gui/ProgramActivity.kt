package org.anderes.app.trejnado.gui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

        playSessionActionButton.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, PlaySessionActivity::class.java)
            context.startActivity(intent)
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val programId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
        val machinelistRecyclerView = findViewById<View>(R.id.machine_list_id) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)
        val databseRef = FirebaseDatabase.getInstance().reference
        val machinesRef = databseRef.child(Constants.TRAINING_PROGRAM_CHILD)
                                    .child(programId)
                                    .child(Constants.TRAINING_PROGRAM_MACHINE_CHILD)
                                    .orderByChild(Constants.TRAINING_PROGRAM_MACHINE_SEQ_CHILD)

        val options = FirebaseRecyclerOptions.Builder<TrainingMachine>().setQuery(machinesRef, TrainingMachine::class.java).build()
        adapter = MachinelistAdapter(options)

        machinelistRecyclerView.layoutManager = mLinearLayoutManager
        machinelistRecyclerView.adapter = adapter

        databseRef
            .child(Constants.TRAINING_PROGRAM_CHILD)
            .child(programId).child(Constants.TRAINING_PROGRAM_NAME_CHILD).addListenerForSingleValueEvent( object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    program_toolbar.title = "Error"
                }

                override fun onDataChange(snpashot: DataSnapshot) {
                    program_toolbar.title = snpashot.value as String
                }

            })
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