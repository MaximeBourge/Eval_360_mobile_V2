package fr.isen.eval_360_mobile.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import fr.isen.eval_360_mobile.DetailNotation

import fr.isen.eval_360_mobile.R

class NameAdapter : RecyclerView.Adapter<NameAdapter.ViewHolder>() {

    private val myDataset = ArrayList<String>()

    init {
        // Récupération des données de la table Firebase
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Parcours du DataSnapshot pour extraire les chaînes de caractères
                for (childSnapshot in dataSnapshot.children) {
                    val myString = childSnapshot.child("username").getValue(String::class.java)
                    Log.d("NameAdapter", "myString: $myString") // Ajout de la ligne de log
                    myDataset.add(myString!!)
                }


                // Notification de l'adaptateur du changement de données
                notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Gestion de l'erreur
            }
        })
    }

    class ViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val textViewList = listOf<TextView>(
            view.findViewById(R.id.textView1),
            view.findViewById(R.id.textView2),
            view.findViewById(R.id.textView3),
            view.findViewById(R.id.textView4),
            view.findViewById(R.id.textView5),
            view.findViewById(R.id.textView6)
        )
        val buttonList = listOf<Button>(
            view.findViewById(R.id.button1),
            view.findViewById(R.id.button2),
            view.findViewById(R.id.button3),
            view.findViewById(R.id.button4),
            view.findViewById(R.id.seeMarkOfGroupButton),
            view.findViewById(R.id.button6)
        )
        init {
            Log.d("ViewHolder", "textViewList: $textViewList")
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notation_eleve, parent, false) as ViewGroup
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return kotlin.math.ceil(myDataset.size / 6.0).toInt()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Masquer tous les TextView par défaut
        holder.textViewList.forEach { textView -> textView.visibility = TextView.GONE }
        holder.buttonList.forEach { button -> button.visibility = Button.GONE }

        // Boucle pour parcourir tous les TextView
        for (i in 0 until 6) {
            val textView = holder.textViewList[i]
            val button = holder.buttonList[i]

            // Afficher le TextView si la position est valide
            val index = position * 6 + i
            if (index < myDataset.size) {
                textView.text = myDataset[index]
                textView.visibility = TextView.VISIBLE
                button.visibility = Button.VISIBLE
            }
            button.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailNotation::class.java)
                val username = textView.text.toString()
                intent.putExtra("USERNAME", username)
                context.startActivity(intent)
            }

        }
    }
}
