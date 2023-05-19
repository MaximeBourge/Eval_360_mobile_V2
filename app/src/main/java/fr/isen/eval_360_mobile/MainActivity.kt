package fr.isen.eval_360_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fr.isen.eval_360_mobile.teacherView.HomePageTeacherActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btn_get_started)
        button.setOnClickListener {
            val intent = Intent(this, HomePageTeacherActivity::class.java)
            startActivity(intent)
        }
    }
}