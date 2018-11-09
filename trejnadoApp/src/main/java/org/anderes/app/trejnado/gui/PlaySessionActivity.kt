package org.anderes.app.trejnado.gui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton

import kotlinx.android.synthetic.main.activity_play_session.*
import org.anderes.app.trejnado.R
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.content_play_session.*
import org.anderes.app.trejnado.Constants
import org.anderes.app.trejnado.TrainingProgram


class PlaySessionActivity : AppCompatActivity() {

    var running: Boolean = false
    var startTime: Long = 0
    lateinit var trainingProgram: TrainingProgram
    var programUnitNo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_session)
        setSupportActionBar(play_session_toolbar)

        fab.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, PlaySessionActivity::class.java)
            intent.putExtra(Constants.PARAM_PROGRAM_ID, trainingProgram.key)
            intent.putExtra(Constants.PARAM_PROGRAM_UNIT_NO, programUnitNo + 1)
            context.startActivity(intent)
        }

        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val programId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
        programUnitNo = intent.getIntExtra(Constants.PARAM_PROGRAM_UNIT_NO, 0)
        val databaseRef = FirebaseDatabase.getInstance().reference
        databaseRef
            .child(Constants.TRAINING_PROGRAM_CHILD)
            .child(programId).addListenerForSingleValueEvent( object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    play_session_toolbar.title = "Error"
                }

                override fun onDataChange(snpashot: DataSnapshot) {
                    trainingProgram = snpashot.getValue(TrainingProgram::class.java)!!
                    play_session_toolbar.title = trainingProgram.name
                    val settinglistView: RecyclerView = findViewById(R.id.machine_settings_view_id)
                    settinglistView.adapter = SettinglistAdapter(trainingProgram.machines.get(programUnitNo).settings)
                    play_session_machine_id.text = trainingProgram.machines.get(programUnitNo).name
                    fab.isEnabled = programUnitNo >= trainingProgram.machines.size - 1
                }

            })

        val stopwatchButton: ImageButton = findViewById(R.id.play_session_stopwatch_button)
        stopwatchButton.setOnClickListener { view ->

            if (!running) {
                startTime = SystemClock.uptimeMillis()
                runTimer()
                stopwatchButton.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                stopwatchButton.setImageResource(android.R.drawable.ic_media_play)
            }
            running = !running
        }
    }

    private fun runTimer() {
        val textView = findViewById(R.id.play_session_edit_time) as TextView
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val millisecondTime = SystemClock.uptimeMillis() - startTime;
                var seconds = millisecondTime / 1000
                seconds = seconds % 60;
                textView.text = String.format("%d", seconds)
                if (running) {
                    handler.postDelayed(this, 100)
                } else {
                    handler.removeCallbacks(this)
                }

            }
        })

    }

}
