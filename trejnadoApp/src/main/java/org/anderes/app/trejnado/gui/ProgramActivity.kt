package org.anderes.app.trejnado.gui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
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
import org.anderes.app.trejnado.*
import org.anderes.app.trejnado.gui.dialog.DialogSessionExistsFragment
import java.util.*

class ProgramActivity : AppCompatActivity(),
        DialogSessionExistsFragment.DialogSessionExistListener {

    private lateinit var adapter: MachinelistAdapter
    private lateinit var programId: String
    private var existsPlaySession: TrainingSession? = null
    private lateinit var trainingProgram: TrainingProgram
    private val databaseRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)
        setSupportActionBar(program_toolbar)

        playSessionActionButton.setOnClickListener {
            if (existsPlaySession != null) {
                val dialog = DialogSessionExistsFragment()
                dialog.date = existsPlaySession?.getTrainingDateAsFormattedString() ?: "'no data'"
                dialog.show(supportFragmentManager, "DialogSessionExistListener")
            } else {
                processNewSession()
            }
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        programId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
        val machinelistRecyclerView = findViewById<View>(R.id.machine_list_id) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)

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
                    trainingProgram = snpashot.getValue(TrainingProgram::class.java)!!
                    program_toolbar.title = trainingProgram.name
                    existsPlaySession = existsPlaySession(trainingProgram)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    program_toolbar.title = "Error: " + databaseError.message
                    Log.w("GUI", databaseError.toException())

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

    override fun onSessionExistsYesClick(dialog: DialogFragment) {
        playSession(existsPlaySession!!)
    }

    override fun onSessionExistsNoClick(dialog: DialogFragment) {
        processNewSession()
    }

    private fun processNewSession() {
        val newSession = TrainingSession()
        newSession.trainingDate = Date().time
        val sortedList = trainingProgram.machines.sortedWith(compareBy { it.sequenceNo })
        for (m in sortedList.iterator()) {
            val unit = TrainingUnit()
            unit.machine = m
            newSession.addUnit(unit)
        }
        trainingProgram.addSession(newSession)
        databaseRef.child(Constants.TRAINING_PROGRAM_CHILD)
            .child(trainingProgram.key!!)
            .setValue(trainingProgram)

        playSession(newSession)
    }

    private fun playSession(session: TrainingSession) {
        val intent = Intent(this, PlaySessionActivity::class.java)
        intent.putExtra(Constants.PARAM_PROGRAM_ID, trainingProgram.key)
        intent.putExtra(Constants.PARAM_PROGRAM_SESSION_ID, session.id)
        intent.putExtra(Constants.PARAM_PROGRAM_SESSION_UNIT_NO, 0)
        startActivity(intent)
    }

    private fun existsPlaySession(trainingProgram: TrainingProgram): TrainingSession? {
        for (s in trainingProgram.sessions.iterator()) {
            if (!s.done) {
                return s
            }
        }
        return null
    }
}