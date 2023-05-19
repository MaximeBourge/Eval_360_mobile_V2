package fr.isen.eval_360_mobile.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.eval_360_mobile.NotationEleve
import fr.isen.eval_360_mobile.R

class GroupAdapterAdrien : RecyclerView.Adapter<GroupAdapterAdrien.ViewHolder>() {

    private val myDataset = ArrayList<String>()
    private var lastClickedButtonText: String? = null

    init {
        // Récupération des données de la table Firebase
        val databaseReference = FirebaseDatabase.getInstance().getReference("Projects")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Parcours du DataSnapshot pour extraire les chaînes de caractères
                for (childSnapshot in dataSnapshot.children) {
                    val projectName = childSnapshot.child("name").getValue(String::class.java)
                    if (projectName != null) {
                        myDataset.add(projectName)
                    }
                }
                // Notification de l'adaptateur du changement de données
                notifyDataSetChanged()

                // Affichage des données
                for (projectName in myDataset) {
                    println(projectName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestion de l'erreur
            }
        })
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val group1 = view.findViewById<Button>(R.id.group1)
        val group2 = view.findViewById<Button>(R.id.group2)
        val group3 = view.findViewById<Button>(R.id.group3)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vertical_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val buttonText = myDataset[position]

        // Masquer tous les boutons par défaut
        holder.group1.visibility = View.GONE
        holder.group2.visibility = View.GONE
        holder.group3.visibility = View.GONE

        // Boucle pour parcourir tous les boutons
        for (i in 0 until 3) {
            val button = when (i) {
                0 -> holder.group1
                1 -> holder.group2
                else -> holder.group3
            }

            // Afficher le bouton si la position est valide
            if (position * 3 + i < myDataset.size) {
                button.text = myDataset[position * 3 + i]
                button.visibility = View.VISIBLE

                // Ajouter un listener pour démarrer l'activité de notation
                button.setOnClickListener {
                    lastClickedButtonText = button.text.toString()
                    val intent = Intent(holder.itemView.context, NotationEleve::class.java).apply {
                        putExtra("buttonText", lastClickedButtonText)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return kotlin.math.ceil(myDataset.size / 3.0).toInt()
    }

}
