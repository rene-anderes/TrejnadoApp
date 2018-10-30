package org.anderes.app.trejnado.gui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.anderes.app.trejnado.R

class SettinglistAdapter: RecyclerView.Adapter<SettinglistAdapter.SettingViewHolder> {
    constructor()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SettinglistAdapter.SettingViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: SettinglistAdapter.SettingViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class SettingViewHolder: RecyclerView.ViewHolder {

        var description: TextView

        constructor(itemView: View) : super(itemView) {
            description = itemView.findViewById(R.id.program_machine_name_id)
        }
    }
}