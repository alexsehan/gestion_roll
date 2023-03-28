package com.example.gestion_roll.newDelivery

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R


class ViewHolderNewDelivery(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //val text: TextView = itemView.findViewById(R.id.date_delivery)
    val city: TextView = itemView.findViewById(R.id.user_delivery)
}