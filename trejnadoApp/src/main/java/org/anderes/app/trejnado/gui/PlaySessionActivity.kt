package org.anderes.app.trejnado.gui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.DialogFragment
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
import org.anderes.app.trejnado.TrainingSession
import org.anderes.app.trejnado.gui.dialog.DialogSessionEndFragment
import org.anderes.app.trejnado.gui.dialog.DialogSessionPauseFragment


class PlaySessionActivity : AppCompatActivity(),
            DialogSessionEndFragment.DialogSessionEndListener,
            DialogSessionPauseFragment.DialogSessionPauseListener {

    private var running: Boolean = false
    private var startTime: Long = 0
    private lateinit var trainingProgram: TrainingProgram
    private var sessionId: String = ""
    private var programUnitNo: Int = 0
    private val databaseRef = FirebaseDatabase.getInstance().reference
    lateinit var time: TextView
    lateinit var wight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_session)
        setSupportActionBar(play_session_toolbar)
        time = findViewById(R.id.play_session_edit_time)
        wight = findViewById(R.id.play_session_edit_wight)
        val lastTime = findViewById<TextView>(R.id.play_session_lasttime)
        val lastWight = findViewById<TextView>(R.id.play_session_lastwight)

        play_session_next.setOnClickListener { view ->
            navigateToUnit(programUnitNo + 1, view)
        }

        play_session_prev.setOnClickListener { view ->
            navigateToUnit(programUnitNo - 1, view)
        }

        play_sesseion_pause.setOnClickListener {
            val dialog = DialogSessionPauseFragment()
            dialog.show(supportFragmentManager, "DialogSessionPauseFragment")
        }

        play_session_end.setOnClickListener {
            val dialog = DialogSessionEndFragment()
            dialog.show(supportFragmentManager, "DialogSessionEndFragment")
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
                    settinglistView.adapter = SettinglistAdapter(session.units[programUnitNo].machine.settings)
                    play_session_machine_id.text =session.units[programUnitNo].machine.name
                    val playNext = findViewById<ImageButton>(R.id.play_session_next)
                    if (isLastUnit()) {
                        playNext.visibility = View.INVISIBLE
                    } else {
                        playNext.visibility = View.VISIBLE
                    }
                    play_session_next.isEnabled = !isLastUnit()
                    val playPrev = findViewById<ImageButton>(R.id.play_session_prev)
                    if (isFirstUnit()) {
                        playPrev.visibility = View.INVISIBLE
                    } else {
                        playPrev.visibility = View.VISIBLE
                    }
                    lastTime.text = findLastTimeByMachineName(session.units[programUnitNo].machine.name!!)
                    lastWight.text = findLastWightByMachineName(session.units[programUnitNo].machine.name!!)
                    time.text = session.units[programUnitNo].duration.toString()
                    if (session.units[programUnitNo].weight == Constants.NO_DATA && lastWight.text != Constants.NO_DATA) {
                        wight.text = lastWight.text
                    } else {
                        wight.text = session.units[programUnitNo].weight
                    }

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

    private fun findLastWightByMachineName(name: String): String {
        for (s in trainingProgram.sessions.sortedWith(compareByDescending { it.trainingDate })) {
            if (s.id == sessionId) {
                continue
            }
            val find = s.units.firstOrNull { u -> u.machine.name.equals(name) }
            if (find != null) {
                return find.weight.toString()
            }
        }
        return Constants.NO_DATA
    }

    private fun findLastTimeByMachineName(name: String): String {
        val sortedList = trainingProgram.sessions.sortedWith(compareByDescending { it.trainingDate })
        for (s in sortedList.iterator()) {
            if (s.id == sessionId) {
                continue
            }
            val find = s.units.firstOrNull { u -> u.machine.name.equals(name) }
            if (find != null) {
                return find.duration.toString()
            }
        }
        return Constants.NO_DATA
    }


    private fun isLastUnit(): Boolean {
        return programUnitNo >= trainingProgram.machines.size - 1
    }

    private fun isFirstUnit(): Boolean {
        return programUnitNo <= 0
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
        saveTrainingSession()
        context.startActivity(intent)
    }

    private fun saveTrainingSession() {
        val session = trainingProgram.findSessionById(sessionId)!!
        session.units[programUnitNo].duration = time.text.toString()
        session.units[programUnitNo].weight = wight.text.toString()
        databaseRef.child(Constants.TRAINING_PROGRAM_CHILD)
            .child(trainingProgram.key!!)
            .setValue(trainingProgram)
    }

    override fun onSessionEndYesClick(dialog: DialogFragment) {
        val session: TrainingSession? = trainingProgram.findSessionById(sessionId)
        session?.done = true
        saveTrainingSession()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSessionEndNoClick(dialog: DialogFragment) {
        val session: TrainingSession? = trainingProgram.findSessionById(sessionId)
        session?.done = false
    }

    override fun onSessionPauseYesClick(dialog: DialogFragment) {
        val session: TrainingSession? = trainingProgram.findSessionById(sessionId)
        session?.done = false
        saveTrainingSession()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSessionPauseNoClick(dialog: DialogFragment) {
        val session: TrainingSession? = trainingProgram.findSessionById(sessionId)
        session?.done = false
    }
}
