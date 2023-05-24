package fr.isen.eval_360_mobile.studentView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.StudentAdapter
import fr.isen.eval_360_mobile.allclasse.Student


class NotationEleveActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notation_eleve)

        recyclerView = findViewById(R.id.recyclerViewListeElevesANoter)
        studentAdapter = StudentAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Remplacez "project1" par l'ID du projet souhaité
        val projectId = "-NW2gYA_lOhWboPnUlyd"

        Log.e("TEST.", "teste.")

        // Récupérer la référence du projet dans la base de données
        val projectRef = FirebaseDatabase.getInstance().reference.child("Projects").child(projectId)

// Écouter les modifications du groupe dans le projet
        projectRef.child("Group").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupId = snapshot.child("groupid").getValue(String::class.java)

                if (groupId != null) {
                    // Récupérer la liste des étudiants du groupe
                    val groupRef = FirebaseDatabase.getInstance().reference.child("groups").child(groupId)
                    groupRef.child("listofstudent").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val studentsList = mutableListOf<Student>()

                            for (dataSnapshot in snapshot.children) {
                                val student = dataSnapshot.getValue(Student::class.java)
                                student?.let {
                                    studentsList.add(it)
                                }
                            }

                            studentAdapter.setStudents(studentsList)

                            // Vérifier s'il y a des élèves dans la liste
                            if (studentsList.isNotEmpty()) {
                                Log.e("NotationEleveActivity", "Il existe un projet avec un groupe contenant des élèves.")
                            } else {
                                Log.e("NotationEleveActivity", "Il existe un projet avec un groupe, mais aucun élève n'est disponible.")
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Gérer l'annulation de la requête
                        }
                    })
                } else {
                    Log.e("NotationEleveActivity","Il n'existe pas de projet avec un groupe contenant des élèves.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Gérer l'annulation de la requête
            }
        })

        recyclerView.adapter = studentAdapter
    }
}
