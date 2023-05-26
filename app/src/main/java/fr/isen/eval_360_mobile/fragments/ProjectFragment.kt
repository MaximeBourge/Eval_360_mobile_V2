package fr.isen.eval_360_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.adapter.GroupAdapter


class ProjectFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_in_project_selected, container, false)
        val verticalRecyclerView = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        if (verticalRecyclerView != null) {
            verticalRecyclerView.adapter = GroupAdapter()
        }
        return view

    }
}