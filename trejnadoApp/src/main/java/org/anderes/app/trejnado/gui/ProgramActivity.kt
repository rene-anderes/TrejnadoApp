package org.anderes.app.trejnado.gui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import org.anderes.app.trejnado.TrainingProgram

class ProgramActivity : AppCompatActivity() {

    lateinit var adapter: MachinelistAdapter
    lateinit var programId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)
        setSupportActionBar(program_toolbar)

        playSessionActionButton.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, PlaySessionActivity::class.java)
            intent.putExtra(Constants.PARAM_PROGRAM_ID, programId)
            intent.putExtra(Constants.PARAM_PROGRAM_SESSION_ID, "uuid-434324-00002")
            intent.putExtra(Constants.PARAM_PROGRAM_SESSION_UNIT_NO, 0)
            context.startActivity(intent)
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        programId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
        val machinelistRecyclerView = findViewById<View>(R.id.machine_list_id) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)
        val databaseRef = FirebaseDatabase.getInstance().reference
        val machinesRef = databaseRef.child(Constants.TRAINING_PROGRAM_CHILD)
                                    .child(programId)
                                    .child(Constants.TRAINING_PROGRAM_MACHINE_CHILD)
                                    .orderByChild(Constants.TRAINING_PROGRAM_MACHINE_SEQ_CHILD)

        val options = FirebaseRecyclerOptions.Builder<TrainingMachine>().setQuery(machinesRef, TrainingMachine::class.java).build()
        adapter = MachinelistAdapter(options)

        machinelistRecyclerView.layoutManager = mLinearLayoutManager
        machinelistRecyclerView.adapter = adapter

        databaseRef
            .child(Constants.TRAINING_PROGRAM_CHILD)
            .child(programId).addListenerForSingleValueEvent( object: ValueEventListener {
                override fun onDataChange(snpashot: DataSnapshot) {
                    Log.d("GUI", "Key: " + snpashot.key)
                    val trainingProgram = snpashot.getValue(TrainingProgram::class.java)
                    program_toolbar.title = trainingProgram!!.name
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    program_toolbar.title = "Error: " + databaseError.message
                    Log.w("GUI", databaseError.toException());

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