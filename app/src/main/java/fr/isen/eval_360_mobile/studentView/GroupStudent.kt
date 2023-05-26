package fr.isen.eval_360_mobile.studentView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.MarkAdapter
import fr.isen.eval_360_mobile.adapter.ProjectAdapter


class GroupStudent : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var markAdapter: MarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_student)

        //val markAdapter = MarkAdapter(listOfStudents) // Remplacez "listOfStudents" par votre liste d'élèves

        recyclerView = findViewById(R.id.recyclerViewListeElevesANoter)
        recyclerView.layoutManager = LinearLayoutManager(this) // Remplacez "this" par votre contexte approprié


    }

}



