package fr.isen.eval_360_mobile.teacherView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.StudentAdapter
import fr.isen.eval_360_mobile.allclasse.Group
import fr.isen.eval_360_mobile.allclasse.Student
import fr.isen.eval_360_mobile.allclasse.Project
import fr.isen.eval_360_mobile.databinding.ActivityParametersOfGroupsBinding


class ParametersOfGroupsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParametersOfGroupsBinding

    private lateinit var groupNameEditText: EditText
    private lateinit var studentCountTextView: TextView
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentEmailEditText: EditText
    private val students: MutableList<Student> = mutableListOf()
    private lateinit var projectId: String
    private lateinit var groupList: MutableList<Group>
    private lateinit var groupListReference: DatabaseReference
    private lateinit var databaseRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParametersOfGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = intent
        projectId = intent.getStringExtra("projectId") ?: ""
        groupList = intent.getSerializableExtra("groupList") as MutableList<Group>? ?: mutableListOf()

        databaseRef = FirebaseDatabase.getInstance().reference
        groupListReference = databaseRef.child("Projects").child(projectId).child("Groupes")

        groupNameEditText = binding.groupNameEditText
        studentCountTextView = binding.studentCountTextView
        studentRecyclerView = binding.studentRecyclerView

        val doneButton = binding.confirmGroupButton


        studentAdapter = StudentAdapter(students)
        studentRecyclerView.adapter = studentAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(this)



        binding.addStudentButton.setOnClickListener {
            val studentName = binding.studentNameEditText.text.toString().trim()
            if (studentName.isNotEmpty()) {
                addStudent(studentName)
                binding.studentNameEditText.text = null
                binding.studentEmailEditText.text = null
            }
        }


        groupNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val groupName = s.toString().trim()
                val groupInfoText = "Le nom du groupe est $groupName"
                binding.groupNameTextView.text = groupInfoText
            }
        })

        doneButton.setOnClickListener {
            val groupName = binding.groupNameEditText.text.toString()


            // Créez une référence au chemin "Projects/{projectId}/Groupes"
            val groupsRef = databaseRef.child("Projects").child(projectId).child("Groupes")


            // Créez une référence unique pour le nouveau groupe dans la sous-branche "Groupes" du projet spécifique
            val groupKey = groupsRef.push().key

            groupKey?.let { groupKey ->
                val groupRef = groupsRef.child(groupKey)
                val group = Group(groupKey, groupName, students)

                groupRef.setValue(group)
            }

            // Mettez à jour la liste des groupes dans l'intent pour ProjectDetailsActivity
            val intent = Intent()
            intent.putExtra("groupName", groupName)
            intent.putExtra("students", students.toTypedArray())
            setResult(RESULT_OK, intent)
            finish()
        }




        updateStudentCount()
    }
    private fun addStudent(studentName: String) {
        val student = Student(studentName)
        students.add(student)
        studentAdapter.notifyItemInserted(students.size - 1)
        updateStudentCount()
    }


    private fun updateStudentCount() {
        val count = students.size
        val countText = getString(R.string.student_count, count)
        studentCountTextView.text = countText
    }

}