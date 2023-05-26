package fr.isen.eval_360_mobile.teacherView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.eval_360_mobile.databinding.ActivityHomePageTeacherBinding

class HomePageTeacherActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomePageTeacherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonProjects.setOnClickListener{
            val intent = Intent(this, ListOfProjectTeacherView::class.java)
            startActivity(intent)
        }
    }
}