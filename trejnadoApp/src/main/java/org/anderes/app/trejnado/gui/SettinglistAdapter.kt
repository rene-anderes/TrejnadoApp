package org.anderes.app.trejnado.gui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.anderes.app.trejnado.R

class SettinglistAdapter(val settings: Map<String, String>) :
    RecyclerView.Adapter<SettinglistAdapter.SettingViewHolder>() {

    val data = settings.toList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SettinglistAdapter.SettingViewHolder {

        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.machine_setting_item, viewGroup, false)
        return SettinglistAdapter.SettingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(viewHolder: SettinglistAdapter.SettingViewHolder, position: Int) {
        viewHolder.description.text = data.get(position).first
        viewHolder.settings.text = data.get(position).second
    }


    class SettingViewHolder: RecyclerView.ViewHolder {

        var description: TextView
        var settings: TextView

        constructor(itemView: View) : super(itemView) {
            description = itemView.findViewById(R.id.machine_setting_desc_id)
            settings = itemView.findViewById(R.id.machine_setting_id)
        }
    }
}