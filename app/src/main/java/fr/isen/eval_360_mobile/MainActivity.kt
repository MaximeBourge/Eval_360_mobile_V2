package fr.isen.eval_360_mobile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fr.isen.eval_360_mobile.studentView.DetailNotation
import fr.isen.eval_360_mobile.studentView.Eleve
import fr.isen.eval_360_mobile.teacherView.HomePageTeacherActivity
import fr.isen.eval_360_mobile.teacherView.ListOfProjectTeacherView


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStudent = findViewById<Button>(R.id.btn_get_student)
        buttonStudent.setOnClickListener {
            val intent = Intent(this, ListOfProjectTeacherView::class.java)
            startActivity(intent)
        }

        val buttonTeacher = findViewById<Button>(R.id.btn_get_teacher)
        buttonTeacher.setOnClickListener {
            val intent = Intent(this, Eleve::class.java)
            startActivity(intent)
        }
    }
}