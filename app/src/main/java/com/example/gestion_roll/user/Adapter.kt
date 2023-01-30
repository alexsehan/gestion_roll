package com.example.gestion_roll.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R

class MyAdapter(val context: Context, private val data: List<Users>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.loginUserManagement.text = data[position].Login

        if(data[position].Admin) {
            holder.isAdminUserManagement.setText(R.string.yes)
        } else {
            holder.isAdminUserManagement.setText(R.string.no)
        }

        if(data[position].Break) {
            holder.isBreakUserManagement.setText(R.string.yes)
        } else {
            holder.isBreakUserManagement.setText(R.string.no)
        }

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Mettre sur pause ?")
            builder.setMessage("Voulez-vous inverser l'état de pause de " + data[position].Login + " ? " +
                    "Il est actuellement sur : "  + if(data[position].Break) "activé." else {"désactivé."} +
                    " Si celui-ci est activé, l'utilisateur ne pourra plus utiliser l'application.")

            builder.setPositiveButton("oui") { _, _ ->
                setBreak(data[position], context)
                if(getBreak(data[position])) {
                    holder.isBreakUserManagement.setText(R.string.no)
                } else {
                    holder.isBreakUserManagement.setText(R.string.yes)
                }
            }
            builder.setNegativeButton("non") { _, _ ->}

            val dialog = builder.create()
            dialog.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}


