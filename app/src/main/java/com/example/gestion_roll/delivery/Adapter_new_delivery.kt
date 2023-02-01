package com.example.gestion_roll.delivery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.user.Users

class AdapterNewDelivery(val context: Context, private var data: List<Client>, private val callback: OnResultCallback) : RecyclerView.Adapter<ViewHolderNewDelivery>() {

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
            val data = Intent().putExtra("EXTRA_RESULT", data[position]  as java.io.Serializable)
            callback.setResult(data)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class AdapterNewDeliveryDriver(val context: Context, private var data: List<Users>, private val callback: OnResultCallback) : RecyclerView.Adapter<ViewHolderNewDelivery>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNewDelivery {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item_delivery, parent, false)
        return ViewHolderNewDelivery(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Users>) {
        data = newData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderNewDelivery, position: Int) {
        holder.city.text = data[position].Login

        holder.itemView.setOnClickListener {
            val data = Intent().putExtra("EXTRA_RESULT", data[position] as java.io.Serializable)
            callback.setResult(data)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
