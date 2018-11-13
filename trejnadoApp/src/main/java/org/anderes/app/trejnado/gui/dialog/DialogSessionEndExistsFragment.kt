package org.anderes.app.trejnado.gui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import org.anderes.app.trejnado.R

class DialogSessionEndExistsFragment : DialogFragment() {

    internal lateinit var mListener: DialogSessionEndExistListener
    var date: String = ""

    interface DialogSessionEndExistListener {
        fun onSessionEndExistsYesClick(dialog: DialogFragment)
        fun onSessionEndExistsNoClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as DialogSessionEndExistListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + " must implement DialogSessionExistListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.text_dialog_session_end_exists)
                .setPositiveButton(R.string.text_yes) { dialog, id ->
                    mListener.onSessionEndExistsYesClick(this)
                }
                .setNegativeButton(R.string.text_no) { dialog, id ->
                    mListener.onSessionEndExistsNoClick(this)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}