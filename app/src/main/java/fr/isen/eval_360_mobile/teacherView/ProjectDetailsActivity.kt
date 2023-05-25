package fr.isen.eval_360_mobile.teacherView

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.ProjectAdapter
import fr.isen.eval_360_mobile.allclasse.Group
import fr.isen.eval_360_mobile.allclasse.Project
import fr.isen.eval_360_mobile.allclasse.Student
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class ProjectDetailsActivity : AppCompatActivity(), ProjectAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var projectListener: ValueEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        recyclerView = findViewById(R.id.RecyclerViewProjects)
        projectAdapter = ProjectAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = projectAdapter

        // Référence à la branche "Projets" dans la base de données
        databaseRef = FirebaseDatabase.getInstance().getReference("Projects")


        // Écouteur pour récupérer les données de la base de données
        projectListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val projects = mutableListOf<Project>()
                for (projectSnapshot in dataSnapshot.children) {
                    val projectId = projectSnapshot.key
                    val projectName = projectSnapshot.child("name").getValue(String::class.java)
                    val groupList = mutableListOf<Group>()

                    for (groupSnapshot in projectSnapshot.child("Groupes").children) {
                        val groupId = groupSnapshot.key
                        val groupName = groupSnapshot.child("nomDuGroup").getValue(String::class.java)
                        val studentList = mutableListOf<Student>()

                        for (studentSnapshot in groupSnapshot.child("students").children) {
                            val studentId = studentSnapshot.key
                            val studentName = studentSnapshot.child("name").getValue(String::class.java)
                            val studentEmail = studentSnapshot.child("email").getValue(String::class.java)
                            val studentMark = studentSnapshot.child("mark").getValue(Int::class.java)

                            val student = Student(
                                studentId?: "",
                                studentName?: "",
                                studentEmail?: "",
                                studentMark?: 0)
                            studentList.add(student)
                        }

                        val group = Group(groupId?: "", groupName?: "", studentList)
                        groupList.add(group)
                    }

                    val project = Project(projectId?: "", projectName?: "", groupList)
                    projects.add(project)
                }

                projectAdapter.setProjects(projects)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Gestion des erreurs de lecture de la base de données
            }
        }

        // Ajout de l'écouteur à la référence de la base de données
        databaseRef.addValueEventListener(projectListener)

        // Utilisation de la bibliothèque Kivy pour exécuter le code Python
        val pythonCode = loadPythonFile("python/api-teacherFirebase.py")
    }


    override fun onDestroy() {
        super.onDestroy()
        // Retrait de l'écouteur lorsque l'activité est détruite
        databaseRef.removeEventListener(projectListener)
    }

    override fun onItemClick(project: Project) {
        val intent = Intent(this, InProjectSelectedActivity::class.java)
        intent.putExtra("projectId", project.id)
        intent.putExtra("projectName", project.name)
        // Ajoutez d'autres informations du projet si nécessaire
        startActivity(intent)
    }


    // Fonction pour charger le contenu d'un fichier Python depuis les ressources
    private fun loadPythonFile(fileName: String): String {
        try {
            // Obtenez le gestionnaire d'actifs (AssetManager)
            val assetManager = assets

            // Ouvrez le fichier Python
            val inputStream: InputStream = assetManager.open(fileName)

            // Lisez le contenu du fichier Python en utilisant un BufferedReader
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }

            // Fermez les flux
            reader.close()
            inputStream.close()

            // Le contenu du fichier Python est maintenant dans stringBuilder
            return stringBuilder.toString()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""
    }



}
