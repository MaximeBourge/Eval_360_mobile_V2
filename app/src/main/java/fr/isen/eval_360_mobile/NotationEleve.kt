package fr.isen.eval_360_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import fr.isen.eval_360_mobile.fragments.EleveFragment
import fr.isen.eval_360_mobile.fragments.NotationEleveFragment


class NotationEleve : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notation_eleve)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_eleve, NotationEleveFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

