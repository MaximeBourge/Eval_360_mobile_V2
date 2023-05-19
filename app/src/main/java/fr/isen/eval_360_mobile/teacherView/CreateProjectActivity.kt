package fr.isen.eval_360_mobile.teacherView

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.allclasse.Group
import fr.isen.eval_360_mobile.allclasse.Project
import java.io.Serializable

class CreateProjectActivity : AppCompatActivity() {

    private lateinit var projectId: String
    private lateinit var groupListReference: DatabaseReference
    private lateinit var projectNameEditText: EditText
    private lateinit var createButton: Button
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)

        projectNameEditText = findViewById(R.id.projectNameEditText)
        createButton = findViewById(R.id.createButton)
        databaseRef = FirebaseDatabase.getInstance().reference

        // Récupérer l'ID du projet et la liste vide des groupes depuis l'intent
        projectId = intent.getStringExtra("projectId") ?: ""
        groupListReference = databaseRef.child("Projects").child(projectId).child("Groupes")

        createButton.setOnClickListener {
            val projectName = projectNameEditText.text.toString().trim()
            if (projectName.isNotEmpty()) {
                val projectId = databaseRef.push().key // Génère un ID unique pour le projet

                projectId?.let { projectKey ->
                        val project = Project(projectKey, projectName, emptyList())

                    groupListReference.setValue(null)

                    val projectRef = databaseRef.child("Projects").child(projectKey)
                    projectRef.setValue(project)

                    val groupsRef = projectRef.child("Groupes")
                    groupsRef.setValue(null) // Initialise la branche "Groupes" à vide

                    val intent = Intent(this, ProjectDetailsActivity::class.java)
                    intent.putExtra("projectId", projectKey)
                    intent.putExtra("groupList", project.groupList as Serializable?) // Passer la liste vide des groupes
                    startActivity(intent)
                }
            }
        }
    }
}
