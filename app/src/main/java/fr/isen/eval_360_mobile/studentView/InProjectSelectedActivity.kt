package fr.isen.eval_360_mobile.studentView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.fragments.ProjectFragment

class InProjectSelectedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_project_selected)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, ProjectFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

}

