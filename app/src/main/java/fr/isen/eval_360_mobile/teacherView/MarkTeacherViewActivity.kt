package fr.isen.eval_360_mobile.teacherView



import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import fr.isen.eval_360_mobile.R
import fr.isen.eval_360_mobile.fragments.ScreenSlidePagerAdapter

class MarkTeacherViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_teacher_view)

        val viewPager = findViewById<ViewPager2>(R.id.markStudentCarousel)
        viewPager.adapter = ScreenSlidePagerAdapter(this)

    }
}