package fr.isen.eval_360_mobile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import fr.isen.eval_360_mobile.fragments.ScreenSlidePagerAdapter
import fr.isen.eval_360_mobile.studentView.Eleve
import fr.isen.eval_360_mobile.studentView.NotationEleveActivity
import fr.isen.eval_360_mobile.teacherView.HomePageTeacherActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStudent = findViewById<Button>(R.id.btn_get_student)
        buttonStudent.setOnClickListener {
            val intent = Intent(this, Eleve::class.java)
            startActivity(intent)
        }

        val buttonTeacher = findViewById<Button>(R.id.btn_get_teacher)
        buttonTeacher.setOnClickListener {
            val intent = Intent(this, HomePageTeacherActivity::class.java)
            startActivity(intent)
        }

        val viewPager = findViewById<ViewPager2>(R.id.infoCarousel)
        viewPager.adapter = ScreenSlidePagerAdapter(this)
    }
}