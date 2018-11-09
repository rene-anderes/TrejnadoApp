package org.anderes.app.trejnado.gui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_play_session.*
import kotlinx.android.synthetic.main.content_play_session.*
import org.anderes.app.trejnado.Constants
import org.anderes.app.trejnado.R
import org.anderes.app.trejnado.TrainingProgram


class PlaySessionActivity : AppCompatActivity() {

    var running: Boolean = false
    var startTime: Long = 0
    lateinit var trainingProgram: TrainingProgram
    var sessionId: String = ""
    var programUnitNo: Int = 0
    val databaseRef = FirebaseDatabase.getInstance().reference
    lateinit var time: TextView
    lateinit var wight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_session)
        setSupportActionBar(play_session_toolbar)
        time = findViewById(R.id.play_session_edit_time)
        wight = findViewById(R.id.play_session_edit_wight)

        play_session_next.setOnClickListener { view ->
            navigateToUnit(programUnitNo + 1, view)
        }

        play_session_prev.setOnClickListener { view ->
            navigateToUnit(programUnitNo - 1, view)
        }

        val programId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
        sessionId = intent.getStringExtra(Constants.PARAM_PROGRAM_SESSION_ID)
        programUnitNo = intent.getIntExtra(Constants.PARAM_PROGRAM_SESSION_UNIT_NO, 0)

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
                    val session = trainingProgram.findSessionById(sessionId)!!
                    settinglistView.adapter = SettinglistAdapter(session.units[programUnitNo].machine!!.settings)
                    play_session_machine_id.text =session.units[programUnitNo].machine!!.name
                    play_session_next.isEnabled = programUnitNo < trainingProgram.machines.size
                    if (programUnitNo > 0) {
                        play_session_prev.isEnabled = true
                    } else {
                        play_session_prev.isEnabled = false
                    }
                    time.text = session.units[programUnitNo].duration.toString()
                    wight.text = session.units[programUnitNo].weight.toString()
                }

            })

        val stopwatchButton: ImageButton = findViewById(R.id.play_session_stopwatch_button)
        stopwatchButton.setOnClickListener {
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
        val textView = findViewById<TextView>(R.id.play_session_edit_time)
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val millisecondTime = SystemClock.uptimeMillis() - startTime
                var seconds = millisecondTime / 1000
                seconds %= 60
                textView.text = String.format("%d", seconds)
                if (running) {
                    handler.postDelayed(this, 100)
                } else {
                    handler.removeCallbacks(this)
                }

            }
        })

    }

    private fun navigateToUnit(targeUnitNo : Int, view: View) {
        val context = view.context
        val intent = Intent(context, PlaySessionActivity::class.java)
        intent.putExtra(Constants.PARAM_PROGRAM_ID, trainingProgram.key)
        intent.putExtra(Constants.PARAM_PROGRAM_SESSION_ID, sessionId)
        intent.putExtra(Constants.PARAM_PROGRAM_SESSION_UNIT_NO, targeUnitNo)

        //if (time.isDirty || wight.isDirty) {
        val session = trainingProgram.findSessionById(sessionId)!!
        session.units[programUnitNo].duration = Integer.parseInt(time.text.toString())
        session.units[programUnitNo].weight = Integer.parseInt(wight.text.toString())
        databaseRef.child(Constants.TRAINING_PROGRAM_CHILD)
            .child(trainingProgram.key!!)
            .setValue(trainingProgram)
        //}
        context.startActivity(intent)
    }
}
