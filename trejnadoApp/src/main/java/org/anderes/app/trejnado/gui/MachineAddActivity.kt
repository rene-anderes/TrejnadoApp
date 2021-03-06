package org.anderes.app.trejnado.gui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_machine_add.*
import org.anderes.app.trejnado.Constants
import org.anderes.app.trejnado.R

class MachineAddActivity() : AppCompatActivity(), MachineEditFragment.OnFragmentInteractionListener {

    private lateinit var trainingProgramId: String

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machine_add)
        setSupportActionBar(machine_add_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        trainingProgramId = intent.getStringExtra(Constants.PARAM_PROGRAM_ID)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_machine_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                val upIntent: Intent? = NavUtils.getParentActivityIntent(this)
                when (upIntent) {
                    null -> throw IllegalStateException("No Parent Activity Intent")
                    else -> {
                        upIntent.putExtra(Constants.PARAM_PROGRAM_ID, trainingProgramId)
                        // This activity is part of this app's task, so simply
                        // navigate up to the logical parent activity.
                        NavUtils.navigateUpTo(this, upIntent)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
