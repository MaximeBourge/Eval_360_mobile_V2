package fr.isen.eval_360_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.StudentAdapter
import fr.isen.eval_360_mobile.allclasse.Student
import fr.isen.eval_360_mobile.allclasse.StudentAdapterListener

class StudentListFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_screen_slide_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewStudents)

        adapter = StudentAdapter(
            listOf(
                Student("1","Alicenhnnnnnnnnnnnnnn", "Alice@isen.yncrea.fr", 12),
                Student("1","Bon", "Bob@isen.yncrea.fr", 15),
                Student("1","Yannis", "Bob@isen.yncrea.fr",9),
                Student("1","Clement", "Bob@isen.yncrea.fr",8),
                Student("1","Maxime", "Bob@isen.yncrea.fr",12),
                Student("1","Adrien", "Bob@isen.yncrea.fr",18),
                Student("1","Loic", "Bob@isen.yncrea.fr",10),
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }
}