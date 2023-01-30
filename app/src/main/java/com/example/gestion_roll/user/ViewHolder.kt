package com.example.gestion_roll.user

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val loginUserManagement: TextView = itemView.findViewById(R.id.city_new_delivery)
    val isAdminUserManagement: TextView = itemView.findViewById(R.id.isAdmin_user_management)
    val isBreakUserManagement: TextView = itemView.findViewById(R.id.isBreak_user_management)
}