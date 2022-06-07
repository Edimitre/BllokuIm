package com.edimitre.bllokuim.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.Description

class DescriptionAdapter(private val onDescriptionClickListener: OnDescriptionClickListener) :
    RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder>() {

    private var allDescriptions: List<Description>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.description_item_view, parent, false)
        return DescriptionViewHolder(view)

    }


    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val currentDescription = allDescriptions?.getOrNull(position)


        if (currentDescription != null) {
            holder.bind(currentDescription, onDescriptionClickListener)

        }

    }


    class DescriptionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val descriptionName: TextView = itemView.findViewById(R.id.description_name_text);


        fun bind(description: Description, onDescriptionClickListener: OnDescriptionClickListener) {

            descriptionName.text = description.name


            itemView.setOnClickListener {
                onDescriptionClickListener.onDescriptionClicked(description)
            }

        }

    }

    override fun getItemCount(): Int {

        return allDescriptions?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun putDescriptions(descriptionList: List<Description>) {
        allDescriptions = descriptionList
        notifyDataSetChanged()
    }

    interface OnDescriptionClickListener {
        fun onDescriptionClicked(description: Description)
    }
}