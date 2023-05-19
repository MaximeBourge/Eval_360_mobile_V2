package fr.isen.eval_360_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.eval_360_mobile.fragments.EleveFragment

class Eleve : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleve)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,EleveFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}