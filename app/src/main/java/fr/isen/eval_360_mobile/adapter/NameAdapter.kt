package fr.isen.eval_360_mobile.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import fr.isen.eval_360_mobile.studentView.DetailNotation

import fr.isen.eval_360_mobile.R

class NameAdapter : RecyclerView.Adapter<NameAdapter.ViewHolder>() {

    private val myDataset = ArrayList<String>()
    private val userIdList = ArrayList<String>()

    init {
        // Récupération des données de la table Firebase
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Parcours du DataSnapshot pour extraire les chaînes de caractères
                for (childSnapshot in dataSnapshot.children) {
                    val myString = childSnapshot.child("username").getValue(String::class.java)
                    Log.d("NameAdapter", "myString: $myString") // Ajout de la ligne de log
                    myString?.let { myDataset.add(it) }
                }
                for (childSnapshot in dataSnapshot.children) {
                    val userId = childSnapshot.key
                    val username = childSnapshot.child("username").getValue(String::class.java)
                    Log.d("NameAdapter", "userId: $userId, username: $username")
                    userId?.let { userIdList.add(it) }
                    username?.let { myDataset.add(it) }
                }
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
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_notation_eleve,
            parent,
            false
        ) as ViewGroup
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
                val user = FirebaseAuth.getInstance().currentUser
                val connectedUserId = user?.uid
                val connectedUserName = user?.displayName

                if (connectedUserId != null && connectedUserName != null && index >= 0 && index < myDataset.size) {
                    val userToBeRatedId = userIdList[index]
                    val userToBeRatedName = myDataset[index]

                    val newTableReference = FirebaseDatabase.getInstance().getReference("notes")
                    val newTable = newTableReference.push()

                    val data = hashMapOf(
                        "connectedUserId" to connectedUserId,
                        "connectedUserName" to connectedUserName,
                        "userToBeRatedId" to userToBeRatedId,
                        "userToBeRatedName" to userToBeRatedName
                    )

                    newTable.setValue(data)
                        .addOnSuccessListener {
                            Log.d("NameAdapter", "Nouvelle table créée avec l'ID : ${newTable.key}")
                        }
                        .addOnFailureListener {
                            Log.e(
                                "NameAdapter",
                                "Erreur lors de la création de la nouvelle table",
                                it
                            )
                        }
                } else {
                    // Gérer le cas où connectedUserId est null, connectedUserName est null, ou index est invalide
                }

            }
        }
    }
}
