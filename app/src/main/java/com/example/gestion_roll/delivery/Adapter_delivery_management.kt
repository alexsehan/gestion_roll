package com.example.gestion_roll.delivery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.newDelivery.Delivery
import com.example.gestion_roll.newDelivery.ViewHolderNewDelivery
import com.example.gestion_roll.user.isAdmin
import java.text.SimpleDateFormat
import java.util.*

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

class AdapterClientDeliveryManagement(val context: Context, var data: MutableList<Delivery>) : RecyclerView.Adapter<ViewHolderDeliveriesByClient>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDeliveriesByClient {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item_deliveries_by_client, parent, false)
        return ViewHolderDeliveriesByClient(view)
    }

    private fun removeItemAt(position: Int) {
        if (position < 0 || position >= data.size) {
            return
        }

        data[position].delete(context)
        data.removeAt(position) // Supprimer l'élément de la liste de données
        notifyItemRemoved(position) // Informer l'adaptateur que l'élément a été supprimé

        // Si la liste est vide, informer l'adaptateur que tout a été supprimé
        if (data.isEmpty()) {
            notifyItemRangeRemoved(0, 1)
        } else {
            // Sinon, informer l'adaptateur que la position des éléments suivants a changé
            notifyItemRangeChanged(position, data.size - position)
        }
    }






    override fun onBindViewHolder(holder: ViewHolderDeliveriesByClient, position: Int) {

        holder.dateDeliveriesByClient.text = formatDate(data[position].date)
        holder.userDeliveriesByClient.text = data[position].livreur.uppercase()
        holder.tagDeliveriesByClient.text = data[position].TAG.toString()
        holder.ccDeliveriesByClient.text = data[position].CC.toString()
        holder.ordDeliveriesByClient.text = data[position].ORDINAIRE.toString()
        holder.etaDeliveriesByClient.text = data[position].ETAGERE.toString()
        holder.rehDeliveriesByClient.text = data[position].REHAUSSE.toString()

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Supprimer la livraison ?")
            builder.setMessage("Vous vous appretez à supprimer la livraison du " + formatDate(data[position].date) + ". " +
                    " Appuyez sur \"oui\" pour confirmer et sur \"non\" pour annuler")

            builder.setPositiveButton("oui") { _, _ ->
                if(position < data.size) {
                    removeItemAt(position)
                    //data[position].delete(context)
                    //data.removeAt(position)
                    //notifyItemRemoved(position)
                }
            }
            builder.setNegativeButton("non") { _, _ ->}

            //Les livraisons ne peuvent être supprimées que par les admins
            val dialog = builder.create()
            isAdmin { list ->
                if(list[0]){
                    if (list[1]) {
                        dialog.show()
                    }
                }
            }
            true
        }


    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
