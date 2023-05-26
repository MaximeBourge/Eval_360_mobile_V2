package fr.isen.eval_360_mobile.teacherView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import fr.isen.eval_360_mobile.adapter.StudentAdapter
import fr.isen.eval_360_mobile.allclasse.Group
import fr.isen.eval_360_mobile.allclasse.Student
import fr.isen.eval_360_mobile.databinding.ActivityGroupsManagerBinding
import fr.isen.eval_360_mobile.studentView.InProjectSelectedActivity


class GroupsManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupsManagerBinding
    private val REQUEST_CODE_PARAMETERS = 1
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var groupNames: MutableList<String>
    private lateinit var students: List<Student>
    private lateinit var projectId: String
    private lateinit var databaseRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        studentAdapter = StudentAdapter(emptyList())

        studentRecyclerView = binding.currentStudentRecyclerView
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.changeParametersGroupButton.setOnClickListener {
            val intent = Intent(this, ParametersOfGroupsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PARAMETERS)
        }

        binding.seeMarkOfGroupButton.setOnClickListener {
            val intent = Intent(this, MarkTeacherViewActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PARAMETERS)
        }

        groupNames = mutableListOf()
        students = mutableListOf()

        // Récupérer l'ID du projet depuis l'intent
        projectId = intent.getStringExtra("projectId") ?: ""

        // Obtenir une référence à la base de données pour les élèves du groupe
        databaseRef = FirebaseDatabase.getInstance().getReference("Projets/$projectId/Groupes")

        // Ajouter un écouteur pour récupérer les élèves du groupe
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedStudents = mutableListOf<Student>() // Utiliser une référence immuable locale

                for (groupSnapshot in snapshot.children) {
                    val group = groupSnapshot.getValue(Group::class.java)
                    group?.students?.forEach { studentId ->
                        // Récupérer les informations de chaque élève à partir de la base de données
                        val studentRef = FirebaseDatabase.getInstance().getReference("Students/$studentId")
                        studentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(studentSnapshot: DataSnapshot) {
                                val student = studentSnapshot.getValue(Student::class.java)
                                if (student != null) {
                                    updatedStudents.add(student) // Ajouter à la référence immuable locale
                                    studentAdapter.setStudents(updatedStudents) // Mettre à jour la RecyclerView
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Gérer l'erreur
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PARAMETERS && resultCode == RESULT_OK) {
            val groupName = data?.getStringExtra("groupName")
            val students = data?.getSerializableExtra("students") as? Array<Student>

            if (groupName != null && students != null) {
                // Afficher le nom du groupe
                binding.currentGroupNametextView.text = "Le nom du groupe est $groupName"

                // Créer une liste d'objets Student à partir des étudiants
                val studentList = students.toList()

                // Mettre à jour la liste des étudiants dans la RecyclerView
                updateStudentList(studentList)
            }
        }
    }




    private fun updateStudentList(students: List<Student>) {
        studentAdapter.setStudents(students)
    }





    private fun updateGroupList(groups: List<String>) {
        val intent = Intent(this, InProjectSelectedActivity::class.java)
        intent.putStringArrayListExtra("groupNames", ArrayList(groups))
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }


}
