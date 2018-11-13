package org.anderes.app.trejnado.gui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import org.anderes.app.trejnado.R

class DialogSessionEndFragment : DialogFragment() {

    internal lateinit var mListener: DialogSessionEndListener

    interface DialogSessionEndListener {
        fun onSessionEndYesClick(dialog: DialogFragment)
        fun onSessionEndNoClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as DialogSessionEndListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.text_dialog_session_end)
                .setPositiveButton(R.string.text_yes) { dialog, id ->
                    mListener.onSessionEndYesClick(this)
                }
                .setNegativeButton(R.string.text_no) { dialog, id ->
                    mListener.onSessionEndNoClick(this)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}