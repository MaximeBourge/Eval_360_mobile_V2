package fr.isen.eval_360_mobile.teacherView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.GroupAdapter
import fr.isen.eval_360_mobile.databinding.ActivityInProjectSelectedBinding

class InProjectSelectedActivity : AppCompatActivity(), GroupAdapter.OnItemClickListener {
    lateinit var binding: ActivityInProjectSelectedBinding
    private lateinit var groupsRecyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var groupNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInProjectSelectedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groupsRecyclerView = binding.groupsRecyclerView
        groupAdapter = GroupAdapter(emptyList(), this)
        groupsRecyclerView.adapter = groupAdapter
        groupsRecyclerView.layoutManager = LinearLayoutManager(this)

        groupNames = intent.getStringArrayListExtra("groupNames") ?: emptyList()
        groupAdapter.setGroups(groupNames)



        val createdGroupName = intent.getStringExtra("createdGroupName")
        if (createdGroupName != null) {
            // Ajoutez le nom du groupe créé à la liste des noms de groupes
            groupNames = groupNames.plus(createdGroupName)
            // Mettez à jour la liste des groupes dans l'adaptateur
            groupAdapter.setGroups(groupNames)
        }

        val projectName = intent.getStringExtra("projectName")
        val projectNameTextView = findViewById<TextView>(R.id.projectNameTextView)
        projectNameTextView.text = projectName

        val groups = intent.getStringArrayExtra("groups")
        if (groups != null) {
            // Traitez les groupes comme vous le souhaitez
        }



//        binding.manageGroupButton.setOnClickListener{
//            val intent = Intent(this, GroupsManagerActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.deleteProjectButton.setOnClickListener {
//            val projectId = intent.getStringExtra("projectId")
//            if (projectId != null) {
//                val databaseRef = FirebaseDatabase.getInstance().getReference("Projets").child(projectId)
//                databaseRef.removeValue()
//                finish() // Ferme l'activité après la suppression du projet
//            }
//        }

    }

    override fun onItemClick(groupName: String) {
        val intent = Intent(this, GroupsManagerActivity::class.java)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
    }

}
