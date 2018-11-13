package org.anderes.app.trejnado.gui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import org.anderes.app.trejnado.R

class DialogSessionExistsFragment : DialogFragment() {

    private lateinit var mListener: DialogSessionExistListener
    var date: String = ""

    interface DialogSessionExistListener {
        fun onSessionExistsYesClick(dialog: DialogFragment)
        fun onSessionExistsNoClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as DialogSessionExistListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + " must implement DialogSessionExistListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.text_dialog_session_exists, date))
                .setPositiveButton(R.string.text_yes) { dialog, id ->
                    mListener.onSessionExistsYesClick(this)
                }
                .setNegativeButton(R.string.text_no) { dialog, id ->
                    mListener.onSessionExistsNoClick(this)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}