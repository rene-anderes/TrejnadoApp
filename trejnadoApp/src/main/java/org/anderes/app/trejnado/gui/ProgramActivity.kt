package org.anderes.app.trejnado.gui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.activity_programlist.*
import org.anderes.app.trejnado.R

class ProgramActivity : AppCompatActivity() {

    companion object {
        const val PARAM_PROGRAM_ID = "PROGRAM_ID"
        const val PARAM_USER_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)
        setSupportActionBar(toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val programId = intent.getStringExtra(PARAM_PROGRAM_ID)
        val programmRecyclerView = findViewById<View>(R.id.machine_list_id) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)
    }
}
