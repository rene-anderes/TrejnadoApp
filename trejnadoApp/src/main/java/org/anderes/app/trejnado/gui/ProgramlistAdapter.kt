package org.anderes.app.trejnado.gui

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import org.anderes.app.trejnado.Constants
import org.anderes.app.trejnado.R
import org.anderes.app.trejnado.TrainingProgram

class ProgramlistAdapter(options: FirebaseRecyclerOptions<TrainingProgram>) :
    FirebaseRecyclerAdapter<TrainingProgram, ProgramlistAdapter.TrainingProgramViewHolder>(options) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrainingProgramViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.program_list_item, viewGroup, false)

        return ProgramlistAdapter.TrainingProgramViewHolder(itemView)

    }

    override fun onBindViewHolder(viewHolder: TrainingProgramViewHolder, position: Int, model: TrainingProgram) {
        viewHolder.programName.text = model.name
        viewHolder.programName.visibility = TextView.VISIBLE
        viewHolder.createDate.text = model.getCreateDateAsFormattedString()
        viewHolder.createDate.visibility = TextView.VISIBLE
        viewHolder.itemView.tag = model.key
        viewHolder.itemView.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, ProgramActivity::class.java)
            intent.putExtra(Constants.PARAM_PROGRAM_ID, view.tag as String)
            context.startActivity(intent)
        }
    }


    class TrainingProgramViewHolder: RecyclerView.ViewHolder {

        var programName: TextView
        var createDate: TextView

        constructor(itemView: View) : super(itemView) {
            programName = itemView.findViewById(R.id.programm_list_name_id)
            createDate = itemView.findViewById(R.id.program_list_create_date_id)
        }

    }

}