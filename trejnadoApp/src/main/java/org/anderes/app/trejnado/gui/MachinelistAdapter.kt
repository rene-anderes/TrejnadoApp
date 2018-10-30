package org.anderes.app.trejnado.gui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import org.anderes.app.trejnado.R
import org.anderes.app.trejnado.TrainingMachine

class MachinelistAdapter(options: FirebaseRecyclerOptions<TrainingMachine>) :
    FirebaseRecyclerAdapter<TrainingMachine, MachinelistAdapter.TrainingMachineViewHolder>(options) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrainingMachineViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.program_item, viewGroup, false)

        return MachinelistAdapter.TrainingMachineViewHolder(itemView)

    }

    override fun onBindViewHolder(viewHolder: TrainingMachineViewHolder, position: Int, model: TrainingMachine) {
        viewHolder.machineName.text = model.name
        viewHolder.machineName.visibility = TextView.VISIBLE
    }

    class TrainingMachineViewHolder: RecyclerView.ViewHolder {

        var machineName: TextView

        constructor(itemView: View) : super(itemView) {
            machineName = itemView.findViewById(R.id.program_machine_name_id)
        }

    }
}