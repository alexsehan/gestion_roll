package com.example.gestion_roll.delivery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.newDelivery.ViewHolderNewDelivery

class AdapterDeliveryManagement(val context: Context, private var data: List<Client>) : RecyclerView.Adapter<ViewHolderNewDelivery>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNewDelivery {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item_delivery, parent, false)
        return ViewHolderNewDelivery(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Client>) {
        data = newData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderNewDelivery, position: Int) {
        holder.city.text = data[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FindDeliveryManagement::class.java)
            intent.putExtra("EXTRA_RESULT", data[position]  as java.io.Serializable)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
