package org.anderes.app.trejnado.gui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import org.anderes.app.trejnado.R

class DialogSessionPauseFragment : DialogFragment() {

    internal lateinit var mListener: DialogSessionPauseListener

    interface DialogSessionPauseListener {
        fun onSessionPauseYesClick(dialog: DialogFragment)
        fun onSessionPauseNoClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as DialogSessionPauseListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + " must implement DialogSessionPauseListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.text_dialog_session_pause)
                .setPositiveButton(R.string.text_yes) { dialog, id ->
                    mListener.onSessionPauseYesClick(this)
                }
                .setNegativeButton(R.string.text_no) { dialog, id ->
                    mListener.onSessionPauseNoClick(this)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}